package com.fw.utils;

public class CustomException extends Exception {

    public CustomException(String message) {
        super(message);
    }

    public RuntimeException CustomRuntimeException(String message) {
        throw new RuntimeException(message);
    }
}
