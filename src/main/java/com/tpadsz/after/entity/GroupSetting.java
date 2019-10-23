package com.tpadsz.after.entity;

import org.apache.commons.lang.StringUtils;

/**
 * @program: blt-alink
 * @description: group_setting table
 * @author: Mr.Ma
 * @create: 2019-03-13 16:23
 **/
public class GroupSetting {

    private Integer id;
    private Integer mid;
    private Integer gid;
    private Integer sid;
    private String x;
    private String y;
    private Integer groupId;
    private String horizontalAngle;//水平角度 --轨道灯
    private String verticalAngle;//垂直角度 --轨道灯
    private String focus;//焦距 --轨道灯

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMid() {
        return mid;
    }

    public void setMid(Integer mid) {
        this.mid = mid;
    }

    public Integer getGid() {
        return gid;
    }

    public void setGid(Integer gid) {
        this.gid = gid;
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
        if (StringUtils.isNotBlank(x)){
            this.x = x;
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

    public String getY() {
        return y;
    }

    public void setY(String y) {
        if (StringUtils.isNotBlank(y)){
            this.y = y;
        }
    }
}
