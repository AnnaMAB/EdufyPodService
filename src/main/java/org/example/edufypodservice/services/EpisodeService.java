package org.example.edufypodservice.services;


import org.example.edufypodservice.dto.EpisodeDto;

import java.util.List;
import java.util.UUID;

public interface EpisodeService {

    EpisodeDto addEpisode(EpisodeDto episodeDto);
    EpisodeDto updateEpisode(EpisodeDto episodeDto);
    String deleteEpisode(EpisodeDto episodeDto);
    EpisodeDto getEpisode(UUID episodeId);
    List<EpisodeDto> getAllEpisodes();
    List<EpisodeDto> getEpisodesByPodcastId(UUID podcastId);




}
