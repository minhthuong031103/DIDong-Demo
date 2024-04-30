package com.mobile.moviebooking.Entity;

public class Seat {
    private int id;
    private int seatNumber;
    private char seatRow;
    private byte status; // 0: available, 1: reserved, 2: selected

    public Seat(int id, int seatNumber, char seatRow, byte status) {
        this.id = id;
        this.seatNumber = seatNumber;
        this.seatRow = seatRow;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public int getSeatNumber() {
        return seatNumber;
    }

    public char getSeatRow() {
        return seatRow;
    }
    public byte getStatus() {
        return status;
    }

    public void setStatus(byte b) {
        this.status = b;
    }

    public char getRow() {
        return seatRow;
    }
    public int seatNumber() {
        return seatNumber;
    }
}
