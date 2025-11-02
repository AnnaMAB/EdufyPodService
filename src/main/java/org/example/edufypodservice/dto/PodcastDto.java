package org.example.edufypodservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PodcastDto {

    private UUID id;
    private String Name;
    private String description;
    private List<EpisodeDto> episodes = new ArrayList<>();
    private List<GenreDto> genres = new ArrayList<>();

    public PodcastDto() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<EpisodeDto> getEpisodes() {
        return episodes;
    }

    public void setEpisodes(List<EpisodeDto> episodes) {
        this.episodes = episodes;
    }

    public List<GenreDto> getGenres() {
        return genres;
    }

    public void setGenres(List<GenreDto> genres) {
        this.genres = genres;
    }

    @Override
    public String toString() {
        return "PodcastDto{" +
                "id=" + id +
                ", Name='" + Name + '\'' +
                ", description='" + description + '\'' +
                ", episodes=" + episodes +
                ", genres=" + genres +
                '}';
    }
}
