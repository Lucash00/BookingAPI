package com.bookingapi.repositories;

import com.bookingapi.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);  // Buscar por email
    boolean existsByEmail(String email);  // Verificar si el email ya existe
}
