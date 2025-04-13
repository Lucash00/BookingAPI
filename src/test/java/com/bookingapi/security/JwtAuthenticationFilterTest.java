package com.bookingapi.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.context.SecurityContextHolder;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class JwtAuthenticationFilterTest {

    private JwtTokenUtils jwtTokenUtils;
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private FilterChain filterChain;

    @BeforeEach
    void setUp() {
        // Mock de JwtTokenUtils
        jwtTokenUtils = mock(JwtTokenUtils.class);
        jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtTokenUtils);
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        filterChain = mock(FilterChain.class);
    }

    @Test
    void testDoFilterInternal_NoToken() throws ServletException, IOException {
        // No hay token en la cabecera
        when(request.getHeader("Authorization")).thenReturn(null);

        // Ejecutar el filtro
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Verificar que el filtro pasa sin hacer autenticación
        verify(filterChain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void testDoFilterInternal_InvalidToken() throws ServletException, IOException {
        // Token con el prefijo "Bearer", pero es inválido
        when(request.getHeader("Authorization")).thenReturn("Bearer invalidToken");
        when(jwtTokenUtils.extractUsername(anyString())).thenReturn(null);  // El username no se extrae correctamente

        // Ejecutar el filtro
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Verificar que el filtro pasa sin hacer autenticación y limpia el contexto
        verify(filterChain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());  // El contexto debe estar limpio
    }


    @Test
    void testDoFilterInternal_ValidToken_WithRoles() throws ServletException, IOException {
        String token = "validToken";
        String username = "john.doe";
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtTokenUtils.extractUsername(token)).thenReturn(username);
        when(jwtTokenUtils.validateToken(token, username)).thenReturn(true);
        when(jwtTokenUtils.extractRoles(token)).thenReturn(Collections.singletonList("ADMIN"));

        // Ejecutar el filtro
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Verificar que se haya autenticado correctamente
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        assertEquals(username, SecurityContextHolder.getContext().getAuthentication().getName());
        assertTrue(SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
            .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN")));
    }

    @Test
    void testDoFilterInternal_ValidToken_NoRoles() throws ServletException, IOException {
        String token = "validToken";
        String username = "john.doe";
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtTokenUtils.extractUsername(token)).thenReturn(username);
        when(jwtTokenUtils.validateToken(token, username)).thenReturn(true);
        when(jwtTokenUtils.extractRoles(token)).thenReturn(Collections.emptyList());

        // Ejecutar el filtro
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Verificar que se haya autenticado correctamente
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        assertEquals(username, SecurityContextHolder.getContext().getAuthentication().getName());
        assertTrue(SecurityContextHolder.getContext().getAuthentication().getAuthorities().isEmpty());
    }

    @Test
    void testDoFilterInternal_TokenWithInvalidUsername() throws ServletException, IOException {
        String token = "validToken";
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtTokenUtils.extractUsername(token)).thenReturn(null); // No se puede extraer el username

        // Ejecutar el filtro
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Verificar que el filtro pasa sin hacer autenticación
        verify(filterChain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

}
