package com.bookingapi.controllers;

import com.bookingapi.models.User;
import com.bookingapi.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.bookingapi.exceptions.ResourceNotFoundException;
import com.bookingapi.exceptions.ValidationException;
import org.springframework.http.HttpStatus;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        try {
            User user = userService.getUserById(id);
            return ResponseEntity.ok(user);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        // Validaciones
        if (user.getName() == null || user.getName().isBlank()) {  // Cambié username por name
            throw new ValidationException("El nombre de usuario es obligatorio.");
        }

        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new ValidationException("El correo electrónico es obligatorio.");
        }

        User createdUser = userService.createUser(user);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        validateUserInput(updatedUser);
        try {
            User user = userService.updateUser(id, updatedUser);
            return ResponseEntity.ok(user);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (ValidationException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); // Puedes enviar ErrorResponse aquí
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }
    
    private void validateUserInput(User user) {
        if (user.getName() == null || user.getName().isBlank()) {  // Cambié username por name
            throw new ValidationException("El nombre de usuario es obligatorio.");
        }

        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new ValidationException("El correo electrónico es obligatorio.");
        }
    }
}
