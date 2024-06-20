package com.example.muvmuv2;

public class ReviewProfile {
    private String userId;
    private String username;
    private String email;
    private String photoProfile;
    private String content;
    private String filmTitle; // Tambahkan atribut filmTitle untuk menyimpan judul film yang diulas
    private float rate;

    public ReviewProfile() {
        // Default constructor required for calls to DataSnapshot.getValue(Review.class)
    }

    public ReviewProfile(String userId, String username, String email, String photoProfile, String content, String filmTitle) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.photoProfile = photoProfile;
        this.content = content;
        this.filmTitle = filmTitle;
        this.content = content;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhotoProfile() {
        return photoProfile;
    }

    public void setPhotoProfile(String photoProfile) {
        this.photoProfile = photoProfile;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFilmTitle() {
        return filmTitle;
    }

    public void setFilmTitle(String filmTitle) {
        this.filmTitle = filmTitle;
    }

    public float getRate() {
        return rate;
    }

    // Setter untuk rate
    public void setRate(float rate) {
        this.rate = rate;
    }
}

