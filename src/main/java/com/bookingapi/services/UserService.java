package com.bookingapi.services;

import com.bookingapi.exceptions.ResourceNotFoundException;
import com.bookingapi.exceptions.ValidationException;
import com.bookingapi.models.Role;
import com.bookingapi.models.User;
import com.bookingapi.repositories.RoleRepository;
import com.bookingapi.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;  // Repositorio para manejar roles

    // Obtener todos los usuarios
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Crear un nuevo usuario con el rol asociado
    public User createUser(User user, String roleName) {
        // Validación básica (puedes agregar más validaciones aquí)
        if (user.getName() == null || user.getEmail() == null) {
            throw new ValidationException("Name and email must be provided");
        }

        // Buscar el rol por nombre
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new ValidationException("Role not found: " + roleName));  // Aquí usamos orElseThrow

        // Asignar el rol al usuario
        user.getRoles().add(role); // Añadir el rol al usuario

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
    public User updateUser(Long id, User updatedUser) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        existingUser.setName(updatedUser.getName());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setPhone(updatedUser.getPhone());

        // No debes modificar el ID
        return userRepository.save(existingUser);
    }
}
