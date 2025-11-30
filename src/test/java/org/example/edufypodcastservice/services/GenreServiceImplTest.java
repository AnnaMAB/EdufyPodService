package org.example.edufypodcastservice.services;

import org.example.edufypodcastservice.converters.UserInfo;
import org.example.edufypodcastservice.dto.GenreDto;
import org.example.edufypodcastservice.dto.PodcastDto;
import org.example.edufypodcastservice.entities.Genre;
import org.example.edufypodcastservice.mapper.FullDtoConverter;
import org.example.edufypodcastservice.mapper.LimitedDtoConverter;
import org.example.edufypodcastservice.repositories.GenreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class GenreServiceImplTest {

    @Mock
    private GenreRepository genreRepositoryMock;

    @Mock
    private UserInfo userInfoMock;

    private final LimitedDtoConverter limitedDtoConverter = new LimitedDtoConverter();

    private final FullDtoConverter fullDtoConverter = new FullDtoConverter(limitedDtoConverter);

    @InjectMocks
    private GenreServiceImpl genreService;

    private Genre genre;
    private GenreDto dto;
    private PodcastDto podcastDto;

    private final UUID genreId = UUID.fromString("00000000-0000-0000-0000-000000000010");
    private final UUID podcastId = UUID.fromString("00000000-0000-0000-0000-000000000011");

    @BeforeEach
    void setUp() {
        genreService = new GenreServiceImpl(genreRepositoryMock, fullDtoConverter, limitedDtoConverter, userInfoMock);

        genre = new Genre();
        genre.setId(genreId);
        genre.setName("Genre One");
        genre.setImageUrl("image.png");
        genre.setThumbnailUrl("thumb.png");
        genre.setPodcasts(new ArrayList<>());

        dto = new GenreDto();
        dto.setId(genreId);
        dto.setName("Genre One");
        dto.setImageUrl("image.png");
        dto.setThumbnailUrl("thumb.png");
        dto.setPodcasts(new ArrayList<>());

        podcastDto = new PodcastDto();
        podcastDto.setId(podcastId);

        when(userInfoMock.getRole()).thenReturn("TEST_ROLE");
    }


    //addGenre()
    @Test
    void addGenre_ShouldSaveAndReturnGenre_WhenValidInput() {
        when(genreRepositoryMock.save(any(Genre.class))).thenReturn(genre);

        Genre result = genreService.addGenre(dto);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(dto.getName(), result.getName());
        assertEquals(dto.getImageUrl(), result.getImageUrl());
        assertEquals(dto.getThumbnailUrl(), result.getThumbnailUrl());
        verify(genreRepositoryMock).save(any(Genre.class));
    }

    @Test
    void addGenre_ShouldUseDefaultUrls_WhenImageOrThumbnailNull() {
        dto.setImageUrl(null);
        dto.setThumbnailUrl(null);

        when(genreRepositoryMock.save(any(Genre.class))).thenAnswer(i -> i.getArguments()[0]);

        Genre result = genreService.addGenre(dto);

        assertEquals("https://default/image.url", result.getImageUrl());
        assertEquals("https://default/thumbnail.url", result.getThumbnailUrl());
        verify(genreRepositoryMock).save(any(Genre.class));
    }

    @Test
    void addGenre_ShouldUseDefaultUrls_WhenImageOrThumbnailIsBlank() {
        dto.setImageUrl("");
        dto.setThumbnailUrl("");
        when(genreRepositoryMock.save(any(Genre.class))).thenAnswer(i -> i.getArguments()[0]);

        Genre result = genreService.addGenre(dto);

        assertEquals("https://default/image.url", result.getImageUrl());
        assertEquals("https://default/thumbnail.url", result.getThumbnailUrl());
        verify(genreRepositoryMock).save(any(Genre.class));
    }

    @Test
    void addGenre_ShouldThrow_WhenNameNull() {
        dto.setName(null);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            genreService.addGenre(dto);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertTrue(exception.getReason().contains("Name is required"));
        verify(genreRepositoryMock, never()).save(any(Genre.class));
    }

    @Test
    void addGenre_ShouldThrow_WhenNameBlank() {
        dto.setName("");

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            genreService.addGenre(dto);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertTrue(exception.getReason().contains("Name is required"));
        verify(genreRepositoryMock, never()).save(any(Genre.class));
    }

    @Test
    void addGenre_ShouldThrow_WhenGetPodcastsIsNotEmpty() {
        dto.getPodcasts().add(podcastDto);

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> genreService.addGenre(dto));

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertEquals("Podcasts can not be added from this page.", ex.getReason());
        verify(genreRepositoryMock, never()).save(any());
    }

    @Test
    void addGenre_ShouldSave_WhenPodcastsNull() {
        dto.setPodcasts(null);
        when(genreRepositoryMock.save(any(Genre.class))).thenAnswer(i -> i.getArguments()[0]);

        Genre result = genreService.addGenre(dto);

        assertNotNull(result);
        assertEquals(dto.getName(), result.getName());
        assertEquals(dto.getImageUrl(), result.getImageUrl());
        assertEquals(dto.getThumbnailUrl(), result.getThumbnailUrl());
        verify(genreRepositoryMock).save(any(Genre.class));
    }

    // updateGenre()
    @Test
    void updateGenre_ShouldSaveAndReturnGenre_WhenValidInput() {
        when(genreRepositoryMock.findById(genreId)).thenReturn(Optional.of(genre));
        when(genreRepositoryMock.save(any(Genre.class))).thenReturn(genre);

        dto.setName("Updated Genre");
        dto.setImageUrl("newImage.png");
        dto.setThumbnailUrl("newThumbnail.png");

        Genre updated = genreService.updateGenre(dto);

        assertEquals("Updated Genre", updated.getName());
        assertEquals("newImage.png", updated.getImageUrl());
        assertEquals("newThumbnail.png", updated.getThumbnailUrl());
        verify(genreRepositoryMock).save(any(Genre.class));
    }

    @Test
    void updateGenre_ShouldSaveAndReturnGenre_WithNoUpdateWhenNull() {
        when(genreRepositoryMock.findById(genreId)).thenReturn(Optional.of(genre));
        when(genreRepositoryMock.save(any(Genre.class))).thenReturn(genre);

        dto.setName(null);
        dto.setImageUrl(null);
        dto.setThumbnailUrl(null);
        dto.setPodcasts(null);

        Genre result = genreService.updateGenre(dto);

        assertEquals(genre.getName(), result.getName());
        assertEquals(genre.getImageUrl(), result.getImageUrl());
        assertEquals(genre.getThumbnailUrl(), result.getThumbnailUrl());
        assertEquals(genre.getPodcasts(), result.getPodcasts());
        verify(genreRepositoryMock).save(any(Genre.class));
    }

    @Test
    void updateGenre_ShouldSaveAndReturnGenre_WithDefaultValuesWhenNoUpdate() {
        when(genreRepositoryMock.findById(genreId)).thenReturn(Optional.of(genre));
        when(genreRepositoryMock.save(any(Genre.class))).thenReturn(genre);

        Genre result = genreService.updateGenre(dto);

        assertEquals(genre.getName(), result.getName());
        assertEquals(genre.getImageUrl(), result.getImageUrl());
        assertEquals(genre.getThumbnailUrl(), result.getThumbnailUrl());

        verify(genreRepositoryMock).save(any(Genre.class));
    }

    @Test
    void updateGenre_ShouldThrow_WhenIdNull() {
        dto.setId(null);

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> genreService.updateGenre(dto));

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertTrue(ex.getReason().contains("Genre id is required"));
        verify(genreRepositoryMock, never()).save(any());
    }

    @Test
    void updateGenre_ShouldThrow_WhenGenreNotFound() {
        when(genreRepositoryMock.findById(genreId)).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> genreService.updateGenre(dto));

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
        assertTrue(ex.getReason().contains("No genre exists with id"));
        verify(genreRepositoryMock, never()).save(any());
    }

    @Test
    void updateGenre_ShouldThrow_WhenNameBlank() {
        dto.setName("");
        when(genreRepositoryMock.findById(genreId)).thenReturn(Optional.of(genre));

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> genreService.updateGenre(dto));

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertTrue(ex.getReason().contains("Name is required"));
        verify(genreRepositoryMock, never()).save(any());
    }

    @Test
    void updateGenre_ShouldThrow_WhenPodcastsNotEmpty() {
        dto.getPodcasts().add(podcastDto);
        when(genreRepositoryMock.findById(genreId)).thenReturn(Optional.of(genre));

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> genreService.updateGenre(dto));

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertEquals("Podcasts can not be added from this page.", ex.getReason());
        verify(genreRepositoryMock, never()).save(any());
    }


    // deleteGenre()
    @Test
    void deleteGenre_ShouldDeleteGenre() {
        when(genreRepositoryMock.existsById(genreId)).thenReturn(true);

        genreService.deleteGenre(genreId);

        verify(genreRepositoryMock, times(1)).deleteById(genreId);
    }

    @Test
    void deleteGenre_ShouldThrow_WhenIdNull() {
        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> genreService.deleteGenre(null));

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertTrue(ex.getReason().contains("Id must be provided"));
        verify(genreRepositoryMock, never()).deleteById(any());
    }

    @Test
    void deleteGenre_ShouldThrow_WhenGenreDoesNotExist() {
        when(genreRepositoryMock.existsById(genreId)).thenReturn(false);

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> genreService.deleteGenre(genreId));

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
        assertTrue(ex.getReason().contains("No genre exists with id"));
        verify(genreRepositoryMock, never()).deleteById(any());
    }


    // getAllGenres()
    @Test
    void getAllGenres_ShouldReturnAllGenres() {
        List<Genre> genres = List.of(genre);
        when(genreRepositoryMock.findAll()).thenReturn(genres);

        List<GenreDto> result = genreService.getAllGenres();

        assertEquals(1, result.size());
        assertEquals(genre.getName(), result.get(0).getName());
        assertEquals(genre.getThumbnailUrl(), result.get(0).getThumbnailUrl());
        assertNull(result.get(0).getImageUrl());
        verify(genreRepositoryMock, times(1)).findAll();
    }

    // getGenreById()
    @Test
    void getGenreById_ShouldReturnGenreDto() {
        when(genreRepositoryMock.findById(genreId)).thenReturn(Optional.of(genre));

        GenreDto result = genreService.getGenreById(genreId);

        assertEquals(dto.getName(), result.getName());
        assertEquals(dto.getPodcasts(), result.getPodcasts());
        assertEquals(dto.getImageUrl(), result.getImageUrl());
        assertEquals(dto.getThumbnailUrl(), result.getThumbnailUrl());
        verify(genreRepositoryMock, times(1)).findById(genreId);
    }

    @Test
    void getGenreById_ShouldThrow_WhenIdNull() {
        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> genreService.getGenreById(null));

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertTrue(ex.getReason().contains("Id must be provided"));
        verify(genreRepositoryMock, never()).save(any());
    }

    @Test
    void getGenreById_ShouldThrow_WhenGenreNotFound() {
        when(genreRepositoryMock.findById(genreId)).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> {
            genreService.getGenreById(genreId);
        });

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
        assertEquals("No genre exists with id: " + genreId + ".", ex.getReason());
        verify(genreRepositoryMock, never()).save(any());
    }

}