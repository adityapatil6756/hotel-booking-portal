package com.hotelbooking.model;

import jakarta.persistence.*;
import java.time.LocalDate;

// One row = one hotel on one night, and how many rooms are booked that night.
@Entity
@Table(name = "room_availability")
public class RoomAvailability {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer hotelId;        // column hotel_id
    private LocalDate stayDate;     // column stay_date
    private Integer bookedRooms;    // column booked_rooms

    public RoomAvailability() {
    }

    public RoomAvailability(Integer hotelId, LocalDate stayDate, Integer bookedRooms) {
        this.hotelId = hotelId;
        this.stayDate = stayDate;
        this.bookedRooms = bookedRooms;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getHotelId() { return hotelId; }
    public void setHotelId(Integer hotelId) { this.hotelId = hotelId; }
    public LocalDate getStayDate() { return stayDate; }
    public void setStayDate(LocalDate stayDate) { this.stayDate = stayDate; }
    public Integer getBookedRooms() { return bookedRooms; }
    public void setBookedRooms(Integer bookedRooms) { this.bookedRooms = bookedRooms; }
}
