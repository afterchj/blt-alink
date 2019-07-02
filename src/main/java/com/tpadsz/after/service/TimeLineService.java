package com.tpadsz.after.service;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * @program: blt-alink
 * @description:
 * @author: Mr.Ma
 * @create: 2019-06-03 11:20
 **/
public interface TimeLineService {
    boolean create(JSONObject params);

    void rename(JSONObject params);

    List<JSONObject>  get(JSONObject params);

    void delete(JSONObject params);

    String createMeshToPC(JSONObject params);

    void updateTimePointState(JSONObject params);

    void deleteOne(String uid,String meshId,Integer tid);

    void createTimeLine(JSONObject params);
}
