package com.gt.magicbox.http;

public class HttpResponseException extends RuntimeException {

    private  int error;

    public HttpResponseException(String message, int error) {
        super(message);
        this.error = error;
    }

    public int getError() {
        return error;
    }
}
