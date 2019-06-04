package com.tpadsz.after.entity;

/**
 * @program: alink-hq
 * @description: 任务点
 * @author: Mr.Ma
 * @create: 2019-05-29 19:06
 **/
public class TimePoint {

    private Integer id;
    private Integer tsid;
    private Integer sid;
    private String time;
    private String state;
    private String create_date;
    private String update_date;
    private String sname;
    private Integer scene_id;

    public Integer getScene_id() {
        return scene_id;
    }

    public void setScene_id(Integer scene_id) {
        this.scene_id = scene_id;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTsid() {
        return tsid;
    }

    public void setTsid(Integer tsid) {
        this.tsid = tsid;
    }

    public Integer getSid() {
        return sid;
    }

    public void setSid(Integer sid) {
        this.sid = sid;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    public String getUpdate_date() {
        return update_date;
    }

    public void setUpdate_date(String update_date) {
        this.update_date = update_date;
    }

    @Override
    public String toString() {
        return "TimePoint{" +
                "id=" + id +
                ", tsid=" + tsid +
                ", sid=" + sid +
                ", time='" + time + '\'' +
                ", state='" + state + '\'' +
                ", create_date='" + create_date + '\'' +
                ", update_date='" + update_date + '\'' +
                ", sname='" + sname + '\'' +
                '}';
    }
}
