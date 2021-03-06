package com.tpadsz.after.entity;

import java.util.Date;

/**
 * Created by chenhao.lu on 2019/3/8.
 */
public class Mesh {
    private Integer id;
    private String mname;
    private String mesh_id;
    private String mesh_type;
    private String is_repeat;
    private String pwd;
    private String uid;
    private Integer project_id;
    private Date create_date;
    private Date update_date;
    private String other;

    public Mesh() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMname() {
        return mname;
    }

    public void setMname(String mname) {
        this.mname = mname;
    }

    public String getMesh_id() {
        return mesh_id;
    }

    public void setMesh_id(String mesh_id) {
        this.mesh_id = mesh_id;
    }

    public String getMesh_type() {
        return mesh_type;
    }

    public void setMesh_type(String mesh_type) {
        this.mesh_type = mesh_type;
    }

    public String getIs_repeat() {
        return is_repeat;
    }

    public void setIs_repeat(String is_repeat) {
        this.is_repeat = is_repeat;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Integer getProject_id() {
        return project_id;
    }

    public void setProject_id(Integer project_id) {
        this.project_id = project_id;
    }

    public Date getCreate_date() {
        return create_date;
    }

    public void setCreate_date(Date create_date) {
        this.create_date = create_date;
    }

    public Date getUpdate_date() {
        return update_date;
    }

    public void setUpdate_date(Date update_date) {
        this.update_date = update_date;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }
}
