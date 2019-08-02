package com.tpadsz.after.entity;

/**
 * @program: blt-alink
 * @description:
 * @author: Mr.Ma
 * @create: 2019-08-02 10:20
 **/
public class GroupBasic {

    private Integer group_id;
    private String gname;

    public Integer getGroup_id() {
        return group_id;
    }

    public void setGroup_id(Integer group_id) {
        this.group_id = group_id;
    }

    public String getGname() {
        return gname;
    }

    public void setGname(String gname) {
        this.gname = gname;
    }

    @Override
    public String toString() {
        return "GroupBasic{" +
                "group_id=" + group_id +
                ", gname='" + gname + '\'' +
                '}';
    }
}
