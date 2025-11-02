package org.example.edufypodservice.services;



import org.example.edufypodservice.dto.EpisodeDto;
import org.example.edufypodservice.repositories.EpisodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
public class EpisodeServiceImpl implements EpisodeService {

    private final EpisodeRepository episodeRepository;

    @Autowired
    public EpisodeServiceImpl(EpisodeRepository episodeRepository) {
        this.episodeRepository = episodeRepository;
    }


    @Override
    public EpisodeDto addEpisode(EpisodeDto episodeDto) {
        return null;
    }

    @Override
    public EpisodeDto updateEpisode(EpisodeDto episodeDto) {
        return null;
    }

    @Override
    public String deleteEpisode(EpisodeDto episodeDto) {
        return "";
    }

    @Override
    public EpisodeDto getEpisode(UUID episodeId) {
        return null;
    }

    @Override
    public List<EpisodeDto> getAllEpisodes() {
        return List.of();
    }

    @Override
    public List<EpisodeDto> getEpisodesByPodcastId(UUID podcastId) {
        return List.of();
    }
}
