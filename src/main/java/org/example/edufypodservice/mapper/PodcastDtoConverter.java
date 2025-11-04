package org.example.edufypodservice.mapper;

import org.example.edufypodservice.dto.PodcastDto;
import org.example.edufypodservice.entities.Podcast;
import org.springframework.stereotype.Component;

@Component
public class PodcastDtoConverter {

    public PodcastDto convertToFullPodcastDto(Podcast podcast) {
        PodcastDto podcastDto = new PodcastDto();
        podcastDto.setId(podcast.getId());
        podcastDto.setName(podcast.getName());
        podcastDto.setDescription(podcast.getDescription());
       // podcastDto.setEpisodes(); //TODO--------------------------------
        //podcastDto.setGenres(); //TODO--------------------------------

        return podcastDto;

    }
}
