package org.example.edufypodcastservice.services;


import jakarta.transaction.Transactional;
import org.example.edufypodcastservice.dto.GenreDto;
import org.example.edufypodcastservice.entities.Genre;
import org.example.edufypodcastservice.mapper.FullDtoConverter;
import org.example.edufypodcastservice.mapper.LimitedDtoConverter;
import org.example.edufypodcastservice.repositories.GenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Service
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;
    private final FullDtoConverter fullDtoConverter;
    private final LimitedDtoConverter limitedDtoConverter;

    @Autowired
    public GenreServiceImpl(GenreRepository genreRepository, FullDtoConverter fullDtoConverter, LimitedDtoConverter limitedDtoConverter) {
        this.genreRepository = genreRepository;
        this.fullDtoConverter = fullDtoConverter;
        this.limitedDtoConverter = limitedDtoConverter;
    }

    @Transactional
    @Override
    public Genre addGenre(GenreDto genreDto) {
        Genre genre = new Genre();
        if (genreDto.getName() == null || genreDto.getName().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name is required");
        }
        if (genreDto.getImageUrl() != null || !genreDto.getImageUrl().isBlank()) {
            genre.setImageUrl(genreDto.getImageUrl());
        }
        if (genreDto.getThumbnailUrl() != null || !genreDto.getThumbnailUrl().isBlank()) {
            genre.setThumbnailUrl(genreDto.getThumbnailUrl());
        }
        if (genreDto.getPodcasts() != null && !genreDto.getPodcasts().isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Podcasts can not be added from this page."
            );
        }
        genre.setName(genreDto.getName());
        return genreRepository.save(genre);
    }

    @Transactional
    @Override
    public Genre updateGenre(GenreDto genreDto) {
        if(genreDto.getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Genre id is required");
        }
        Genre genre = genreRepository.findById(genreDto.getId()).orElseThrow(() -> {
            //   F_LOG.warn("{} tried to book a workout with id {} that doesn't exist.", role, workoutToBook.getId());
            return new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    String.format("No genre exists with id: %s.", genreDto.getId())
            );
        });
        if (genreDto.getName() != null && !genreDto.getName().equals(genre.getName())) {
            if (genreDto.getName().isBlank()){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name is required");
            }
            genre.setName(genreDto.getName());
        }
        if (genreDto.getImageUrl() != null || !genreDto.getImageUrl().equals(genre.getImageUrl())) {
            genre.setImageUrl(genreDto.getImageUrl());
        }
        if (genreDto.getThumbnailUrl() != null || !genreDto.getThumbnailUrl().equals(genre.getThumbnailUrl())) {
            genre.setThumbnailUrl(genreDto.getThumbnailUrl());
        }
        if (genreDto.getPodcasts() != null && !genreDto.getPodcasts().isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Podcasts can not be added from this page."
            );
        }
        return genreRepository.save(genre);
    }

    @Transactional
    @Override
    public String deleteGenre(UUID genreId) {
        if (genreId == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Id must be provided"
            );
        }
        if (!genreRepository.existsById(genreId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    String.format("No genre exists with id: %s.", genreId)
            );
        }
        genreRepository.deleteById(genreId);
        return String.format("Genre with Id: %s has been successfully deleted.", genreId);
    }

    @Override
    public List<GenreDto> getAllGenres() {
        List<Genre> Genre = genreRepository.findAll();
        List<GenreDto> genresDto = new ArrayList<>();
        for (Genre genre : Genre) {
            genresDto.add(limitedDtoConverter.convertToLimitedGenreDto(genre));
        }
        return genresDto;
    }

    @Override
    public GenreDto getGenreById(UUID id) {
        if (id == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Id must be provided"
            );
        }
        Genre genre = genreRepository.findById(id).orElseThrow(() -> {
         //   F_LOG.warn("{} tried to book a workout with id {} that doesn't exist.", role, workoutToBook.getId());
            return new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    String.format("No genre exists with id: %s.", id)
            );
        });
        return fullDtoConverter.convertToFullGenreDto(genre);
    }
}
