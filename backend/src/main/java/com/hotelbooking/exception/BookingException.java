package com.hotelbooking.exception;

import org.springframework.http.HttpStatus;

// Our own error. We throw it when a rule is broken (bad dates, no rooms, booking not found...)
public class BookingException extends RuntimeException {

    private final HttpStatus status;

    public BookingException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
