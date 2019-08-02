package com.tpadsz.after.entity;

import java.util.List;

/**
 * @program: blt-alink
 * @description: 区域扩展
 * @author: Mr.Ma
 * @create: 2019-08-02 09:44
 **/
public class PlaceExtend extends Place {

    private Integer lcount;//区域灯数量
    private List<GroupList> groupList;

    public List<GroupList> getGroupList() {
        return groupList;
    }

    public void setGroupList(List<GroupList> groupList) {
        this.groupList = groupList;
    }

    public Integer getLcount() {
        return lcount;
    }

    public void setLcount(Integer lcount) {
        this.lcount = lcount;
    }

    @Override
    public String toString() {
        return "PlaceExtend{" +
                "id=" + getId() +
                ", pname='" + getPname() + '\'' +
                ", place_id=" + getPlace_id() +
                ", lcount=" + lcount +
                ", groupList=" + groupList +
                '}';
    }
}
