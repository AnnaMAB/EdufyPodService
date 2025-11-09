package org.example.edufypodcastservice.repositories;

import org.example.edufypodcastservice.entities.Podcast;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PodcastRepository extends JpaRepository<Podcast, UUID> {
    List<Podcast> findByName(String name);
    List<Podcast> findByGenres_Name(String genreName);
}
