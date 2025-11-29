package org.example.edufypodcastservice.services;


import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.edufypodcastservice.converters.UserInfo;
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
    private final UserInfo userInfo;
    private static final Logger F_LOG = LogManager.getLogger("functionality");

    @Autowired
    public GenreServiceImpl(GenreRepository genreRepository, FullDtoConverter fullDtoConverter,
                            LimitedDtoConverter limitedDtoConverter, UserInfo userInfo) {
        this.genreRepository = genreRepository;
        this.fullDtoConverter = fullDtoConverter;
        this.limitedDtoConverter = limitedDtoConverter;
        this.userInfo = userInfo;
    }

    @Transactional
    @Override
    public Genre addGenre(GenreDto genreDto) {
        Genre genre = new Genre();
        if (genreDto.getName() == null || genreDto.getName().isBlank()) {
            F_LOG.warn("{} tried to add a genre without a name.", userInfo.getRole());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name is required");
        }
        if (genreDto.getImageUrl() != null || !genreDto.getImageUrl().isBlank()) {
            genre.setImageUrl(genreDto.getImageUrl());
        }else {
            genre.setImageUrl("https://default/image.url");
        }
        if (genreDto.getThumbnailUrl() != null || !genreDto.getThumbnailUrl().isBlank()) {
            genre.setThumbnailUrl(genreDto.getThumbnailUrl());
        }else {
            genre.setThumbnailUrl("https://default/thumbnail.url");
        }
        if (genreDto.getPodcasts() != null && !genreDto.getPodcasts().isEmpty()) {
            F_LOG.warn("{} tried to add podcast to a genre from the wrong endpoint.", userInfo.getRole());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Podcasts can not be added from this page."
            );
        }
        genre.setName(genreDto.getName());

        F_LOG.info("{} added a genre with id {}.", userInfo.getRole(), genre.getId());
        return genreRepository.save(genre);
    }

    @Transactional
    @Override
    public Genre updateGenre(GenreDto genreDto) {
        String role = userInfo.getRole();
        if(genreDto.getId() == null) {
            F_LOG.warn("{} tried to update a genre without providing an id.", role);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Genre id is required");
        }
        Genre genre = genreRepository.findById(genreDto.getId()).orElseThrow(() -> {
            F_LOG.warn("{} tried to retrieve a season with id {} that doesn't exist.", role, genreDto.getId());
            return new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    String.format("No genre exists with id: %s.", genreDto.getId())
            );
        });
        if (genreDto.getName() != null && !genreDto.getName().equals(genre.getName())) {
            if (genreDto.getName().isBlank()){
                F_LOG.warn("{} tried to update a genre with invalid name.", role);
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
            F_LOG.warn("{} tried to add podcast to a genre from the wrong endpoint.", role);
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Podcasts can not be added from this page."
            );
        }

        F_LOG.info("{} updated a genre with id {}.", role, genre.getId());
        return genreRepository.save(genre);
    }

    @Transactional
    @Override
    public String deleteGenre(UUID genreId) {
        String role = userInfo.getRole();
        if (genreId == null) {
            F_LOG.warn("{} tried to delete a genre without providing an id.", role);
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Id must be provided"
            );
        }
        if (!genreRepository.existsById(genreId)) {
            F_LOG.warn("{} tried to delete a genre with id {} that doesn't exist.", role, genreId);
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    String.format("No genre exists with id: %s.", genreId)
            );
        }
        genreRepository.deleteById(genreId);

        F_LOG.info("{} deleted genre with id:", role, genreId);
        return String.format("Genre with Id: %s has been successfully deleted.", genreId);
    }

    @Override
    public List<GenreDto> getAllGenres() {
        List<Genre> Genre = genreRepository.findAll();
        List<GenreDto> genresDto = new ArrayList<>();
        for (Genre genre : Genre) {
            genresDto.add(limitedDtoConverter.convertToLimitedGenreDto(genre));
        }

        F_LOG.info("{} retrieved all genres.", userInfo.getRole());
        return genresDto;
    }

    @Override
    public GenreDto getGenreById(UUID genreId) {
        String role = userInfo.getRole();
        if (genreId == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Id must be provided"
            );
        }
        Genre genre = genreRepository.findById(genreId).orElseThrow(() -> {
            F_LOG.warn("{} tried to retrieve a genre with id {} that doesn't exist.", role, genreId);
            return new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    String.format("No genre exists with id: %s.", genreId)
            );
        });

        F_LOG.info("{} retrieved genre with id {}.", role, genreId);
        return fullDtoConverter.convertToFullGenreDto(genre);
    }
}
