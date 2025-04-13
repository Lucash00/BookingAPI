package com.bookingapi.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.bookingapi.security.JwtTokenUtils;

@RestController
public class AuthController {

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest loginRequest) {
        // Aquí validas el usuario y la contraseña (por simplicidad se asume que son correctos)
        // Normalmente se validaría contra la base de datos
        return JwtTokenUtils.generateToken(loginRequest.getUsername());
    }
}

// Clase auxiliar para recibir la solicitud de login
class LoginRequest {
    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

