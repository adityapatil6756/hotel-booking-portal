package com.hotelbooking.controller;

import com.hotelbooking.model.Booking;
import com.hotelbooking.model.BookingRequest;
import com.hotelbooking.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin(origins = "http://localhost:5173")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    // POST /api/bookings   (JSON body)  -> 201 Created
    @PostMapping
    public ResponseEntity<Booking> createBooking(@Valid @RequestBody BookingRequest request) {
        Booking booking = bookingService.createBooking(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(booking);
    }

    // GET /api/bookings/HB-1A2B3C4D?email=ravi@example.com
    @GetMapping("/{reference}")
    public Booking getBooking(@PathVariable String reference, @RequestParam String email) {
        return bookingService.getBooking(reference, email);
    }

    // POST /api/bookings/HB-1A2B3C4D/cancel?email=ravi@example.com
    @PostMapping("/{reference}/cancel")
    public Booking cancelBooking(@PathVariable String reference, @RequestParam String email) {
        return bookingService.cancelBooking(reference, email);
    }
}
