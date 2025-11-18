package org.example.edufypodcastservice.mapper;

import org.example.edufypodcastservice.dto.EpisodeDto;
import org.example.edufypodcastservice.dto.GenreDto;
import org.example.edufypodcastservice.dto.PodcastDto;
import org.example.edufypodcastservice.entities.Episode;
import org.example.edufypodcastservice.entities.Genre;
import org.example.edufypodcastservice.entities.Podcast;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class FullDtoConverter {

    private final LimitedDtoConverter limitedDtoConverter;

    public FullDtoConverter(LimitedDtoConverter limitedDtoConverter) {
        this.limitedDtoConverter = limitedDtoConverter;
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
        if (episode.getSeasonId() != null) {
            episodeDto.setSeasonId(episode.getSeasonId());
        }
        episodeDto.setPodcast(limitedDtoConverter.convertToLimitedPodcastDto(episode.getPodcast()));

        return episodeDto;
    }


    public PodcastDto convertToFullPodcastDto(Podcast podcast) {
        PodcastDto podcastDto = new PodcastDto();
        podcastDto.setId(podcast.getId());
        podcastDto.setName(podcast.getName());
        podcastDto.setDescription(podcast.getDescription());
        podcastDto.setThumbnailUrl(podcast.getThumbnailUrl());
        podcastDto.setImageUrl(podcast.getImageUrl());
        podcastDto.setProducerId(podcast.getProducerId());
        List<Episode> episodes = podcast.getEpisodes();
        List<EpisodeDto> episodeDtos = new ArrayList<>();
        for (Episode episode : episodes) {
            EpisodeDto episodeDto = limitedDtoConverter.convertToLimitedEpisodeDto(episode);
            episodeDtos.add(episodeDto);
        }
        podcastDto.setEpisodes(episodeDtos);
        List<Genre> genres = podcast.getGenres();
        List<GenreDto> genreDtos = new ArrayList<>();
        for (Genre genre : genres) {
            GenreDto genreDto = limitedDtoConverter.convertToLimitedGenreDto(genre);
            genreDtos.add(genreDto);
        }
        podcastDto.setGenres(genreDtos);
        return podcastDto;
    }

    public GenreDto convertToFullGenreDto(Genre genre) {
        GenreDto genreDto = new GenreDto();
        genreDto.setId(genre.getId());
        genreDto.setName(genre.getName());
        genreDto.setThumbnailUrl(genre.getThumbnailUrl());
        genreDto.setImageUrl(genre.getImageUrl());
        List<Podcast> podcasts = genre.getPodcasts();
        List<PodcastDto> podDtos = new ArrayList<>();
        for (Podcast podcast : podcasts) {
            PodcastDto podDto = limitedDtoConverter.convertToLimitedPodcastDto(podcast);
            podDtos.add(podDto);
        }
        genreDto.setPodcasts(podDtos);
        return genreDto;
    }


}
