package com.hotelbooking.repository;

import com.hotelbooking.model.RoomAvailability;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface RoomAvailabilityRepository extends JpaRepository<RoomAvailability, Integer> {

    // Finds the row for one hotel on one night (null if nobody booked that night yet)
    RoomAvailability findByHotelIdAndStayDate(Integer hotelId, LocalDate stayDate);
}
