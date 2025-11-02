package org.example.edufypodservice.services;


import org.example.edufypodservice.dto.PodcastDto;
import org.example.edufypodservice.repositories.PodcastRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PodcastServiceImpl implements PodcastService{

    private final PodcastRepository podcastRepository;

    @Autowired
    public PodcastServiceImpl(PodcastRepository podcastRepository) {
        this.podcastRepository = podcastRepository;
    }


    @Override
    public PodcastDto addPodcast(PodcastDto podcastDto) {
        return null;
    }

    @Override
    public PodcastDto updatePodcast(PodcastDto podcastDto) {
        return null;
    }

    @Override
    public String deletePodcast(PodcastDto podcastDto) {
        return "";
    }

    @Override
    public List<PodcastDto> getAllPodcasts() {
        return List.of();
    }

    @Override
    public PodcastDto getPodcastById(UUID id) {
        return null;
    }

    @Override
    public PodcastDto getPodcastByName(String podcastName) {
        return null;
    }

    @Override
    public List<PodcastDto> getPodcastsByGenre(String genre) {
        return List.of();
    }
}
