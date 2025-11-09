package org.example.edufypodcastservice.mapper;

import org.example.edufypodcastservice.dto.EpisodeDto;
import org.example.edufypodcastservice.entities.Episode;
import org.springframework.stereotype.Component;

@Component
public class EpisodeDtoConverter {

    private final PodcastDtoConverter podcastDtoConverter;

    public EpisodeDtoConverter(PodcastDtoConverter podcastDtoConverter) {
        this.podcastDtoConverter = podcastDtoConverter;
    }

    public EpisodeDto convertToFullEpisodeDto(Episode episode) {
        EpisodeDto episodeDto = new EpisodeDto();
        episodeDto.setId(episode.getId());
        episodeDto.setTitle(episode.getTitle());
        episodeDto.setUrl(episode.getUrl());
        episodeDto.setDurationSeconds(episode.getDurationSeconds());
        episodeDto.setDescription(episode.getDescription());
        episodeDto.setReleaseDate(episode.getReleaseDate());
        episodeDto.setImageUrl(episode.getImageUrl());
        episodeDto.setThumbnailUrl(episode.getThumbnailUrl());
        episodeDto.setPodcast(podcastDtoConverter.convertToLimitedPodcastDto(episode.getPodcast()));

        return episodeDto;
    }


    public EpisodeDto convertToLimitedEpisodeDto(Episode episode) {
        EpisodeDto episodeDto = new EpisodeDto();
        episodeDto.setId(episode.getId());
        episodeDto.setTitle(episode.getTitle());
        episodeDto.setUrl(episode.getUrl());
        episodeDto.setDurationSeconds(episode.getDurationSeconds());
        episodeDto.setDescription(episode.getDescription());
        episodeDto.setReleaseDate(episode.getReleaseDate());
        episodeDto.setThumbnailUrl(episode.getThumbnailUrl());
        episodeDto.setPodcastId(episode.getPodcast().getId());
        return episodeDto;
    }


}
