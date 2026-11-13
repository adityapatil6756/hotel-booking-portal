package com.hotelbooking.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

// Catches errors from all controllers and returns one consistent JSON format.
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BookingException.class)
    public ResponseEntity<ErrorResponse> handleBookingException(BookingException ex) {
        return buildResponse(ex.getStatus(), ex.getMessage());
    }

    // @Valid failed on the request body
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        String message = "";
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            message = message + fieldError.getDefaultMessage() + " ";
        }
        return buildResponse(HttpStatus.BAD_REQUEST, message.trim());
    }

    // A date such as "abc" was sent in the URL
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST,
                "Invalid value for '" + ex.getName() + "'. Dates must be in yyyy-MM-dd format.");
    }

    // A required URL parameter is missing
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingParameter(MissingServletRequestParameterException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, "Missing required parameter: " + ex.getParameterName());
    }

    // Broken JSON or a wrong date format in the body
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleUnreadableBody(HttpMessageNotReadableException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST,
                "Invalid request body. Please check the JSON and use yyyy-MM-dd for dates.");
    }

    // Anything unexpected
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleOtherErrors(Exception ex) {
        ex.printStackTrace();
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong on the server. Please try again.");
    }

    private ResponseEntity<ErrorResponse> buildResponse(HttpStatus status, String message) {
        ErrorResponse body = new ErrorResponse(status.value(), status.getReasonPhrase(), message);
        return new ResponseEntity<>(body, status);
    }
}
