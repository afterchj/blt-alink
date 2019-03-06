package com.tpadsz.after.exception;

/**
 * Created by chenhao.lu on 2019/3/6.
 */
public class SystemAlgorithmException extends ApplicationException {
    private static final long serialVersionUID = 1L;

    public SystemAlgorithmException() {
    }

    public SystemAlgorithmException(String message, Throwable cause) {
        super(message, cause);
    }

    public SystemAlgorithmException(String message) {
        super(message);
    }

    public String getCode() {
        return "913";
    }
}
