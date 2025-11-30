package org.example.edufypodcastservice.repositories;

import org.example.edufypodcastservice.entities.Genre;
import org.example.edufypodcastservice.entities.Podcast;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ActiveProfiles("test")
@DataJpaTest
class PodcastRepositoryTest {

    @Autowired
    private PodcastRepository podcastRepository;

    @Autowired
    private GenreRepository genreRepository;

    private Podcast podcast1;
    private Podcast podcast2;
    private Genre tech;
    private Genre comedy;

    @BeforeEach
    void setUp() {
        genreRepository.deleteAll();
        podcastRepository.deleteAll();

        tech = new Genre();
        tech.setName("Technology");

        comedy = new Genre();
        comedy.setName("Comedy");

        genreRepository.save(tech);
        genreRepository.save(comedy);



        podcast1 = new Podcast();
        podcast1.setName("Tech Talks");
        podcast1.setDescription("A tech podcast");
        podcast1.setProducerId(UUID.randomUUID());
        podcast1.getGenres().add(tech);
        tech.getPodcasts().add(podcast1);

        podcast2 = new Podcast();
        podcast2.setName("Funny Times");
        podcast2.setDescription("A comedy podcast");
        podcast2.setProducerId(UUID.randomUUID());
        podcast2.getGenres().add(comedy);
        comedy.getPodcasts().add(podcast2);

        podcastRepository.save(podcast1);
        podcastRepository.save(podcast2);

    }

    @Test
    void testFindByName() {
        List<Podcast> results = podcastRepository.findByName("Tech Talks");

        assertThat(results.size()).isEqualTo(1);
        assertThat(results.get(0).getName()).isEqualTo("Tech Talks");
        assertThat(results.get(0).getDescription()).isEqualTo("A tech podcast");
    }

    @Test
    void testFindByGenres_Name() {
        List<Podcast> results = podcastRepository.findByGenres_Name("Technology");

        assertThat(results.size()).isEqualTo(1);
        assertThat(results.get(0).getName()).isEqualTo("Tech Talks");
        assertThat(results.get(0).getDescription()).isEqualTo("A tech podcast");
    }
}