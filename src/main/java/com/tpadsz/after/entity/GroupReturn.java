package com.tpadsz.after.entity;

/**
 * @program: blt-alink
 * @description: f_group
 * @author: Mr.Ma
 * @create: 2019-03-06 13:42
 **/
public class GroupReturn {

    private String uid;
    private String groupId;//组id
    private String gname;
    private String meshId;//网络id

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGname() {
        return gname;
    }

    public void setGname(String gname) {
        this.gname = gname;
    }

    public String getMeshId() {
        return meshId;
    }

    public void setMeshId(String meshId) {
        this.meshId = meshId;
    }
}
