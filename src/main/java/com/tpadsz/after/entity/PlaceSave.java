package com.tpadsz.after.entity;

/**
 * @program: blt-alink
 * @description: 区域扩展
 * @author: Mr.Ma
 * @create: 2019-08-02 09:44
 **/
public class PlaceSave extends Place {

    private String uid;
    private String meshId;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getMeshId() {
        return meshId;
    }

    public void setMeshId(String meshId) {
        this.meshId = meshId;
    }
}
