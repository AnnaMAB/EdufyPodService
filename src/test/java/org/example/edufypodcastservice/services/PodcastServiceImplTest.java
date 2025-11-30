package org.example.edufypodcastservice.services;

import org.example.edufypodcastservice.converters.UserInfo;
import org.example.edufypodcastservice.dto.EpisodeDto;
import org.example.edufypodcastservice.dto.GenreDto;
import org.example.edufypodcastservice.dto.PodcastDto;
import org.example.edufypodcastservice.entities.Episode;
import org.example.edufypodcastservice.entities.Genre;
import org.example.edufypodcastservice.entities.Podcast;
import org.example.edufypodcastservice.external.ProducerApiClient;
import org.example.edufypodcastservice.mapper.FullDtoConverter;
import org.example.edufypodcastservice.mapper.LimitedDtoConverter;
import org.example.edufypodcastservice.repositories.GenreRepository;
import org.example.edufypodcastservice.repositories.PodcastRepository;
import org.junit.jupiter.api.AfterEach;
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

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class PodcastServiceImplTest {

    @Mock
    private PodcastRepository podcastRepositoryMock;

    @Mock
    private GenreRepository genreRepositoryMock;

    @Mock
    private ProducerApiClient producerApiClientMock;

    @Mock
    private UserInfo userInfoMock;

    private final LimitedDtoConverter limitedDtoConverter = new LimitedDtoConverter();
    private final FullDtoConverter fullDtoConverter = new FullDtoConverter(limitedDtoConverter);

    @InjectMocks
    private PodcastServiceImpl podcastService;

    private PodcastDto dto;
    private Podcast podcast;
    private Genre genre;
    private GenreDto genreDto;

    private final UUID podcastId = UUID.fromString("00000000-0000-0000-0000-000000000101");
    private final UUID genreId = UUID.fromString("00000000-0000-0000-0000-000000000102");
    private final UUID producerId = UUID.fromString("00000000-0000-0000-0000-000000000103");


    @BeforeEach
    void setUp() {
        podcastService = new PodcastServiceImpl(
                podcastRepositoryMock, fullDtoConverter, genreRepositoryMock, producerApiClientMock, userInfoMock
        );

        when(userInfoMock.getRole()).thenReturn("TEST_ROLE");

        genre = new Genre();
        genre.setId(genreId);
        genre.setName("Genre One");
        genre.setPodcasts(new ArrayList<>());

        genreDto = new GenreDto();
        genreDto.setId(genreId);

        dto = new PodcastDto();
        dto.setId(podcastId);
        dto.setName("Podcast One");
        dto.setDescription("Description");
        dto.setGenres(List.of(genreDto));
        dto.setProducerId(producerId);
        dto.setImageUrl("image.png");
        dto.setThumbnailUrl("thumb.png");

        podcast = new Podcast();
        podcast.setId(podcastId);
        podcast.setName(dto.getName());
        podcast.setDescription(dto.getDescription());
        podcast.setProducerId(dto.getProducerId());
        podcast.setImageUrl(dto.getImageUrl());
        podcast.setThumbnailUrl(dto.getThumbnailUrl());
        podcast.setGenres(List.of(genre));
    }

    // addPodcast()
    @Test
    void addPodcast_ShouldCallProducerApi_WhenValidInput() {
        when(podcastRepositoryMock.save(any(Podcast.class))).thenReturn(podcast);
        when(producerApiClientMock.producerExists(producerId)).thenReturn(true);
        doNothing().when(producerApiClientMock).addPodcastToProducer(podcast.getId(), producerId);

        Podcast result = podcastService.addPodcast(dto);

        assertNotNull(result);
        assertEquals(podcastId, result.getId());
        assertEquals(dto.getName(), result.getName());
        assertEquals(dto.getDescription(), result.getDescription());
        assertEquals(dto.getProducerId(), result.getProducerId());
        assertEquals(dto.getImageUrl(), result.getImageUrl());
        assertEquals(dto.getThumbnailUrl(), result.getThumbnailUrl());
        verify(producerApiClientMock).addPodcastToProducer(podcast.getId(), producerId);
    }

    @Test
    void addPodcast_ShouldDeletePodcast_WhenProducerApiFails() {
        when(podcastRepositoryMock.save(any(Podcast.class))).thenReturn(podcast);
        when(producerApiClientMock.producerExists(producerId)).thenReturn(true);
        doThrow(new RuntimeException("API error")).when(producerApiClientMock)
                .addPodcastToProducer(podcast.getId(), producerId);
        doNothing().when(podcastRepositoryMock).deleteById(podcast.getId());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            podcastService.addPodcast(dto);
        });

        assertTrue(ex.getMessage().contains("Failed to add podcast to producer. Podcast deleted."));
        verify(podcastRepositoryMock).deleteById(podcast.getId());
    }

    // addPodcastDetails()
    @Test
    void addPodcastDetails_ShouldSave_WhenValidInput() {
        when(producerApiClientMock.producerExists(producerId)).thenReturn(true);
        when(genreRepositoryMock.findById(genreId)).thenReturn(Optional.of(genre));
        when(genreRepositoryMock.save(any(Genre.class))).thenAnswer(i -> i.getArguments()[0]);
        when(podcastRepositoryMock.save(any(Podcast.class))).thenAnswer(i -> {
            Podcast p = i.getArgument(0);
            p.setId(podcastId);
            return p;
        });

        Podcast result = podcastService.addPodcastDetails(dto);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(dto.getName(), result.getName());
        assertEquals(dto.getDescription(), result.getDescription());
        assertEquals(dto.getProducerId(), result.getProducerId());
        assertEquals(dto.getImageUrl(), result.getImageUrl());
        assertEquals(dto.getThumbnailUrl(), result.getThumbnailUrl());
        verify(podcastRepositoryMock).save(any(Podcast.class));
    }

    @Test
    void addPodcastDetails_ShouldUseDefaultUrls_WhenImageAndThumbnailNull() {
        dto.setImageUrl(null);
        dto.setThumbnailUrl(null);
        when(producerApiClientMock.producerExists(producerId)).thenReturn(true);
        when(genreRepositoryMock.findById(genreId)).thenReturn(Optional.of(genre));
        when(podcastRepositoryMock.save(any(Podcast.class))).thenAnswer(i -> {
            Podcast p = i.getArgument(0);
            p.setId(podcastId);
            return p;
        });

        Podcast result = podcastService.addPodcastDetails(dto);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(dto.getName(), result.getName());
        assertEquals(dto.getDescription(), result.getDescription());
        assertEquals(dto.getProducerId(), result.getProducerId());
        assertEquals("https://default/image.url", result.getImageUrl());
        assertEquals("https://default/thumbnail.url", result.getThumbnailUrl());
        verify(podcastRepositoryMock).save(any(Podcast.class));
    }

    @Test
    void addPodcastDetails_ShouldUseDefaultUrls_WhenImageAndThumbnailIsBlank() {
        dto.setImageUrl("");
        dto.setThumbnailUrl("");
        when(producerApiClientMock.producerExists(producerId)).thenReturn(true);
        when(genreRepositoryMock.findById(genreId)).thenReturn(Optional.of(genre));
        when(podcastRepositoryMock.save(any(Podcast.class))).thenAnswer(i -> {
            Podcast p = i.getArgument(0);
            p.setId(podcastId);
            return p;
        });

        Podcast result = podcastService.addPodcastDetails(dto);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(dto.getName(), result.getName());
        assertEquals(dto.getDescription(), result.getDescription());
        assertEquals(dto.getProducerId(), result.getProducerId());
        assertEquals("https://default/image.url", result.getImageUrl());
        assertEquals("https://default/thumbnail.url", result.getThumbnailUrl());
        verify(podcastRepositoryMock).save(any(Podcast.class));
    }

    @Test
    void addPodcastDetails_ShouldThrow_WhenNameNull() {
        dto.setName(null);

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> {
            podcastService.addPodcastDetails(dto);
        });

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertTrue(ex.getReason().contains("Name is required"));
        verify(podcastRepositoryMock, never()).save(any(Podcast.class));
    }

    @Test
    void addPodcastDetails_ShouldThrow_WhenNameIsBlank() {
        dto.setName("");

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> {
            podcastService.addPodcastDetails(dto);
        });

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertTrue(ex.getReason().contains("Name is required"));
        verify(podcastRepositoryMock, never()).save(any(Podcast.class));
    }

    @Test
    void addPodcastDetails_ShouldThrow_WhenDescriptionNull() {
        dto.setDescription(null);

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> {
            podcastService.addPodcastDetails(dto);
        });

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertTrue(ex.getReason().contains("Description is required"));
        verify(podcastRepositoryMock, never()).save(any(Podcast.class));
    }

    @Test
    void addPodcastDetails_ShouldThrow_WhenDescriptionIsBlank() {
        dto.setDescription("");

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> {
            podcastService.addPodcastDetails(dto);
        });

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertTrue(ex.getReason().contains("Description is required"));
        verify(podcastRepositoryMock, never()).save(any(Podcast.class));
    }

    @Test
    void addPodcastDetails_ShouldThrow_WhenGenresNull() {
        dto.setGenres(null);

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> {
            podcastService.addPodcastDetails(dto);
        });

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertTrue(ex.getReason().contains("Genres is required"));
        verify(podcastRepositoryMock, never()).save(any(Podcast.class));
    }

    @Test
    void addPodcastDetails_ShouldThrow_WhenGenresIsEmpty() {
        dto.setGenres(new ArrayList<>());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> {
            podcastService.addPodcastDetails(dto);
        });

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertTrue(ex.getReason().contains("Genres is required"));
        verify(podcastRepositoryMock, never()).save(any(Podcast.class));
    }

    @Test
    void addPodcastDetails_ShouldThrow_WhenProducerIdNull() {
        dto.setProducerId(null);

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> {
            podcastService.addPodcastDetails(dto);
        });

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertTrue(ex.getReason().contains("Producer is required"));
        verify(podcastRepositoryMock, never()).save(any(Podcast.class));
    }

    @Test
    void addPodcastDetails_ShouldThrow_WhenProducerDoesNotExist() {
        when(producerApiClientMock.producerExists(producerId)).thenReturn(false);

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> {
            podcastService.addPodcastDetails(dto);
        });

        assertEquals(HttpStatus.CONFLICT, ex.getStatusCode());
        assertTrue(ex.getReason().contains("Producer don't exists"));
        verify(podcastRepositoryMock, never()).save(any(Podcast.class));
    }

    @Test
    void addPodcastDetails_ShouldSkipGenre_WhenIdIsNull() {
        GenreDto noIdGenreDto = new GenreDto();
        noIdGenreDto.setName("No ID Genre");

        dto.setGenres(List.of(noIdGenreDto));
        when(producerApiClientMock.producerExists(producerId)).thenReturn(true);
        when(podcastRepositoryMock.save(any(Podcast.class))).thenAnswer(i -> i.getArguments()[0]);

        Podcast result = podcastService.addPodcastDetails(dto);

        assertNotNull(result);
        assertTrue(result.getGenres().isEmpty());
        verify(podcastRepositoryMock).save(any(Podcast.class));
    }


    // updatePodcast()
    @Test
    void updatePodcast_ShouldUpdateFields_WhenValidChangesProvided() {
        dto.setName("New Name");
        dto.setDescription("New Description");
        dto.setImageUrl("newImage.png");
        dto.setThumbnailUrl("newThumb.png");

        when(podcastRepositoryMock.findById(podcastId)).thenReturn(Optional.of(podcast));
        when(podcastRepositoryMock.save(any(Podcast.class))).thenAnswer(i -> i.getArgument(0));

        Podcast result = podcastService.updatePodcast(dto);

        assertEquals("New Name", result.getName());
        assertEquals("New Description", result.getDescription());
        assertEquals("newImage.png", result.getImageUrl());
        assertEquals("newThumb.png", result.getThumbnailUrl());
        assertEquals(dto.getProducerId(), result.getProducerId());
        verify(podcastRepositoryMock).save(any(Podcast.class));
    }

    @Test
    void updatePodcast_ShouldSavePodcast_WithNoUpdateWhenIsBlank() {
        List<Genre> genresOld = podcast.getGenres();
        dto.setImageUrl("");
        dto.setThumbnailUrl("");
        dto.setGenres(new ArrayList<>());

        when(podcastRepositoryMock.findById(podcastId)).thenReturn(Optional.of(podcast));
        when(podcastRepositoryMock.save(any(Podcast.class))).thenAnswer(i -> i.getArgument(0));

        Podcast result = podcastService.updatePodcast(dto);

        assertEquals(podcast.getGenres(), genresOld);
        assertEquals(podcast.getName(), result.getName());
        assertEquals(podcast.getDescription(), result.getDescription());
        assertEquals(podcast.getImageUrl(), result.getImageUrl());
        assertEquals(podcast.getThumbnailUrl(), result.getThumbnailUrl());
        verify(podcastRepositoryMock, times(1)).save(any(Podcast.class));
    }

    @Test
    void updatePodcast_ShouldSavePodcast_WithNoUpdateWhenNull() {
        List<Episode> episodesOld = podcast.getEpisodes();
        List<Genre> genresOld = podcast.getGenres();
        dto.setName(null);
        dto.setDescription(null);
        dto.setImageUrl(null);
        dto.setThumbnailUrl(null);
        dto.setProducerId(null);
        dto.setEpisodes(null);
        dto.setGenres(null);

        when(podcastRepositoryMock.findById(podcastId)).thenReturn(Optional.of(podcast));
        when(podcastRepositoryMock.save(any(Podcast.class))).thenAnswer(i -> i.getArgument(0));

        Podcast result = podcastService.updatePodcast(dto);

        assertEquals(podcast.getGenres(), genresOld);
        assertEquals(podcast.getEpisodes(), episodesOld);
        assertEquals(podcast.getName(), result.getName());
        assertEquals(podcast.getDescription(), result.getDescription());
        assertEquals(podcast.getImageUrl(), result.getImageUrl());
        assertEquals(podcast.getThumbnailUrl(), result.getThumbnailUrl());
        assertEquals(podcast.getProducerId(), result.getProducerId());
        verify(podcastRepositoryMock, times(1)).save(any(Podcast.class));
    }

    @Test
    void updatePodcast_ShouldAddGenres_WhenGenreIdProvided() {
        when(podcastRepositoryMock.findById(podcastId)).thenReturn(Optional.of(podcast));
        when(genreRepositoryMock.findById(genreId)).thenReturn(Optional.of(genre));
        when(podcastRepositoryMock.save(any(Podcast.class))).thenAnswer(i -> i.getArgument(0));

        podcast.setGenres(new ArrayList<>());
        dto.setGenres(List.of(genreDto));

        Podcast result = podcastService.updatePodcast(dto);

        assertEquals(dto.getName(), result.getName());
        assertEquals(dto.getDescription(), result.getDescription());
        assertFalse(result.getGenres().isEmpty());
        verify(genreRepositoryMock).save(any());
        verify(podcastRepositoryMock).save(any());
    }

    @Test
    void updatePodcast_ShouldNotAddGenres_WhenGenreIdNotProvided() {
        when(podcastRepositoryMock.findById(podcastId)).thenReturn(Optional.of(podcast));
        when(podcastRepositoryMock.save(any(Podcast.class))).thenAnswer(i -> i.getArgument(0));

        genreDto.setId(null);
        podcast.setGenres(new ArrayList<>());
        dto.setGenres(List.of(genreDto));

        Podcast result = podcastService.updatePodcast(dto);

        assertEquals(dto.getName(), result.getName());
        assertEquals(dto.getDescription(), result.getDescription());
        verify(genreRepositoryMock, never()).save(any());
        verify(podcastRepositoryMock).save(any());
    }

    @Test
    void updatePodcast_ShouldCallProducerApi_WhenProducerChanged() {
        when(podcastRepositoryMock.findById(podcastId)).thenReturn(Optional.of(podcast));
        dto.setProducerId(UUID.randomUUID());
        when(producerApiClientMock.producerExists(dto.getProducerId())).thenReturn(true);
        doNothing().when(producerApiClientMock).removePodcastFromProducer(podcastId, producerId);
        doNothing().when(producerApiClientMock).addPodcastToProducer(podcastId, dto.getProducerId());

        when(podcastRepositoryMock.save(any(Podcast.class))).thenAnswer(i -> i.getArgument(0));

        podcastService.updatePodcast(dto);

        verify(producerApiClientMock, times(1)).removePodcastFromProducer(podcastId, producerId);
        verify(producerApiClientMock, times(1)).addPodcastToProducer(podcastId, dto.getProducerId());
        verify(podcastRepositoryMock, times(1)).save(any(Podcast.class));
    }

    @Test
    void updatePodcast_ShouldThrow_WhenIdNull() {
        dto.setId(null);

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> podcastService.updatePodcast(dto));

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertEquals("Podcast id is required", ex.getReason());
        verify(podcastRepositoryMock, never()).save(any());
    }

    @Test
    void updatePodcast_ShouldThrow_WhenPodcastNotFound() {
        when(podcastRepositoryMock.findById(dto.getId())).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> podcastService.updatePodcast(dto));

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
        assertEquals("No podcast exists with id: " + podcastId + ".", ex.getReason());
        verify(podcastRepositoryMock, never()).save(any());
    }

    @Test
    void updatePodcast_ShouldThrow_WhenNameBlankOnUpdate() {
        when(podcastRepositoryMock.findById(podcastId)).thenReturn(Optional.of(podcast));
        dto.setName("");

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> podcastService.updatePodcast(dto));

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertTrue(ex.getReason().contains("Name can not be left blank."));
    }

    @Test
    void updatePodcast_ShouldThrow_WhenDescriptionBlankOnUpdate() {
        when(podcastRepositoryMock.findById(podcastId)).thenReturn(Optional.of(podcast));
        dto.setDescription("");

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> podcastService.updatePodcast(dto));

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertTrue(ex.getReason().contains("Description can not be left blank."));
    }

    @Test
    void updatePodcast_ShouldThrow_WhenEpisodesProvided() {
        when(podcastRepositoryMock.findById(podcastId)).thenReturn(Optional.of(podcast));

        dto.setEpisodes(List.of(new EpisodeDto()));

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> podcastService.updatePodcast(dto));

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertTrue(ex.getReason().contains("Episodes can not be modified"));
    }

    @Test
    void updatePodcast_ShouldThrow_WhenProducerDoesNotExist() {
        when(podcastRepositoryMock.findById(podcastId)).thenReturn(Optional.of(podcast));
        dto.setProducerId(UUID.randomUUID());
        when(producerApiClientMock.producerExists(dto.getProducerId())).thenReturn(false);

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> podcastService.updatePodcast(dto));

        assertEquals(HttpStatus.CONFLICT, ex.getStatusCode());
        verify(podcastRepositoryMock, never()).save(any());
    }


    // deletePodcast()
    @Test
    void deletePodcast_ShouldDeleteSuccessfully_WhenIdExists() {
        when(podcastRepositoryMock.findById(podcastId)).thenReturn(Optional.of(podcast));

        doNothing().when(producerApiClientMock)
                .removePodcastFromProducer(podcast.getId(), podcast.getProducerId());

        String result = podcastService.deletePodcast(podcastId);

        verify(podcastRepositoryMock, times(1)).deleteById(podcastId);
        verify(producerApiClientMock, times(1)).removePodcastFromProducer(podcast.getId(), podcast.getProducerId());
        assertTrue(result.contains("Podcast with Id: " + podcastId + ", and associated episodes have been successfully deleted."));
    }

    @Test
    void deletePodcast_ShouldThrowBadRequest_WhenIdIsNull() {
        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> podcastService.deletePodcast(null)
        );

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertTrue(ex.getReason().contains("Id must be provided"));
    }
    @Test
    void deletePodcast_ShouldThrowNotFound_WhenPodcastDoesNotExist() {
        when(podcastRepositoryMock.findById(podcastId)).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> podcastService.deletePodcast(podcastId)
        );

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
        assertTrue(ex.getReason().contains("No podcast exists with id"));
    }


    // getAllPodcasts()
    @Test
    void getAllPodcasts_ShouldReturnAllConvertedDtos() {
        List<Podcast> podcasts = List.of(podcast);
        when(podcastRepositoryMock.findAll()).thenReturn(podcasts);

        List<PodcastDto> result = podcastService.getAllPodcasts();

        assertEquals(1, result.size());
        assertEquals(podcast.getImageUrl(), result.get(0).getImageUrl());
        assertEquals(podcast.getName(), result.get(0).getName());
        assertEquals(podcast.getDescription(), result.get(0).getDescription());
        verify(podcastRepositoryMock).findAll();
    }

    // getPodcastById()
    @Test
    void getPodcastById_ShouldReturnDto_WhenExists() {
        UUID id = UUID.randomUUID();
        when(podcastRepositoryMock.findById(id)).thenReturn(Optional.of(podcast));

        PodcastDto result = podcastService.getPodcastById(id);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(podcast.getId(), result.getId());
        assertEquals(podcast.getProducerId(), result.getProducerId());
        assertEquals(podcast.getImageUrl(), result.getImageUrl());
        verify(podcastRepositoryMock).findById(id);

    }

    @Test
    void getPodcastById_ShouldThrowBadRequest_WhenIdIsNull() {
        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> podcastService.getPodcastById(null)
        );

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertTrue(ex.getReason().contains("Id must be provided"));
    }

    @Test
    void getPodcastById_ShouldThrowNotFound_WhenPodcastMissing() {
        UUID id = UUID.randomUUID();
        when(podcastRepositoryMock.findById(id)).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> podcastService.getPodcastById(id)
        );

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
        assertTrue(ex.getReason().contains("No podcast exists with id: " + id + "."));
    }


    // getPodcastByName()
    @Test
    void getPodcastByName_ShouldReturnDtoList() {
        when(podcastRepositoryMock.findByName("TestCast"))
                .thenReturn(List.of(podcast));

        List<PodcastDto> result = podcastService.getPodcastByName("TestCast");

        assertEquals(1, result.size());
        assertEquals(podcast.getImageUrl(), result.get(0).getImageUrl());
        assertEquals(podcast.getName(), result.get(0).getName());
        assertEquals(podcast.getDescription(), result.get(0).getDescription());
        verify(podcastRepositoryMock, times(1)).findByName("TestCast");
    }


    // getPodcastsByGenre()
    @Test
    void getPodcastsByGenre_ShouldReturnConvertedList() {
        when(podcastRepositoryMock.findByGenres_Name("News"))
                .thenReturn(List.of(podcast));
        List<PodcastDto> result = podcastService.getPodcastsByGenre("News");

        assertEquals(1, result.size());
        assertEquals(podcast.getImageUrl(), result.get(0).getImageUrl());
        assertEquals(podcast.getName(), result.get(0).getName());
        assertEquals(podcast.getDescription(), result.get(0).getDescription());
        verify(podcastRepositoryMock, times(1)).findByGenres_Name("News");
    }


    // podcastExists()
    @Test
    void podcastExists_ShouldReturnBoolean() {
        when(podcastRepositoryMock.existsById(podcastId)).thenReturn(true);

        Boolean exists = podcastService.podcastExists(podcastId);

        assertTrue(exists);
        verify(podcastRepositoryMock, times(1)).existsById(podcastId);
    }


    // podcastAssociatedWithProducer()
    @Test
    void podcastAssociatedWithProducer_ShouldReturnTrue_WhenMatched() {
        podcast.setProducerId(producerId);

        when(podcastRepositoryMock.findById(podcastId)).thenReturn(Optional.of(podcast));

        boolean result = podcastService.podcastAssociatedWithProducer(podcastId, producerId);

        assertTrue(result);
    }

    @Test
    void podcastAssociatedWithProducer_ShouldThrow_WhenPodcastIdNull() {
        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> podcastService.podcastAssociatedWithProducer(null, UUID.randomUUID())
        );

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertTrue(ex.getReason().contains("PodcastId must be provided"));
    }

    @Test
    void podcastAssociatedWithProducer_ShouldThrow_WhenPodcastNotFound() {
        UUID id = UUID.randomUUID();
        when(podcastRepositoryMock.findById(id)).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> podcastService.podcastAssociatedWithProducer(id, UUID.randomUUID())
        );

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
        assertTrue(ex.getReason().contains("No podcast exists with id: " + id + "."));
    }

    @Test
    void podcastAssociatedWithProducer_ShouldThrow_WhenProducerIdNull() {
        when(podcastRepositoryMock.findById(any())).thenReturn(Optional.of(podcast));

        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> podcastService.podcastAssociatedWithProducer(podcastId, null)
        );

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertTrue(ex.getReason().contains("ProducerId must be provided"));
    }

    @Test
    void podcastAssociatedWithProducer_ShouldReturnFalse() {
        podcast.setProducerId(null);
        when(podcastRepositoryMock.findById(any())).thenReturn(Optional.of(podcast));

        when(podcastRepositoryMock.findById(podcastId)).thenReturn(Optional.of(podcast));

        boolean result = podcastService.podcastAssociatedWithProducer(podcastId, producerId);

        assertFalse(result);
    }

    @Test
    void podcastAssociatedWithProducer_ShouldReturnFalse_WhenProducerMismatch() {
        podcast.setProducerId(UUID.randomUUID());

        when(podcastRepositoryMock.findById(podcastId)).thenReturn(Optional.of(podcast));

        boolean result = podcastService.podcastAssociatedWithProducer(podcastId, producerId);

        assertFalse(result);
    }

}