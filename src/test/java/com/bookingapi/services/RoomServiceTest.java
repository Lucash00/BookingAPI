package com.bookingapi.services;

import com.bookingapi.exceptions.ResourceNotFoundException;
import com.bookingapi.exceptions.ValidationException;
import com.bookingapi.models.Room;
import com.bookingapi.repositories.RoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Optional;

class RoomServiceTest {

    private RoomService roomService;
    private RoomRepository roomRepository;

    @BeforeEach
    void setUp() {
        // Mockear el repositorio de habitaciones
        roomRepository = mock(RoomRepository.class);
        roomService = new RoomService();
        roomService.roomRepository = roomRepository;
    }

    @Test
    void testGetAllRooms() {
        // Definir comportamiento del mock para obtener todas las habitaciones
        Room room1 = new Room();
        room1.setName("Room 1");
        room1.setCapacity(2);
        
        Room room2 = new Room();
        room2.setName("Room 2");
        room2.setCapacity(4);

        when(roomRepository.findAll()).thenReturn(Arrays.asList(room1, room2));

        // Llamar al método y verificar el comportamiento
        assertEquals(2, roomService.getAllRooms().size());
        verify(roomRepository, times(1)).findAll();
    }

    @Test
    void testCreateRoom_ValidData() {
        // Crear una habitación válida
        Room room = new Room();
        room.setName("New Room");
        room.setCapacity(3);

        when(roomRepository.save(room)).thenReturn(room);

        // Llamada al método y verificación
        Room createdRoom = roomService.createRoom(room);
        assertNotNull(createdRoom);
        assertEquals("New Room", createdRoom.getName());
        verify(roomRepository, times(1)).save(room);
    }

    @Test
    void testCreateRoom_InvalidData() {
        // Crear habitación con datos inválidos
        Room room = new Room();
        room.setName(null);  // Nombre nulo
        room.setCapacity(0); // Capacidad inválida

        // Llamar al método y verificar que se lanza una excepción
        assertThrows(ValidationException.class, () -> roomService.createRoom(room));
    }

    @Test
    void testGetRoomById_Found() {
        // Crear una habitación mock
        Room room = new Room();
        room.setName("Room 1");
        room.setCapacity(2);
        when(roomRepository.findById(1L)).thenReturn(Optional.of(room));

        // Llamar al método y verificar
        Room foundRoom = roomService.getRoomById(1L);
        assertNotNull(foundRoom);
        assertEquals("Room 1", foundRoom.getName());
    }

    @Test
    void testGetRoomById_NotFound() {
        // Configurar el mock para que no se encuentre la habitación
        when(roomRepository.findById(1L)).thenReturn(Optional.empty());

        // Llamar al método y verificar que lanza ResourceNotFoundException
        assertThrows(ResourceNotFoundException.class, () -> roomService.getRoomById(1L));
    }

    @Test
    void testDeleteRoom_Success() {
        // Configurar el mock para que exista la habitación
        when(roomRepository.existsById(1L)).thenReturn(true);

        // Llamar al método y verificar que se elimine correctamente
        roomService.deleteRoom(1L);
        verify(roomRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteRoom_NotFound() {
        // Configurar el mock para que no exista la habitación
        when(roomRepository.existsById(1L)).thenReturn(false);

        // Llamar al método y verificar que lanza ResourceNotFoundException
        assertThrows(ResourceNotFoundException.class, () -> roomService.deleteRoom(1L));
    }

    @Test
    void testUpdateRoom_Success() {
        // Crear habitación y mockear repositorios
        Room room = new Room();
        room.setName("Updated Room");
        room.setCapacity(5);

        Room existingRoom = new Room();
        existingRoom.setName("Old Room");
        existingRoom.setCapacity(2);

        when(roomRepository.existsById(1L)).thenReturn(true);
        when(roomRepository.findById(1L)).thenReturn(Optional.of(existingRoom));
        when(roomRepository.save(existingRoom)).thenReturn(existingRoom);

        // Llamar al método y verificar
        Room updatedRoom = roomService.updateRoom(1L, room);
        assertEquals("Updated Room", updatedRoom.getName());
        assertEquals(5, updatedRoom.getCapacity());
        verify(roomRepository, times(1)).save(existingRoom);
    }

    @Test
    void testUpdateRoom_RoomNotFound() {
        // Datos de la habitación a actualizar (con datos válidos pero ID no existente)
        Long roomId = 1L;
        Room room = new Room();
        room.setName("Room 101");
        room.setCapacity(2);

        // Configuración del mock para simular que la habitación no existe
        when(roomRepository.existsById(roomId)).thenReturn(false);  // Simula que no existe la habitación

        // Llamada al método y verificación de la excepción esperada
        assertThrows(ResourceNotFoundException.class, () -> roomService.updateRoom(roomId, room));
    }


    @Test
    void testUpdateRoom_InvalidData() {
        // Crear habitación con datos inválidos
        Room room = new Room();
        room.setName(null);  // Nombre nulo
        room.setCapacity(0); // Capacidad inválida

        // Llamar al método y verificar que se lanza una excepción
        assertThrows(ValidationException.class, () -> roomService.updateRoom(1L, room));
    }
}
