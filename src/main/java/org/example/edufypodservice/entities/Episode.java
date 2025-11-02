package org.example.edufypodservice.entities;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
public class Episode {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "episode_id")
    private UUID id;
    @Column(length = 500, nullable = false)
    private String url;
    @Column(length = 50, nullable = false)
    private String title;
    @Column(length = 500, nullable = false)
    private String description;
    @Column(nullable = false)
    private LocalDate releaseDate;
    @Column(nullable = false)
    private int durationSeconds;
    @ManyToOne
    @JoinColumn(name = "podcast_id", nullable = false)
    @JsonIgnoreProperties({"episodes", "genres"})
    private Podcast podcast;

    public Episode() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public int getDurationSeconds() {
        return durationSeconds;
    }

    public void setDurationSeconds(int durationSeconds) {
        this.durationSeconds = durationSeconds;
    }

    public Podcast getPodcast() {
        return podcast;
    }

    public void setPodcast(Podcast podcast) {
        this.podcast = podcast;
    }

    @Override
    public String toString() {
        return "Episode{" +
                "id=" + id +
                ", url='" + url + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", releaseDate=" + releaseDate +
                ", durationSeconds=" + durationSeconds +
                '}';
    }
}
