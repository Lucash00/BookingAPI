package com.bookingapi.services;

import com.bookingapi.exceptions.ResourceNotFoundException;
import com.bookingapi.models.Payment;
import com.bookingapi.models.Booking;
import com.bookingapi.repositories.PaymentRepository;
import com.bookingapi.repositories.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private BookingRepository bookingRepository;

    // Crear un nuevo pago
    public Payment createPayment(Long bookingId, BigDecimal amount, String method, String status) {
        // Verificar que la reserva exista
        Optional<Booking> bookingOpt = bookingRepository.findById(bookingId);
        if (!bookingOpt.isPresent()) {
            throw new ResourceNotFoundException("Booking not found");
        }
        
        Booking booking = bookingOpt.get();
        
        // Crear el pago
        Payment payment = new Payment(amount, method, status, booking);
        
        return paymentRepository.save(payment); // Guardar el pago
    }

    // Obtener un pago por ID
    public Payment getPaymentById(Long paymentId) {
        return paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found"));
    }

    // Obtener el pago asociado a una reserva
    public Payment getPaymentByBookingId(Long bookingId) {
        return paymentRepository.findByBookingId(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment for the booking not found"));
    }
}
