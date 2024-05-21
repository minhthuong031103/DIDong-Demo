package com.mobile.moviebooking.Entity;


import java.io.Serializable;

public class Ticket implements Serializable {
    public String Id;
    public String movieName;
    public String movieTime;
    public String movieDate;
    public String movieLocation;
    public String movieAddress;
    public String movieGenres;
    public String ticketPrice;
    public String ticketScreen;

    public String getTicketScreen() {
        return ticketScreen;
    }

    public void setTicketScreen(String ticketScreen) {
        this.ticketScreen = ticketScreen;
    }

    public String getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(String ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    public String getMovieGenres() {
        return movieGenres;
    }

    public void setMovieGenres(String movieGenres) {
        this.movieGenres = movieGenres;
    }

    public String getMovieAddress() {
        return movieAddress;
    }

    public void setMovieAddress(String movieAddress) {
        this.movieAddress = movieAddress;
    }

    public String movieSeat;
    public String movieImgUrl;


    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getMovieTime() {
        return movieTime;
    }

    public void setMovieTime(String movieTime) {
        this.movieTime = movieTime;
    }

    public String getMovieDate() {
        return movieDate;
    }

    public void setMovieDate(String movieDate) {
        this.movieDate = movieDate;
    }

    public String getMovieLocation() {
        return movieLocation;
    }

    public void setMovieLocation(String movieLocation) {
        this.movieLocation = movieLocation;
    }

    public String getMovieSeat() {
        return movieSeat;
    }

    public void setMovieSeat(String movieSeat) {
        this.movieSeat = movieSeat;
    }

    public String getMovieImgUrl() {
        return movieImgUrl;
    }

    public void setMovieImgUrl(String movieImgUrl) {
        this.movieImgUrl = movieImgUrl;
    }
    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }
}
