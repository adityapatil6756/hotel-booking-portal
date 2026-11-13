package com.hotelbooking.controller;

import com.hotelbooking.model.AvailabilityResponse;
import com.hotelbooking.model.Hotel;
import com.hotelbooking.service.HotelService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/hotels")
@CrossOrigin(origins = "http://localhost:5173")   // allow the React app to call this API
public class HotelController {

    private final HotelService hotelService;

    public HotelController(HotelService hotelService) {
        this.hotelService = hotelService;
    }

    // GET /api/hotels
    @GetMapping
    public List<Hotel> getAllHotels() {
        return hotelService.getAllHotels();
    }

    // GET /api/hotels/search?location=Goa&checkIn=2026-12-10&checkOut=2026-12-12
    @GetMapping("/search")
    public List<Hotel> searchHotels(
            @RequestParam(required = false) String location,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkIn,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOut) {
        return hotelService.searchHotels(location, checkIn, checkOut);
    }

    // GET /api/hotels/1
    @GetMapping("/{id}")
    public Hotel getHotelById(@PathVariable Integer id) {
        return hotelService.getHotelById(id);
    }

    // GET /api/hotels/1/availability?checkIn=2026-12-10&checkOut=2026-12-12
    @GetMapping("/{id}/availability")
    public AvailabilityResponse checkAvailability(
            @PathVariable Integer id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkIn,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOut) {
        return hotelService.checkAvailability(id, checkIn, checkOut);
    }
}
