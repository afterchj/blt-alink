package com.tpadsz.after.entity.time;

import com.alibaba.fastjson.JSONObject;

/**
 * @program: blt-alink
 * @description:
 * @author: Mr.Ma
 * @create: 2019-08-22 14:45
 **/
public class Timer {

    private String timer;
    private JSONObject TimerLine;


        public JSONObject getTimerLine() {
        return TimerLine;
    }

    public void setTimerLine(JSONObject timerLine) {
        TimerLine = timerLine;
    }

    public String getTimer() {
        return timer;
    }

    public void setTimer(String timer) {
        this.timer = timer;
    }

    @Override
    public String toString() {
        return "Timer{" +
                "timer='" + timer + '\'' +
                ", TimerLine=" + TimerLine +
                '}';
    }
}
