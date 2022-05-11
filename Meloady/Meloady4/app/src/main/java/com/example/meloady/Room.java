package com.example.meloady;

import java.util.ArrayList;
import java.util.HashMap;

public class Room {
    private String name;
    private Boolean isPublic;
    private Song songPlaying;
    private HashMap<String,Long> time = new HashMap<String,Long>();
    private ArrayList<Song> songQu = new ArrayList<Song>();
    private ArrayList<String> users = new ArrayList<String>();
    private String password;
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Room() {
    }

    public Room(String name, Boolean isPublic) {
        this.name = name;
        this.isPublic = isPublic;
        time.put("real", 0l);
        time.put("move",0l);
    }

    public Room(String name, Boolean isPublic, String password){
        this.name = name;
        this.isPublic = isPublic;
        this.password = password;
        time.put("real", 0l);
        time.put("move",0l);
    }

    public ArrayList<String> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<String> users) {
        this.users = users;
    }

    public Song getSongPlaying() {
        return songPlaying;
    }

    public void setSongPlaying(Song songPlaying) {
        this.songPlaying = songPlaying;
    }

    public HashMap<String, Long> getTime() {
        return time;
    }

    public void setTime(HashMap<String, Long> time) {
        this.time = time;
    }

    public ArrayList<Song> getSongQu() {
        return songQu;
    }

    public void setSongQu(ArrayList<Song> songQu) {
        this.songQu = songQu;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getPublic() {
        return isPublic;
    }

    public void setPublic(Boolean aPublic) {
        isPublic = aPublic;
    }
}
