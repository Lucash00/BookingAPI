package com.bookingapi.repositories;

import com.bookingapi.models.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    // Obtener todas las reseñas de un usuario
    List<Review> findByUserId(Long userId);

    // Obtener todas las reseñas de una habitación
    List<Review> findByRoomId(Long roomId);
}
