package com.hotelbooking.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String bookingReference;   // column booking_reference

    // Many bookings belong to one hotel
    @ManyToOne
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;

    private String customerName;       // column customer_name
    private String email;
    private LocalDate checkIn;         // column check_in
    private LocalDate checkOut;        // column check_out
    private BigDecimal totalAmount;    // column total_amount
    private String status;             // CONFIRMED or CANCELLED

    public Booking() {
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getBookingReference() { return bookingReference; }
    public void setBookingReference(String bookingReference) { this.bookingReference = bookingReference; }
    public Hotel getHotel() { return hotel; }
    public void setHotel(Hotel hotel) { this.hotel = hotel; }
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public LocalDate getCheckIn() { return checkIn; }
    public void setCheckIn(LocalDate checkIn) { this.checkIn = checkIn; }
    public LocalDate getCheckOut() { return checkOut; }
    public void setCheckOut(LocalDate checkOut) { this.checkOut = checkOut; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
