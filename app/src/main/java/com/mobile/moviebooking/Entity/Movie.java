package com.mobile.moviebooking.Entity;

public class Movie {
    private String Id;
    private String name;
    private String poster;
    private String description;
    private Double rating;
    private int ratingCount;
    private String genre;
    private String duration;
    private String releaseDate;
    private String trailer;
    private boolean isPlaying;

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public Movie(String name, String poster, String description, Double rating, int ratingCount, String genre, String duration, String releaseDate) {
        this.name = name;
        this.poster = poster;
        this.description = description;
        this.rating = rating;
        this.genre = genre;
        this.duration = duration;
        this.releaseDate = releaseDate;
        this.ratingCount = ratingCount;
    }
    public Movie (){}
    public String getId() {
        return Id;
    }
    public void setId(String id) {
        Id = id;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public void setRatingCount(int ratingCount) {
        this.ratingCount = ratingCount;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setTrailer(String trailer) {
        this.trailer = trailer;
    }

    public String getPoster() {
        return poster;
    }

    public String getDescription() {
        return description;
    }

    public Double getRating() {
        return rating;
    }

    public int getRatingCount() {
        return ratingCount;
    }

    public String getGenre() {
        return genre;
    }

    public String getDuration() {
        return duration;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getTrailer() {
        return trailer;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
