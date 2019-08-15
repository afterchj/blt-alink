package com.tpadsz.after.entity;

import java.util.List;

/**
 * @program: blt-alink
 * @description:
 * @author: Mr.Ma
 * @create: 2019-03-07 17:50
 **/
public class GroupExtend {

    private Integer groupId;
    private String gname;
    private List<LightExtend> lightLists;


    public List<LightExtend> getLightLists() {
        return lightLists;
    }

    public void setLightLists(List<LightExtend> lightLists) {
        this.lightLists = lightLists;
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

    @Override
    public String toString() {
        return "GroupList{" +
                ", groupId=" + groupId +
                ", gname='" + gname + '\'' +
                ", lightLists=" + lightLists +
                '}';
    }
}
