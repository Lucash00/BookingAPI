package com.bookingapi.controllers;

import com.bookingapi.models.Room;
import com.bookingapi.services.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.bookingapi.exceptions.ResourceNotFoundException;
import com.bookingapi.exceptions.ValidationException;
import org.springframework.http.HttpStatus;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    @Autowired
    private RoomService roomService;

    @GetMapping
    public List<Room> getAllRooms() {
        return roomService.getAllRooms();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Room> getRoomById(@PathVariable Long id) {
        try {
            Room room = roomService.getRoomById(id);
            return ResponseEntity.ok(room);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Room> createRoom(@RequestBody Room room) {
        // Validaciones
        if (room.getName() == null || room.getName().isEmpty()) {
            throw new ValidationException("El nombre de la habitación es obligatorio.");
        }

        if (room.getCapacity() <= 0) {  // Validación de capacidad positiva
            throw new ValidationException("La capacidad de la habitación debe ser mayor a 0.");
        }

        Room createdRoom = roomService.createRoom(room);
        return new ResponseEntity<>(createdRoom, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Room> updateRoom(@PathVariable Long id, @RequestBody Room updatedRoom) {
        validateRoomInput(updatedRoom);
        try {
            Room room = roomService.updateRoom(id, updatedRoom);
            return ResponseEntity.ok(room);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (ValidationException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoom(@PathVariable Long id) {
        try {
            roomService.deleteRoom(id);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }
    
    private void validateRoomInput(Room room) {
        if (room.getName() == null || room.getName().isEmpty()) {
            throw new ValidationException("El nombre de la habitación es obligatorio.");
        }

        if (room.getCapacity() <= 0) {  // Validación de capacidad positiva
            throw new ValidationException("La capacidad de la habitación debe ser mayor a 0.");
        }
    }

}
