package com.tpadsz.after.entity.time;

import java.util.List;

/**
 * @program: blt-alink
 * @description:
 * @author: Mr.Ma
 * @create: 2019-08-22 14:42
 **/
public class MeshTimer {

    private String meshId;
    private String mname;
    private List<Timer> timerList;

    public List<Timer> getTimerList() {
        return timerList;
    }

    public void setTimerList(List<Timer> timerList) {
        this.timerList = timerList;
    }

    public String getMeshId() {
        return meshId;
    }

    public void setMeshId(String meshId) {
        this.meshId = meshId;
    }

    public String getMname() {
        return mname;
    }

    public void setMname(String mname) {
        this.mname = mname;
    }

    @Override
    public String toString() {
        return "MeshTimer{" +
                "meshId='" + meshId + '\'' +
                ", mname='" + mname + '\'' +
                ", timerList=" + timerList +
                '}';
    }
}
