package com.bookingapi.controllers;

import com.bookingapi.models.Booking;
import com.bookingapi.services.BookingService;
import com.bookingapi.exceptions.ResourceNotFoundException;
import com.bookingapi.exceptions.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @GetMapping
    public List<Booking> getAllBookings() {
        return bookingService.getAllBookings();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Booking> getBookingById(@PathVariable Long id) {
        try {
            Booking booking = bookingService.getBookingById(id);
            return ResponseEntity.ok(booking);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Booking> createBooking(@RequestBody Booking booking) {
        validateBookingInput(booking);
        Booking createdBooking = bookingService.createBooking(booking);
        return new ResponseEntity<>(createdBooking, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Booking> updateBooking(@PathVariable Long id, @RequestBody Booking updatedBooking) {
        validateBookingInput(updatedBooking);
        try {
            Booking booking = bookingService.updateBooking(id, updatedBooking);
            return ResponseEntity.ok(booking);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);  // Proporcionar un mensaje específico
        } catch (ValidationException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);  // Validación específica
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable Long id) {
        try {
            bookingService.deleteBooking(id);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    private void validateBookingInput(Booking booking) {
        if (booking.getCustomerName() == null || booking.getCustomerName().isBlank()) {
            throw new ValidationException("El nombre del cliente es obligatorio.");
        }

        if (booking.getService() == null || booking.getService().isBlank()) {
            throw new ValidationException("El servicio es obligatorio.");
        }

        if (booking.getBookingDate() == null) {
            throw new ValidationException("La fecha de la reserva es obligatoria.");
        }

        if (booking.getUser() == null) {
            throw new ValidationException("El usuario asociado es obligatorio.");
        }

        if (booking.getRoom() == null) {
            throw new ValidationException("La habitación asociada es obligatoria.");
        }
    }
}
