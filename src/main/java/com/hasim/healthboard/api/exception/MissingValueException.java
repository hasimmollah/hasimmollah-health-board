package com.hasim.healthboard.api.exception;

import com.hasim.healthboard.api.model.ErrorResponse;

public class MissingValueException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private ErrorResponse validationErrorResponse;

    public MissingValueException() {
        super();
    }

    public MissingValueException(String message) {
        super(message);
    }

    public MissingValueException(String message, Throwable cause) {
        super(message, cause);
    }

    public MissingValueException(Throwable cause) {
        super(cause);
    }

    public void setValidationErrorResponse(ErrorResponse validationErrorResponse) {
        this.validationErrorResponse = validationErrorResponse;

    }

    public ErrorResponse getValidationErrorResponse() {
        return validationErrorResponse;

    }
}
