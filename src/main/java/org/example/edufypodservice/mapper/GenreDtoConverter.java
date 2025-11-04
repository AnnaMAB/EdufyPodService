package org.example.edufypodservice.mapper;

import org.example.edufypodservice.dto.GenreDto;
import org.example.edufypodservice.entities.Genre;
import org.springframework.stereotype.Component;

@Component
public class GenreDtoConverter {

    public GenreDto convertToFullGenreDto(Genre genre) {
        GenreDto genreDto = new GenreDto();
        genreDto.setId(genre.getId());
        genreDto.setName(genre.getName());
        //genreDto.setPodcasts(); //TODO--------------------------------
        return genreDto;

    }


}
