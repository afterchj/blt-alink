package com.tpadsz.after.entity;

import java.util.Date;
import java.util.List;

/**
 * @program: blt-alink
 * @description:
 * @author: Mr.Ma
 * @create: 2019-03-07 17:50
 **/
public class GroupList {

    private Integer id;
    private Integer groupId;
    private String gname;
    private Integer mid;
    private String meshId;
    private String x;
    private String y;
//    private int status;

//    public int getStatus() {
//        return status;
//    }

//    public void setStatus(int status) {
//        this.status = status;
//    }

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

    public String getMeshId() {
        return meshId;
    }

    public void setMeshId(String meshId) {
        this.meshId = meshId;
    }

    private Integer pid;
    private Date createDate;
    private Date updateDate;
    private List<LightList> lightLists;

    public List<LightList> getLightLists() {
        return lightLists;
    }

    public void setLightLists(List<LightList> lightLists) {
        this.lightLists = lightLists;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public String getGname() {
        return gname;
    }

    public void setGname(String gname) {
        this.gname = gname;
    }

    public Integer getMid() {
        return mid;
    }

    public void setMid(Integer mid) {
        this.mid = mid;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    @Override
    public String toString() {
        return "GroupList{" +
                "id=" + id +
                ", groupId=" + groupId +
                ", gname='" + gname + '\'' +
                ", mid=" + mid +
                ", meshId='" + meshId + '\'' +
                ", x='" + x + '\'' +
                ", y='" + y + '\'' +
                ", pid=" + pid +
                ", createDate=" + createDate +
                ", updateDate=" + updateDate +
                ", lightLists=" + lightLists +
                '}';
    }
}
