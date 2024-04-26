package com.mobile.moviebooking.Entity;

public class Celeb {
    private String firstName;
    private String lastName;
    private String imageUrl;
    private String infoUrl;

    public Celeb(String firstName, String lastName, String imageUrl, String infoUrl) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.imageUrl = imageUrl;
        this.infoUrl = infoUrl;
    }

    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public String getImageUrl() {
        return imageUrl;
    }
    public String getInfoUrl() {
        return infoUrl;
    }
}
