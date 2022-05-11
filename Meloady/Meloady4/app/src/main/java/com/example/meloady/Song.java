package com.example.meloady;

import java.io.Serializable;

public class Song implements Serializable {
    String name;
    String artist;
    String duration;
    String songUrl;
    String user;

    public Song(String name, String artist, String duration, String songUrl,  String user) {
        this.name = name;
        this.artist = artist;
        this.duration = duration;
        this.songUrl = songUrl;
        this.user = user;
    }

    public Song() {
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getSongUrl() {
        return songUrl;
    }

    public void setSongUrl(String songUrl) {
        this.songUrl = songUrl;
    }
}
