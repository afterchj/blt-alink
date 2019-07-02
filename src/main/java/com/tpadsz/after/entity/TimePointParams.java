package com.tpadsz.after.entity;

/**
 * @program: alink-hq
 * @description: 任务点
 * @author: Mr.Ma
 * @create: 2019-05-29 19:06
 **/
public class TimePointParams {

    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    private Integer time;
    private String state;
    private Integer scene_id;
    private String uid;
    private String mesh_id;
    private Integer tid;
    private Integer hour;
    private Integer minute;
    private Integer pos_x;
    private Integer ttime;
    private  Integer detail_sence_id;
    private Integer light_status;

    public Integer getLight_status() {
        return light_status;
    }

    public void setLight_status(Integer light_status) {
        this.light_status = light_status;
    }

    public Integer getHour() {
        return hour;
    }

    public void setHour(Integer hour) {
        this.hour = hour;
    }

    public Integer getMinute() {
        return minute;
    }

    public void setMinute(Integer minute) {
        this.minute = minute;
    }

    public Integer getPos_x() {
        return pos_x;
    }

    public void setPos_x(Integer pos_x) {
        this.pos_x = pos_x;
    }

    public Integer getTtime() {
        return ttime;
    }

    public void setTtime(Integer ttime) {
        this.ttime = ttime;
    }

    public Integer getDetail_sence_id() {
        return detail_sence_id;
    }

    public void setDetail_sence_id(Integer detail_sence_id) {
        this.detail_sence_id = detail_sence_id;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Integer getScene_id() {
        return scene_id;
    }

    public void setScene_id(Integer scene_id) {
        this.scene_id = scene_id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getMesh_id() {
        return mesh_id;
    }

    public void setMesh_id(String mesh_id) {
        this.mesh_id = mesh_id;
    }

    public Integer getTid() {
        return tid;
    }

    public void setTid(Integer tid) {
        this.tid = tid;
    }
}
