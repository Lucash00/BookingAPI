package com.bookingapi.repositories;

import com.bookingapi.models.Booking;
import com.bookingapi.models.Room;
import com.bookingapi.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

@SpringBootTest
public class BookingRepositoryTest {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoomRepository roomRepository;

    private User testUser;
    private Room testRoom;

    @BeforeEach
    public void setUp() {
        // Limpiar las bases de datos antes de cada test
        bookingRepository.deleteAll();
        userRepository.deleteAll();
        roomRepository.deleteAll();

        // Crear un usuario de prueba
        testUser = new User();
        testUser.setName("testuser");
        testUser.setPassword("testpassword");
        userRepository.save(testUser);

        // Crear una habitación de prueba
        testRoom = new Room();
        testRoom.setName("Room 101");
        testRoom.setCapacity(2);
        roomRepository.save(testRoom);
    }

    @Test
    public void testSaveBooking() {
        // Crear una reserva
        Booking booking = new Booking();
        booking.setUser(testUser);
        booking.setRoom(testRoom);
        booking.setBookingDate(LocalDate.of(2025, 5, 1));
        booking.setBookingDate(LocalDate.of(2025, 5, 5)); 

        // Guardar la reserva
        Booking savedBooking = bookingRepository.save(booking);

        // Verificar que la reserva se guardó correctamente
        assertNotNull(savedBooking);
        assertNotNull(savedBooking.getId()); 
        assertEquals(testUser.getName(), savedBooking.getUser().getName());
        assertEquals(testRoom.getName(), savedBooking.getRoom().getName());
    }

    @Test
    public void testFindById() {
        // Crear y guardar una reserva
        Booking booking = new Booking();
        booking.setUser(testUser);
        booking.setRoom(testRoom);
        booking.setBookingDate(LocalDate.parse("2025-05-05")); 
        Booking savedBooking = bookingRepository.save(booking);

        // Buscar la reserva por su ID
        Booking foundBooking = bookingRepository.findById(savedBooking.getId()).orElse(null);

        // Verificar que la reserva fue encontrada correctamente
        assertNotNull(foundBooking);
        assertEquals(savedBooking.getId(), foundBooking.getId());
    }


    @Test
    public void testFindByIdNotFound() {
        // Intentamos encontrar una reserva que no existe
        Booking foundBooking = bookingRepository.findById(999L).orElse(null);

        // Verificar que no se encontró la reserva
        assertNull(foundBooking);
    }

    @Test
    public void testBookingWithInvalidUser() {
        // Crear una reserva con un usuario no existente
        User invalidUser = new User();
        invalidUser.setName("invaliduser");
        invalidUser.setPassword("invalidpassword");

        // Intentamos guardar la reserva
        Booking booking = new Booking();
        booking.setUser(invalidUser);  // Usuario no guardado en la base de datos
        booking.setRoom(testRoom);
        booking.setBookingDate(LocalDate.parse("2025-05-01"));  
        booking.setBookingDate(LocalDate.parse("2025-05-05"));  


        // Verificamos que la reserva no se puede guardar
        assertThrows(Exception.class, () -> {
            bookingRepository.save(booking);
        });
    }
}
