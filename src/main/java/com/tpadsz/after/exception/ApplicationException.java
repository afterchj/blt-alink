package com.tpadsz.after.exception;

import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * Created by chenhao.lu on 2019/3/6.
 */
public abstract class ApplicationException extends Exception {
    private static final long serialVersionUID = 1L;

    public ApplicationException() {
        super("Error occurred in service.");
    }

    public ApplicationException(String message) {
        super(message);
    }

    public ApplicationException(String message, Throwable cause) {
        super(message, cause);
    }

    public String getMessage() {
        String msg = super.getMessage();

        Throwable child;
        for(Object parent = this; (child = this.getNestedException((Throwable)parent)) != null; parent = child) {
            String msg2 = child.getMessage();
            if(msg2 != null) {
                if(msg != null) {
                    msg = msg + ": " + msg2;
                } else {
                    msg = msg2;
                }
            }

            if(child instanceof ApplicationException) {
                break;
            }
        }

        return msg;
    }

    private Throwable getNestedException(Throwable parent) {
        return parent.getCause();
    }

    public void printStackTrace() {
        super.printStackTrace();
        Object parent = this;

        Throwable child;
        while((child = this.getNestedException((Throwable)parent)) != null) {
            if(child != null) {
                System.err.print("Caused by: ");
                child.printStackTrace();
                if(child instanceof ApplicationException) {
                    break;
                }

                parent = child;
            }
        }

    }

    public void printStackTrace(PrintStream s) {
        super.printStackTrace(s);
        Object parent = this;

        Throwable child;
        while((child = this.getNestedException((Throwable)parent)) != null) {
            if(child != null) {
                s.print("Caused by: ");
                child.printStackTrace(s);
                if(child instanceof ApplicationException) {
                    break;
                }

                parent = child;
            }
        }

    }

    public void printStackTrace(PrintWriter w) {
        super.printStackTrace(w);
        Object parent = this;

        Throwable child;
        while((child = this.getNestedException((Throwable)parent)) != null) {
            if(child != null) {
                w.print("Caused by: ");
                child.printStackTrace(w);
                if(child instanceof ApplicationException) {
                    break;
                }

                parent = child;
            }
        }

    }

    public abstract String getCode();
}
