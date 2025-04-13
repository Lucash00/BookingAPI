package com.bookingapi.repositories;

import com.bookingapi.models.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @BeforeEach
    public void setUp() {
        roleRepository.deleteAll();
        Role adminRole = new Role();
        adminRole.setName("ADMIN");
        roleRepository.save(adminRole);
    }


    @Test
    public void testFindByName() {
        // Buscamos el rol por nombre
        Role foundRole = roleRepository.findByName("ADMIN").orElse(null);

        // Verificamos que el rol se encontró
        assertNotNull(foundRole);
        assertEquals("ADMIN", foundRole.getName());
    }

    @Test
    public void testFindByNameNotFound() {
        // Buscamos un rol que no existe
        Role foundRole = roleRepository.findByName("NON_EXISTENT_ROLE").orElse(null);

        // Verificamos que no se encontró el rol
        assertNull(foundRole);
    }
}
