package com.bookingapi.repositories;

import com.bookingapi.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        userRepository.deleteAll(); // Limpiar la base de datos antes de cada test
    }

    @Test
    public void testSaveUser() {
        // Creamos un nuevo usuario
        User user = new User();
        user.setName("john_doe");
        user.setEmail("john.doe@example.com");

        // Guardamos el usuario
        User savedUser = userRepository.save(user);

        // Verificamos que el usuario se guardó correctamente
        assertNotNull(savedUser);
        assertNotNull(savedUser.getId()); // El ID debe haberse generado
        assertEquals("john_doe", savedUser.getName());
        assertEquals("john.doe@example.com", savedUser.getEmail());
    }

    @Test
    public void testFindById() {
        // Creamos un nuevo usuario y lo guardamos
        User user = new User();
        user.setName("jane_doe");
        user.setEmail("jane.doe@example.com");
        User savedUser = userRepository.save(user);

        // Buscamos el usuario por su ID
        User foundUser = userRepository.findById(savedUser.getId()).orElse(null);

        // Verificamos que el usuario fue encontrado correctamente
        assertNotNull(foundUser);
        assertEquals(savedUser.getId(), foundUser.getId());
        assertEquals("jane_doe", foundUser.getName());
        assertEquals("jane.doe@example.com", foundUser.getEmail());
    }

    @Test
    public void testFindByIdNotFound() {
        // Buscamos un usuario que no existe
        User foundUser = userRepository.findById(999L).orElse(null);

        // Verificamos que no se encontró el usuario
        assertNull(foundUser);
    }
}
