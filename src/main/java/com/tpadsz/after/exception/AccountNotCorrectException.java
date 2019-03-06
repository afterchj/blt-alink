package com.tpadsz.after.exception;

/**
 * Created by chenhao.lu on 2019/3/6.
 */
public class AccountNotCorrectException extends ApplicationException{
    private static final long serialVersionUID = 1L;

    public AccountNotCorrectException() {
    }

    public AccountNotCorrectException(String message, Throwable cause) {
        super(message, cause);
    }

    public AccountNotCorrectException(String message) {
        super(message);
    }

    public String getCode() {
        return "911";
    }
}
