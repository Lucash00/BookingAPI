package com.bookingapi.exceptions;

public class RoleNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;  // Agregar este campo

    public RoleNotFoundException(String message) {
        super(message);
    }
}
