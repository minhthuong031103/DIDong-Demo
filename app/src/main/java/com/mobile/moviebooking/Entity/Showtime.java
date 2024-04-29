package com.mobile.moviebooking.Entity;

import java.util.Date;

public class Showtime {
    private int id;
    private int price;
    private Date showtime;
    private int screenId;
    private int filmId;

    public Showtime(int id, int price, Date showtime, int screenId , int filmId) {
        this.id = id;
        this.price = price;
        this.showtime = showtime;
        this.screenId = screenId;
        this.filmId = filmId;
    }

    public int getId() {
        return id;
    }

    public int getPrice() {
        return price;
    }

    public Date getShowtime() {
        return showtime;
    }

    public int getScreenId() {
        return screenId;
    }

    public int getFilmId() {
        return filmId;
    }
}
