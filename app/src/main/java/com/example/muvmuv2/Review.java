package com.example.muvmuv2;

public class Review {
    private String userId;
    private String username;
    private String email;
    private String profilePhoto;
    private String content;
    private String filmTitle; // Tambahkan atribut filmTitle untuk menyimpan judul film yang diulas
    private float rate;

    public Review() {
        // Default constructor required for calls to DataSnapshot.getValue(Review.class)
    }

    public Review(String userId, String username, String email, String profilePhoto, String content, String filmTitle) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.profilePhoto = profilePhoto;
        this.content = content;
        this.filmTitle = filmTitle;
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

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
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
