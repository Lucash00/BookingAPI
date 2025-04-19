package com.bookingapi.models;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import com.bookingapi.exceptions.ValidationException;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "rooms")
@EntityListeners(AuditingEntityListener.class)
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String location;
    private int capacity;
    private boolean available;

    private String type; // Tipo de habitación (Ej: Estándar, Deluxe, Suite)
    private BigDecimal pricePerNight; // Precio por noche
    private String description; // Descripción de la habitación
    private String imageUrl; // URL de la imagen

    @Enumerated(EnumType.STRING)
    private MaintenanceStatus maintenanceStatus; // Estado de mantenimiento (e.g. EN_MANTENIMIENTO, DISPONIBLE)

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @CreatedBy
    @JsonIgnore
    @Column(updatable = false)
    private String createdBy;

    @LastModifiedBy
    private String updatedBy;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Booking> bookings;

    // Constructores
    public Room() {}

    public Room(String name, String location, int capacity, boolean available, String type, 
                BigDecimal pricePerNight, String description, String imageUrl, 
                MaintenanceStatus maintenanceStatus, List<Booking> bookings) {
        this.name = name;
        this.location = location;
        this.capacity = capacity;
        this.available = available;
        this.type = type;
        this.pricePerNight = pricePerNight;
        this.description = description;
        this.imageUrl = imageUrl;
        this.maintenanceStatus = maintenanceStatus;
        this.bookings = bookings;
    }

    // Getters y setters
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getPricePerNight() {
        return pricePerNight;
    }

    public void setPricePerNight(BigDecimal pricePerNight) {
        this.pricePerNight = pricePerNight;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public MaintenanceStatus getMaintenanceStatus() {
        return maintenanceStatus;
    }

    public void setMaintenanceStatus(MaintenanceStatus maintenanceStatus) {
        this.maintenanceStatus = maintenanceStatus;
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    // Validación de entrada
    public void validateRoomInput(Room room) {
        if (room.getName() == null || room.getName().trim().isEmpty()) {
            throw new ValidationException("Room name is required.");
        }
        if (room.getLocation() == null || room.getLocation().trim().isEmpty()) {
            throw new ValidationException("Room location is required.");
        }
        if (room.getCapacity() <= 0) {
            throw new ValidationException("Room capacity must be greater than 0.");
        }
        if (room.getPricePerNight() == null || room.getPricePerNight().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("Room price per night must be greater than 0.");
        }
        if (room.getType() == null || room.getType().trim().isEmpty()) {
            throw new ValidationException("Room type is required.");
        }
    }
}
