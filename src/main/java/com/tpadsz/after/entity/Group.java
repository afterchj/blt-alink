package com.tpadsz.after.entity;

/**
 * @program: blt-alink
 * @description:
 * @author: Mr.Ma
 * @create: 2019-03-06 16:40
 **/
public class Group {

    private Integer id;
    private String gname;
    private Integer mid;
    private Integer groupId;
    private String meshId;
    private String uid;
    private Integer gid;
    private Integer dgid;//目标组的gid
    private Integer dGroupId;//目标组的组id
    private Integer pid;

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public Integer getdGroupId() {
        return dGroupId;
    }

    public void setdGroupId(Integer dGroupId) {
        this.dGroupId = dGroupId;
    }

    public Integer getDgid() {
        return dgid;
    }

    public void setDgid(Integer dgid) {
        this.dgid = dgid;
    }

    public Integer getGid() {
        return gid;
    }

    public void setGid(Integer gid) {
        this.gid = gid;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGname() {
        return gname;
    }

    public void setGname(String gname) {
        this.gname = gname;
    }

    public Integer getMid() {
        return mid;
    }

    public void setMid(Integer mid) {
        this.mid = mid;
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

}
