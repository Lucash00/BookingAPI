//package com.bookingapi.controllers;
//
//import com.bookingapi.security.JwtTokenUtils;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.context.TestConfiguration;
//import org.springframework.context.annotation.Bean;
//import org.springframework.http.MediaType;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.util.List;
//
//import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.spy;
//
//@WebMvcTest(AuthController.class)
//class AuthControllerTest {
//
//    // Crear un logger para la clase de test
//    private static final Logger logger = LoggerFactory.getLogger(AuthControllerTest.class);
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private AuthenticationManager authenticationManager;
//
//    @SuppressWarnings("removal")
//	@MockBean
//    private JwtTokenUtils jwtTokenUtils; // Usamos MockBean aquí para reemplazar el bean real
//
//    private final String TOKEN = "fake-jwt-token";
//
//    @BeforeEach
//    void setUp() {
//        logger.info("Configurando el entorno de prueba");
//
//        UsernamePasswordAuthenticationToken authRequest =
//                new UsernamePasswordAuthenticationToken("testuser", "testpass");
//        Authentication authResult =
//                new UsernamePasswordAuthenticationToken("testuser", "testpass", List.of());
//
//        // Configuración de mock para el AuthenticationManager
//        Mockito.when(authenticationManager.authenticate(authRequest)).thenReturn(authResult);
//
//        // Usamos un spy en lugar de mock si la clase no es mockeable
//        jwtTokenUtils = spy(jwtTokenUtils);
//
//        // Mockeamos el método generateToken correctamente
//        Mockito.when(jwtTokenUtils.generateToken("testuser")).thenReturn(TOKEN);
//
//        logger.info("Configuración de prueba completada");
//    }
//
//    @Test
//    void loginSuccess_returnsToken() throws Exception {
//        logger.info("Iniciando la prueba de login");
//
//        String json = """
//            {
//                "username": "testuser",
//                "password": "testpass"
//            }
//        """; 
//
//        mockMvc.perform(post("/login")
//                .with(csrf())
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(json))
//                .andExpect(status().isOk())
//                .andExpect(content().string(TOKEN));
//
//        // Verificamos que el método generateToken fue llamado correctamente
//        verify(jwtTokenUtils).generateToken("testuser");
//
//        logger.info("Prueba de login completada con éxito");
//    }
//
//    @TestConfiguration
//    static class MockConfig {
//
//        @Bean
//        public AuthenticationManager authenticationManager() {
//            return authentication -> {
//                if ("testuser".equals(authentication.getName()) && "testpass".equals(authentication.getCredentials())) {
//                    return new UsernamePasswordAuthenticationToken("testuser", "testpass", List.of());
//                }
//                throw new RuntimeException("Invalid credentials");
//            };
//        }
//    }
//}
