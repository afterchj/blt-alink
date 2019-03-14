package com.tpadsz.after.exception;

/**
 * Created by chenhao.lu on 2019/3/6.
 */
public class MobileNotExistedException extends ApplicationException{
    private static final long serialVersionUID = 1L;

    public MobileNotExistedException() {
    }

    public MobileNotExistedException(String message, Throwable cause) {
        super(message, cause);
    }

    public MobileNotExistedException(String message) {
        super(message);
    }

    public String getCode() {
        return "914";
    }
}
