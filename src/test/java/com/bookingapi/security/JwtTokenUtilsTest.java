package com.bookingapi.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.util.List;

class JwtTokenUtilsTest {

    private JwtTokenUtils jwtTokenUtils;

    private String username;
    private String secretKey;
    private List<String> roles;

    @BeforeEach
    void setUp() {
        username = "john.doe";
        secretKey = "SpringPrueba0123456789012345678901234567890";  // 32 caracteres
        roles = List.of("USER"); // Puedes ajustar esto según tu lógica

        jwtTokenUtils = spy(new JwtTokenUtils());
        ReflectionTestUtils.setField(jwtTokenUtils, "secretKey", secretKey);
        jwtTokenUtils.init();
    }

    @Test
    void testGenerateToken() {
        String token = jwtTokenUtils.generateToken(username, roles);
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void testExtractUsername() {
        String token = jwtTokenUtils.generateToken(username, roles);
        String extractedUsername = jwtTokenUtils.extractUsername(token);
        assertEquals(username, extractedUsername);
    }

    @Test
    void testExtractRoles() {
        String token = jwtTokenUtils.generateToken(username, roles);

        // Aquí puedes eliminar el mock si el método funciona correctamente
        List<String> extractedRoles = jwtTokenUtils.extractRoles(token);
        assertNotNull(extractedRoles);
        assertTrue(extractedRoles.contains("USER"));
    }

    @Test
    void testIsTokenExpired() {
        String token = jwtTokenUtils.generateToken(username, roles);
        assertFalse(jwtTokenUtils.isTokenExpired(token));
    }

    @Test
    void testValidateToken() {
        String token = jwtTokenUtils.generateToken(username, roles);

        assertTrue(jwtTokenUtils.validateToken(token, username));

        String invalidUsername = "invalid.user";
        assertFalse(jwtTokenUtils.validateToken(token, invalidUsername));

        // Simular token expirado
        when(jwtTokenUtils.isTokenExpired(token)).thenReturn(true);
        assertFalse(jwtTokenUtils.validateToken(token, username));
    }
}
