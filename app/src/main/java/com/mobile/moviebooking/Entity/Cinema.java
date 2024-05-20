package com.mobile.moviebooking.Entity;

import java.util.ArrayList;
import java.util.List;

public class Cinema {
    private int price;
    private int id;
    private String name;
    private String address;
    private String logoUrl;
    public boolean isExpanded = false;
    private List<Showtime> showtimes = new ArrayList<>();
    public Cinema(String name, String address, String logoUrl) {
            this.name = name;
            this.address = address;
            this.logoUrl = logoUrl;
    }
    public Cinema(String name, String address, String logoUrl, List<Showtime> showtimes) {
        this.name = name;
        this.address = address;
        this.logoUrl = logoUrl;
        this.showtimes = showtimes;
    }
    public Cinema(int id, String name, String address, String logoUrl) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.logoUrl = logoUrl;
    }
    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getAddress() {
        return address;
    }
    public String getLogoUrl() {
        return logoUrl;
    }
    public List<Showtime> getShowtimes() {
        return showtimes;
    }

    public int getPrice() {
        return price;
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setShowtimes(List<Showtime> showtimes) {
        this.showtimes = showtimes;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }
    public void setPrice(int price) {
        this.price = price;
    }
}
