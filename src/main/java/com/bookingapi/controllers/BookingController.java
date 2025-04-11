package com.bookingapi.controllers;

import com.bookingapi.models.Booking;
import com.bookingapi.services.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.bookingapi.exceptions.ResourceNotFoundException;
import com.bookingapi.exceptions.ValidationException;
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
            // La excepción se maneja en GlobalExceptionHandler, por lo que no es necesario manejarla aquí
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Booking> createBooking(@RequestBody Booking booking) {
        // Validaciones para asegurarse de que los campos no estén vacíos
        if (booking.getCustomerName() == null || booking.getCustomerName().isBlank()) {
            throw new ValidationException("El nombre del cliente es obligatorio.");
        }

        if (booking.getService() == null || booking.getService().isBlank()) {
            throw new ValidationException("El servicio es obligatorio.");
        }

        if (booking.getBookingDate() == null) {
            throw new ValidationException("La fecha de la reserva es obligatoria.");
        }

        Booking createdBooking = bookingService.createBooking(booking);
        return new ResponseEntity<>(createdBooking, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Booking> updateBooking(@PathVariable Long id, @RequestBody Booking updatedBooking) {
        // Validaciones para asegurarse de que los campos no estén vacíos
        if (updatedBooking.getCustomerName() == null || updatedBooking.getCustomerName().isBlank()) {
            throw new ValidationException("El nombre del cliente es obligatorio.");
        }

        if (updatedBooking.getService() == null || updatedBooking.getService().isBlank()) {
            throw new ValidationException("El servicio es obligatorio.");
        }

        if (updatedBooking.getBookingDate() == null) {
            throw new ValidationException("La fecha de la reserva es obligatoria.");
        }

        try {
            Booking booking = bookingService.updateBooking(id, updatedBooking);
            return ResponseEntity.ok(booking); // 200 OK
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.notFound().build(); // 404 Not Found
        } catch (ValidationException ex) {
            return ResponseEntity.badRequest().body(null); // 400 Bad Request
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable Long id) {
        try {
            bookingService.deleteBooking(id);
            return ResponseEntity.noContent().build(); // 204 No Content
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.notFound().build(); // 404 Not Found
        }
    }
}
