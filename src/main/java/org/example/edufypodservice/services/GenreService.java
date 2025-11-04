package org.example.edufypodservice.services;


import org.example.edufypodservice.dto.GenreDto;
import org.example.edufypodservice.entities.Genre;

import java.util.List;
import java.util.UUID;

public interface GenreService {

    Genre addGenre(GenreDto genreDto);
    Genre updateGenre(GenreDto genreDto);
    String deleteGenre(UUID genreId);
    List<GenreDto> getAllGenres();
    GenreDto getGenreById(UUID id);

}
