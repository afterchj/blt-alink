package com.tpadsz.after.entity;

import java.io.Serializable;

/**
 * Created by zhangjun.chen on 2019/6/5.
 */
public class TimeBean implements Serializable {
    private Integer tid;
    private Integer mid;
    private String json;

    @Override
    public String toString() {
        return "TimeBean{" +
                "tid=" + tid +
                ", mid=" + mid +
                ", json='" + json + '\'' +
                '}';
    }

    public Integer getTid() {
        return tid;
    }

    public void setTid(Integer tid) {
        this.tid = tid;
    }

    public Integer getMid() {
        return mid;
    }

    public void setMid(Integer mid) {
        this.mid = mid;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }
}
