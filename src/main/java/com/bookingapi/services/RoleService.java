package com.bookingapi.services;

import com.bookingapi.exceptions.ValidationException;
import com.bookingapi.models.Role;
import com.bookingapi.models.User;
import com.bookingapi.repositories.RoleRepository;
import com.bookingapi.repositories.UserRepository;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    public Role createRole(Role role) {
        return roleRepository.save(role);  // Crear un nuevo rol
    }

    public Optional<Role> getRoleByName(String roleName) {
        return roleRepository.findByName(roleName);  // Obtener un rol por nombre
    }

    // Método para asignar un rol a un usuario
    public void assignRoleToUser(Long userId, String roleName) {
        Optional<User> userOpt = userRepository.findById(userId);
        Optional<Role> roleOpt = roleRepository.findByName(roleName);

        if (userOpt.isPresent() && roleOpt.isPresent()) {
            User user = userOpt.get();
            Role role = roleOpt.get();
            user.getRoles().add(role); // Asignamos el rol al usuario
            userRepository.save(user); // Guardamos al usuario actualizado
        } else {
            throw new ValidationException("User or Role not found");
        }
    }
}
