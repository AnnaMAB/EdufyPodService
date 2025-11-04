package org.example.edufypodservice.controller;

import org.example.edufypodservice.dto.EpisodeDto;
import org.example.edufypodservice.dto.GenreDto;
import org.example.edufypodservice.dto.PodcastDto;
import org.example.edufypodservice.entities.Episode;
import org.example.edufypodservice.entities.Genre;
import org.example.edufypodservice.entities.Podcast;
import org.example.edufypodservice.services.EpisodeServiceImpl;
import org.example.edufypodservice.services.GenreServiceImpl;
import org.example.edufypodservice.services.PodcastServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequestMapping("/podcasts")
public class AddEditDeleteController {

    private final PodcastServiceImpl podcastService;
    private final EpisodeServiceImpl episodeService;
    private final GenreServiceImpl genreService;

    @Autowired
    public AddEditDeleteController(PodcastServiceImpl podcastService, EpisodeServiceImpl episodeService, GenreServiceImpl genreService) {
        this.podcastService = podcastService;
        this.episodeService = episodeService;
        this.genreService = genreService;
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/addpodcast")
    public ResponseEntity<Podcast> addPodcast(@RequestBody PodcastDto podcastDto) {
        return ResponseEntity.ok(podcastService.addPodcast(podcastDto));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/updatepodcast")
    public ResponseEntity<Podcast> updatePodcast(@RequestBody PodcastDto podcastDto) {
        return ResponseEntity.ok(podcastService.updatePodcast(podcastDto));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/deletepodcast/{id}")
    public ResponseEntity<String> deletePodcast(@PathVariable UUID id) {
        return ResponseEntity.ok(podcastService.deletePodcast(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/addepisode")
    public ResponseEntity<Episode> addEpisode(@RequestBody EpisodeDto episodeDto) {
        return ResponseEntity.ok(episodeService.addEpisode(episodeDto));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/updateepisode")
    public ResponseEntity<Episode> updateEpisode(@RequestBody EpisodeDto episodeDto) {
        return ResponseEntity.ok(episodeService.updateEpisode(episodeDto));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/deleteepisode/{id}")
    public ResponseEntity<String> deleteEpisode(@PathVariable UUID id) {
        return ResponseEntity.ok(episodeService.deleteEpisode(id));
    }

  @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/addgenre")
    public ResponseEntity<Genre> addGenre(@RequestBody GenreDto genreDto) {
        return ResponseEntity.ok(genreService.addGenre(genreDto));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/updategenre")
    public ResponseEntity<Genre> updateGenre(@RequestBody GenreDto genreDto) {
        return ResponseEntity.ok(genreService.updateGenre(genreDto));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/deletegenre/{id}")
    public ResponseEntity<String> deleteGenre(@PathVariable UUID id) {
        return ResponseEntity.ok(genreService.deleteGenre(id));
    }

}
