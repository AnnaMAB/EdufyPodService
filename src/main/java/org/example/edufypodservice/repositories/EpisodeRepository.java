package org.example.edufypodservice.repositories;

import org.example.edufypodservice.entities.Podcast;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EpisodeRepository extends JpaRepository<Podcast, Integer> {
}
