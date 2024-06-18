package com.example.muvmuv2;

public class Film {
    private String title;
    private String director;
    private String duration;
    private String genre;
    private String rate;
    private String synopsis;
    private String year;
    private String pictDetail;
    private String poster;
    private String trailerUrl;

    // Buat constructor kosong untuk Firebase
    public Film() {
    }

    // Buat constructor dengan parameter
    public Film(String title, String director, String duration, String genre, String rate, String synopsis, String year, String pictDetail, String poster, String trailerUrl) {
        this.title = title;
        this.director = director;
        this.duration = duration;
        this.genre = genre;
        this.rate = rate;
        this.synopsis = synopsis;
        this.year = year;
        this.pictDetail = pictDetail;
        this.poster = poster;
        this.trailerUrl = trailerUrl;
    }

    // Buat getter dan setter untuk setiap properti

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getPictDetail() {
        return pictDetail;
    }

    public void setPictDetail(String pictDetail) {
        this.pictDetail = pictDetail;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getTrailerUrl() {
        return trailerUrl;
    }

    public void setTrailerUrl(String trailerUrl) {
        this.trailerUrl = trailerUrl;
    }
}
