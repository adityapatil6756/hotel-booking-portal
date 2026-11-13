package com.hotelbooking.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

// The JSON body sent by React when creating a booking.
// Note: there is NO price here. The backend calculates the price itself.
public class BookingRequest {

    @NotNull(message = "Hotel id is required.")
    private Integer hotelId;

    @NotBlank(message = "Customer name is required.")
    @Size(max = 100, message = "Name must be at most 100 characters.")
    private String customerName;

    @NotBlank(message = "Email is required.")
    @Email(message = "Please enter a valid email address.")
    private String email;

    @NotNull(message = "Check-in date is required.")
    private LocalDate checkIn;

    @NotNull(message = "Check-out date is required.")
    private LocalDate checkOut;

    public Integer getHotelId() { return hotelId; }
    public void setHotelId(Integer hotelId) { this.hotelId = hotelId; }
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public LocalDate getCheckIn() { return checkIn; }
    public void setCheckIn(LocalDate checkIn) { this.checkIn = checkIn; }
    public LocalDate getCheckOut() { return checkOut; }
    public void setCheckOut(LocalDate checkOut) { this.checkOut = checkOut; }
}
