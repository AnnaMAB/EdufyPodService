package org.example.edufymediaadder.services;

import org.example.edufymediaadder.entities.Podcast;
import org.springframework.stereotype.Service;

@Service
public interface PodcastService {
    Podcast addPodcast(Podcast podcast);
    Podcast updatePodcast(Podcast podcast);
    String deletePodcast(Podcast podcast);

}
