package org.example.edufypodcastservice.controller;


import org.example.edufypodcastservice.dto.EpisodeDto;
import org.example.edufypodcastservice.entities.Episode;
import org.example.edufypodcastservice.services.EpisodeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/pods/podcasts/episodes")
public class EpisodeController {

    private final EpisodeServiceImpl episodeService;


    @Autowired
    public EpisodeController(EpisodeServiceImpl episodeService) {
        this.episodeService = episodeService;
    }


    @GetMapping("/getepisode/{id}")
    public ResponseEntity<EpisodeDto> getEpisodeById(@PathVariable UUID id) {
        return ResponseEntity.ok(episodeService.getEpisode(id));
    }


    @GetMapping("/allepisodes")
    public ResponseEntity<List<EpisodeDto>> getAllEpisodes() {
        return ResponseEntity.ok(episodeService.getAllEpisodes());
    }


    @GetMapping("/getpodcastepisodes/{podcastId}")
    public ResponseEntity<List<EpisodeDto>> getEpisodesByPodcast(@PathVariable UUID podcastId) {
        return ResponseEntity.ok(episodeService.getEpisodesByPodcastId(podcastId));
    }


    @GetMapping("/{id}/exists")
    public ResponseEntity<Boolean> episodeExists(@PathVariable UUID id) {
        return ResponseEntity.ok(episodeService.episodeExists(id));
    }



    @PostMapping("/addepisode")
    public ResponseEntity<Episode> addEpisode(@RequestBody EpisodeDto episodeDto) {
        return ResponseEntity.ok(episodeService.addEpisode(episodeDto));
    }


    @PutMapping("/updateepisode")
    public ResponseEntity<Episode> updateEpisode(@RequestBody EpisodeDto episodeDto) {
        return ResponseEntity.ok(episodeService.updateEpisode(episodeDto));
    }


    @DeleteMapping("/deleteepisode/{id}")
    public ResponseEntity<String> deleteEpisode(@PathVariable UUID id) {
        return ResponseEntity.ok(episodeService.deleteEpisode(id));
    }


    @PutMapping("/addseasontoepisode/{episodeId}/{seasonId}")
    public ResponseEntity<EpisodeDto> addSeasonToEpisode(@PathVariable UUID episodeId, @PathVariable UUID seasonId) {
        return ResponseEntity.ok(episodeService.addSeasonToEpisode(episodeId, seasonId));
    }

    @PutMapping("/removeseasonfromepisode/{episodeId}/{seasonId}")
    public ResponseEntity<EpisodeDto> removeSeasonFromEpisode(@PathVariable UUID episodeId, @PathVariable UUID seasonId) {
        return ResponseEntity.ok(episodeService.removeSeasonFromEpisode(episodeId, seasonId));
    }

}
