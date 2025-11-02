package org.example.edufypodservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class GenreDto {

    private UUID id;
    private String name;
    private List<PodcastDto> podcasts = new ArrayList<>();

    public GenreDto() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<PodcastDto> getPodcasts() {
        return podcasts;
    }

    public void setPodcasts(List<PodcastDto> podcasts) {
        this.podcasts = podcasts;
    }

    @Override
    public String toString() {
        return "GenreDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", podcasts=" + podcasts +
                '}';
    }
}
