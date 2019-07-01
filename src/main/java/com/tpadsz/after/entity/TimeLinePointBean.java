package com.tpadsz.after.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhangjun.chen on 2019/6/2.
 */
public class TimeLinePointBean implements Serializable{
    private List<Value> valueList;

    public List<Value> getValueList() {
        return valueList;
    }

    public void setValueList(List<Value> valueList) {
        this.valueList = valueList;
    }

    public TimeLinePointBean(List<Value> valueList) {
        this.valueList = valueList;
    }

    public static class Value{
        private int pos_x;
        private int sence_index;
        private int light_status;
        private int time;
        private int hour;
        private int minute;
        private List<Value> detailvalueList;

        public int getPos_x() {
            return pos_x;
        }

        public void setPos_x(int pos_x) {
            this.pos_x = pos_x;
        }

        public int getSence_index() {
            return sence_index;
        }

        public void setSence_index(int sence_index) {
            this.sence_index = sence_index;
        }

        public int getLight_status() {
            return light_status;
        }

        public void setLight_status(int light_status) {
            this.light_status = light_status;
        }

        public int getTime() {
            return time;
        }

        public void setTime(int time) {
            this.time = time;
        }

        public int getHour() {
            return hour;
        }

        public void setHour(int hour) {
            this.hour = hour;
        }

        public int getMinute() {
            return minute;
        }

        public void setMinute(int minute) {
            this.minute = minute;
        }

        public List<Value> getDetailvalueList() {
            return detailvalueList;
        }

        public void setDetailvalueList(List<Value> detailvalueList) {
            this.detailvalueList = detailvalueList;
        }

        public Value(int pos_x, int sence_index, int light_status, int time, int hour, int minute, List<Value> detailvalueList) {
            this.pos_x = pos_x;
            this.sence_index = sence_index;
            this.light_status = light_status;
            this.time = time;
            this.hour = hour;
            this.minute = minute;
            this.detailvalueList = detailvalueList;
        }

        public Value(int pos_x, int sence_index, int light_status, int time, int hour, int minute) {
            this.pos_x = pos_x;
            this.sence_index = sence_index;
            this.light_status = light_status;
            this.time = time;
            this.hour = hour;
            this.minute = minute;
        }

        public Value(int sence_index, int light_status, int time, int hour, int minute) {
            this.sence_index = sence_index;
            this.light_status = light_status;
            this.time = time;
            this.hour = hour;
            this.minute = minute;
        }
    }
}
