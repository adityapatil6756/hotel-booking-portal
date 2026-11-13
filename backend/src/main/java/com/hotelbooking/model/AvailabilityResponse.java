package com.hotelbooking.model;

import java.math.BigDecimal;
import java.time.LocalDate;

// Answer of GET /api/hotels/{id}/availability
public class AvailabilityResponse {

    private Integer hotelId;
    private String hotelName;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private int nights;
    private BigDecimal pricePerNight;
    private BigDecimal totalAmount;
    private int totalRooms;
    private int availableRooms;
    private boolean available;

    public AvailabilityResponse(Integer hotelId, String hotelName, LocalDate checkIn, LocalDate checkOut,
                                int nights, BigDecimal pricePerNight, BigDecimal totalAmount,
                                int totalRooms, int availableRooms) {
        this.hotelId = hotelId;
        this.hotelName = hotelName;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.nights = nights;
        this.pricePerNight = pricePerNight;
        this.totalAmount = totalAmount;
        this.totalRooms = totalRooms;
        this.availableRooms = availableRooms;
        this.available = availableRooms > 0;
    }

    public Integer getHotelId() { return hotelId; }
    public String getHotelName() { return hotelName; }
    public LocalDate getCheckIn() { return checkIn; }
    public LocalDate getCheckOut() { return checkOut; }
    public int getNights() { return nights; }
    public BigDecimal getPricePerNight() { return pricePerNight; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public int getTotalRooms() { return totalRooms; }
    public int getAvailableRooms() { return availableRooms; }
    public boolean isAvailable() { return available; }
}
