package org.example.edufypodcastservice.services;


import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.edufypodcastservice.converters.UserInfo;
import org.example.edufypodcastservice.dto.GenreDto;
import org.example.edufypodcastservice.dto.PodcastDto;
import org.example.edufypodcastservice.entities.Genre;
import org.example.edufypodcastservice.entities.Podcast;
import org.example.edufypodcastservice.external.ProducerApiClient;
import org.example.edufypodcastservice.mapper.FullDtoConverter;
import org.example.edufypodcastservice.repositories.GenreRepository;
import org.example.edufypodcastservice.repositories.PodcastRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PodcastServiceImpl implements PodcastService{

    private final PodcastRepository podcastRepository;
    private final FullDtoConverter fullDtoConverter;
    private final GenreRepository genreRepository;
    private final ProducerApiClient producerApiClient;
    private final UserInfo userInfo;
    private static final Logger F_LOG = LogManager.getLogger("functionality");

    @Autowired
    public PodcastServiceImpl(PodcastRepository podcastRepository, FullDtoConverter fullDtoConverter,
                              GenreRepository genreRepository, ProducerApiClient producerApiClient, UserInfo userInfo) {
        this.podcastRepository = podcastRepository;
        this.fullDtoConverter = fullDtoConverter;
        this.genreRepository = genreRepository;
        this.producerApiClient = producerApiClient;
        this.userInfo = userInfo;
    }


    @Override
    public Podcast addPodcast(PodcastDto podcastDto) {
        String role = userInfo.getRole();
        Podcast saved = addPodcastDetails(podcastDto);
        try {
            producerApiClient.addPodcastToProducer(saved.getId(), podcastDto.getProducerId());
        } catch (Exception e) {
            podcastRepository.deleteById(saved.getId());
            F_LOG.warn("{} failed to add podcast to producer. Podcast not saved.", role);
            throw new RuntimeException("Failed to add podcast to producer. Podcast deleted.", e);
        }
        F_LOG.info("{} added a podcast with id {}.", role, podcastDto.getId());
        return saved;
    }

    @Transactional
    public Podcast addPodcastDetails(PodcastDto podcastDto) {
        String role = userInfo.getRole();
        Podcast podcast = new Podcast();
        if (podcastDto.getName() == null || podcastDto.getName().isBlank()) {
            F_LOG.warn("{} tried to add a podcast without a name.", role);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name is required");
        }
        if (podcastDto.getDescription() == null || podcastDto.getDescription().isBlank()) {
            F_LOG.warn("{} tried to add a podcast without a description.", role);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Description is required");
        }
        if (podcastDto.getGenres() == null || podcastDto.getGenres().isEmpty()) {
            F_LOG.warn("{} tried to add a podcast without a genre.", role);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Genres is required");
        }
        if (podcastDto.getProducerId() == null) {
            F_LOG.warn("{} tried to add a podcast without a producerId.", role);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Producer is required");
        }
        if (!producerApiClient.producerExists(podcastDto.getProducerId())) {
            F_LOG.warn("{} tried to add a podcast with a producer that dont exists.", role);
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Producer don't exists");
        }
        if (podcastDto.getImageUrl() != null && !podcastDto.getImageUrl().isBlank()) {
            podcast.setImageUrl(podcastDto.getImageUrl());
        }else {
            podcast.setImageUrl("https://default/image.url");
        }
        if (podcastDto.getThumbnailUrl() != null && !podcastDto.getThumbnailUrl().isBlank()) {
            podcast.setThumbnailUrl(podcastDto.getThumbnailUrl());
        }else {
            podcast.setThumbnailUrl("https://default/thumbnail.url");
        }
        podcast.setName(podcastDto.getName());
        podcast.setDescription(podcastDto.getDescription());
        List<GenreDto> genresDto = podcastDto.getGenres();
        List<Genre> genres = new ArrayList<>();
        for (GenreDto genreDto : genresDto) {
            if (genreDto.getId() != null) {
                Optional<Genre> genre = genreRepository.findById(genreDto.getId());
                if (genre.isPresent()) {
                    genres.add(genre.get());
                    genre.get().getPodcasts().add(podcast);
                    genreRepository.save(genre.get());
                }
            }
        }
        podcast.setGenres(genres);
        podcast.setProducerId(podcastDto.getProducerId());

        F_LOG.info("{} added a podcast with id {}.", role, podcast.getId());
        return podcastRepository.save(podcast);
    }

    @Transactional
    @Override
    public Podcast updatePodcast(PodcastDto podcastDto) {
        String role = userInfo.getRole();
        if(podcastDto.getId() == null) {
            F_LOG.warn("{} tried to retrieve a podcast without providing an id {}.", role, podcastDto.getId());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Podcast id is required");
        }
        Podcast podcast = podcastRepository.findById(podcastDto.getId()).orElseThrow(() -> {
            F_LOG.warn("{} tried to retrieve an podcast with id {} that doesn't exist.", role, podcastDto.getId());
            return new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    String.format("No podcast exists with id: %s.", podcastDto.getId())
            );
        });
        if (podcastDto.getName() != null && !podcastDto.getName().equals(podcast.getName())) {
            if(podcastDto.getName().isBlank()) {
                F_LOG.warn("{} tried to update an podcast with invalid name.", role);
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Name can not be left blank."
                );
            }
            podcast.setName(podcastDto.getName());
        }
        if (podcastDto.getDescription() != null && !podcastDto.getDescription().equals(podcast.getDescription())) {
            if(podcastDto.getDescription().isBlank()) {
                F_LOG.warn("{} tried to update an podcast with invalid description.", role);
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Description can not be left blank."
                );
            }
            podcast.setDescription(podcastDto.getDescription());
        }
        if (podcastDto.getEpisodes() != null && !podcastDto.getEpisodes().isEmpty()) {
            F_LOG.warn("{} tried to add episodes to a podcast from the wrong endpoint.", role);
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Episodes can not be modified from this page."
            );
        }

        if (podcastDto.getProducerId() != null && !podcastDto.getProducerId().equals(podcast.getProducerId())) {
            if (!producerApiClient.producerExists(podcastDto.getProducerId())) {
                F_LOG.warn("{} tried to update a podcast with a producer that don't exists.", role);
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Producer don't exists");
            }
            producerApiClient.removePodcastFromProducer(podcast.getId(), podcast.getProducerId());
            producerApiClient.addPodcastToProducer(podcast.getId(), podcastDto.getProducerId());
            podcast.setProducerId(podcastDto.getProducerId());
        }
        if (podcastDto.getImageUrl() != null && !podcastDto.getImageUrl().equals(podcast.getImageUrl())) {
            podcast.setImageUrl(podcastDto.getImageUrl());
        }
        if (podcastDto.getThumbnailUrl() != null && !podcastDto.getThumbnailUrl().equals(podcast.getThumbnailUrl())) {
            podcast.setThumbnailUrl(podcastDto.getThumbnailUrl());
        }
        if (podcastDto.getGenres() != null && !podcastDto.getGenres().isEmpty()) {
            List<GenreDto> genresDto = podcastDto.getGenres();
            List<Genre> genres = new ArrayList<>();
            for (GenreDto genreDto : genresDto) {
                if (genreDto.getId() != null) {
                    Optional<Genre> genre = genreRepository.findById(genreDto.getId());
                    if (genre.isPresent()) {
                        genres.add(genre.get());
                        genre.get().getPodcasts().add(podcast);
                        genreRepository.save(genre.get());
                    }
                }
            }
            podcast.setGenres(genres);
        }

        F_LOG.info("{} updated an podcast with id {}.", role, podcast.getId());
        return podcastRepository.save(podcast);
    }

    @Transactional
    @Override
    public String deletePodcast(UUID podcastId) {
        String role = userInfo.getRole();
         if (podcastId == null) {
             F_LOG.warn("{} tried to delete a podcast without providing an id.", role);
             throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Id must be provided"
            );
        }
        Podcast podcast = podcastRepository.findById(podcastId).orElseThrow(() -> {
            F_LOG.warn("{} tried to delete a podcast with id {} that doesn't exist.", role, podcastId);
            return new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    String.format("No podcast exists with id: %s.", podcastId)
            );
        });
        UUID podcastUuid = podcast.getId();
        UUID producerUuid = podcast.getProducerId();
        producerApiClient.removePodcastFromProducer(podcastUuid, producerUuid);
        podcastRepository.deleteById(podcastId);

        F_LOG.info("{} deleted a podcast with id:", role, podcastId);
        return String.format("Podcast with Id: %s, and associated episodes have been successfully deleted.", podcastId);
    }

    @Override
    public List<PodcastDto> getAllPodcasts() {
        List<Podcast> podcasts = podcastRepository.findAll();
        List<PodcastDto> podcastDtos = new ArrayList<>();
        for (Podcast podcast : podcasts) {
            podcastDtos.add(fullDtoConverter.convertToFullPodcastDto(podcast));
        }

        F_LOG.info("{} retrieved all podcast.", userInfo.getRole());
        return podcastDtos;
    }

    @Override
    public PodcastDto getPodcastById(UUID podcastId) {
        String role = userInfo.getRole();
        if (podcastId == null) {
            F_LOG.warn("{} tried to retrieve a podcast without providing an id.", role);
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Id must be provided"
            );
        }
        Podcast podcast = podcastRepository.findById(podcastId).orElseThrow(() -> {
            F_LOG.warn("{} tried to retrieve a podcast with id {} that doesn't exist.", role, podcastId);
            return new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    String.format("No podcast exists with id: %s.", podcastId)
            );
        });

        F_LOG.info("{} retrieved a podcast with id {}.", role, podcastId);
        return fullDtoConverter.convertToFullPodcastDto(podcast);
    }

    @Override
    public List<PodcastDto> getPodcastByName(String podcastName) {
        List<Podcast> podcasts = podcastRepository.findByName(podcastName);
        List<PodcastDto> podcastDtos = new ArrayList<>();
        for (Podcast podcast : podcasts) {
            podcastDtos.add(fullDtoConverter.convertToFullPodcastDto(podcast));
        }

        F_LOG.info("{} retrieved podcasts with name: {}.", userInfo.getRole(), podcastName);
        return podcastDtos;
    }

    @Override
    public List<PodcastDto> getPodcastsByGenre(String genre) {
        List<Podcast> podcasts = podcastRepository.findByGenres_Name(genre);
        List<PodcastDto> podcastDtos = new ArrayList<>();
        for (Podcast podcast : podcasts) {
            podcastDtos.add(fullDtoConverter.convertToFullPodcastDto(podcast));
        }

        F_LOG.info("{} retrieved podcasts with genre: {}.", userInfo.getRole(), genre);
        return podcastDtos;
    }

    @Override
    public Boolean podcastExists(UUID id) {
        boolean exists = podcastRepository.existsById(id);
        F_LOG.info("{} checked if podcast exists", userInfo.getRole());
        return exists;
    }

    @Override
    public Boolean podcastAssociatedWithProducer(UUID podcastId, UUID producerId) {
        String role = userInfo.getRole();
        if (podcastId == null) {
            F_LOG.warn("{} tried to retrieve a podcast without providing an id.", role);
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "PodcastId must be provided"
            );
        }
        Podcast podcast = podcastRepository.findById(podcastId).orElseThrow(() -> {
            F_LOG.warn("{} tried to retrieve a podcast with id {} that doesn't exist.", role, podcastId);
            return new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    String.format("No podcast exists with id: %s.", podcastId)
            );

        });
        if (producerId == null) {
            F_LOG.warn("{} tried to check a producer without providing an id.", role);
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "ProducerId must be provided"
            );
        }
        if (podcast.getProducerId() == null || !podcast.getProducerId().equals(producerId)) {
            return false;
        }

        F_LOG.info("{} checked producer/podcast association.", userInfo.getRole());
        return true;
    }
}
