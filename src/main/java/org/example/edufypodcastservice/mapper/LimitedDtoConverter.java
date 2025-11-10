package org.example.edufypodcastservice.mapper;

import org.example.edufypodcastservice.dto.EpisodeDto;
import org.example.edufypodcastservice.dto.GenreDto;
import org.example.edufypodcastservice.dto.PodcastDto;
import org.example.edufypodcastservice.entities.Episode;
import org.example.edufypodcastservice.entities.Genre;
import org.example.edufypodcastservice.entities.Podcast;
import org.springframework.stereotype.Component;

@Component
public class LimitedDtoConverter {

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


    public GenreDto convertToLimitedGenreDto(Genre genre) {
        GenreDto genreDto = new GenreDto();
        genreDto.setId(genre.getId());
        genreDto.setName(genre.getName());
        genreDto.setThumbnailUrl(genre.getThumbnailUrl());
        return genreDto;
    }


    public PodcastDto convertToLimitedPodcastDto(Podcast podcast) {
        PodcastDto podcastDto = new PodcastDto();
        podcastDto.setId(podcast.getId());
        podcastDto.setName(podcast.getName());
        podcastDto.setDescription(podcast.getDescription());
        podcastDto.setThumbnailUrl(podcast.getThumbnailUrl());
        return podcastDto;
    }


}
