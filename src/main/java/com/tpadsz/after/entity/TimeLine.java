package com.tpadsz.after.entity;

/**
 * @program: alink-hq
 * @description: 定时线
 * @author: Mr.Ma
 * @create: 2019-05-28 11:40
 **/
public class TimeLine {

    private int id;
    private String tname;
    private String mname;
    private Integer mid;
    private String mesh_id;
    private String state;
    private String repetition;
    private String week;
    private String create_date;
    private String update_date;
    private Integer tid;

    public String getMesh_id() {
        return mesh_id;
    }

    public void setMesh_id(String mesh_id) {
        this.mesh_id = mesh_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getTid() {
        return tid;
    }

    public void setTid(Integer tid) {
        this.tid = tid;
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

    public String getTname() {
        return tname;
    }

    public void setTname(String tname) {
        this.tname = tname;
    }

    public String getMname() {
        return mname;
    }

    public void setMname(String mname) {
        this.mname = mname;
    }

    public Integer getMid() {
        return mid;
    }

    public void setMid(Integer mid) {
        this.mid = mid;
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

    @Override
    public String toString() {
        return "TimeLine{" +
                "id=" + id +
                ", tname='" + tname + '\'' +
                ", mname='" + mname + '\'' +
                ", mid=" + mid +
                ", state='" + state + '\'' +
                ", repetition='" + repetition + '\'' +
                ", week='" + week + '\'' +
                ", create_date='" + create_date + '\'' +
                ", update_date='" + update_date + '\'' +
                ", tid=" + tid +
                '}';
    }
}
