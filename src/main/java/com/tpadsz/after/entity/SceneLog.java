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

    public SceneLog() {
    }
    private SceneLog(Builder builder){
        this.id = builder.id;
        this.sceneId = builder.sceneId;
        this.meshId = builder.meshId;
        this.uid = builder.uid;
        this.operation = builder.operation;
        this.bltFlag = builder.bltFlag;
    }

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

    /**
     * 建造者模式
     */
    public static final class Builder{
        private Integer id;
        private Integer sceneId;
        private String meshId;
        private String uid;
        private String operation;
        private String bltFlag;

        public Builder() {
        }
        public Builder id(Integer id){
            this.id = id;
            return this;
        }
        public Builder sceneId(Integer sceneId){
            this.sceneId = sceneId;
            return this;
        }
        public Builder meshId(String meshId){
            this.meshId = meshId;
            return this;
        }
        public Builder uid(String uid){
            this.uid = uid;
            return this;
        }
        public Builder operation(String operation){
            this.operation = operation;
            return this;
        }
        public Builder bltFlag(String bltFlag){
            this.bltFlag = bltFlag;
            return this;
        }
        public SceneLog build(){
            return new SceneLog(this);
        }
    }
}
