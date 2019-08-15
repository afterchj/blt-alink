package com.tpadsz.after.entity;

import java.util.List;

/**
 * @program: blt-alink
 * @description: 区域扩展
 * @author: Mr.Ma
 * @create: 2019-08-02 09:44
 **/
public class PlaceExtend extends Place {
    private List<GroupExtend> groupList;

    public List<GroupExtend> getGroupList() {
        return groupList;
    }

    public void setGroupList(List<GroupExtend> groupList) {
        this.groupList = groupList;
    }
}
