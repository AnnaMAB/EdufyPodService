package org.example.edufypodcastservice.repositories;

import org.example.edufypodcastservice.entities.Episode;
import org.example.edufypodcastservice.entities.Podcast;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@ActiveProfiles("test")
@DataJpaTest
class EpisodeRepositoryTest {

    @Autowired
    private EpisodeRepository episodeRepository;

    @Autowired
    private PodcastRepository podcastRepository;

    @Autowired
    private GenreRepository genreRepository;

    Podcast podcast1;
    Podcast podcast2;

    @BeforeEach
    void setUp() {
        episodeRepository.deleteAll();
        podcastRepository.deleteAll();
        genreRepository.deleteAll();

        podcast1 = new Podcast();
        podcast1.setName("First Podcast");
        podcast1.setDescription("Test desc");
        podcast1.setProducerId(UUID.randomUUID());

        podcast2 = new Podcast();
        podcast2.setName("Podcast Two");
        podcast2.setDescription("Test desc");
        podcast2.setProducerId(UUID.randomUUID());

        podcastRepository.save(podcast1);
        podcastRepository.save(podcast2);

        Episode ep1 = new Episode();
        ep1.setTitle("Episode 1");
        ep1.setUrl("url1");
        ep1.setDescription("Desc 1");
        ep1.setReleaseDate(LocalDate.now());
        ep1.setDurationSeconds(300L);
        ep1.setPodcast(podcast1);

        Episode ep2 = new Episode();
        ep2.setTitle("Episode 2");
        ep2.setUrl("url2");
        ep2.setDescription("Desc 2");
        ep2.setReleaseDate(LocalDate.now());
        ep2.setDurationSeconds(320L);
        ep2.setPodcast(podcast1);

        Episode ep3 = new Episode();
        ep3.setTitle("Episode 3");
        ep3.setUrl("url3");
        ep3.setDescription("Desc 3");
        ep3.setReleaseDate(LocalDate.now());
        ep3.setDurationSeconds(250L);
        ep3.setPodcast(podcast2);

        episodeRepository.save(ep1);
        episodeRepository.save(ep2);
        episodeRepository.save(ep3);
    }

    @Test
    void testFindAllByPodcastId() {
        List<Episode> episodesForPodcast1 = episodeRepository.findAllByPodcast_Id(podcast1.getId());

        assertThat(episodesForPodcast1.size()).isEqualTo(2);

        assertThat(episodesForPodcast1.get(0).getTitle()).isEqualTo("Episode 1");
        assertThat(episodesForPodcast1.get(1).getTitle()).isEqualTo("Episode 2");
    }

    @Test
    void testFindAllByPodcastId_NoEpisodesFound() {
        UUID randomId = UUID.randomUUID();

        List<Episode> result = episodeRepository.findAllByPodcast_Id(randomId);

        assertThat(result.size()).isEqualTo(0);
    }

    @Test
    void testFindByUrl() {
        Optional<Episode> found = episodeRepository.findByUrl("url2");

        assertThat(found).isPresent();
        assertThat(found.get().getTitle()).isEqualTo("Episode 2");
    }

    @Test
    void testFindByUrl_NotFound() {
        Optional<Episode> found = episodeRepository.findByUrl("non-existing-url");

        assertThat(found).isEmpty();
    }

}