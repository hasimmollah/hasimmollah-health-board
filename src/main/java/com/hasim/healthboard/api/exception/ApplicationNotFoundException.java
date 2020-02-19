package com.hasim.healthboard.api.exception;

public class ApplicationNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ApplicationNotFoundException() {
        super();
    }

    public ApplicationNotFoundException(String message) {
        super(message);
    }

    public ApplicationNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ApplicationNotFoundException(Throwable cause) {
        super(cause);
    }
}
