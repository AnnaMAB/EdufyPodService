package org.example.edufypodservice.mapper;

import org.example.edufypodservice.dto.EpisodeDto;
import org.example.edufypodservice.entities.Episode;
import org.springframework.stereotype.Component;

@Component
public class EpisodeDtoConverter {

    public EpisodeDto convertToFullEpisodeDto(Episode episode) {
        EpisodeDto episodeDto = new EpisodeDto();
        episodeDto.setId(episode.getId());
        episodeDto.setTitle(episode.getTitle());
        episodeDto.setUrl(episode.getUrl());
        episodeDto.setDurationSeconds(episode.getDurationSeconds());
        episodeDto.setDescription(episode.getDescription());
        episodeDto.setReleaseDate(episode.getReleaseDate());
        //episodeDto.setPodcastId(); //TODO--------------------------------

        return episodeDto;
    }


}
