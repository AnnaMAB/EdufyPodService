package org.example.edufypodcastservice.services;

import org.example.edufypodcastservice.converters.UserInfo;
import org.example.edufypodcastservice.dto.EpisodeDto;
import org.example.edufypodcastservice.dto.PodcastDto;
import org.example.edufypodcastservice.entities.Episode;
import org.example.edufypodcastservice.entities.Genre;
import org.example.edufypodcastservice.entities.Podcast;
import org.example.edufypodcastservice.mapper.FullDtoConverter;
import org.example.edufypodcastservice.mapper.LimitedDtoConverter;
import org.example.edufypodcastservice.repositories.EpisodeRepository;
import org.example.edufypodcastservice.repositories.PodcastRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class EpisodeServiceImplTest {

    @Mock
    private EpisodeRepository episodeRepositoryMock;
    @Mock
    private PodcastRepository podcastRepositoryMock;
    @Mock
    private UserInfo userInfoMock;

    private final LimitedDtoConverter limited = new LimitedDtoConverter();
    private final FullDtoConverter full = new FullDtoConverter(limited);

    @InjectMocks
    private EpisodeServiceImpl episodeService;

    private EpisodeDto dto;
    private Podcast podcast;
    private Episode episode;


    private final UUID podcastId = UUID.fromString("00000000-0000-0000-0000-000000000201");
    private final UUID episodeId = UUID.fromString("00000000-0000-0000-0000-000000000202");

    @BeforeEach
    void setUp() {
        episodeService = new EpisodeServiceImpl(episodeRepositoryMock, full, limited,
                podcastRepositoryMock, userInfoMock);

        dto = new EpisodeDto();
        dto.setTitle("Episode One");
        dto.setUrl("http://episode.url");
        dto.setDescription("Description of ep");
        dto.setDurationSeconds(300L);
        dto.setPodcastId(podcastId);
        dto.setImageUrl("img.png");
        dto.setThumbnailUrl("thumb.png");
        dto.setId(episodeId);
        dto.setReleaseDate(LocalDate.now());

        podcast = new Podcast();
        podcast.setId(podcastId);

        episode = new Episode();
        episode.setId(episodeId);
        episode.setPodcast(podcast);
        episode.setTitle(dto.getTitle());
        episode.setDescription(dto.getDescription());
        episode.setDurationSeconds(dto.getDurationSeconds());
        episode.setUrl(dto.getUrl());
        episode.setThumbnailUrl(dto.getThumbnailUrl());
        episode.setImageUrl(dto.getImageUrl());
        episode.setReleaseDate(dto.getReleaseDate());
    }

    // addEpisode
    @Test
    void addEpisode_ShouldAddEpisode_WhenValidInput() {
        when(podcastRepositoryMock.findById(podcastId)).thenReturn(Optional.of(podcast));
        when(episodeRepositoryMock.save(any())).thenAnswer(invocation -> {
            Episode ep = invocation.getArgument(0);
            if (ep.getId() == null) {
                ep.setId(UUID.randomUUID());
            }
            return ep;
        });

        Episode result = episodeService.addEpisode(dto);

        assertNotNull(result.getId());
        assertEquals(dto.getPodcastId(), result.getPodcast().getId());
        assertEquals(dto.getDescription(), result.getDescription());
        assertEquals(dto.getDurationSeconds(), result.getDurationSeconds());
        assertEquals(dto.getThumbnailUrl(), result.getThumbnailUrl());
        assertEquals(dto.getImageUrl(), result.getImageUrl());
        verify(episodeRepositoryMock).save(any(Episode.class));
    }

    @Test
    void addEpisode_ShouldUseDefaultThumbnailOrImage_WhenNull() {
        dto.setThumbnailUrl(null);
        dto.setImageUrl(null);
        when(podcastRepositoryMock.findById(podcastId)).thenReturn(Optional.of(podcast));
        when(episodeRepositoryMock.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Episode result = episodeService.addEpisode(dto);

        assertEquals("https://default/thumbnail.url", result.getThumbnailUrl());
        assertEquals("https://default/image.url", result.getImageUrl());
        verify(episodeRepositoryMock, times(1)).save(any());
    }

    @Test
    void addEpisode_ShouldUseDefaultThumbnailOrImage_WhenBlank() {
        dto.setImageUrl("");
        dto.setThumbnailUrl("");
        when(podcastRepositoryMock.findById(podcastId)).thenReturn(Optional.of(podcast));
        when(episodeRepositoryMock.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Episode result = episodeService.addEpisode(dto);

        assertEquals("https://default/thumbnail.url", result.getThumbnailUrl());
        assertEquals("https://default/image.url", result.getImageUrl());
        verify(episodeRepositoryMock, times(1)).save(any());
    }

    @Test
    void addEpisode_ShouldThrow_WhenTitleNull() {
        dto.setTitle(null);

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () ->
                episodeService.addEpisode(dto));

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertTrue(ex.getReason().contains("Title is required"));
        verify(episodeRepositoryMock, never()).save(any());
    }

    @Test
    void addEpisode_ShouldThrow_WhenTitleBlank() {
        dto.setTitle("");

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () ->
                episodeService.addEpisode(dto));

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertTrue(ex.getReason().contains("Title is required"));
        verify(episodeRepositoryMock, never()).save(any());
    }

    @Test
    void addEpisode_ShouldThrow_WhenUrlNull() {
        dto.setUrl(null);

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () ->
                episodeService.addEpisode(dto));

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertTrue(ex.getReason().contains("Url is required"));
        verify(episodeRepositoryMock, never()).save(any());
    }

    @Test
    void addEpisode_ShouldThrow_WhenUrlBlank() {
        dto.setUrl("");

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () ->
                episodeService.addEpisode(dto));

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertTrue(ex.getReason().contains("Url is required"));
        verify(episodeRepositoryMock, never()).save(any());
    }

    @Test
    void addEpisode_ShouldThrow_WhenDescriptionNull() {
        dto.setDescription(null);

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () ->
                episodeService.addEpisode(dto));

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertTrue(ex.getReason().contains("Description is required"));
        verify(episodeRepositoryMock, never()).save(any());
    }

    @Test
    void addEpisode_ShouldThrow_WhenDescriptionBlank() {
        dto.setDescription(" ");

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () ->
                episodeService.addEpisode(dto));

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertTrue(ex.getReason().contains("Description is required"));
        verify(episodeRepositoryMock, never()).save(any());
    }

    @Test
    void addEpisode_ShouldThrow_WhenDurationNull() {
        dto.setDurationSeconds(null);

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () ->
                episodeService.addEpisode(dto));

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertTrue(ex.getReason().contains("Duration is required"));
        verify(episodeRepositoryMock, never()).save(any());
    }

    @Test
    void addEpisode_ShouldThrow_WhenPodcastDtoProvidedInsteadOfId() {
        dto.setPodcast(new PodcastDto());
        dto.setPodcastId(null);

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () ->
                episodeService.addEpisode(dto));

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertTrue(ex.getReason().contains("PodcastId, not podcastDto, is required"));
        verify(episodeRepositoryMock, never()).save(any());
    }

    @Test
    void addEpisode_ShouldThrow_WhenPodcastIdNull() {
        dto.setPodcastId(null);

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () ->
                episodeService.addEpisode(dto));

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertTrue(ex.getReason().contains("PodcastId is required"));
        verify(episodeRepositoryMock, never()).save(any());
    }

    @Test
    void addEpisode_ShouldThrow_WhenPodcastNotFound() {
        when(podcastRepositoryMock.findById(podcastId)).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () ->
                episodeService.addEpisode(dto));

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
        assertEquals("No podcast exists with id: " + dto.getPodcastId() + ".", ex.getReason());
        verify(episodeRepositoryMock, never()).save(any());
    }

    // updateEpisode()
    @Test
    void updateEpisode_ShouldUpdateEpisode_WhenValidInput() {
        UUID newPodcastId = UUID.randomUUID();
        Podcast newPodcast = new Podcast();
        newPodcast.setId(newPodcastId);

        dto.setId(episode.getId());
        dto.setPodcastId(newPodcastId);
        dto.setTitle("Updated Title");
        dto.setUrl("http://updated.url");
        dto.setDescription("Updated Description");
        dto.setDurationSeconds(400L);
        dto.setThumbnailUrl("updated_thumb.png");
        dto.setImageUrl("updated_img.png");
        dto.setPodcastId(newPodcastId);

        when(episodeRepositoryMock.findById(episode.getId())).thenReturn(Optional.of(episode));
        when(podcastRepositoryMock.findById(newPodcastId)).thenReturn(Optional.of(newPodcast));
        when(episodeRepositoryMock.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Episode result = episodeService.updateEpisode(dto);

        assertEquals(dto.getTitle(), result.getTitle());
        assertEquals(dto.getUrl(), result.getUrl());
        assertEquals(dto.getDescription(), result.getDescription());
        assertEquals(dto.getDurationSeconds(), result.getDurationSeconds());
        assertEquals(dto.getThumbnailUrl(), result.getThumbnailUrl());
        assertEquals(dto.getImageUrl(), result.getImageUrl());
        verify(episodeRepositoryMock, times(1)).save(any());
    }

    @Test
    void updateEpisode_ShouldSaveEpisode_WithNoUpdateWhenIsBlank() {
        dto.setThumbnailUrl("");
        dto.setImageUrl("");

        when(episodeRepositoryMock.findById(episode.getId())).thenReturn(Optional.of(episode));
        when(episodeRepositoryMock.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Episode result = episodeService.updateEpisode(dto);

        assertEquals(episode.getThumbnailUrl(), result.getThumbnailUrl());
        assertEquals(episode.getImageUrl(), result.getImageUrl());
        verify(episodeRepositoryMock, times(1)).save(any());
    }

    @Test
    void updateEpisode_ShouldSaveEpisode_WithNoUpdateWhenIsSame() {
        when(episodeRepositoryMock.findById(episode.getId())).thenReturn(Optional.of(episode));
        when(episodeRepositoryMock.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Episode result = episodeService.updateEpisode(dto);

        assertEquals(episode.getThumbnailUrl(), result.getThumbnailUrl());
        assertEquals(episode.getImageUrl(), result.getImageUrl());
        verify(episodeRepositoryMock, times(1)).save(any());
    }

    @Test
    void updateEpisode_ShouldSaveWithoutUpdate_WhenNull() {
        dto.setId(episode.getId());
        dto.setPodcastId(null);
        dto.setTitle(null);
        dto.setUrl(null);
        dto.setDescription(null);
        dto.setDurationSeconds(null);
        dto.setThumbnailUrl(null);
        dto.setImageUrl(null);

        when(episodeRepositoryMock.findById(episode.getId())).thenReturn(Optional.of(episode));
        when(episodeRepositoryMock.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Episode result = episodeService.updateEpisode(dto);

        assertEquals(episode.getTitle(), result.getTitle());
        assertEquals(episode.getUrl(), result.getUrl());
        assertEquals(episode.getDescription(), result.getDescription());
        assertEquals(episode.getDurationSeconds(), result.getDurationSeconds());
        assertEquals(episode.getThumbnailUrl(), result.getThumbnailUrl());
        assertEquals(episode.getImageUrl(), result.getImageUrl());
        verify(episodeRepositoryMock, times(1)).save(any());
    }

    @Test
    void updateEpisode_ShouldThrow_WhenIdNull() {
        dto.setId(null);

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () ->
                episodeService.updateEpisode(dto));

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertTrue(ex.getReason().contains("Episode id is required"));
        verify(episodeRepositoryMock, never()).save(any());
    }

    @Test
    void updateEpisode_ShouldThrow_WhenEpisodeNotFound() {
        when(episodeRepositoryMock.findById(dto.getId())).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () ->
                episodeService.updateEpisode(dto));

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
        assertTrue(ex.getReason().contains("No episode exists with id"));
        verify(episodeRepositoryMock, never()).save(any());
    }

    @Test
    void updateEpisode_ShouldThrow_WhenTitleBlank() {
        dto.setTitle("");

        when(episodeRepositoryMock.findById(dto.getId())).thenReturn(Optional.of(episode));

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () ->
                episodeService.updateEpisode(dto));

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertTrue(ex.getReason().contains("Title can not be left blank"));
        verify(episodeRepositoryMock, never()).save(any());
    }

    @Test
    void updateEpisode_ShouldThrow_WhenUrlBlank() {
        dto.setUrl("");

        when(episodeRepositoryMock.findById(dto.getId())).thenReturn(Optional.of(episode));

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () ->
                episodeService.updateEpisode(dto));

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertTrue(ex.getReason().contains("Url can not be left blank"));
        verify(episodeRepositoryMock, never()).save(any());
    }

    @Test
    void updateEpisode_ShouldThrow_WhenDescriptionBlank() {
        dto.setDescription("");

        when(episodeRepositoryMock.findById(dto.getId())).thenReturn(Optional.of(episode));

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () ->
                episodeService.updateEpisode(dto));

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertTrue(ex.getReason().contains("Description can not be left blank"));
        verify(episodeRepositoryMock, never()).save(any());
    }

    @Test
    void updateEpisode_ShouldThrow_WhenReleaseDateChanged() {
        dto.setReleaseDate(LocalDate.now().plusDays(1));

        when(episodeRepositoryMock.findById(episode.getId())).thenReturn(Optional.of(episode));

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () ->
                episodeService.updateEpisode(dto));

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertTrue(ex.getReason().contains("Release date can not be changed"));
        verify(episodeRepositoryMock, never()).save(any());
    }

    @Test
    void updateEpisode_ShouldThrow_WhenPodcastNotFound() {
        dto.setPodcastId(UUID.randomUUID());

        when(episodeRepositoryMock.findById(episode.getId())).thenReturn(Optional.of(episode));
        when(podcastRepositoryMock.findById(dto.getPodcastId())).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () ->
                episodeService.updateEpisode(dto));

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
        assertTrue(ex.getReason().contains("No podcast exists with id: " + dto.getPodcastId() + "."));
        verify(episodeRepositoryMock, never()).save(any());
    }


    //deleteEpisode()
    @Test
    void deleteEpisode_ShouldDeleteEpisode_WhenValidId() {
        UUID episodeId = episode.getId();

        when(episodeRepositoryMock.existsById(episodeId)).thenReturn(true);
        doNothing().when(episodeRepositoryMock).deleteById(episodeId);

        String result = episodeService.deleteEpisode(episodeId);

        assertEquals("Episode with Id: " + episodeId + " has been successfully deleted.", result);
        verify(episodeRepositoryMock, times(1)).deleteById(episodeId);
    }

    @Test
    void deleteEpisode_ShouldThrow_WhenIdNull() {
        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () ->
                episodeService.deleteEpisode(null));

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertTrue(ex.getReason().contains("Id must be provided"));
        verify(episodeRepositoryMock, never()).deleteById(any());
    }

    @Test
    void deleteEpisode_ShouldThrow_WhenEpisodeDoesNotExist() {
        UUID nonExistentId = UUID.randomUUID();
        when(episodeRepositoryMock.existsById(nonExistentId)).thenReturn(false);

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () ->
                episodeService.deleteEpisode(nonExistentId));

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
        assertEquals("No episode exists with id: " + nonExistentId + ".", ex.getReason());
        verify(episodeRepositoryMock, never()).deleteById(any());
    }

    // getEpisode()
    @Test
    void getEpisode_ShouldReturnEpisodeDto_WhenValidId() {
        UUID episodeId = episode.getId();
        when(episodeRepositoryMock.findById(episodeId)).thenReturn(Optional.of(episode));

        EpisodeDto result = episodeService.getEpisode(episodeId);

        assertNotNull(result);
        assertEquals(episode.getDescription(), result.getDescription());
        assertEquals(episode.getTitle(), result.getTitle());
        assertEquals(episode.getThumbnailUrl(), result.getThumbnailUrl());
        assertEquals(episode.getImageUrl(), result.getImageUrl());
        assertEquals(episode.getReleaseDate(), result.getReleaseDate());
        verify(episodeRepositoryMock).findById(episodeId);
    }

    @Test
    void getEpisode_ShouldThrow_WhenIdNull() {
        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () ->
                episodeService.getEpisode(null));

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertTrue(ex.getReason().contains("Id must be provided"));
        verify(episodeRepositoryMock, never()).findById(any());
    }

    @Test
    void getEpisode_ShouldThrow_WhenEpisodeDoesNotExist() {
        UUID episodeId = UUID.randomUUID();
        when(episodeRepositoryMock.findById(episodeId)).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () ->
                episodeService.getEpisode(episodeId));

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
        assertEquals("No episode exists with id: " + episodeId + ".", ex.getReason());
        verify(episodeRepositoryMock).findById(episodeId);
    }


    // getAllEpisodes()
    @Test
    void getAllEpisodes_ShouldReturnAllEpisodes() {
        List<Episode> episodes = List.of(episode);
        when(episodeRepositoryMock.findAll()).thenReturn(episodes);

        List<EpisodeDto> result = episodeService.getAllEpisodes();

        assertEquals(1, result.size());
        assertEquals(episode.getDescription(), result.get(0).getDescription());
        assertEquals(episode.getTitle(), result.get(0).getTitle());
        assertEquals(episode.getThumbnailUrl(), result.get(0).getThumbnailUrl());
        verify(episodeRepositoryMock).findAll();
    }

    // getEpisodesByPodcastId()
    @Test
    void getEpisodesByPodcastId_ShouldReturnEpisodes_WhenValidPodcastId() {
        List<Episode> episodes = List.of(episode);
        when(episodeRepositoryMock.findAllByPodcast_Id(podcastId)).thenReturn(episodes);

        List<EpisodeDto> result = episodeService.getEpisodesByPodcastId(podcastId);

        assertEquals(1, result.size());
        assertEquals(episode.getDescription(), result.get(0).getDescription());
        assertEquals(episode.getTitle(), result.get(0).getTitle());
        assertEquals(episode.getThumbnailUrl(), result.get(0).getThumbnailUrl());
        verify(episodeRepositoryMock).findAllByPodcast_Id(podcastId);
    }


    // episodeExists()
    @Test
    void episodeExists_ShouldReturnTrue_WhenEpisodeExists() {
        UUID episodeId = episode.getId();
        when(episodeRepositoryMock.existsById(episodeId)).thenReturn(true);

        Boolean result = episodeService.episodeExists(episodeId);

        assertTrue(result);
        verify(episodeRepositoryMock, times(1)).existsById(episodeId);
    }

    @Test
    void episodeExists_ShouldReturnFalse_WhenEpisodeDoesNotExist() {
        UUID episodeId = UUID.randomUUID();
        when(episodeRepositoryMock.existsById(episodeId)).thenReturn(false);

        Boolean result = episodeService.episodeExists(episodeId);

        assertFalse(result);
        verify(episodeRepositoryMock, times(1)).existsById(episodeId);
    }


    // addSeasonToEpisode()
    @Test
    void addSeasonToEpisode_ShouldAddSeason_WhenValidIds() {
        UUID seasonId = UUID.randomUUID();
        episode.setSeasonId(null);

        when(episodeRepositoryMock.findById(episodeId)).thenReturn(Optional.of(episode));
        when(episodeRepositoryMock.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        EpisodeDto result = episodeService.addSeasonToEpisode(episodeId, seasonId);

        assertEquals(dto.getId(), result.getId());
        assertEquals(seasonId, episode.getSeasonId());
        verify(episodeRepositoryMock).findById(episodeId);
        verify(episodeRepositoryMock).save(episode);
    }

    @Test
    void addSeasonToEpisode_ShouldReplaceExistingSeason_WhenDifferentSeasonProvided() {
        UUID originalSeasonId = UUID.randomUUID();
        UUID newSeasonId = UUID.randomUUID();

        episode.setSeasonId(originalSeasonId);

        when(episodeRepositoryMock.findById(episodeId)).thenReturn(Optional.of(episode));
        when(episodeRepositoryMock.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        EpisodeDto result = episodeService.addSeasonToEpisode(episodeId, newSeasonId);

        assertEquals(dto.getId(), result.getId());
        assertEquals(newSeasonId, episode.getSeasonId());
        verify(episodeRepositoryMock).findById(episodeId);
        verify(episodeRepositoryMock).save(episode);
    }


    @Test
    void addSeasonToEpisode_ShouldNotSave_WhenSeasonAlreadySet() {
        UUID seasonId = UUID.randomUUID();
        episode.setSeasonId(seasonId);

        when(episodeRepositoryMock.findById(episodeId)).thenReturn(Optional.of(episode));

        EpisodeDto result = episodeService.addSeasonToEpisode(episodeId, seasonId);

        assertEquals(dto.getId(), result.getId());
        assertEquals(seasonId, episode.getSeasonId());
        verify(episodeRepositoryMock).findById(episodeId);
        verify(episodeRepositoryMock, never()).save(any());
    }

    @Test
    void addSeasonToEpisode_ShouldThrow_WhenEpisodeIdNull() {
        UUID seasonId = UUID.randomUUID();

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () ->
                episodeService.addSeasonToEpisode(null, seasonId));

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertTrue(ex.getReason().contains("Episode id is required"));
        verify(episodeRepositoryMock, never()).findById(any());
        verify(episodeRepositoryMock, never()).save(any());
    }

    @Test
    void addSeasonToEpisode_ShouldThrow_WhenSeasonIdNull() {

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () ->
                episodeService.addSeasonToEpisode(episodeId, null));

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertTrue(ex.getReason().contains("Season id is required"));
        verify(episodeRepositoryMock, never()).findById(any());
        verify(episodeRepositoryMock, never()).save(any());
    }

    @Test
    void addSeasonToEpisode_ShouldThrow_WhenEpisodeNotFound() {
        UUID seasonId = UUID.randomUUID();

        when(episodeRepositoryMock.findById(episodeId)).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () ->
                episodeService.addSeasonToEpisode(episodeId, seasonId));

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
        assertEquals("No episode exists with id: " + episodeId + ".", ex.getReason());
        verify(episodeRepositoryMock).findById(episodeId);
        verify(episodeRepositoryMock, never()).save(any());
    }


    // removeSeasonFromEpisode()
    @Test
    void removeSeasonFromEpisode_ShouldRemoveSeason_WhenSeasonMatches() {
        UUID seasonId = UUID.randomUUID();
        episode.setSeasonId(seasonId);

        when(episodeRepositoryMock.findById(episodeId)).thenReturn(Optional.of(episode));
        when(episodeRepositoryMock.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        EpisodeDto result = episodeService.removeSeasonFromEpisode(episodeId, seasonId);

        assertNull(episode.getSeasonId());
        assertEquals(dto.getId(), result.getId());
        assertEquals(episode.getId(), result.getId());
        verify(episodeRepositoryMock).findById(episodeId);
        verify(episodeRepositoryMock).save(episode);
    }

    @Test
    void removeSeasonFromEpisode_ShouldThrow_WhenEpisodeIdNull() {
        UUID seasonId = UUID.randomUUID();

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () ->
                episodeService.removeSeasonFromEpisode(null, seasonId));

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertTrue(ex.getReason().contains("Episode id is required"));
        verify(episodeRepositoryMock, never()).findById(any());
        verify(episodeRepositoryMock, never()).save(any());
    }

    @Test
    void removeSeasonFromEpisode_ShouldThrow_WhenSeasonIdNull() {
        UUID episodeId = episode.getId();

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () ->
                episodeService.removeSeasonFromEpisode(episodeId, null));

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertTrue(ex.getReason().contains("Season id is required"));
        verify(episodeRepositoryMock, never()).findById(any());
        verify(episodeRepositoryMock, never()).save(any());
    }

    @Test
    void removeSeasonFromEpisode_ShouldThrow_WhenEpisodeNotFound() {
        UUID seasonId = UUID.randomUUID();

        when(episodeRepositoryMock.findById(episodeId)).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () ->
                episodeService.removeSeasonFromEpisode(episodeId, seasonId));

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
        assertEquals("No episode exists with id: " + episodeId + ".", ex.getReason());
        verify(episodeRepositoryMock).findById(episodeId);
        verify(episodeRepositoryMock, never()).save(any());
    }

    @Test
    void removeSeasonFromEpisode_ShouldThrow_WhenSeasonNotAssigned() {
        UUID seasonId = UUID.randomUUID();
        episode.setSeasonId(null);

        when(episodeRepositoryMock.findById(episodeId)).thenReturn(Optional.of(episode));

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () ->
                episodeService.removeSeasonFromEpisode(episodeId, seasonId));

        assertEquals(HttpStatus.CONFLICT, ex.getStatusCode());
        assertTrue(ex.getReason().contains("SeasonId is not currently assigned to episode"));
        verify(episodeRepositoryMock).findById(episodeId);
        verify(episodeRepositoryMock, never()).save(any());
    }

    @Test
    void removeSeasonFromEpisode_ShouldThrow_WhenSeasonNotSame() {
        UUID seasonId = UUID.randomUUID();
        episode.setSeasonId(UUID.randomUUID());

        when(episodeRepositoryMock.findById(episodeId)).thenReturn(Optional.of(episode));

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () ->
                episodeService.removeSeasonFromEpisode(episodeId, seasonId));

        assertEquals(HttpStatus.CONFLICT, ex.getStatusCode());
        assertTrue(ex.getReason().contains("SeasonId is not currently assigned to episode"));
        verify(episodeRepositoryMock).findById(episodeId);
        verify(episodeRepositoryMock, never()).save(any());
    }

    // getIdAndGenreFromUrl()
    @Test
    void getIdAndGenreFromUrl_ShouldReturnIdAndGenres_WhenEpisodeExists() {
        String url = "http://example.com/episode";
        Genre genre1 = new Genre(); genre1.setName("Comedy");
        Genre genre2 = new Genre(); genre2.setName("Education");
        podcast.setGenres(List.of(genre1, genre2));
        episode.setPodcast(podcast);
        episode.setUrl(url);

        when(episodeRepositoryMock.findByUrl(url)).thenReturn(Optional.of(episode));

        Map<UUID, List<String>> result = episodeService.getIdAndGenreFromUrl(url);

        assertTrue(result.containsKey(episode.getId()));
        List<String> genres = result.get(episode.getId());
        assertEquals(2, genres.size());
        assertTrue(genres.contains("Comedy"));
        assertTrue(genres.contains("Education"));
        verify(episodeRepositoryMock).findByUrl(url);
    }

    @Test
    void getIdAndGenreFromUrl_ShouldThrow_WhenEpisodeNotFound() {
        String url = "http://example.com/nonexistent";

        when(episodeRepositoryMock.findByUrl(url)).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () ->
                episodeService.getIdAndGenreFromUrl(url));

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
        assertEquals("No episode exists with url: " + url + ".", ex.getReason());
        verify(episodeRepositoryMock).findByUrl(url);
    }

}