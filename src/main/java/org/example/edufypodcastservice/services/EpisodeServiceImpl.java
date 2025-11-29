package org.example.edufypodcastservice.services;

import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.edufypodcastservice.converters.UserInfo;
import org.example.edufypodcastservice.dto.EpisodeDto;
import org.example.edufypodcastservice.entities.Episode;
import org.example.edufypodcastservice.entities.Genre;
import org.example.edufypodcastservice.entities.Podcast;
import org.example.edufypodcastservice.mapper.FullDtoConverter;
import org.example.edufypodcastservice.mapper.LimitedDtoConverter;
import org.example.edufypodcastservice.repositories.EpisodeRepository;
import org.example.edufypodcastservice.repositories.PodcastRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.*;


@Service
public class EpisodeServiceImpl implements EpisodeService {

    private final EpisodeRepository episodeRepository;
    private final FullDtoConverter fullDtoConverter;
    private final LimitedDtoConverter limitedDtoConverter;
    private final PodcastRepository podcastRepository;
    private final UserInfo userInfo;
    private static final Logger F_LOG = LogManager.getLogger("functionality");

    @Autowired
    public EpisodeServiceImpl(EpisodeRepository episodeRepository, FullDtoConverter fullDtoConverter,
                              LimitedDtoConverter limitedDtoConverter, PodcastRepository podcastRepository, UserInfo userInfo) {
        this.episodeRepository = episodeRepository;
        this.fullDtoConverter = fullDtoConverter;
        this.limitedDtoConverter = limitedDtoConverter;
        this.podcastRepository = podcastRepository;
        this.userInfo = userInfo;
    }

    @Transactional
    @Override
    public Episode addEpisode(EpisodeDto episodeDto) {
        String role = userInfo.getRole();
        Episode episode = new Episode();
        if (episodeDto.getTitle() == null || episodeDto.getTitle().isBlank()) {
            F_LOG.warn("{} tried to add an episode without a title.", role);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Title is required");
        }
        if (episodeDto.getUrl() == null || episodeDto.getUrl().isBlank()) {
            F_LOG.warn("{} tried to add an episode without an url.", role);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Url is required");
        }
        if (episodeDto.getDescription() == null || episodeDto.getDescription().isBlank()) {
            F_LOG.warn("{} tried to add an episode without an description.", role);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Description is required");
        }
        if (episodeDto.getDurationSeconds() == null) {
            F_LOG.warn("{} tried to add an episode without duration.", role);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Duration is required");
        }
        if (episodeDto.getPodcast() != null) {
            F_LOG.warn("{} tried to add an episode podcastDto instead of podcastId.", role);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "PodcastId, not podcastDto, is required");
        }
        if (episodeDto.getThumbnailUrl() != null && !episodeDto.getThumbnailUrl().isBlank()) {
            episode.setThumbnailUrl(episodeDto.getThumbnailUrl());
        }else {
            episode.setThumbnailUrl("https://default/thumbnail.url");
        }
        if (episodeDto.getImageUrl() != null && !episodeDto.getImageUrl().isBlank()) {
            episode.setImageUrl(episodeDto.getImageUrl());
        }else {
            episode.setImageUrl("https://default/image.url");
        }
        if (episodeDto.getPodcastId() == null) {
            F_LOG.warn("{} tried to add an episode without podcastId.", role);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "PodcastId is required");
        }
        Podcast podcast = podcastRepository.findById(episodeDto.getPodcastId()).orElseThrow(() -> {
            F_LOG.warn("{} tried to retrieve a podcast with id {} that doesn't exist.", role, episodeDto.getPodcastId());
            return new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    String.format("No podcast exists with id: %s.", episodeDto.getPodcast())
            );
        });
        episode.setTitle(episodeDto.getTitle());
        episode.setUrl(episodeDto.getUrl());
        episode.setDescription(episodeDto.getDescription());
        episode.setDurationSeconds(episodeDto.getDurationSeconds());
        episode.setReleaseDate(LocalDate.now());
        episode.setPodcast(podcast);
        podcast.getEpisodes().add(episode);

        F_LOG.info("{} added a episode with id {}.", role, episode.getId());
        return episodeRepository.save(episode);
    }

    @Transactional
    @Override
    public Episode updateEpisode(EpisodeDto episodeDto) {
        String role = userInfo.getRole();
        if(episodeDto.getId() == null) {
            F_LOG.warn("{} tried to retrieve an episode without providing an id {}.", role, episodeDto.getPodcastId());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Episode id is required");
        }
        Episode episode = episodeRepository.findById(episodeDto.getId()).orElseThrow(() -> {
            F_LOG.warn("{} tried to retrieve an episode with id {} that doesn't exist.", role, episodeDto.getPodcastId());
            return new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    String.format("No episode exists with id: %s.", episodeDto.getId())
            );
        });

        if (episodeDto.getTitle() != null && !episodeDto.getTitle().equals(episode.getTitle())) {
            if(episodeDto.getTitle().isBlank()) {
                F_LOG.warn("{} tried to update an episode with invalid title.", role);
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Title can not be left blank."
                );
            }
            episode.setTitle(episodeDto.getTitle());
        }
        if (episodeDto.getUrl() != null && !episodeDto.getUrl().equals(episode.getUrl())) {
            if(episodeDto.getUrl().isBlank()) {
                F_LOG.warn("{} tried to update an episode with invalid url.", role);
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Url can not be left blank."
                );
            }
            episode.setUrl(episodeDto.getUrl());
        }
        if (episodeDto.getDescription() != null && !episodeDto.getDescription().equals(episode.getDescription())) {
            if(episodeDto.getDescription().isBlank()) {
                F_LOG.warn("{} tried to update an episode with invalid description.", role);
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Description can not be left blank."
                );
            }
            episode.setDescription(episodeDto.getDescription());
        }
        if (episodeDto.getDurationSeconds() != null && !episodeDto.getDurationSeconds().equals(episode.getDurationSeconds())) {
            episode.setDurationSeconds(episodeDto.getDurationSeconds());
        }
        if (episodeDto.getReleaseDate() != null && !episodeDto.getReleaseDate().equals(episode.getReleaseDate())) {
            F_LOG.warn("{} tried to update an episodes release date, which is not allowed.", role);
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Release date can not be changed."
            );
        }
        if (episodeDto.getThumbnailUrl() != null && !episodeDto.getThumbnailUrl().equals(episode.getThumbnailUrl())) {
            episode.setThumbnailUrl(episodeDto.getThumbnailUrl());
        }
        if (episodeDto.getImageUrl() != null && !episodeDto.getImageUrl().equals(episode.getImageUrl())) {
            episode.setImageUrl(episodeDto.getImageUrl());
        }
        if (episodeDto.getPodcastId() != null && !episodeDto.getPodcastId().equals(episode.getPodcast().getId())) {
            episode.getPodcast().getEpisodes().remove(episode);
            Podcast podcast = podcastRepository.findById(episodeDto.getPodcastId()).orElseThrow(() -> {
                F_LOG.warn("{} tried to retrieve a podcast with id {} that doesn't exist.", role, episodeDto.getPodcastId());
                return new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        String.format("No podcast exists with id: %s.", episodeDto.getPodcast())
                );
            });
            episode.setPodcast(podcast);
            podcast.getEpisodes().add(episode);
        }

        F_LOG.info("{} updated an episode with id {}.", role, episode.getId());
        return episodeRepository.save(episode);
    }

    @Transactional
    @Override
    public String deleteEpisode(UUID episodeId) {
        String role = userInfo.getRole();
        if (episodeId == null) {
            F_LOG.warn("{} tried to delete an episode without providing an id.", role);
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Id must be provided"
            );
        }
        if (!episodeRepository.existsById(episodeId)) {
            F_LOG.warn("{} tried to delete an episode with id {} that doesn't exist.", role, episodeId);
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    String.format("No episode exists with id: %s.", episodeId)
            );
        }
        episodeRepository.deleteById(episodeId);

        F_LOG.info("{} deleted episode with id:", role, episodeId);
        return String.format("Episode with Id: %s has been successfully deleted.", episodeId);
    }

    @Override
    public EpisodeDto getEpisode(UUID episodeId) {
        String role = userInfo.getRole();
        if (episodeId == null) {
            F_LOG.warn("{} tried to retrieve an episode without providing an id.", role);
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Id must be provided"
            );
        }
        Episode episode = episodeRepository.findById(episodeId).orElseThrow(() -> {
            F_LOG.warn("{} tried to retrieve an episode with id {} that doesn't exist.", role, episodeId);
            return new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    String.format("No episode exists with id: %s.", episodeId)
            );
        });

        F_LOG.info("{} retrieved an episode with id {}.", role, episodeId);
        return fullDtoConverter.convertToFullEpisodeDto(episode);
    }

    @Override
    public List<EpisodeDto> getAllEpisodes() {
        List<Episode> episodes = episodeRepository.findAll();
        List<EpisodeDto> episodeDtos = new ArrayList<>();
        for (Episode episode : episodes) {
            episodeDtos.add(fullDtoConverter.convertToFullEpisodeDto(episode));
        }

        F_LOG.info("{} retrieved all episodes.", userInfo.getRole());
        return episodeDtos;
    }

    @Override
    public List<EpisodeDto> getEpisodesByPodcastId(UUID podcastId) {
        List<Episode> episodes = episodeRepository.findAllByPodcast_Id(podcastId);
        List<EpisodeDto> episodeDtos = new ArrayList<>();
        for (Episode episode : episodes) {
            episodeDtos.add(limitedDtoConverter.convertToLimitedEpisodeDto(episode));
        }

        F_LOG.info("{} retrieved episodes associated with podcastId {}.", userInfo.getRole(), podcastId);
        return episodeDtos;
    }

    @Override
    public Boolean episodeExists(UUID episodeId) {
        boolean exists = episodeRepository.existsById(episodeId);
        F_LOG.info("{} checked if episode exists", userInfo.getRole());
        return exists;
    }

    @Override
    public EpisodeDto addSeasonToEpisode(UUID episodeId, UUID seasonId) {
        String role = userInfo.getRole();
        if(episodeId == null) {
            F_LOG.warn("{} tried to add a season without providing episodeId.", role);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Episode id is required");
        }
        if(seasonId == null) {
            F_LOG.warn("{} tried to add a season without providing seasonId.", role);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Season id is required");
        }
        Episode episode = episodeRepository.findById(episodeId).orElseThrow(() -> {
            F_LOG.warn("{} tried to retrieve an episode with id {} that doesn't exist.", role, episodeId);
            return new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    String.format("No episode exists with id: %s.", episodeId)
            );
        });
        System.out.println(seasonId);
        if (episode.getSeasonId() == null || !episode.getSeasonId().equals(seasonId)) {
            episode.setSeasonId(seasonId);
            episodeRepository.save(episode);
        }

        F_LOG.info("{} added a season to episode with {}.", role, episodeId);
        return fullDtoConverter.convertToFullEpisodeDto(episode);
    }

    @Override
    public EpisodeDto removeSeasonFromEpisode(UUID episodeId, UUID seasonId) {
        String role = userInfo.getRole();
        if(episodeId == null) {
            F_LOG.warn("{} tried to remove a season without providing episodeId.", role);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Episode id is required");
        }
        if(seasonId == null) {
            F_LOG.warn("{} tried to remove a season without providing seasonId.", role);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Season id is required");
        }
        Episode episode = episodeRepository.findById(episodeId).orElseThrow(() -> {
            F_LOG.warn("{} tried to retrieve an episode with id {} that doesn't exist.", role, episodeId);
            return new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    String.format("No episode exists with id: %s.", episodeId)
            );
        });
        if (episode.getSeasonId() != null && episode.getSeasonId().equals(seasonId)) {
            episode.setSeasonId(null);
            episodeRepository.save(episode);
        } else {
            F_LOG.warn("{} tried to remove a seasonId is not currently assigned to episode with id {}.", role, episodeId);
            throw new ResponseStatusException(HttpStatus.CONFLICT, "SeasonId is not currently assigned to episode");
        }

        F_LOG.info("{} added removed season from episode with {}.", role, episodeId);
        return limitedDtoConverter.convertToLimitedEpisodeDto(episode);
    }

    @Override
    public Map<UUID, List<String>> getIdAndGenreFromUrl(String url) {
        String role = userInfo.getRole();
        Map<UUID, List<String>> idAndGenre = new HashMap<>();
        Episode episode = episodeRepository.findByUrl(url).orElseThrow(() -> {
            F_LOG.warn("{} tried to retrieve an episode with url {} that doesn't exist.", role, url);
            return new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    String.format("No episode exists with url: %s.", url)
            );
        });
        List<Genre> genres = episode.getPodcast().getGenres();
        List<String> genreNames = new ArrayList<>();
        for (Genre genre : genres) {
            genreNames.add(genre.getName());
        }

        idAndGenre.put(episode.getId(), genreNames);

        F_LOG.info("{} retrieved an episode with url {}.", role, url);
        return idAndGenre;
    }
}
