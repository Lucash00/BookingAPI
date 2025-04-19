package com.bookingapi.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.bookingapi.exceptions.ValidationException;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "bookings")
@EntityListeners(AuditingEntityListener.class)
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String customerName;

    @NotBlank
    private String service;

    @NotNull
    @FutureOrPresent
    private LocalDate bookingDate;

    private boolean confirmed;

    private LocalDateTime startDateTime;

    private LocalDateTime endDateTime;

    private Integer durationInMinutes;

    private LocalDateTime checkInTime;

    private LocalDateTime checkOutTime;

    private String discountCode;

    @NotNull
    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    private Boolean cancelled;

    private String cancellationReason;

    private Boolean rescheduled;

    private Long rescheduledFromBookingId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull
    private User user;

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    @NotNull
    private Room room;

    @ManyToMany
    @JoinTable(
        name = "booking_addons",
        joinColumns = @JoinColumn(name = "booking_id"),
        inverseJoinColumns = @JoinColumn(name = "addon_id")
    )
    private Set<Addon> addons = new HashSet<>();

    @OneToOne(mappedBy = "booking", cascade = CascadeType.ALL)
    private Payment payment;

    @OneToOne(mappedBy = "booking", cascade = CascadeType.ALL)
    private Review review;

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

    public Booking() {}

    public Booking(String customerName, String service, LocalDate bookingDate, boolean confirmed, User user, Room room) {
        this.customerName = customerName;
        this.service = service;
        this.bookingDate = bookingDate;
        this.confirmed = confirmed;
        this.user = user;
        this.room = room;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getService() { return service; }
    public void setService(String service) { this.service = service; }

    public LocalDate getBookingDate() { return bookingDate; }
    public void setBookingDate(LocalDate bookingDate) { this.bookingDate = bookingDate; }

    public boolean isConfirmed() { return confirmed; }
    public void setConfirmed(boolean confirmed) { this.confirmed = confirmed; }

    public LocalDateTime getStartDateTime() { return startDateTime; }
    public void setStartDateTime(LocalDateTime startDateTime) { this.startDateTime = startDateTime; }

    public LocalDateTime getEndDateTime() { return endDateTime; }
    public void setEndDateTime(LocalDateTime endDateTime) { this.endDateTime = endDateTime; }

    public Integer getDurationInMinutes() { return durationInMinutes; }
    public void setDurationInMinutes(Integer durationInMinutes) { this.durationInMinutes = durationInMinutes; }

    public LocalDateTime getCheckInTime() { return checkInTime; }
    public void setCheckInTime(LocalDateTime checkInTime) { this.checkInTime = checkInTime; }

    public LocalDateTime getCheckOutTime() { return checkOutTime; }
    public void setCheckOutTime(LocalDateTime checkOutTime) { this.checkOutTime = checkOutTime; }

    public String getDiscountCode() { return discountCode; }
    public void setDiscountCode(String discountCode) { this.discountCode = discountCode; }

    public BookingStatus getStatus() { return status; }
    public void setStatus(BookingStatus status) { this.status = status; }

    public Boolean getCancelled() { return cancelled; }
    public void setCancelled(Boolean cancelled) { this.cancelled = cancelled; }

    public String getCancellationReason() { return cancellationReason; }
    public void setCancellationReason(String cancellationReason) { this.cancellationReason = cancellationReason; }

    public Boolean getRescheduled() { return rescheduled; }
    public void setRescheduled(Boolean rescheduled) { this.rescheduled = rescheduled; }

    public Long getRescheduledFromBookingId() { return rescheduledFromBookingId; }
    public void setRescheduledFromBookingId(Long rescheduledFromBookingId) { this.rescheduledFromBookingId = rescheduledFromBookingId; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Room getRoom() { return room; }
    public void setRoom(Room room) { this.room = room; }

    public Set<Addon> getAddons() { return addons; }
    public void setAddons(Set<Addon> addons) { this.addons = addons; }

    public Payment getPayment() { return payment; }
    public void setPayment(Payment payment) { this.payment = payment; }

    public Review getReview() { return review; }
    public void setReview(Review review) { this.review = review; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public String getCreatedBy() { return createdBy; }
    public String getUpdatedBy() { return updatedBy; }

    // Validación de entrada
    public void validateBookingInput(Booking booking) {
        if (booking.getCustomerName() == null || booking.getCustomerName().trim().isEmpty()) {
            throw new ValidationException("Customer name is required.");
        }
        if (booking.getService() == null || booking.getService().trim().isEmpty()) {
            throw new ValidationException("Service is required.");
        }
        if (booking.getBookingDate() == null || booking.getBookingDate().isBefore(LocalDate.now())) {
            throw new ValidationException("Booking date cannot be in the past.");
        }
        if (booking.getUser() == null) {
            throw new ValidationException("User is required.");
        }
        if (booking.getRoom() == null) {
            throw new ValidationException("Room is required.");
        }
        if (booking.getStatus() == null) {
            throw new ValidationException("Status is required.");
        }
        if (booking.getDurationInMinutes() != null && booking.getDurationInMinutes() <= 0) {
            throw new ValidationException("Duration must be greater than 0.");
        }
        if (booking.isConfirmed() && (booking.getCheckInTime() == null || booking.getCheckOutTime() == null)) {
            throw new ValidationException("Check-in and check-out times must be provided for confirmed bookings.");
        }
    }

}
