package com.tpadsz.after.exception;

/**
 * Created by chenhao.lu on 2019/3/6.
 */
public class PasswordNotCorrectException extends ApplicationException{
    private static final long serialVersionUID = 1L;

    public PasswordNotCorrectException() {
    }

    public PasswordNotCorrectException(String message, Throwable cause) {
        super(message, cause);
    }

    public PasswordNotCorrectException(String message) {
        super(message);
    }

    public String getCode() {
        return "912";
    }
}
