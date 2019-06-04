package com.tpadsz.after.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tpadsz.after.dao.TimeLineDao;
import com.tpadsz.after.entity.TimeLine;
import com.tpadsz.after.entity.TimeLineList;
import com.tpadsz.after.entity.TimePointParams;
import com.tpadsz.after.service.TimeLineService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: blt-alink
 * @description:
 * @author: Mr.Ma
 * @create: 2019-06-03 11:20
 **/
@Service("timeLineService")
public class TimeLineServiceImpl implements TimeLineService {

    @Resource
    private TimeLineDao timeLineDao;

    @Override
    public boolean create(JSONObject params) {
        TimeLine timeLine = setTimeLine(params);
        Integer count = timeLineDao.createTimeLine(timeLine);
        if (count==null){
            return false;
        }
        List<TimePointParams> timePointParamsList = setTimePointList(params);
        Integer count2 = timeLineDao.createTimePoint(timePointParamsList);
        if (count2==null){
            return false;
        }
        return true;
    }

    @Override
    public void rename(JSONObject params) {
        Integer tid = params.getInteger("tid");
        String meshId = params.getString("meshId");
        String tname = params.getString("tname");
        String uid = params.getString("uid");
        timeLineDao.updateTname(tid,meshId,tname,uid);
    }

    @Override
    public List<TimeLineList> get(JSONObject params) {
        String uid = params.getString("uid");
        String meshId = params.getString("meshId");
        List<TimeLineList> timeLineLists = timeLineDao.getByUidAndMeshId(uid,meshId);
        return timeLineLists;
    }

    @Override
    public Integer delete(JSONObject params) {
        String uid = params.getString("uid");
        String meshId = params.getString("meshId");
        Integer tid = params.getInteger("tid");
        return timeLineDao.delete(uid,meshId,tid);
    }

    public TimeLine setTimeLine(JSONObject params){
        TimeLine timeLine = new TimeLine();
        timeLine.setTname(params.getString("tname"));
        timeLine.setTid(params.getInteger("tid"));
        timeLine.setWeek(params.getString("week"));
        timeLine.setState(params.getString("state"));
        timeLine.setRepetition(params.getString("repetition"));
        timeLine.setMesh_id(params.getString("meshId"));;
        return timeLine;
    }

    public List<TimePointParams> setTimePointList(JSONObject params){
        List<TimePointParams> timePointList = new ArrayList<>();
        String uid = params.getString("uid");
        String meshId = params.getString("meshId");
        Integer tid = params.getInteger("tid");
        TimePointParams timePointParams;
        JSONArray timePointArray = params.getJSONArray("timePointList");
        for (int i=0;i<timePointArray.size();i++){
            Integer sceneId = timePointArray.getJSONObject(i).getInteger("sceneId");
            timePointParams = new TimePointParams();
            timePointParams.setMesh_id(meshId);
            timePointParams.setUid(uid);
            timePointParams.setTid(tid);
            timePointParams.setState(timePointArray.getJSONObject(i).getString("state"));
            timePointParams.setTime(timePointArray.getJSONObject(i).getString("time"));
            if (sceneId!=null){
                timePointParams.setScene_id(sceneId);
            }
            timePointList.add(timePointParams);
        }
        return timePointList;
    }

}
