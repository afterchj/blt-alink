package com.tpadsz.after.service;

import com.alibaba.fastjson.JSONObject;
import com.tpadsz.after.entity.LightList;
import com.tpadsz.after.entity.LightReturn;
import com.tpadsz.after.exception.SystemAlgorithmException;

import java.util.List;
import java.util.Map;

/**
 * @program: blt-alink
 * @description:
 * @author: Mr.Ma
 * @create: 2019-03-07 14:19
 **/
public interface LightAjustService {
    void saveLightAjustLog(String meshId, String bltFlag,String operation, String lmacs);

    void updateLightName(String lmac, String lname, Integer pid);

    String saveLight(List<LightList> lightLists) throws SystemAlgorithmException;

    Integer deleteLight(String lmac);

    void updateLightGid(List<LightList> lightLists, String meshId, String bltFlag, String operation) throws SystemAlgorithmException;

//    void saveLightColor(String uid,String meshId,String lmac, String x, String y);

//    String getLightOff(String lmac);

    Map<String,Integer> getLid(String lmac);

    void saveTempLight(LightList lightList);

    void deleteLightSettingByLmac(String lmac);

    void updateLight(String lmac, Integer groupId, Integer mid, Integer pid);

    List<LightReturn> getAllByMid(Integer mid);

    void moveLightsToDiffGroups(JSONObject params);

    void updateLightXY(JSONObject params);
}
