package com.tpadsz.after.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhangjun.chen on 2019/6/5.
 */
public class TimeBean implements Serializable {
    private String meshId;
    private List<TimeLineListObj> timeLineListObjList;
    private List<TimeLinePointBean> timeLinePointBeanList;//和上一行index同步

    public String getMeshId() {
        return meshId;
    }

    public void setMeshId(String meshId) {
        this.meshId = meshId;
    }

    public List<TimeLineListObj> getTimeLineListObjList() {
        return timeLineListObjList;
    }

    public void setTimeLineListObjList(List<TimeLineListObj> timeLineListObjList) {
        this.timeLineListObjList = timeLineListObjList;
    }

    public List<TimeLinePointBean> getTimeLinePointBeanList() {
        return timeLinePointBeanList;
    }

    public void setTimeLinePointBeanList(List<TimeLinePointBean> timeLinePointBeanList) {
        this.timeLinePointBeanList = timeLinePointBeanList;
    }

    public TimeBean(String meshId, List<TimeLineListObj> timeLineListObjList, List<TimeLinePointBean> timeLinePointBeanList) {
        this.meshId = meshId;
        this.timeLineListObjList = timeLineListObjList;
        this.timeLinePointBeanList = timeLinePointBeanList;
    }
}
