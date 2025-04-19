package com.bookingapi.controllers;

import com.bookingapi.models.Payment;
import com.bookingapi.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    // Crear un nuevo pago
    @PostMapping
    public Payment createPayment(@RequestParam Long bookingId, 
                                 @RequestParam BigDecimal amount, 
                                 @RequestParam String method, 
                                 @RequestParam String status) {
        return paymentService.createPayment(bookingId, amount, method, status);
    }

    // Obtener un pago por ID
    @GetMapping("/{id}")
    public Payment getPaymentById(@PathVariable Long id) {
        return paymentService.getPaymentById(id);
    }

    // Obtener el pago por ID de la reserva
    @GetMapping("/booking/{bookingId}")
    public Payment getPaymentByBookingId(@PathVariable Long bookingId) {
        return paymentService.getPaymentByBookingId(bookingId);
    }
}
