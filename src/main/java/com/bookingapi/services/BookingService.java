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
    BookingRepository bookingRepository;

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public Booking getBookingById(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva no encontrada con id: " + id));
    }

    public Booking createBooking(Booking booking) {
        // Validar la entrada antes de crear la reserva
        booking.validateBookingInput(booking);
        return bookingRepository.save(booking);
    }

    public Booking updateBooking(Long id, Booking updatedBooking) {
        // Buscar la reserva existente
        Booking existingBooking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva no encontrada con id: " + id));

        // Validar la entrada antes de la actualización
        updatedBooking.validateBookingInput(updatedBooking);

        // Actualizar solo los campos relevantes
        existingBooking.setCustomerName(updatedBooking.getCustomerName());
        existingBooking.setService(updatedBooking.getService());
        existingBooking.setBookingDate(updatedBooking.getBookingDate());
        existingBooking.setConfirmed(updatedBooking.isConfirmed());
        existingBooking.setUser(updatedBooking.getUser());
        existingBooking.setRoom(updatedBooking.getRoom());
        existingBooking.setStartDateTime(updatedBooking.getStartDateTime());
        existingBooking.setEndDateTime(updatedBooking.getEndDateTime());
        existingBooking.setDurationInMinutes(updatedBooking.getDurationInMinutes());
        existingBooking.setCheckInTime(updatedBooking.getCheckInTime());
        existingBooking.setCheckOutTime(updatedBooking.getCheckOutTime());
        existingBooking.setDiscountCode(updatedBooking.getDiscountCode());
        existingBooking.setStatus(updatedBooking.getStatus());
        existingBooking.setCancelled(updatedBooking.getCancelled());
        existingBooking.setCancellationReason(updatedBooking.getCancellationReason());
        existingBooking.setRescheduled(updatedBooking.getRescheduled());
        existingBooking.setRescheduledFromBookingId(updatedBooking.getRescheduledFromBookingId());
        existingBooking.setAddons(updatedBooking.getAddons());

        // Guardar y devolver la reserva actualizada
        return bookingRepository.save(existingBooking);
    }

    public void deleteBooking(Long id) {
        // Eliminar la reserva por id
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva no encontrada con id: " + id));
        bookingRepository.delete(booking);
    }
}
