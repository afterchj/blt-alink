package com.tpadsz.after.entity;

/**
 * @program: alink-hq
 * @description: 任务点
 * @author: Mr.Ma
 * @create: 2019-05-29 19:06
 **/
public class TimePointParams {

    private String time;
    private String state;
    private Integer scene_id;
    private String uid;
    private String mesh_id;
    private Integer tid;

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
