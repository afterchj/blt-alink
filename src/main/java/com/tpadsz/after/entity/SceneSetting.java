package com.tpadsz.after.entity;

/**
 * @program: blt-alink
 * @description: secen_setting  table
 * @author: Mr.Ma
 * @create: 2019-03-13 16:03
 **/
public class SceneSetting {

    private Integer id;
    private Integer sid;
    private String x;
    private String y;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSid() {
        return sid;
    }

    public void setSid(Integer sid) {
        this.sid = sid;
    }

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
}
