package com.tpadsz.after.entity;

/**
 * @program: blt-alink
 * @description:
 * @author: Mr.Ma
 * @create: 2019-06-03 17:32
 **/
public class TimePointList {

    private Integer sceneId;
    private String time;
    private String state;
//    private Date create_date;
//    private Date update_date;


    public Integer getSceneId() {
        return sceneId;
    }

    public void setSceneId(Integer sceneId) {
        this.sceneId = sceneId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

//    public Date getCreate_date() {
//        return create_date;
//    }
//
//    public void setCreate_date(Date create_date) {
//        this.create_date = create_date;
//    }
//
//    public Date getUpdate_date() {
//        return update_date;
//    }
//
//    public void setUpdate_date(Date update_date) {
//        this.update_date = update_date;
//    }

    @Override
    public String toString() {
        return "TimePointList{" +
                "sceneId=" + sceneId +
                ", time='" + time + '\'' +
                ", state='" + state + '\'' +
                '}';
    }
}
