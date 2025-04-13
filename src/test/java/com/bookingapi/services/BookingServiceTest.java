package com.bookingapi.services;

import com.bookingapi.models.Booking;
import com.bookingapi.repositories.BookingRepository;
import com.bookingapi.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

@SpringBootTest
class BookingServiceTest {

    private BookingService bookingService;
    private BookingRepository bookingRepository;

    @BeforeEach
    void setUp() {
        // Mockear el repositorio
        bookingRepository = mock(BookingRepository.class);
        bookingService = new BookingService();
        bookingService.bookingRepository = bookingRepository;
    }

    @Test
    void testGetAllBookings() {
        // Definir comportamiento del mock
        when(bookingRepository.findAll()).thenReturn(List.of(new Booking(), new Booking()));

        // Llamada al método y verificación
        assertEquals(2, bookingService.getAllBookings().size());
        verify(bookingRepository, times(1)).findAll();
    }

    @Test
    void testGetBookingById_Found() {
        // Definir comportamiento del mock
        Long bookingId = 1L;
        Booking mockBooking = new Booking();
        mockBooking.setId(bookingId);
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(mockBooking));

        // Llamada al método y verificación
        Booking booking = bookingService.getBookingById(bookingId);
        assertNotNull(booking);
        assertEquals(bookingId, booking.getId());
    }

    @Test
    void testGetBookingById_NotFound() {
        // Definir comportamiento del mock para que no se encuentre la reserva
        Long bookingId = 1L;
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.empty());

        // Llamada al método y verificación de excepción
        assertThrows(ResourceNotFoundException.class, () -> bookingService.getBookingById(bookingId));
    }

    @Test
    void testCreateBooking() {
        // Definir comportamiento del mock
        Booking newBooking = new Booking();
        when(bookingRepository.save(newBooking)).thenReturn(newBooking);

        // Llamada al método y verificación
        Booking createdBooking = bookingService.createBooking(newBooking);
        assertNotNull(createdBooking);
        verify(bookingRepository, times(1)).save(newBooking);
    }

    @Test
    void testUpdateBooking() {
        // Definir comportamiento del mock
        Long bookingId = 1L;
        Booking existingBooking = new Booking();
        existingBooking.setId(bookingId);
        existingBooking.setCustomerName("Old Name"); // Asegúrate de darle un valor inicial
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(existingBooking));

        // Datos de la actualización
        Booking updatedBooking = new Booking();
        updatedBooking.setCustomerName("Updated Name");

        // Definir el comportamiento del mock para el método save
        when(bookingRepository.save(existingBooking)).thenReturn(existingBooking);  // Guardar y devolver el objeto actualizado

        // Llamada al método y verificación
        Booking updatedBookingResult = bookingService.updateBooking(bookingId, updatedBooking);

        // Verifica que el nombre fue actualizado correctamente
        assertNotNull(updatedBookingResult);  // El resultado no debe ser nulo
        assertEquals("Updated Name", updatedBookingResult.getCustomerName());  // Verifica que el nombre fue actualizado

        // Verificar que el método save fue llamado
        verify(bookingRepository, times(1)).save(existingBooking);
    }



    @Test
    void testDeleteBooking() {
        // Definir comportamiento del mock
        Long bookingId = 1L;
        Booking booking = new Booking();
        booking.setId(bookingId);
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));

        // Llamada al método y verificación
        bookingService.deleteBooking(bookingId);
        verify(bookingRepository, times(1)).delete(booking);
    }

    @Test
    void testDeleteBooking_NotFound() {
        // Definir comportamiento del mock para que no se encuentre la reserva
        Long bookingId = 1L;
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.empty());

        // Llamada al método y verificación de excepción
        assertThrows(ResourceNotFoundException.class, () -> bookingService.deleteBooking(bookingId));
    }
}
