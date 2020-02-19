package com.hasim.healthboard.api.exception;

import com.hasim.healthboard.api.model.ErrorResponse;

public class MinValueLengthException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private ErrorResponse validationErrorResponse;
    

    private final Integer minLength;

    private final String value;

    public MinValueLengthException(Integer minLength, String value) {
        super();
        this.minLength = minLength;
        this.value = value;
    }

    

    public void setValidationErrorResponse(ErrorResponse validationErrorResponse) {
        this.validationErrorResponse = validationErrorResponse;

    }

    public ErrorResponse getValidationErrorResponse() {
        return validationErrorResponse;

    }



    public Integer getMinLength() {
        return minLength;
    }



    public String getValue() {
        return value;
    }
}
