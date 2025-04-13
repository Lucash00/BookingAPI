package com.bookingapi.models;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.bookingapi.exceptions.ValidationException;
import com.fasterxml.jackson.annotation.JsonIgnore;

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

    public Room(String name, String location, int capacity, boolean available, List<Booking> bookings) {
        this.name = name;
        this.location = location;
        this.capacity = capacity;
        this.available = available;
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
    }

}
