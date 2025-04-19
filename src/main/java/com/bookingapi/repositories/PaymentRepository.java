package com.bookingapi.repositories;

import com.bookingapi.models.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    // Obtener un pago por su ID
    Optional<Payment> findById(Long paymentId);
    
    // Obtener un pago por el ID de la reserva
    Optional<Payment> findByBookingId(Long bookingId);
}
