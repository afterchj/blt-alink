package com.tpadsz.after.exception;

/**
 * Created by chenhao.lu on 2019/3/6.
 */
public class AccountDisabledException extends ApplicationException{
    private static final long serialVersionUID = 1L;

    public AccountDisabledException() {
    }

    public AccountDisabledException(String message, Throwable cause) {
        super(message, cause);
    }

    public AccountDisabledException(String message) {
        super(message);
    }

    public String getCode() {
        return "917";
    }
}
