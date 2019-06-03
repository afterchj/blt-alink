package com.tpadsz.after.exception;

/**
 * Created by chenhao.lu on 2019/3/6.
 */
public class AdminNotAllowedException extends Exception{

    public AdminNotAllowedException() {
    }


    public AdminNotAllowedException(String message) {
        super(message);
    }

}
