package com.tpadsz.after.exception;

/**
 * 未发现区域
 */
public class NameDuplicateException extends Exception {

    public NameDuplicateException() {
    }

    public NameDuplicateException(String message) {
        super(message);
    }
}
