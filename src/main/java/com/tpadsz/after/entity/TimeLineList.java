package com.tpadsz.after.entity;

/**
 * @program: blt-alink
 * @description:
 * @author: Mr.Ma
 * @create: 2019-06-03 17:28
 **/
public class TimeLineList {

//    private String tname;
//    private Integer tid;
//    private String state;
//    private String repetition;
//    private String week;
//    private Date create_date;
//    private Date update_date;
//    private List<TimePointList> timePointLists;
    private Integer tid;
    private Integer id;
    private String item_title;
    private String dayObj;
    private Integer ischoose;
    private String item_desc;
    private Integer item_set;
    private Integer item_tag;

    public Integer getTid() {
        return tid;
    }

    public void setTid(Integer tid) {
        this.tid = tid;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getItem_title() {
        return item_title;
    }

    public void setItem_title(String item_title) {
        this.item_title = item_title;
    }

    public String getDayObj() {
        return dayObj;
    }

    public void setDayObj(String dayObj) {
        this.dayObj = dayObj;
    }

    public Integer getIschoose() {
        return ischoose;
    }

    public void setIschoose(Integer ischoose) {
        this.ischoose = ischoose;
    }

    public String getItem_desc() {
        return item_desc;
    }

    public void setItem_desc(String item_desc) {
        this.item_desc = item_desc;
    }

    public Integer getItem_set() {
        return item_set;
    }

    public void setItem_set(Integer item_set) {
        this.item_set = item_set;
    }

    public Integer getItem_tag() {
        return item_tag;
    }

    public void setItem_tag(Integer item_tag) {
        this.item_tag = item_tag;
    }
    //    public Date getCreate_date() {
//        return create_date;
//    }
//
//    public void setCreate_date(Date create_date) {
//        this.create_date = create_date;
//    }
//
//    public Date getUpdate_date() {
//        return update_date;
//    }
//
//    public void setUpdate_date(Date update_date) {
//        this.update_date = update_date;
//    }

//    public List<TimePointList> getTimePointLists() {
//        return timePointLists;
//    }

//    public void setTimePointLists(List<TimePointList> timePointLists) {
//        this.timePointLists = timePointLists;
//    }

}
