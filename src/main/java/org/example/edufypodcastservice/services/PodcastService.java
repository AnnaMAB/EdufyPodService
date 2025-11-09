package org.example.edufypodcastservice.services;


import org.example.edufypodcastservice.dto.PodcastDto;
import org.example.edufypodcastservice.entities.Podcast;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface PodcastService {
    Podcast addPodcast(PodcastDto podcastDto);
    Podcast updatePodcast(PodcastDto podcastDto);
    String deletePodcast(UUID podcastId);

    List<PodcastDto> getAllPodcasts();
    PodcastDto getPodcastById(UUID id);
    List<PodcastDto> getPodcastByName(String podcastName);
    List<PodcastDto> getPodcastsByGenre(String genre);

}
