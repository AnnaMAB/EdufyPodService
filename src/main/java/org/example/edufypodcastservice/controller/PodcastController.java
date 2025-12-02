package org.example.edufypodcastservice.controller;

import org.example.edufypodcastservice.dto.PodcastDto;
import org.example.edufypodcastservice.entities.Podcast;
import org.example.edufypodcastservice.services.PodcastServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/pods/podcasts/podcasts")
public class PodcastController {

    private final PodcastServiceImpl podcastService;

    @Autowired
    public PodcastController(PodcastServiceImpl podcastService) {
        this.podcastService = podcastService;
    }


    @PreAuthorize("hasAnyRole('edufy_User','edufy_Admin')")
    @GetMapping("/getpodcast/{id}")
    public ResponseEntity<PodcastDto> getPodcastById(@PathVariable UUID id) {
        return ResponseEntity.ok(podcastService.getPodcastById(id));
    }


    @PreAuthorize("hasAnyRole('edufy_User','edufy_Admin')")
    @GetMapping("/allpodcasts")
    public ResponseEntity<List<PodcastDto>> getAllPodcasts() {
        return ResponseEntity.ok(podcastService.getAllPodcasts());
    }

    @PreAuthorize("hasAnyRole('edufy_User','edufy_Admin')")
    @GetMapping("/getpodcastnamed")
    public ResponseEntity<List<PodcastDto>> getPodcastByName(@RequestParam String name) {
        return ResponseEntity.ok(podcastService.getPodcastByName(name));
    }

    @PreAuthorize("hasAnyRole('edufy_User','edufy_Admin')")
    @GetMapping("/podcastbygenre")
    public ResponseEntity<List<PodcastDto>> getAllPodcastsByGenre(@RequestParam String genre) {
        return ResponseEntity.ok(podcastService.getPodcastsByGenre(genre));
    }

    @PreAuthorize("hasAnyRole('edufy_User','edufy_Admin')")
    @GetMapping("/{id}/exists")
    public ResponseEntity<Boolean> podcastExists(@PathVariable UUID id) {
        return ResponseEntity.ok(podcastService.podcastExists(id));
    }

    @PreAuthorize("hasAnyRole('edufy_User','edufy_Admin')")
    @GetMapping("/{podcastid}/producerassociated/{producerid}")
    public ResponseEntity<Boolean> producerAssociated(@PathVariable UUID podcastid, @PathVariable UUID producerid) {
        return ResponseEntity.ok(podcastService.podcastAssociatedWithProducer(podcastid, producerid));
    }

    @PreAuthorize("hasRole('edufy_Admin')")
    @PostMapping("/addpodcast")
    public ResponseEntity<Podcast> addPodcast(@RequestBody PodcastDto podcastDto) {
        return ResponseEntity.ok(podcastService.addPodcast(podcastDto));
    }

    @PreAuthorize("hasRole('edufy_Admin')")
    @PutMapping("/updatepodcast")
    public ResponseEntity<Podcast> updatePodcast(@RequestBody PodcastDto podcastDto) {
        return ResponseEntity.ok(podcastService.updatePodcast(podcastDto));
    }

    @PreAuthorize("hasRole('edufy_Admin')")
    @DeleteMapping("/deletepodcast/{id}")
    public ResponseEntity<String> deletePodcast(@PathVariable UUID id) {
        return ResponseEntity.ok(podcastService.deletePodcast(id));
    }

}
