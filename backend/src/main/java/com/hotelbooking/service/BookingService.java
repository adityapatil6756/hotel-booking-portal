package com.hotelbooking.service;

import com.hotelbooking.exception.BookingException;
import com.hotelbooking.model.Booking;
import com.hotelbooking.model.BookingRequest;
import com.hotelbooking.model.Hotel;
import com.hotelbooking.model.RoomAvailability;
import com.hotelbooking.repository.BookingRepository;
import com.hotelbooking.repository.HotelRepository;
import com.hotelbooking.repository.RoomAvailabilityRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class BookingService {

    private final HotelRepository hotelRepository;
    private final BookingRepository bookingRepository;
    private final RoomAvailabilityRepository availabilityRepository;

    public BookingService(HotelRepository hotelRepository,
                          BookingRepository bookingRepository,
                          RoomAvailabilityRepository availabilityRepository) {
        this.hotelRepository = hotelRepository;
        this.bookingRepository = bookingRepository;
        this.availabilityRepository = availabilityRepository;
    }

    // ---------- Simple helper methods (these are the ones we unit test) ----------

    // Check-in 10 Nov, check-out 13 Nov -> 3 nights
    public int calculateNights(LocalDate checkIn, LocalDate checkOut) {
        return (int) ChronoUnit.DAYS.between(checkIn, checkOut);
    }

    // Total = nights x price per night
    public BigDecimal calculateTotal(BigDecimal pricePerNight, int nights) {
        return pricePerNight.multiply(BigDecimal.valueOf(nights));
    }

    // Date rules. Throws an error with a clear message if a rule is broken.
    public void validateDates(LocalDate checkIn, LocalDate checkOut) {
        if (checkIn == null || checkOut == null) {
            throw new BookingException("Please select both check-in and check-out dates.", HttpStatus.BAD_REQUEST);
        }
        if (checkIn.isBefore(LocalDate.now())) {
            throw new BookingException("Check-in date cannot be in the past.", HttpStatus.BAD_REQUEST);
        }
        if (!checkOut.isAfter(checkIn)) {
            throw new BookingException("Check-out date must be after check-in date.", HttpStatus.BAD_REQUEST);
        }
    }

    // The nights a guest sleeps in the hotel. The check-out day is NOT included.
    // 10 Nov to 13 Nov -> [10 Nov, 11 Nov, 12 Nov]
    public List<LocalDate> getStayNights(LocalDate checkIn, LocalDate checkOut) {
        List<LocalDate> nights = new ArrayList<>();
        LocalDate day = checkIn;
        while (day.isBefore(checkOut)) {
            nights.add(day);
            day = day.plusDays(1);
        }
        return nights;
    }

    // A room is free on a night only if booked rooms are less than total rooms
    public boolean isRoomFree(int bookedRooms, int totalRooms) {
        return bookedRooms < totalRooms;
    }

    // ---------- Availability ----------

    // How many rooms are booked on one night (0 if there is no row yet)
    public int getBookedRooms(Integer hotelId, LocalDate night) {
        RoomAvailability row = availabilityRepository.findByHotelIdAndStayDate(hotelId, night);
        if (row == null) {
            return 0;
        }
        return row.getBookedRooms();
    }

    // Rooms available for the WHOLE stay = the smallest free count across all nights
    public int getAvailableRooms(Hotel hotel, LocalDate checkIn, LocalDate checkOut) {
        int lowest = hotel.getTotalRooms();
        List<LocalDate> nights = getStayNights(checkIn, checkOut);
        for (LocalDate night : nights) {
            int free = hotel.getTotalRooms() - getBookedRooms(hotel.getId(), night);
            if (free < lowest) {
                lowest = free;
            }
        }
        return lowest;
    }

    // ---------- Create booking ----------
    // @Transactional = all database changes below succeed together, or none are saved.
    @Transactional
    public Booking createBooking(BookingRequest request) {
        // 1. Validate dates
        validateDates(request.getCheckIn(), request.getCheckOut());

        // 2. Lock the hotel row. Other customers booking this hotel now wait here.
        Hotel hotel = hotelRepository.findByIdForUpdate(request.getHotelId());
        if (hotel == null) {
            throw new BookingException("Hotel not found.", HttpStatus.NOT_FOUND);
        }

        // 3. Check every night. If any night is full, stop.
        List<LocalDate> nights = getStayNights(request.getCheckIn(), request.getCheckOut());
        for (LocalDate night : nights) {
            int booked = getBookedRooms(hotel.getId(), night);
            if (!isRoomFree(booked, hotel.getTotalRooms())) {
                throw new BookingException("Sorry, no rooms are available on " + night
                        + ". Please choose different dates.", HttpStatus.CONFLICT);
            }
        }

        // 4. Reserve: add 1 booked room to every night
        for (LocalDate night : nights) {
            RoomAvailability row = availabilityRepository.findByHotelIdAndStayDate(hotel.getId(), night);
            if (row == null) {
                row = new RoomAvailability(hotel.getId(), night, 0);
            }
            row.setBookedRooms(row.getBookedRooms() + 1);
            availabilityRepository.save(row);
        }

        // 5. Calculate the total on the backend
        int numberOfNights = calculateNights(request.getCheckIn(), request.getCheckOut());
        BigDecimal total = calculateTotal(hotel.getPricePerNight(), numberOfNights);

        // 6. Save the booking
        Booking booking = new Booking();
        booking.setBookingReference("HB-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        booking.setHotel(hotel);
        booking.setCustomerName(request.getCustomerName().trim());
        booking.setEmail(request.getEmail().trim().toLowerCase());
        booking.setCheckIn(request.getCheckIn());
        booking.setCheckOut(request.getCheckOut());
        booking.setTotalAmount(total);
        booking.setStatus("CONFIRMED");
        return bookingRepository.save(booking);
    }

    // ---------- Retrieve booking ----------
    public Booking getBooking(String reference, String email) {
        Booking booking = bookingRepository.findByBookingReferenceAndEmailIgnoreCase(reference.trim(), email.trim());
        if (booking == null) {
            // Same message for wrong reference or wrong email, so nobody can guess references
            throw new BookingException("No booking found for this reference and email.", HttpStatus.NOT_FOUND);
        }
        return booking;
    }

    // ---------- Cancel booking ----------
    @Transactional
    public Booking cancelBooking(String reference, String email) {
        // Lock the booking row (stops double cancel)
        Booking booking = bookingRepository.findForCancel(reference.trim(), email.trim());
        if (booking == null) {
            throw new BookingException("No booking found for this reference and email.", HttpStatus.NOT_FOUND);
        }
        if (booking.getStatus().equals("CANCELLED")) {
            throw new BookingException("This booking is already cancelled.", HttpStatus.BAD_REQUEST);
        }
        if (booking.getCheckIn().isBefore(LocalDate.now())) {
            throw new BookingException("This booking cannot be cancelled because check-in date has passed.",
                    HttpStatus.BAD_REQUEST);
        }

        // Lock the hotel row too, so cancel and new bookings never change counts at the same time
        hotelRepository.findByIdForUpdate(booking.getHotel().getId());

        // Release: subtract 1 booked room from every night of the stay
        List<LocalDate> nights = getStayNights(booking.getCheckIn(), booking.getCheckOut());
        for (LocalDate night : nights) {
            RoomAvailability row = availabilityRepository.findByHotelIdAndStayDate(booking.getHotel().getId(), night);
            if (row != null && row.getBookedRooms() > 0) {
                row.setBookedRooms(row.getBookedRooms() - 1);
                availabilityRepository.save(row);
            }
        }

        booking.setStatus("CANCELLED");
        return bookingRepository.save(booking);
    }
}
