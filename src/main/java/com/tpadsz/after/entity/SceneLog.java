package com.tpadsz.after.entity;

/**
 * @program: blt-alink
 * @description: scene_log table
 * @author: Mr.Ma
 * @create: 2019-03-13 17:11
 **/
public class SceneLog {

    private Integer id;
    private Integer sceneId;
    private String meshId;
    private String uid;
    private String operation;
    private String bltFlag;

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

    public String getMeshId() {
        return meshId;
    }

    public void setMeshId(String meshId) {
        this.meshId = meshId;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getBltFlag() {
        return bltFlag;
    }

    public void setBltFlag(String bltFlag) {
        this.bltFlag = bltFlag;
    }
}
