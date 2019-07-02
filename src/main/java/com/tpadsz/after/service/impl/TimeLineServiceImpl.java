package com.tpadsz.after.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tpadsz.after.dao.TimeLineDao;
import com.tpadsz.after.entity.TimeBean;
import com.tpadsz.after.entity.TimeLine;
import com.tpadsz.after.entity.TimePointParams;
import com.tpadsz.after.service.TimeLineService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

        String jsonStr = params.toJSONString();
        timeLineDao.createTimeJson(params.getInteger("tid"),params.getString("meshId"),params.getString("uid"),jsonStr);
        return true;
    }

    @Override
    public void rename(JSONObject params) {
        Integer tid = params.getInteger("tid");
        String meshId = params.getString("meshId");
        String tname = params.getString("tname");
        String uid = params.getString("uid");
        timeLineDao.updateTname(tid,meshId,tname,uid);
        TimeBean timeBean = timeLineDao.getTimeJson(tid,meshId,uid);
        String json = timeBean.getJson();
        JSONObject jsonObject = JSONObject.parseObject(json);
        jsonObject.remove("tname");
        jsonObject.put("tname",tname);
        jsonObject.remove("item_title");
        jsonObject.put("item_title",tname);
        json = jsonObject.toJSONString();
        timeLineDao.updateTimeJson(tid,meshId,uid,json);

    }

    @Override
    public List<JSONObject> get(JSONObject params) {
        String uid = params.getString("uid");
        String meshId = params.getString("meshId");
        List<TimeBean> timeLineLists = timeLineDao.getByUidAndMeshId(uid,meshId);
        List<JSONObject> json = new ArrayList<>();
        for (TimeBean timeBean:timeLineLists){
            String jsonStr = timeBean.getJson();
            JSONObject jsonObject = JSONObject.parseObject(jsonStr);
            json.add(jsonObject);
        }
        return json;
    }

    @Override
    public void delete(JSONObject params) {
        String uid = params.getString("uid");
        String meshId = params.getString("meshId");
        JSONArray timeLineArray = params.getJSONArray("timeLineLists");
        for (int i=0;i<timeLineArray.size();i++){
            Integer tid = timeLineArray.getJSONObject(i).getInteger("tid");
            timeLineDao.deleteTimePoint(uid,meshId,tid);
            timeLineDao.deleteTimeDetail(uid,meshId,tid);
            timeLineDao.deleteTimeJson(uid,meshId,tid);
            timeLineDao.deleteTimeLine(uid,meshId,tid);
//            timeLineDao.delete(uid,meshId,tid);
        }

    }

    @Override
    public void deleteOne(String uid,String meshId,Integer tid){
        timeLineDao.delete(uid,meshId,tid);
    }

    @Override
    public void createTimeLine(JSONObject params) {
        TimeLine timeLine = setTimeLine(params);
        String jsonStr = params.toJSONString();
        timeLineDao.createTimeJson(params.getInteger("tid"),params.getString("meshId"),params.getString("uid"),jsonStr);
    }

    /**
     * 创建多个meshId
     * @param params count 创建meshId的个数
     * @return 返回String类型的meshId集合
     */
    @Override
    public String createMeshToPC(JSONObject params) {
        StringBuffer sb = new StringBuffer();
        Integer count = params.getInteger("count");
        Map<String,Object> map;
        for (int i=0;i<count;i++){
            int num=0;
            do{
                map = timeLineDao.getOneMeshId();
                String mesh_id = String.valueOf(map.get("mesh_id"));
                Integer id = (Integer) map.get("id");
                num = timeLineDao.deleteOneMeshId(id);
                if (num>0){
                    //删除num个MeshId
                    sb.append(mesh_id).append(",");
                    timeLineDao.insertRepeated(mesh_id);
                    break;
                }
            }while (num<=0);
        }
        return sb.toString().substring(0,sb.toString().lastIndexOf(","));
    }

    @Override
    public void updateTimePointState(JSONObject params) {
        String uid = params.getString("uid");
        String meshId = params.getString("meshId");
        JSONArray timeLineArray = params.getJSONArray("timeLineList");
        TimePointParams timePointParams;
        List<TimePointParams> timePointList = new ArrayList<>();
        for (int i=0;i<timeLineArray.size();i++){
            timePointParams = new TimePointParams();
            timePointParams.setMesh_id(meshId);
            timePointParams.setUid(uid);
            timePointParams.setTid(timeLineArray.getJSONObject(i).getInteger("tid"));
            timePointParams.setState(timeLineArray.getJSONObject(i).getString("state"));
            timePointList.add(timePointParams);
        }
        timeLineDao.updateTimePointState(timePointList);
    }


    public TimeLine setTimeLine(JSONObject params){
        TimeLine timeLine = new TimeLine();
        timeLine.setTname(params.getString("tname"));
        timeLine.setTid(params.getInteger("tid"));
        timeLine.setWeek(params.getString("week"));
        timeLine.setState(params.getString("state"));
        timeLine.setRepetition(params.getString("repetition"));
        timeLine.setMesh_id(params.getString("meshId"));
        timeLine.setDayObj(params.getString("dayObj"));
        if (params.getBoolean("ischoose")){
            timeLine.setIschoose(0);
        }else {
            timeLine.setIschoose(1);
        }
        timeLine.setItem_desc(params.getString("item_desc"));
        timeLine.setItem_set(params.getInteger("item_set"));
        timeLine.setItem_tag(params.getInteger("item_tag"));
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
            Integer sceneId = timePointArray.getJSONObject(i).getInteger("sence_index");
            timePointParams = new TimePointParams();
            timePointParams.setMesh_id(meshId);
            timePointParams.setUid(uid);
            timePointParams.setTid(tid);
            timePointParams.setScene_id(sceneId);
//            timePointParams.setState(timePointArray.getJSONObject(i).getString("state"));
            timePointParams.setTime(timePointArray.getJSONObject(i).getInteger("time"));
            timePointParams.setHour(timePointArray.getJSONObject(i).getInteger("hour"));
            timePointParams.setMinute(timePointArray.getJSONObject(i).getInteger("minute"));
            timePointParams.setPos_x(timePointArray.getJSONObject(i).getInteger("pos_x"));
            timePointParams.setDetail_sence_id(0);
            timePointParams.setLight_status(timePointArray.getJSONObject(i).getInteger("light_status"));
            if (sceneId==23){
                //多场景
                timeLineDao.insertTimeDatail(timePointParams);
                Integer detailId = timePointParams.getId();
//                System.out.println("detailId: "+detailId);
                JSONArray detailValueList = timePointArray.getJSONObject(i).getJSONArray("detailValueList");
                for(int j=0;j<detailValueList.size();j++){
//                    timePointParams.setState(detailValueList.getJSONObject(j).getString("state"));
                    timePointParams.setTime(detailValueList.getJSONObject(j).getInteger("time"));
                    timePointParams.setHour(detailValueList.getJSONObject(j).getInteger("hour"));
                    timePointParams.setMinute(detailValueList.getJSONObject(j).getInteger("minute"));
                    timePointParams.setPos_x(detailValueList.getJSONObject(j).getInteger("pos_x"));
                    timePointParams.setScene_id(detailValueList.getJSONObject(j).getInteger("sence_index"));
                    timePointParams.setLight_status(detailValueList.getJSONObject(j).getInteger("light_status"));
                    timePointParams.setDetail_sence_id(detailId);
                    timePointList.add(timePointParams);
                }
            }else {
                //单场景
                timePointList.add(timePointParams);
            }
        }
        return timePointList;
    }

}
