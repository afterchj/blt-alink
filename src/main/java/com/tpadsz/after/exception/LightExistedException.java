package com.tpadsz.after.exception;

/**
 * Created by chenhao.lu on 2019/3/6.
 */
public class LightExistedException extends ApplicationException{
    private static final long serialVersionUID = 1L;

    public LightExistedException() {
    }

    public LightExistedException(String message, Throwable cause) {
        super(message, cause);
    }

    public LightExistedException(String message) {
        super(message);
    }

    public String getCode() {
        return "915";
    }
}
