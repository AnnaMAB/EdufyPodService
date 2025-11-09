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
public class PodcastDtoConverter {

    private final EpisodeDtoConverter episodeDtoConverter;
    private final GenreDtoConverter genreDtoConverter;

    public PodcastDtoConverter(final EpisodeDtoConverter episodeDtoConverter,
                               final GenreDtoConverter genreDtoConverter) {
        this.episodeDtoConverter = episodeDtoConverter;
        this.genreDtoConverter = genreDtoConverter;
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
            EpisodeDto episodeDto = episodeDtoConverter.convertToLimitedEpisodeDto(episode);
            episodeDtos.add(episodeDto);
        }
        podcastDto.setEpisodes(episodeDtos);
        List<Genre> genres = podcast.getGenres();
        List<GenreDto> genreDtos = new ArrayList<>();
        for (Genre genre : genres) {
            GenreDto genreDto = genreDtoConverter.convertToFullGenreDto(genre);
            genreDtos.add(genreDto);
        }
        podcastDto.setGenres(genreDtos);
        return podcastDto;
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
