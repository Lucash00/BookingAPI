package com.bookingapi.services;

import com.bookingapi.exceptions.ValidationException;
import com.bookingapi.models.Role;
import com.bookingapi.models.User;
import com.bookingapi.repositories.RoleRepository;
import com.bookingapi.repositories.UserRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    @Autowired RoleRepository roleRepository;
    @Autowired UserRepository userRepository;

    // Crear un nuevo rol con validación para evitar duplicados
    public Role createRole(Role role) {
        Optional<Role> existingRole = roleRepository.findByName(role.getName());
        if (existingRole.isPresent()) {
            throw new ValidationException("Role with name '" + role.getName() + "' already exists.");
        }
        return roleRepository.save(role);
    }

    // Obtener un rol por su nombre
    public Optional<Role> getRoleByName(String roleName) {
        return roleRepository.findByName(roleName);
    }

    // Obtener todos los roles
    public List<Role> getAllRoles() {
        return roleRepository.findAll();  // Obtiene todos los roles
    }

    // Asignar un rol a un usuario
    public void assignRoleToUser(Long userId, String roleName) {
        Optional<User> userOpt = userRepository.findById(userId);
        Optional<Role> roleOpt = roleRepository.findByName(roleName);

        if (userOpt.isPresent() && roleOpt.isPresent()) {
            User user = userOpt.get();
            Role role = roleOpt.get();

            // Verificar si el usuario ya tiene el rol
            if (user.getRoles().contains(role)) {
                throw new ValidationException("User already has the role '" + roleName + "'.");
            }

            user.getRoles().add(role);  // Asignamos el rol al usuario
            userRepository.save(user);  // Guardamos al usuario actualizado
        } else {
            throw new ValidationException("User or Role not found");
        }
    }

    // Eliminar un rol por nombre
    public void deleteRoleByName(String roleName) {
        Optional<Role> roleOpt = roleRepository.findByName(roleName);
        if (roleOpt.isPresent()) {
            roleRepository.delete(roleOpt.get());  // Eliminar el rol
        } else {
            throw new ValidationException("Role not found with name: " + roleName);
        }
    }
}
