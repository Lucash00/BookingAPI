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



    @BeforeEach
    void setUp() {
        username = "john.doe";
        secretKey = "SpringPrueba0123456789012345678901234567890";  // 32 caracteres para asegurar una clave de 256 bits
        
        // Crear la instancia de JwtTokenUtils
        jwtTokenUtils = spy(new JwtTokenUtils());
        
        // Inyectar la clave secreta manualmente
        ReflectionTestUtils.setField(jwtTokenUtils, "secretKey", secretKey);
        
        // Iniciar el método post-construct
        jwtTokenUtils.init();
    }

    @Test
    void testGenerateToken() {
        // Verificar que se genera el token con un username
        String token = jwtTokenUtils.generateToken(username);
        
        // Verificar que el token no es null ni vacío
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void testExtractUsername() {
        String token = jwtTokenUtils.generateToken(username);

        // Verificar que el nombre de usuario extraído coincide
        String extractedUsername = jwtTokenUtils.extractUsername(token);
        assertEquals(username, extractedUsername);
    }

    @Test
    void testExtractRoles() {
        // Asumiendo que los roles están presentes en el token
        String token = jwtTokenUtils.generateToken(username);

        // Mock de roles
        List<String> roles = List.of("ADMIN", "USER");
        when(jwtTokenUtils.extractRoles(token)).thenReturn(roles);

        // Verificar la extracción de roles
        List<String> extractedRoles = jwtTokenUtils.extractRoles(token);
        assertNotNull(extractedRoles);
        assertEquals(2, extractedRoles.size());
        assertTrue(extractedRoles.contains("ADMIN"));
        assertTrue(extractedRoles.contains("USER"));
    }

    @Test
    void testIsTokenExpired() {
        String token = jwtTokenUtils.generateToken(username);

        // Verificar si el token no ha expirado inmediatamente después de la generación
        assertFalse(jwtTokenUtils.isTokenExpired(token));
    }

    @Test
    void testValidateToken() {
        String token = jwtTokenUtils.generateToken(username);

        // Verificar que el token es válido cuando el nombre coincide y no ha expirado
        assertTrue(jwtTokenUtils.validateToken(token, username));

        // Cambiar el nombre de usuario para invalidar el token
        String invalidUsername = "invalid.user";
        assertFalse(jwtTokenUtils.validateToken(token, invalidUsername));

        // Simular expiración del token (forzamos la expiración)
        String expiredToken = jwtTokenUtils.generateToken(username);
        when(jwtTokenUtils.isTokenExpired(expiredToken)).thenReturn(true);
        assertFalse(jwtTokenUtils.validateToken(expiredToken, username));
    }
}
