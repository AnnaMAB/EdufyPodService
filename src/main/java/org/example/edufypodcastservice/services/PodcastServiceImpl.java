package org.example.edufypodcastservice.services;


import jakarta.transaction.Transactional;
import org.example.edufypodcastservice.dto.GenreDto;
import org.example.edufypodcastservice.dto.PodcastDto;
import org.example.edufypodcastservice.entities.Genre;
import org.example.edufypodcastservice.entities.Podcast;
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

    @Autowired
    public PodcastServiceImpl(PodcastRepository podcastRepository, FullDtoConverter fullDtoConverter, GenreRepository genreRepository) {
        this.podcastRepository = podcastRepository;
        this.fullDtoConverter = fullDtoConverter;
        this.genreRepository = genreRepository;
    }

    @Transactional
    @Override
    public Podcast addPodcast(PodcastDto podcastDto) {
        Podcast podcast = new Podcast();
        if (podcastDto.getName() == null || podcastDto.getName().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name is required");
        }
        if (podcastDto.getDescription() == null || podcastDto.getDescription().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Description is required");
        }
        if (podcastDto.getGenres() == null || podcastDto.getGenres().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Genres is required");
        }
        if (podcastDto.getProducerId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Producer is required");
        }
        if (podcastDto.getImageUrl() != null && !podcastDto.getImageUrl().isBlank()) {
            podcast.setImageUrl(podcastDto.getImageUrl());
        }
        if (podcastDto.getThumbnailUrl() != null && !podcastDto.getThumbnailUrl().isBlank()) {
            podcast.setThumbnailUrl(podcastDto.getThumbnailUrl());
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
                }
            }
        }
        podcast.setGenres(genres);
        return podcastRepository.save(podcast);
    }

    @Transactional
    @Override
    public Podcast updatePodcast(PodcastDto podcastDto) {
        if(podcastDto.getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Podcast id is required");
        }
        Podcast podcast = podcastRepository.findById(podcastDto.getId()).orElseThrow(() -> {
            //   F_LOG.warn("{} tried to book a workout with id {} that doesn't exist.", role, workoutToBook.getId());
            return new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    String.format("No podcast exists with id: %s.", podcastDto.getId())
            );
        });

        if (podcastDto.getName() != null && !podcastDto.getName().equals(podcast.getName())) {
            if(podcastDto.getName().isBlank()) {
                // F_LOG.warn("{} tried to update a workout with invalid title.", role);
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Name can not be left blank."
                );
            }
            podcast.setName(podcastDto.getName());
        }
        if (podcastDto.getDescription() != null && !podcastDto.getDescription().equals(podcast.getDescription())) {
            if(podcastDto.getDescription().isBlank()) {
                // F_LOG.warn("{} tried to update a workout with invalid title.", role);
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Description can not be left blank."
                );
            }
            podcast.setDescription(podcastDto.getDescription());
        }
        if (podcastDto.getEpisodes() != null && !podcastDto.getEpisodes().isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Episodes can not be modified from this page."
            );
        }

        if (podcastDto.getProducerId() != null && !podcastDto.getProducerId().equals(podcast.getProducerId())) {
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
                    }
                }
            }
            podcast.setGenres(genres);
        }
        return podcastRepository.save(podcast);
    }

    @Transactional
    @Override
    public String deletePodcast(UUID podcastId) {
         if (podcastId == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Id must be provided"
            );
        }
        if (!podcastRepository.existsById(podcastId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    String.format("No podcast exists with id: %s.", podcastId)
            );
        }
        podcastRepository.deleteById(podcastId);
        return String.format("Podcast with Id: %s, and associated episodes have been successfully deleted.", podcastId);
    }

    @Override
    public List<PodcastDto> getAllPodcasts() {
        List<Podcast> podcasts = podcastRepository.findAll();
        List<PodcastDto> podcastDtos = new ArrayList<>();
        for (Podcast podcast : podcasts) {
            podcastDtos.add(fullDtoConverter.convertToFullPodcastDto(podcast));
        }
        return podcastDtos;
    }

    @Override
    public PodcastDto getPodcastById(UUID id) {
        if (id == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Id must be provided"
            );
        }
        Podcast podcast = podcastRepository.findById(id).orElseThrow(() -> {
            //   F_LOG.warn("{} tried to book a workout with id {} that doesn't exist.", role, workoutToBook.getId());
            return new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    String.format("No podcast exists with id: %s.", id)
            );
        });
        return fullDtoConverter.convertToFullPodcastDto(podcast);
    }

    @Override
    public List<PodcastDto> getPodcastByName(String podcastName) {
        List<Podcast> podcasts = podcastRepository.findByName(podcastName);
        List<PodcastDto> podcastDtos = new ArrayList<>();
        for (Podcast podcast : podcasts) {
            podcastDtos.add(fullDtoConverter.convertToFullPodcastDto(podcast));
        }
        return podcastDtos;
    }

    @Override
    public List<PodcastDto> getPodcastsByGenre(String genre) {
        List<Podcast> podcasts = podcastRepository.findByGenres_Name(genre);
        List<PodcastDto> podcastDtos = new ArrayList<>();
        for (Podcast podcast : podcasts) {
            podcastDtos.add(fullDtoConverter.convertToFullPodcastDto(podcast));
        }
        return podcastDtos;
    }

    @Override
    public Boolean podcastExists(UUID id) {
        boolean exists = podcastRepository.existsById(id);
        return exists;
    }

    @Override
    public Boolean podcastAssociatedWithProducer(UUID podcastId, UUID producerId) {
        if (podcastId == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "PodcastId must be provided"
            );
        }
        Podcast podcast = podcastRepository.findById(podcastId).orElseThrow(() -> {
            //   F_LOG.warn("{} tried to book a workout with id {} that doesn't exist.", role, workoutToBook.getId());
            return new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    String.format("No podcast exists with id: %s.", podcastId)
            );

        });
        if (producerId == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "ProducerId must be provided"
            );
        }
        return podcast.getProducerId().equals(producerId);
    }
}
