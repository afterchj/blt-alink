package com.tpadsz.after.exception;

/**
 * Created by chenhao.lu on 2019/3/6.
 */
public class TokenNotEffectiveException extends ApplicationException{
    private static final long serialVersionUID = 1L;

    public TokenNotEffectiveException() {
    }

    public TokenNotEffectiveException(String message, Throwable cause) {
        super(message, cause);
    }

    public TokenNotEffectiveException(String message) {
        super(message);
    }

    public String getCode() {
        return "916";
    }
}
