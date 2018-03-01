package com.ge.digital.model;

public class DefaultResponse {
    private String message;

    public DefaultResponse(String message) {
        this.message = message;
    }

    public DefaultResponse() {
        this.message = "ok";
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
