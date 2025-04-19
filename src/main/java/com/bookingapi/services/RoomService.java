package com.bookingapi.services;

import com.bookingapi.exceptions.ResourceNotFoundException;
import com.bookingapi.models.Room;
import com.bookingapi.repositories.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomService {

    @Autowired RoomRepository roomRepository;

    // Obtener todas las habitaciones
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    // Crear una nueva habitación
    public Room createRoom(Room room) {
        // Validación básica (puedes agregar más validaciones aquí)
        room.validateRoomInput(room);  // Utilizando el método de validación que ya definiste
        return roomRepository.save(room);
    }

    // Buscar una habitación por ID
    public Room getRoomById(Long id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with id: " + id));
    }

    // Eliminar una habitación
    public void deleteRoom(Long id) {
        if (!roomRepository.existsById(id)) {
            throw new ResourceNotFoundException("Room not found with id: " + id);
        }
        roomRepository.deleteById(id);
    }

    // Actualizar una habitación existente
    public Room updateRoom(Long id, Room room) {
        // Validación básica para actualizar
        room.validateRoomInput(room);  // Utilizando el método de validación que ya definiste

        // Verificar si la habitación existe
        Room existingRoom = roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with id: " + id));

        // Actualizar los campos relevantes
        existingRoom.setName(room.getName());
        existingRoom.setLocation(room.getLocation());
        existingRoom.setCapacity(room.getCapacity());
        existingRoom.setAvailable(room.isAvailable());
        existingRoom.setType(room.getType());
        existingRoom.setPricePerNight(room.getPricePerNight());
        existingRoom.setDescription(room.getDescription());
        existingRoom.setImageUrl(room.getImageUrl());
        existingRoom.setMaintenanceStatus(room.getMaintenanceStatus());

        // Guardar la habitación actualizada
        return roomRepository.save(existingRoom);
    }
}
