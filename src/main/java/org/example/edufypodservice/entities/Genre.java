package org.example.edufypodservice.entities;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
public class Genre {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "genre_id")
    private UUID id;
    @Column(length = 40, nullable = false)
    private String name;
    @ManyToMany
    @JoinTable(name = "genre_podcast", joinColumns = @JoinColumn(name = "genre_id"), inverseJoinColumns = @JoinColumn(name = "podcast_id"))
    @JsonIgnoreProperties({"episodes","genres"})
    private List<Podcast> podcasts = new ArrayList<>();

    public Genre() {

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

    public List<Podcast> getPodcasts() {
        return podcasts;
    }

    public void setPodcasts(List<Podcast> podcasts) {
        this.podcasts = podcasts;
    }

    @Override
    public String toString() {
        return "Genre{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
