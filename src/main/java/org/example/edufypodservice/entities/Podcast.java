package org.example.edufymediaadder.entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;


@Entity
public class Podcast implements MediaInterface {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "podcast_id")
    private Integer id;
    @Column(length = 40, nullable = false)
    private String Title;
    @ManyToMany
    @JoinColumn(name = "genre_id")
    private List<Genre> genre = new ArrayList<>();
    @ManyToMany
    @JoinColumn(name = "album_id")
    private List<Album> albums = new ArrayList<>();
    @ManyToMany
    @JoinColumn(name = "playlist_id")
    private List<Playlist> playlists = new ArrayList<>();

    public Podcast() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Genre> getGenre() {
        return genre;
    }

    public void setGenre(List<Genre> genre) {
        this.genre = genre;
    }

    public List<Album> getAlbums() {
        return albums;
    }

    public void setAlbums(List<Album> albums) {
        this.albums = albums;
    }

    public List<Playlist> getPlaylists() {
        return playlists;
    }

    public void setPlaylists(List<Playlist> playlists) {
        this.playlists = playlists;
    }


    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    @Override
    public String toString() {
        return "Podcast{" +
                "id=" + id +
                ", Title='" + Title + '\'' +
                ", genre=" + genre +
                ", albums=" + albums +
                ", playlists=" + playlists +
                '}';
    }
}
