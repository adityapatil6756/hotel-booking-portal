package com.hotelbooking;

import com.hotelbooking.exception.BookingException;
import com.hotelbooking.service.BookingService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// Simple tests. 
class BookingServiceTest {

    // null repositories are fine
    private final BookingService service = new BookingService(null, null, null);

    @Test
    void nightsAreCalculatedCorrectly() {
        LocalDate checkIn = LocalDate.of(2026, 11, 10);
        LocalDate checkOut = LocalDate.of(2026, 11, 13);
        assertEquals(3, service.calculateNights(checkIn, checkOut));
    }

    @Test
    void totalAmountIsNightsTimesPrice() {
        BigDecimal total = service.calculateTotal(new BigDecimal("2000"), 3);
        assertEquals(0, new BigDecimal("6000").compareTo(total));
    }

    @Test
    void pastCheckInIsRejected() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        assertThrows(BookingException.class, () -> service.validateDates(yesterday, LocalDate.now().plusDays(2)));
    }

    @Test
    void checkOutMustBeAfterCheckIn() {
        LocalDate day = LocalDate.now().plusDays(5);
        assertThrows(BookingException.class, () -> service.validateDates(day, day));
        assertThrows(BookingException.class, () -> service.validateDates(day, day.minusDays(1)));
    }

    @Test
    void validDatesAreAccepted() {
        LocalDate checkIn = LocalDate.now().plusDays(5);
        assertDoesNotThrow(() -> service.validateDates(checkIn, checkIn.plusDays(2)));
    }

    @Test
    void checkOutDayIsNotAStayNight() {
        List<LocalDate> nights = service.getStayNights(LocalDate.of(2026, 11, 10), LocalDate.of(2026, 11, 13));
        assertEquals(3, nights.size());
        assertTrue(nights.contains(LocalDate.of(2026, 11, 12)));
        assertFalse(nights.contains(LocalDate.of(2026, 11, 13)));
    }

    @Test
    void overlappingStaysShareANight() {
        // Customer A: 10-12 Nov, Customer B: 11-13 Nov -> both sleep on 11 Nov
        List<LocalDate> a = service.getStayNights(LocalDate.of(2026, 11, 10), LocalDate.of(2026, 11, 12));
        List<LocalDate> b = service.getStayNights(LocalDate.of(2026, 11, 11), LocalDate.of(2026, 11, 13));
        assertTrue(a.contains(LocalDate.of(2026, 11, 11)) && b.contains(LocalDate.of(2026, 11, 11)));
    }

    @Test
    void backToBackStaysDoNotShareANight() {
        // 10-12 Nov and 12-14 Nov: the second guest arrives on the day the first leaves
        List<LocalDate> a = service.getStayNights(LocalDate.of(2026, 11, 10), LocalDate.of(2026, 11, 12));
        List<LocalDate> b = service.getStayNights(LocalDate.of(2026, 11, 12), LocalDate.of(2026, 11, 14));
        for (LocalDate night : a) {
            assertFalse(b.contains(night));
        }
    }

    @Test
    void roomIsFreeOnlyWhenBookedIsLessThanTotal() {
        assertTrue(service.isRoomFree(2, 3));   // 1 room left
        assertFalse(service.isRoomFree(3, 3));  // hotel full
    }
}
