package org.example.edufypodcastservice.mapper;

import org.example.edufypodcastservice.dto.GenreDto;
import org.example.edufypodcastservice.dto.PodcastDto;
import org.example.edufypodcastservice.entities.Genre;
import org.example.edufypodcastservice.entities.Podcast;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class GenreDtoConverter {

    private PodcastDtoConverter podcastDtoConverter;

    public GenreDtoConverter(PodcastDtoConverter podcastDtoConverter) {
        this.podcastDtoConverter = podcastDtoConverter;
    }

    public GenreDto convertToLimitedGenreDto(Genre genre) {
        GenreDto genreDto = new GenreDto();
        genreDto.setId(genre.getId());
        genreDto.setName(genre.getName());
        genreDto.setThumbnailUrl(genre.getThumbnailUrl());
        return genreDto;
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
            PodcastDto podDto = podcastDtoConverter.convertToLimitedPodcastDto(podcast);
            podDtos.add(podDto);
        }
        genreDto.setPodcasts(podDtos);
        return genreDto;
    }


}
