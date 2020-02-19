package com.hasim.healthboard.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
@JsonTypeName("Error")                                                                                         
@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT ,use = JsonTypeInfo.Id.NAME)
public class ErrorResponse {
    @JsonProperty("Message")
    private String message;
    @JsonProperty("Code")
    private String code;

    public ErrorResponse() {
        super();
    }

    public ErrorResponse(String code, String message) {
        super();
        this.code = code;
        this.message = message;
    }

    @Override
    public String toString() {
        return "ErrorResponse Code:" + code + " ErrorResponse Message: " + message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}