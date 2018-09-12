package com.vietdung.beautymusic.model;

public class Author {
    private int id;
    private String nameSong;
    private String nameAuthor;

    public Author(int id, String nameSong, String nameAuthor) {
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
}
