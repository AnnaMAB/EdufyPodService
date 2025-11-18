package org.example.edufypodcastservice.controller;

import org.example.edufypodcastservice.dto.PodcastDto;
import org.example.edufypodcastservice.entities.Podcast;
import org.example.edufypodcastservice.services.PodcastServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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


    @GetMapping("/getpodcast/{id}")
    public ResponseEntity<PodcastDto> getPodcastById(@PathVariable UUID id) {
        return ResponseEntity.ok(podcastService.getPodcastById(id));
    }


    @GetMapping("/allpodcasts")
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

    @GetMapping("/{id}/exists")
    public ResponseEntity<Boolean> podcastExists(@PathVariable UUID id) {
        return ResponseEntity.ok(podcastService.podcastExists(id));
    }

    @GetMapping("/{podcastid}/producerassociated/{producerid}")
    public ResponseEntity<Boolean> producerAssociated(@PathVariable UUID podcastid, @PathVariable UUID producerid) {
        return ResponseEntity.ok(podcastService.podcastAssociatedWithProducer(podcastid, producerid));
    }


    @PostMapping("/addpodcast")
    public ResponseEntity<Podcast> addPodcast(@RequestBody PodcastDto podcastDto) {
        return ResponseEntity.ok(podcastService.addPodcast(podcastDto));
    }


    @PutMapping("/updatepodcast")
    public ResponseEntity<Podcast> updatePodcast(@RequestBody PodcastDto podcastDto) {
        return ResponseEntity.ok(podcastService.updatePodcast(podcastDto));
    }


    @DeleteMapping("/deletepodcast/{id}")
    public ResponseEntity<String> deletePodcast(@PathVariable UUID id) {
        return ResponseEntity.ok(podcastService.deletePodcast(id));
    }

}
