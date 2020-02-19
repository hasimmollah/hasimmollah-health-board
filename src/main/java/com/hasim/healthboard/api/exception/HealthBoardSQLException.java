package com.hasim.healthboard.api.exception;

import com.hasim.healthboard.api.model.ErrorResponse;

public class HealthBoardSQLException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private ErrorResponse validationErrorResponse;

    public HealthBoardSQLException() {
        super();
    }

    public HealthBoardSQLException(String message) {
        super(message);
    }

    public HealthBoardSQLException(String message, Throwable cause) {
        super(message, cause);
    }

    public HealthBoardSQLException(Throwable cause) {
        super(cause);
    }

    public void setValidationErrorResponse(ErrorResponse validationErrorResponse) {
        this.validationErrorResponse = validationErrorResponse;

    }

    public ErrorResponse getValidationErrorResponse() {
        return validationErrorResponse;

    }
}
