package com.bookingapi.repositories;

import com.bookingapi.models.Room;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class RoomRepositoryTest {

    @Autowired
    private RoomRepository roomRepository;

    @BeforeEach
    public void setUp() {
        roomRepository.deleteAll(); // Limpiar la base de datos antes de cada test
    }

    @Test
    public void testSaveRoom() {
        // Creamos una nueva habitación
        Room room = new Room();
        room.setName("Room 101");
        room.setCapacity(2);

        // Guardamos la habitación
        Room savedRoom = roomRepository.save(room);

        // Verificamos que la habitación se guardó correctamente
        assertNotNull(savedRoom);
        assertNotNull(savedRoom.getId()); // El ID debe haberse generado
        assertEquals("Room 101", savedRoom.getName());
        assertEquals(2, savedRoom.getCapacity());
    }

    @Test
    public void testFindById() {
        // Creamos una nueva habitación y la guardamos
        Room room = new Room();
        room.setName("Room 202");
        room.setCapacity(4);
        Room savedRoom = roomRepository.save(room);

        // Buscamos la habitación por su ID
        Room foundRoom = roomRepository.findById(savedRoom.getId()).orElse(null);

        // Verificamos que la habitación fue encontrada correctamente
        assertNotNull(foundRoom);
        assertEquals(savedRoom.getId(), foundRoom.getId());
        assertEquals("Room 202", foundRoom.getName());
        assertEquals(4, foundRoom.getCapacity());
    }

    @Test
    public void testFindByIdNotFound() {
        // Buscamos una habitación que no existe
        Room foundRoom = roomRepository.findById(999L).orElse(null);

        // Verificamos que no se encontró la habitación
        assertNull(foundRoom);
    }
}
