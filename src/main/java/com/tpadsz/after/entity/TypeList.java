package com.tpadsz.after.entity;

/**
 * @program: blt-alink
 * @description:
 * @author: Mr.Ma
 * @create: 2019-03-07 17:58
 **/
public class TypeList {

    private String tname;
    private String tnum;

    public String getTname() {
        return tname;
    }

    public void setTname(String tname) {
        this.tname = tname;
    }

    public String getTnum() {
        return tnum;
    }

    public void setTnum(String tnum) {
        this.tnum = tnum;
    }

    @Override
    public String toString() {
        return "TypeList{" +
                "tname='" + tname + '\'' +
                ", tnum='" + tnum + '\'' +
                '}';
    }
}
