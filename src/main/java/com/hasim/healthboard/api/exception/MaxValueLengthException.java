package com.hasim.healthboard.api.exception;

import com.hasim.healthboard.api.model.ErrorResponse;

public class MaxValueLengthException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private ErrorResponse validationErrorResponse;


    private final Integer maxLength;

    private final String value;

    public MaxValueLengthException(Integer maxLength, String value) {
        super();
        this.maxLength = maxLength;
        this.value = value;
    }


    public void setValidationErrorResponse(ErrorResponse validationErrorResponse) {
        this.validationErrorResponse = validationErrorResponse;

    }

    public ErrorResponse getValidationErrorResponse() {
        return validationErrorResponse;

    }


    public Integer getMaxLength() {
        return maxLength;
    }


    public String getValue() {
        return value;
    }
}
