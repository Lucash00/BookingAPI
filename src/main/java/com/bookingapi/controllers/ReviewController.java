package com.bookingapi.controllers;

import com.bookingapi.models.Review;
import com.bookingapi.services.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    // Crear una nueva reseña
    @PostMapping
    public Review createReview(@RequestParam Long userId, 
                               @RequestParam Long bookingId, 
                               @RequestParam Long roomId, 
                               @RequestParam Integer rating, 
                               @RequestParam String comment) {
        return reviewService.createReview(userId, bookingId, roomId, rating, comment);
    }

    // Obtener una reseña por ID
    @GetMapping("/{id}")
    public Review getReviewById(@PathVariable Long id) {
        return reviewService.getReviewById(id);
    }

    // Obtener todas las reseñas de un usuario
    @GetMapping("/user/{userId}")
    public List<Review> getReviewsByUser(@PathVariable Long userId) {
        return reviewService.getReviewsByUser(userId);
    }

    // Obtener todas las reseñas de una habitación
    @GetMapping("/room/{roomId}")
    public List<Review> getReviewsByRoom(@PathVariable Long roomId) {
        return reviewService.getReviewsByRoom(roomId);
    }

    // Eliminar una reseña por ID
    @DeleteMapping("/{id}")
    public void deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
    }
}
