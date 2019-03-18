package com.tpadsz.after.entity;

/**
 * @program: blt-alink
 * @description: f_console_log
 * @author: Mr.Ma
 * @create: 2019-03-13 10:19
 **/
public class GroupConsoleLog {

    private String x;
    private String y;
    private String lmac;

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y;
    }

    public String getLmac() {
        return lmac;
    }

    public void setLmac(String lmac) {
        this.lmac = lmac;
    }

    @Override
    public String toString() {
        return "GroupConsoleLog{" +
                "x='" + x + '\'' +
                ", y='" + y + '\'' +
                ", lmac='" + lmac + '\'' +
                '}';
    }
}
