package com.hotelbooking.repository;

import com.hotelbooking.model.Booking;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookingRepository extends JpaRepository<Booking, Integer> {

    // Used to retrieve a booking: reference AND email must both match
    Booking findByBookingReferenceAndEmailIgnoreCase(String bookingReference, String email);

    // Used when cancelling: locks the booking row so a double-click cannot cancel twice
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT b FROM Booking b WHERE b.bookingReference = :ref AND LOWER(b.email) = LOWER(:email)")
    Booking findForCancel(@Param("ref") String reference, @Param("email") String email);
}
