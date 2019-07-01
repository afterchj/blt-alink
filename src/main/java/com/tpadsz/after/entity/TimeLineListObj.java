package com.tpadsz.after.entity;

import java.io.Serializable;

/**
 * Created by jiankai.wang on 2019/5/6.
 * 定时列表集合
 */
public class TimeLineListObj implements Serializable {
    private int Id;
    private int item_tag;  //0-未选中  1-选中  2-隐藏
    private String item_title;
    private String item_desc;
    private String dayObj;
    private int item_set;  //0-未应用  1-应用
    private boolean ischoose;  //item项是否被选中
    private int tid;

    public int getTid() {
        return tid;
    }

    public void setTid(int tid) {
        this.tid = tid;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public TimeLineListObj() {
    }

    public int getItem_tag() {
        return item_tag;
    }

    public void setItem_tag(int item_tag) {
        this.item_tag = item_tag;
    }

    public String getItem_title() {
        return item_title;
    }

    public void setItem_title(String item_title) {
        this.item_title = item_title;
    }

    public String getItem_desc() {
        return item_desc;
    }

    public void setItem_desc(String item_desc) {
        this.item_desc = item_desc;
    }

    public int getItem_set() {
        return item_set;
    }

    public void setItem_set(int item_set) {
        this.item_set = item_set;
    }

    public boolean ischoose() {
        return ischoose;
    }

    public void setIschoose(boolean ischoose) {
        this.ischoose = ischoose;
    }

    public String getDayObj() {
        return dayObj;
    }

    public void setDayObj(String dayObj) {
        this.dayObj = dayObj;
    }
}
