package com.na21k.audioplayer;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "songs", indices = {@Index("id"), @Index("artist"), @Index("album")})
public class Song {

    @PrimaryKey
    private final long id;
    private String trackNumber;
    private String title;
    private String artist;
    private String album;
    private String genre;
    private String date;
    private String path;
    //private String albumArtist;

    public Song(long id, String trackNumber, String title, String artist, String album, String genre, String date, String path) {
        this.id = id;
        this.trackNumber = trackNumber;
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.genre = genre;
        this.date = date;
        this.path = path;
    }

    public long getId() {
        return id;
    }

    public String getTrackNumber() {
        return trackNumber;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public String getAlbum() {
        return album;
    }

    public String getGenre() {
        return genre;
    }

    public String getDate() {
        return date;
    }

    public String getPath() {
        return path;
    }
}
