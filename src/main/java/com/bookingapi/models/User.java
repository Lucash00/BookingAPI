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
import java.util.ArrayList;

@Entity
@Table(name = "users")
@EntityListeners(AuditingEntityListener.class)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false) // Esta propiedad es obligatoria
    private String name;

    @Column(nullable = false, unique = true) // Email obligatorio y único
    private String email;

    @Column(nullable = false) // Teléfono obligatorio
    private String phone;

    @Column(nullable = false) // Contraseña obligatoria
    private String password;

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

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private List<Role> roles;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Booking> bookings;

    // Constructores
    public User() {
        this.roles = new ArrayList<>();
    }

    public User(String name, String email, String phone, String password, List<Booking> bookings, List<Role> roles) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
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

    // Validaciones
    public void validateUserInput(User user) {
        if (user.getName() == null || user.getName().trim().isEmpty()) {
            throw new ValidationException("User name is required.");
        }
        if (user.getEmail() == null || !user.getEmail().matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$")) {
            throw new ValidationException("Valid email is required.");
        }
        if (user.getPhone() == null || !user.getPhone().matches("^\\+?[0-9]*$")) {
            throw new ValidationException("Valid phone number is required.");
        }
        if (user.getPassword() == null || user.getPassword().length() < 8) { // Contraseña debe tener al menos 8 caracteres
            throw new ValidationException("Password must be at least 8 characters long.");
        }
    }
}
