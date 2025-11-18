package org.example.edufypodcastservice.controller;


import org.example.edufypodcastservice.dto.GenreDto;
import org.example.edufypodcastservice.entities.Genre;
import org.example.edufypodcastservice.services.EpisodeServiceImpl;
import org.example.edufypodcastservice.services.GenreServiceImpl;
import org.example.edufypodcastservice.services.PodcastServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/pods/podcasts/genres")
public class GenreController {

    private final GenreServiceImpl genreService;

    @Autowired
    public GenreController(GenreServiceImpl genreService) {
        this.genreService = genreService;
    }


    @GetMapping("/getgenre/{id}")
    public ResponseEntity<GenreDto> getGenreById(@PathVariable UUID id) {
        return ResponseEntity.ok(genreService.getGenreById(id));
    }


    @GetMapping("/allgenres")
    public ResponseEntity<List<GenreDto>> getAllGenres() {
        return ResponseEntity.ok(genreService.getAllGenres());
    }



    @PostMapping("/addgenre")
    public ResponseEntity<Genre> addGenre(@RequestBody GenreDto genreDto) {
        return ResponseEntity.ok(genreService.addGenre(genreDto));
    }


    @PutMapping("/updategenre")
    public ResponseEntity<Genre> updateGenre(@RequestBody GenreDto genreDto) {
        return ResponseEntity.ok(genreService.updateGenre(genreDto));
    }


    @DeleteMapping("/deletegenre/{id}")
    public ResponseEntity<String> deleteGenre(@PathVariable UUID id) {
        return ResponseEntity.ok(genreService.deleteGenre(id));
    }

}
