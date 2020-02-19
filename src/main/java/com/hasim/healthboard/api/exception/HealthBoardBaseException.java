package com.hasim.healthboard.api.exception;

import com.hasim.healthboard.api.model.ErrorResponse;

public class HealthBoardBaseException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private ErrorResponse validationErrorResponse;

    public HealthBoardBaseException() {
        super();
    }

    public HealthBoardBaseException(String message) {
        super(message);
    }

    public HealthBoardBaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public HealthBoardBaseException(Throwable cause) {
        super(cause);
    }

    public void setValidationErrorResponse(ErrorResponse validationErrorResponse) {
        this.validationErrorResponse = validationErrorResponse;

    }

    public ErrorResponse getValidationErrorResponse() {
        return validationErrorResponse;

    }
}
