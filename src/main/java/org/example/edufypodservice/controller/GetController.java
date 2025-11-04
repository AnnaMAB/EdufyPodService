package org.example.edufypodservice.controller;


import org.example.edufypodservice.dto.EpisodeDto;
import org.example.edufypodservice.dto.GenreDto;
import org.example.edufypodservice.dto.PodcastDto;
import org.example.edufypodservice.services.EpisodeServiceImpl;
import org.example.edufypodservice.services.GenreServiceImpl;
import org.example.edufypodservice.services.PodcastServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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


    @GetMapping("/getpodcast/{id}")
    public ResponseEntity<PodcastDto> getPodcastById(@PathVariable UUID id) {
        return ResponseEntity.ok(podcastService.getPodcastById(id));
    }


    @GetMapping("/podcasts")
    public ResponseEntity<List<PodcastDto>> getAllPodcasts() {
        return ResponseEntity.ok(podcastService.getAllPodcasts());
    }


    @GetMapping("/getpodcastnamed")
    public ResponseEntity<List<PodcastDto>> getPodcastByName(@RequestParam String name) {
        return ResponseEntity.ok(podcastService.getPodcastByName(name));
    }


    @GetMapping("/podcastbygenre")
    public ResponseEntity<List<PodcastDto>> getAllPodcastsByGenre(@RequestParam String genre) {
        return ResponseEntity.ok(podcastService.getPodcastsByGenre(genre));
    }



    @GetMapping("/getepisode/{id}")
    public ResponseEntity<EpisodeDto> getEpisodeById(@PathVariable UUID id) {
        return ResponseEntity.ok(episodeService.getEpisode(id));
    }


    @GetMapping("/episodes")
    public ResponseEntity<List<EpisodeDto>> getAllEpisodes() {
        return ResponseEntity.ok(episodeService.getAllEpisodes());
    }


    @GetMapping("/getpodcastepisodes/")
    public ResponseEntity<List<EpisodeDto>> getEpisodesByPodcast(@RequestParam UUID id) {
        return ResponseEntity.ok(episodeService.getEpisodesByPodcastId(id));
    }


    @GetMapping("/getgenre/{id}")
    public ResponseEntity<GenreDto> getGenreById(@PathVariable UUID id) {
        return ResponseEntity.ok(genreService.getGenreById(id));
    }


    @GetMapping("/genres")
    public ResponseEntity<List<GenreDto>> getAllGenres() {
        return ResponseEntity.ok(genreService.getAllGenres());
    }



}
