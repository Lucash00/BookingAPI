package com.bookingapi.repositories;

import com.bookingapi.models.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    // Puedes agregar consultas personalizadas más adelante si lo necesitas
}
