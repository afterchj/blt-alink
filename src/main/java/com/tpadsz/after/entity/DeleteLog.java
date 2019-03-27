package com.tpadsz.after.entity;

import java.io.Serializable;
import java.util.Date;

public class DeleteLog implements Serializable {

    private static final long serialVersionUID = 1L;
    private int id;
    private Date delete_date;
    private String uid;
    private Integer lightFlag;
    private String other;

    public DeleteLog() {
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getLightFlag() {
        return lightFlag;
    }

    public void setLightFlag(Integer lightFlag) {
        this.lightFlag = lightFlag;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDelete_date() {
        return delete_date;
    }

    public void setDelete_date(Date delete_date) {
        this.delete_date = delete_date;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }
}
