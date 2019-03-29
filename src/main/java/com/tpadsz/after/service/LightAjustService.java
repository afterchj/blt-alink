package com.tpadsz.after.service;

import com.tpadsz.after.entity.LightList;

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

    void updateLightName(String lmac, String lname);

    void saveLight(List<LightList> lightLists) throws Exception;

    Integer deleteLight(String lmac);

    void updateLightGid(List<LightList> lightLists) throws Exception;

//    void saveLightColor(String uid,String meshId,String lmac, String x, String y);

//    String getLightOff(String lmac);

    Map<String,Integer> getLid(String lmac);

    void saveTempLight(LightList lightList);
}
