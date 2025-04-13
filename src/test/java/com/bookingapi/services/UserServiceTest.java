package com.bookingapi.services;

import com.bookingapi.exceptions.ResourceNotFoundException;
import com.bookingapi.exceptions.ValidationException;
import com.bookingapi.models.Role;
import com.bookingapi.models.User;
import com.bookingapi.repositories.RoleRepository;
import com.bookingapi.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    private User user;
    private Role role;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");
        user.setPhone("123456789");

        role = new Role();
        role.setName("USER");
    }

    // Test para obtener todos los usuarios
    @Test
    public void testGetAllUsers() {
        when(userRepository.findAll()).thenReturn(List.of(user));

        var users = userService.getAllUsers();

        assertNotNull(users);
        assertEquals(1, users.size());
        verify(userRepository, times(1)).findAll();
    }

    // Test para crear un usuario
    @Test
    public void testCreateUser() {
        // Creamos roles de prueba
        Role role = new Role();
        role.setName("USER");
        List<Role> roles = new ArrayList<>();
        roles.add(role);  // Aseguramos que la lista no esté vacía

        // Creamos el usuario con la lista de roles inicializada
        User newUser = new User("John Doe", "john.doe@example.com", "123456789", null, null, roles);

        when(userRepository.save(any(User.class))).thenReturn(newUser);

        // Simulamos que el rol existe
        when(roleRepository.findByName("USER")).thenReturn(Optional.of(role));

        User createdUser = userService.createUser(newUser, "USER");  // Aseguramos que pasamos el rol correcto

        // Verificaciones
        assertNotNull(createdUser);
        assertEquals("John Doe", createdUser.getName());
        assertEquals("123456789", createdUser.getPhone());
        assertFalse(createdUser.getRoles().isEmpty());  // Verificamos que roles no esté vacío
        verify(userRepository, times(1)).save(any(User.class));
    }




    // Test para crear un usuario con rol que no existe
    @Test
    public void testCreateUserWithNonExistingRole() {
        when(roleRepository.findByName(any())).thenReturn(Optional.empty());

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            userService.createUser(user, "NON_EXISTING_ROLE");
        });

        assertEquals("Role not found: NON_EXISTING_ROLE", exception.getMessage());
    }

    // Test para obtener un usuario por ID
    @Test
    public void testGetUserById() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User foundUser = userService.getUserById(1L);

        assertNotNull(foundUser);
        assertEquals("John Doe", foundUser.getName());
        verify(userRepository, times(1)).findById(1L);
    }

    // Test para obtener un usuario por ID que no existe
    @Test
    public void testGetUserById_NotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            userService.getUserById(1L);
        });

        assertEquals("User not found with id: 1", exception.getMessage());
    }

    // Test para eliminar un usuario
    @Test
    public void testDeleteUser() {
        when(userRepository.existsById(1L)).thenReturn(true);

        userService.deleteUser(1L);

        verify(userRepository, times(1)).deleteById(1L);
    }

    // Test para eliminar un usuario que no existe
    @Test
    public void testDeleteUser_NotFound() {
        when(userRepository.existsById(1L)).thenReturn(false);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            userService.deleteUser(1L);
        });

        assertEquals("User not found with id: 1", exception.getMessage());
    }

    // Test para actualizar un usuario
    @Test
    public void testUpdateUser() {
        User updatedUser = new User();
        updatedUser.setName("Jane Doe");
        updatedUser.setEmail("jane.doe@example.com");
        updatedUser.setPhone("987654321");

        // Simulamos la búsqueda del usuario existente
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        
        // Simulamos la asignación automática del ID cuando el usuario es guardado
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User u = invocation.getArgument(0);
            // Usamos setAccessible para permitir el acceso a campos privados
            try {
                var field = User.class.getDeclaredField("id");
                field.setAccessible(true);  // Permite modificar un campo privado
                field.set(u, 1L); // Asignamos el id
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
            return u;
        });

        // Llamada al servicio para actualizar el usuario
        User returnedUser = userService.updateUser(1L, updatedUser);

        // Verificaciones
        assertNotNull(returnedUser);
        assertEquals("Jane Doe", returnedUser.getName());
        assertEquals("987654321", returnedUser.getPhone());
        assertEquals(1L, returnedUser.getId()); // Verificamos que el ID se mantiene
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(any(User.class));
    }



    // Test para actualizar un usuario que no existe
    @Test
    public void testUpdateUser_NotFound() {
        User updatedUser = new User();
        updatedUser.setName("Jane Doe");
        updatedUser.setEmail("jane.doe@example.com");
        updatedUser.setPhone("987654321");

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            userService.updateUser(1L, updatedUser);
        });

        assertEquals("User not found with id: 1", exception.getMessage());
    }
}
