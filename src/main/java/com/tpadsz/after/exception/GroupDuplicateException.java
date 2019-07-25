package com.tpadsz.after.exception;

/**
 * 未发现区域
 */
public class GroupDuplicateException extends Exception {

    public GroupDuplicateException() {
    }

    public GroupDuplicateException(String message) {
        super(message);
    }
}
