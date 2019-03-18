package com.tpadsz.after.entity;

/**
 * @program: blt-alink
 * @description: scene table
 * @author: Mr.Ma
 * @create: 2019-03-13 17:36
 **/
public class SceneAjust {

    private Integer id;
    private Integer sceneId;
    private String sname;
    private Integer mid;
    private String uid;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSceneId() {
        return sceneId;
    }

    public void setSceneId(Integer sceneId) {
        this.sceneId = sceneId;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public Integer getMid() {
        return mid;
    }

    public void setMid(Integer mid) {
        this.mid = mid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
