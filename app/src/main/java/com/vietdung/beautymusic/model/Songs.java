package com.vietdung.beautymusic.model;

public class Songs {
    private int id;
    private String nameSong;
    private String nameAuthor ;
    private String albums;

    public Songs(int id, String nameSong, String nameAuthor) {
        this.id = id;
        this.nameSong = nameSong;
        this.nameAuthor = nameAuthor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNameSong() {
        return nameSong;
    }

    public void setNameSong(String nameSong) {
        this.nameSong = nameSong;
    }

    public String getNameAuthor() {
        return nameAuthor;
    }

    public void setNameAuthor(String nameAuthor) {
        this.nameAuthor = nameAuthor;
    }

    public String getAlbums() {
        return albums;
    }

    public void setAlbums(String albums) {
        this.albums = albums;
    }
}
