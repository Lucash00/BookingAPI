package com.bookingapi.controllers;

import com.bookingapi.models.Role;
import com.bookingapi.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    @Autowired
    private RoleService roleService;

    // Crear un nuevo rol (solo ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public Role createRole(@RequestBody Role role) {
        return roleService.createRole(role);
    }

    // Obtener un rol por nombre (solo ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{roleName}")
    public Optional<Role> getRoleByName(@PathVariable String roleName) {
        return roleService.getRoleByName(roleName);
    }

    // Obtener todos los roles (solo ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<Role> getAllRoles() {
        return roleService.getAllRoles();
    }

    // Asignar un rol a un usuario (solo ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/assign")
    public void assignRoleToUser(@RequestParam Long userId, @RequestParam String roleName) {
        roleService.assignRoleToUser(userId, roleName);
    }

    // Eliminar un rol por nombre (solo ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{roleName}")
    public void deleteRoleByName(@PathVariable String roleName) {
        roleService.deleteRoleByName(roleName);
    }
}
