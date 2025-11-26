package org.example.edufypodcastservice.controller;


import org.example.edufypodcastservice.dto.EpisodeDto;
import org.example.edufypodcastservice.entities.Episode;
import org.example.edufypodcastservice.services.EpisodeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/pods/podcasts/episodes")
public class EpisodeController {

    private final EpisodeServiceImpl episodeService;


    @Autowired
    public EpisodeController(EpisodeServiceImpl episodeService) {
        this.episodeService = episodeService;
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/getepisode/{id}")
    public ResponseEntity<EpisodeDto> getEpisodeById(@PathVariable UUID id) {
        return ResponseEntity.ok(episodeService.getEpisode(id));
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/allepisodes")
    public ResponseEntity<List<EpisodeDto>> getAllEpisodes() {
        return ResponseEntity.ok(episodeService.getAllEpisodes());
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/getpodcastepisodes/{podcastId}")
    public ResponseEntity<List<EpisodeDto>> getEpisodesByPodcast(@PathVariable UUID podcastId) {
        return ResponseEntity.ok(episodeService.getEpisodesByPodcastId(podcastId));
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/getidandgenrefromurl")
    public ResponseEntity<Map<UUID, List<String>>> getIdAndGenreFromUrl(@RequestParam String url) {
        return ResponseEntity.ok(episodeService.getIdAndGenreFromUrl(url));
    }


    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/{id}/exists")
    public ResponseEntity<Boolean> episodeExists(@PathVariable UUID id) {
        return ResponseEntity.ok(episodeService.episodeExists(id));
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
    @PutMapping("/addseasontoepisode/{episodeId}/{seasonId}")
    public ResponseEntity<EpisodeDto> addSeasonToEpisode(@PathVariable UUID episodeId, @PathVariable UUID seasonId) {
        return ResponseEntity.ok(episodeService.addSeasonToEpisode(episodeId, seasonId));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/removeseasonfromepisode/{episodeId}/{seasonId}")
    public ResponseEntity<EpisodeDto> removeSeasonFromEpisode(@PathVariable UUID episodeId, @PathVariable UUID seasonId) {
        return ResponseEntity.ok(episodeService.removeSeasonFromEpisode(episodeId, seasonId));
    }

}
