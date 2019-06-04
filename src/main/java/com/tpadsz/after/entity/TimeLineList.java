package com.tpadsz.after.entity;

import java.util.List;

/**
 * @program: blt-alink
 * @description:
 * @author: Mr.Ma
 * @create: 2019-06-03 17:28
 **/
public class TimeLineList {

    private String tname;
    private Integer tid;
    private String state;
    private String repetition;
    private String week;
//    private Date create_date;
//    private Date update_date;
    private List<TimePointList> timePointLists;

    public String getTname() {
        return tname;
    }

    public void setTname(String tname) {
        this.tname = tname;
    }

    public Integer getTid() {
        return tid;
    }

    public void setTid(Integer tid) {
        this.tid = tid;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getRepetition() {
        return repetition;
    }

    public void setRepetition(String repetition) {
        this.repetition = repetition;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
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

    public List<TimePointList> getTimePointLists() {
        return timePointLists;
    }

    public void setTimePointLists(List<TimePointList> timePointLists) {
        this.timePointLists = timePointLists;
    }

    @Override
    public String toString() {
        return "TimeLineList{" +
                "tname='" + tname + '\'' +
                ", tid=" + tid +
                ", state='" + state + '\'' +
                ", repetition='" + repetition + '\'' +
                ", week='" + week + '\'' +
//                ", create_date=" + create_date +
//                ", update_date=" + update_date +
                ", timePointLists=" + timePointLists +
                '}';
    }
}
