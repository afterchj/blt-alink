package com.tpadsz.after.exception;

/**
 * 未发现区域
 */
public class PlaceNotFoundException extends Exception {

    public PlaceNotFoundException() {
    }

    public PlaceNotFoundException(String message) {
        super(message);
    }
}
