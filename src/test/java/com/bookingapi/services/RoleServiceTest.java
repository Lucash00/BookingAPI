package com.bookingapi.services;

import com.bookingapi.exceptions.ValidationException;
import com.bookingapi.models.Role;
import com.bookingapi.models.User;
import com.bookingapi.repositories.RoleRepository;
import com.bookingapi.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Optional;

class RoleServiceTest {

    private RoleService roleService;
    private RoleRepository roleRepository;
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        // Mockear los repositorios
        roleRepository = mock(RoleRepository.class);
        userRepository = mock(UserRepository.class);
        
        roleService = new RoleService();
        roleService.roleRepository = roleRepository;
        roleService.userRepository = userRepository;
    }

    @Test
    void testCreateRole() {
        // Definir comportamiento del mock para guardar un rol
        Role newRole = new Role();
        newRole.setName("ADMIN");
        when(roleRepository.save(newRole)).thenReturn(newRole);

        // Llamada al método y verificación
        Role createdRole = roleService.createRole(newRole);
        assertNotNull(createdRole);
        assertEquals("ADMIN", createdRole.getName());
        verify(roleRepository, times(1)).save(newRole);
    }

    @Test
    void testGetRoleByName_Found() {
        // Definir comportamiento del mock para obtener un rol por nombre
        Role mockRole = new Role();
        mockRole.setName("ADMIN");
        when(roleRepository.findByName("ADMIN")).thenReturn(Optional.of(mockRole));

        // Llamada al método y verificación
        Optional<Role> roleOpt = roleService.getRoleByName("ADMIN");
        assertTrue(roleOpt.isPresent());
        assertEquals("ADMIN", roleOpt.get().getName());
    }

    @Test
    void testGetRoleByName_NotFound() {
        // Definir comportamiento del mock para que no se encuentre el rol
        when(roleRepository.findByName("ADMIN")).thenReturn(Optional.empty());

        // Llamada al método y verificación de que no se encontró el rol
        Optional<Role> roleOpt = roleService.getRoleByName("ADMIN");
        assertFalse(roleOpt.isPresent());
    }

    @Test
    void testAssignRoleToUser() {
        // Crear usuario con constructor (sin necesidad de setId)
        Long userId = 1L;
        String roleName = "ADMIN";

        // Crear un objeto de usuario y asignarle el id manualmente solo para la prueba
        User mockUser = new User("John Doe", "john.doe@example.com", "123456789", null, null, null);
        
        // Inicializar la lista de roles (evitar NullPointerException)
        mockUser.setRoles(new ArrayList<>());  // Inicializamos la lista de roles vacía

        Role mockRole = new Role();
        mockRole.setName(roleName);

        // Configurar el comportamiento de los mocks
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(roleRepository.findByName(roleName)).thenReturn(Optional.of(mockRole));

        // Llamar al método para asignar el rol
        roleService.assignRoleToUser(userId, roleName);

        // Verificar que se ha guardado el usuario con el nuevo rol
        verify(userRepository, times(1)).save(mockUser);
    }




    @Test
    void testAssignRoleToUser_UserNotFound() {
        // Definir comportamiento del mock para que no se encuentre el usuario
        Long userId = 1L;
        String roleName = "ADMIN";

        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        when(roleRepository.findByName(roleName)).thenReturn(Optional.of(new Role()));

        // Llamada al método y verificación de excepción
        assertThrows(ValidationException.class, () -> roleService.assignRoleToUser(userId, roleName));
    }

    @Test
    void testAssignRoleToUser_RoleNotFound() {
        // Definir comportamiento del mock para que no se encuentre el rol
        Long userId = 1L;
        String roleName = "ADMIN";

        when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));
        when(roleRepository.findByName(roleName)).thenReturn(Optional.empty());

        // Llamada al método y verificación de excepción
        assertThrows(ValidationException.class, () -> roleService.assignRoleToUser(userId, roleName));
    }
}
