package com.tpadsz.after.exception;

/**
 * Created by chenhao.lu on 2019/3/18.
 */
public class TokenReplacedException extends TokenNotEffectiveException{
    private static final long serialVersionUID = 1L;

    public TokenReplacedException() {
    }

    public TokenReplacedException(String message, Throwable cause) {
        super(message, cause);
    }

    public TokenReplacedException(String message) {
        super(message);
    }



}
