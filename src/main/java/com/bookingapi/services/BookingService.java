package com.bookingapi.services;

import com.bookingapi.exceptions.ResourceNotFoundException;
import com.bookingapi.exceptions.ValidationException;
import com.bookingapi.models.Booking;
import com.bookingapi.repositories.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingService {

	@Autowired
	private BookingRepository bookingRepository;

	// Obtener todas las reservas
	public List<Booking> getAllBookings() {
		return bookingRepository.findAll();
	}

	// Crear una nueva reserva
	public Booking createBooking(Booking booking) {
		// Validación básica (puedes agregar más validaciones aquí)
		if (booking.getCustomerName() == null || booking.getService() == null) {
			throw new ValidationException("Customer name and service must be provided");
		}
		return bookingRepository.save(booking);
	}

	// Buscar una reserva por ID
	public Booking getBookingById(Long id) {
		return bookingRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + id));
	}

	// Eliminar una reserva
	public void deleteBooking(Long id) {
		// Verificar si la reserva existe antes de eliminarla
		if (!bookingRepository.existsById(id)) {
			throw new ResourceNotFoundException("Booking not found with id: " + id);
		}
		bookingRepository.deleteById(id);
	}

	// Actualizar una reserva existente
	public Booking updateBooking(Long id, Booking booking) {
		if (!bookingRepository.existsById(id)) {
			throw new ResourceNotFoundException("Booking not found with id: " + id);
		}

		// Validación básica para actualizar
		if (booking.getCustomerName() == null || booking.getService() == null) {
			throw new ValidationException("Customer name and service must be provided");
		}

		booking.setId(id);
		return bookingRepository.save(booking);
	}

	// Consultas personalizadas pueden ir aquí

	public void printBookingDetails(Booking booking) {
		System.out.println("Customer Name: " + booking.getCustomerName());
		System.out.println("Service: " + booking.getService());
		System.out.println("Booking Date: " + booking.getBookingDate());
		System.out.println("Confirmed: " + booking.isConfirmed());
	}
}
