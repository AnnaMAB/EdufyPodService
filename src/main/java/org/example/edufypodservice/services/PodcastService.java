package org.example.edufypodservice.services;


import org.example.edufypodservice.dto.PodcastDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface PodcastService {
    PodcastDto addPodcast(PodcastDto podcastDto);
    PodcastDto updatePodcast(PodcastDto podcastDto);
    String deletePodcast(PodcastDto podcastDto);

    List<PodcastDto> getAllPodcasts();
    PodcastDto getPodcastById(UUID id);
    PodcastDto getPodcastByName(String podcastName);
    List<PodcastDto> getPodcastsByGenre(String genre);

}
