package com.tpadsz.after.entity;

import org.apache.commons.lang.StringUtils;

/**
 * @program: blt-alink
 * @description: light_setting
 * @author: Mr.Ma
 * @create: 2019-03-13 13:41
 **/
public class LightSetting {

    private Integer id;
    private Integer sid;
    private Integer lid;
    private String x;
    private String y;
    private String off;
    private String horizontalAngle;//水平角度 --轨道灯
    private String verticalAngle;//垂直角度 --轨道灯
    private String focus;//焦距 --轨道灯

    public String getOff() {
        return off;
    }

    public void setOff(String off) {
        this.off = off;
    }

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

    public Integer getLid() {
        return lid;
    }

    public void setLid(Integer lid) {
        this.lid = lid;
    }

    public String getX() {
        return x;
    }

    public void setX(String x) {
        if (StringUtils.isNotBlank(x)){
            this.x = x;
        }
    }

    public String getY() {
        return y;
    }

    public void setY(String y) {
        if (StringUtils.isNotBlank(y)){
            this.y = y;
        }
    }

    public String getHorizontalAngle() {
        return horizontalAngle;
    }

    public void setHorizontalAngle(String horizontalAngle) {
        if (StringUtils.isNotBlank(horizontalAngle)){
            this.horizontalAngle = horizontalAngle;
        }
    }

    public String getVerticalAngle() {
        return verticalAngle;
    }

    public void setVerticalAngle(String verticalAngle) {
        if (StringUtils.isNotBlank(verticalAngle)){
            this.verticalAngle = verticalAngle;
        }
    }

    public String getFocus() {
        return focus;
    }

    public void setFocus(String focus) {
        if (StringUtils.isNotBlank(focus)){
            this.focus = focus;
        }
    }
}
