package org.example.edufypodservice.services;


import org.example.edufypodservice.dto.GenreDto;
import org.example.edufypodservice.repositories.GenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;

    @Autowired
    public GenreServiceImpl(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }


    @Override
    public GenreDto addGenre(GenreDto genreDto) {
        return null;
    }

    @Override
    public GenreDto updateGenre(GenreDto genreDto) {
        return null;
    }

    @Override
    public String deleteGenre(GenreDto genreDto) {
        return "";
    }

    @Override
    public List<GenreDto> getAllGenres() {
        return List.of();
    }
}
