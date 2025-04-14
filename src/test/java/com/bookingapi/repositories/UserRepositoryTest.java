package com.bookingapi.repositories;

import com.bookingapi.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

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
        User user = new User();
        user.setName("john_doe");
        user.setEmail("john.doe@example.com");

        User savedUser = userRepository.save(user);

        assertNotNull(savedUser);
        assertNotNull(savedUser.getId());
        assertEquals("john_doe", savedUser.getName());
        assertEquals("john.doe@example.com", savedUser.getEmail());
    }

    @Test
    public void testFindById() {
        User user = new User();
        user.setName("jane_doe");
        user.setEmail("jane.doe@example.com");
        User savedUser = userRepository.save(user);

        User foundUser = userRepository.findById(savedUser.getId()).orElse(null);

        assertNotNull(foundUser);
        assertEquals(savedUser.getId(), foundUser.getId());
        assertEquals("jane_doe", foundUser.getName());
        assertEquals("jane.doe@example.com", foundUser.getEmail());
    }

    @Test
    public void testFindByIdNotFound() {
        User foundUser = userRepository.findById(999L).orElse(null);

        assertNull(foundUser);
    }

    @Test
    public void testFindByEmail() {
        User user = new User();
        user.setName("john_doe");
        user.setEmail("john.doe@example.com");
        userRepository.save(user);

        Optional<User> foundUser = userRepository.findByEmail("john.doe@example.com");

        assertTrue(foundUser.isPresent());
        assertEquals("john.doe@example.com", foundUser.get().getEmail());
    }

    @Test
    public void testFindByEmailNotFound() {
        Optional<User> foundUser = userRepository.findByEmail("nonexistent@example.com");

        assertFalse(foundUser.isPresent());
    }

    @Test
    public void testExistsByEmail() {
        User user = new User();
        user.setName("john_doe");
        user.setEmail("john.doe@example.com");
        userRepository.save(user);

        assertTrue(userRepository.existsByEmail("john.doe@example.com"));
        assertFalse(userRepository.existsByEmail("nonexistent@example.com"));
    }
}

