package com.mobile.moviebooking.Entity;

public class Food {
    private int id;
    private String name;
    private String description;
    private int price;
    private String image;
    private int quantity = 0;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getPrice() {
        return price;
    }

    public String getImage() {
        return image;
    }
    public int getQuantity() {
        return quantity;
    }

    public Food(int id, String name, String description, int price, String image) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.image = image;
    }

    public void setQuantity(int i) {
        quantity = i;
    }
}
