package com.example.muvmuv2;

public class Poster {
    private String director;
    private String imageUrl;
    private String title;
    private String year;
    private String key;

    public Poster() {
        // Default constructor required for Firebase
    }

    public Poster(String director, String imageUrl, String title, String year) {
        this.director = director;
        this.imageUrl = imageUrl;
        this.title = title;
        this.year = year;
    }

    // Getter and setter methods
    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
