package org.example.edufypodservice.services;


import org.example.edufypodservice.dto.EpisodeDto;
import org.example.edufypodservice.entities.Episode;

import java.util.List;
import java.util.UUID;

public interface EpisodeService {

    Episode addEpisode(EpisodeDto episodeDto);
    Episode updateEpisode(EpisodeDto episodeDto);
    String deleteEpisode(UUID episodeId);
    EpisodeDto getEpisode(UUID episodeId);
    List<EpisodeDto> getAllEpisodes();
    List<EpisodeDto> getEpisodesByPodcastId(UUID podcastId);




}
