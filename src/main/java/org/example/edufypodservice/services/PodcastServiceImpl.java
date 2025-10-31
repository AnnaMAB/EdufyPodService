package org.example.edufymediaadder.services;

import org.example.edufymediaadder.entities.Podcast;
import org.example.edufymediaadder.repositories.PodcastRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PodcastServiceImpl implements PodcastService{

    private final PodcastRepository podcastRepository;

    @Autowired
    public PodcastServiceImpl(PodcastRepository podcastRepository) {
        this.podcastRepository = podcastRepository;
    }

    @Override
    public Podcast addPodcast(Podcast podcast) {
        podcastRepository.save(podcast);
        return podcast;
    }

    @Override
    public Podcast updatePodcast(Podcast podcast) {
        podcastRepository.save(podcast);
        return podcast;
    }

    @Override
    public String deletePodcast(Podcast podcast) {
        podcastRepository.delete(podcast);
        return "Podcast deleted";
    }

}
