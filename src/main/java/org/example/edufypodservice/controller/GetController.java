package org.example.edufypodservice.controller;


import org.example.edufypodservice.dto.PodcastDto;
import org.example.edufypodservice.services.EpisodeServiceImpl;
import org.example.edufypodservice.services.GenreServiceImpl;
import org.example.edufypodservice.services.PodcastServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/podcasts")
public class GetController {

    private final PodcastServiceImpl podcastService;
    private final EpisodeServiceImpl episodeService;
    private final GenreServiceImpl genreService;

    @Autowired
    public GetController(PodcastServiceImpl podcastService, EpisodeServiceImpl episodeService, GenreServiceImpl genreService) {
        this.podcastService = podcastService;
        this.episodeService = episodeService;
        this.genreService = genreService;
    }
/*
    List<GenreDto> getAllGenres();
    GenreDto getGenreById(UUID id);

    List<PodcastDto> getAllPodcasts();
    PodcastDto getPodcastById(int id);
    PodcastDto getPodcastByName(String podcastName);
    List<PodcastDto> getPodcastsByGenre(String genre);

  EpisodeDto getEpisode(UUID episodeId);
    List<EpisodeDto> getAllEpisodes();
    List<EpisodeDto> getEpisodesByPodcastId(UUID podcastId);
  */

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/getpodcast/{id}")
    public ResponseEntity<PodcastDto> getPodcast(@PathVariable UUID id) {
        return ResponseEntity.ok(podcastService.getPodcastById(id));
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/podcasts")
    public ResponseEntity<List<PodcastDto>> getAllPodcasts() {
        return ResponseEntity.ok(podcastService.getAllPodcasts());
    }


    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/podcasts")
    public ResponseEntity<List<PodcastDto>> getAllPodcastsByGenre(@RequestParam String genre) {
        return ResponseEntity.ok(podcastService.getPodcastsByGenre(genre));
    }

}
