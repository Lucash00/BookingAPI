package com.bookingapi.services;

import com.bookingapi.exceptions.ResourceNotFoundException;
import com.bookingapi.exceptions.ValidationException;
import com.bookingapi.models.Booking;
import com.bookingapi.models.Review;
import com.bookingapi.models.Room;
import com.bookingapi.models.User;
import com.bookingapi.repositories.BookingRepository;
import com.bookingapi.repositories.ReviewRepository;
import com.bookingapi.repositories.RoomRepository;
import com.bookingapi.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoomRepository roomRepository;

    // Crear una nueva reseña
    public Review createReview(Long userId, Long bookingId, Long roomId, Integer rating, String comment) {
        Optional<User> userOpt = userRepository.findById(userId);
        Optional<Booking> bookingOpt = bookingRepository.findById(bookingId);
        Optional<Room> roomOpt = roomRepository.findById(roomId);

        if (!userOpt.isPresent()) {
            throw new ResourceNotFoundException("User not found");
        }
        if (!bookingOpt.isPresent()) {
            throw new ResourceNotFoundException("Booking not found");
        }
        if (!roomOpt.isPresent()) {
            throw new ResourceNotFoundException("Room not found");
        }

        User user = userOpt.get();
        Booking booking = bookingOpt.get();
        Room room = roomOpt.get();

        if (rating < 1 || rating > 5) {
            throw new ValidationException("Rating must be between 1 and 5");
        }

        Review review = new Review(rating, comment, user, booking, room);
        return reviewRepository.save(review);
    }

    // Obtener reseña por ID
    public Review getReviewById(Long reviewId) {
        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found"));
    }

    // Obtener reseñas de un usuario
    public List<Review> getReviewsByUser(Long userId) {
        return reviewRepository.findByUserId(userId); // Asumir que tienes un método para obtener reseñas por usuario
    }

    // Obtener reseñas de un hotel
    public List<Review> getReviewsByRoom(Long roomId) {
        return reviewRepository.findByRoomId(roomId); // Asumir que tienes un método para obtener reseñas por habitación
    }

    // Eliminar una reseña
    public void deleteReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found"));

        reviewRepository.delete(review);
    }
}
