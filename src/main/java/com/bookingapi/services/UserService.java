package com.bookingapi.services;

import com.bookingapi.exceptions.ResourceNotFoundException;
import com.bookingapi.exceptions.ValidationException;
import com.bookingapi.models.User;
import com.bookingapi.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // Obtener todos los usuarios
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Crear un nuevo usuario
    public User createUser(User user) {
        // Validación básica (puedes agregar más validaciones aquí)
        if (user.getName() == null || user.getEmail() == null) {  // Cambiado a `name` y `email`
            throw new ValidationException("Name and email must be provided");
        }
        return userRepository.save(user);
    }

    // Buscar un usuario por ID
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    // Eliminar un usuario
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    // Actualizar un usuario existente
 // En UserService.java
    public User updateUser(Long id, User updatedUser) throws ResourceNotFoundException {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + id));

        existingUser.setName(updatedUser.getName());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setPhone(updatedUser.getPhone());

        // No debes modificar el ID
        return userRepository.save(existingUser);
    }

}
