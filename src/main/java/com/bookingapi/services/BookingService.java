package com.bookingapi.services;

import com.bookingapi.models.Booking;
import com.bookingapi.repositories.BookingRepository;
import com.bookingapi.exceptions.ResourceNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public Booking getBookingById(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva no encontrada con id: " + id));
    }

    public Booking createBooking(Booking booking) {
        return bookingRepository.save(booking);
    }

    public Booking updateBooking(Long id, Booking updatedBooking) {
        Booking existingBooking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva no encontrada con id: " + id));

        existingBooking.setCustomerName(updatedBooking.getCustomerName());
        existingBooking.setService(updatedBooking.getService());
        existingBooking.setBookingDate(updatedBooking.getBookingDate());
        existingBooking.setConfirmed(updatedBooking.isConfirmed());
        existingBooking.setUser(updatedBooking.getUser());
        existingBooking.setRoom(updatedBooking.getRoom());

        return bookingRepository.save(existingBooking);
    }

    public void deleteBooking(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva no encontrada con id: " + id));
        bookingRepository.delete(booking);
    }
}
