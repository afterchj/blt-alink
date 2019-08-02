package com.tpadsz.after.entity;

import java.util.List;

/**
 * @program: blt-alink
 * @description: 区域扩展
 * @author: Mr.Ma
 * @create: 2019-08-02 09:44
 **/
public class PlaceExtend extends Place {

    private List<GroupList> groupList;

    public List<GroupList> getGroupList() {
        return groupList;
    }

    public void setGroupList(List<GroupList> groupList) {
        this.groupList = groupList;
    }

    @Override
    public String toString() {
        return "PlaceExtend{" +
                "id=" + getId() +
                ", pname='" + getPname() + '\'' +
                ", place_id=" + getPlace_id() +
                ", groupList=" + groupList +
                '}';
    }
}
