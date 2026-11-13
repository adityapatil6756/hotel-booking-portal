package com.hotelbooking.service;

import com.hotelbooking.exception.BookingException;
import com.hotelbooking.model.AvailabilityResponse;
import com.hotelbooking.model.Hotel;
import com.hotelbooking.repository.HotelRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class HotelService {

    private final HotelRepository hotelRepository;
    private final BookingService bookingService;

    public HotelService(HotelRepository hotelRepository, BookingService bookingService) {
        this.hotelRepository = hotelRepository;
        this.bookingService = bookingService;
    }

    public List<Hotel> getAllHotels() {
        return hotelRepository.findAll();
    }

    public Hotel getHotelById(Integer id) {
        return hotelRepository.findById(id)
                .orElseThrow(() -> new BookingException("Hotel not found.", HttpStatus.NOT_FOUND));
    }

    // Location is optional. Dates are optional, but if given they must be valid.
    // When dates are given, only hotels with a free room for all nights are returned.
    public List<Hotel> searchHotels(String location, LocalDate checkIn, LocalDate checkOut) {
        List<Hotel> hotels;
        if (location == null || location.trim().isEmpty()) {
            hotels = hotelRepository.findAll();
        } else {
            hotels = hotelRepository.findByLocationContainingIgnoreCase(location.trim());
        }

        if (checkIn == null && checkOut == null) {
            return hotels;
        }

        bookingService.validateDates(checkIn, checkOut);

        List<Hotel> availableHotels = new ArrayList<>();
        for (Hotel hotel : hotels) {
            if (bookingService.getAvailableRooms(hotel, checkIn, checkOut) > 0) {
                availableHotels.add(hotel);
            }
        }
        return availableHotels;
    }

    public AvailabilityResponse checkAvailability(Integer hotelId, LocalDate checkIn, LocalDate checkOut) {
        bookingService.validateDates(checkIn, checkOut);
        Hotel hotel = getHotelById(hotelId);

        int nights = bookingService.calculateNights(checkIn, checkOut);
        BigDecimal total = bookingService.calculateTotal(hotel.getPricePerNight(), nights);
        int availableRooms = bookingService.getAvailableRooms(hotel, checkIn, checkOut);

        return new AvailabilityResponse(hotel.getId(), hotel.getName(), checkIn, checkOut, nights,
                hotel.getPricePerNight(), total, hotel.getTotalRooms(), availableRooms);
    }
}
