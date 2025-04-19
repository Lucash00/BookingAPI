package com.bookingapi.repositories;

import com.bookingapi.models.Addon;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AddonRepository extends JpaRepository<Addon, Long> {
    Optional<Addon> findByName(String name);
}
