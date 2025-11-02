package org.example.edufypodservice.services;


import org.example.edufypodservice.dto.GenreDto;

import java.util.List;
import java.util.UUID;

public interface GenreService {

    GenreDto addGenre(GenreDto genreDto);
    GenreDto updateGenre(GenreDto genreDto);
    String deleteGenre(GenreDto genreDto);
    List<GenreDto> getAllGenres();
    GenreDto getGenreById(UUID id);

}
