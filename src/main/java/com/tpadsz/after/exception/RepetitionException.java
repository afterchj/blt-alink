package com.tpadsz.after.exception;

/**
 * Created by hongjian.chen on 2018/12/13.
 */
public class RepetitionException extends Exception {

    private int code;

    public RepetitionException() {
    }

    public RepetitionException(int code, String message) {
        super(message);
        this.code = code;
    }
    public int getCode() {
        return code;
    }
}
