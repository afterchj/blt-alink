package com.tpadsz.after.service;

import com.alibaba.fastjson.JSONObject;
import com.tpadsz.after.entity.PlaceExtend;
import com.tpadsz.after.exception.NameDuplicateException;

import java.util.List;
import java.util.Map;

/**
 * @program: blt-alink
 * @description:
 * @author: Mr.Ma
 * @create: 2019-08-01 09:35
 **/
public interface PlaceService {
    Map<String,Object> create(JSONObject params) throws NameDuplicateException;

    void delete(JSONObject params);

    void rename(JSONObject params) throws NameDuplicateException;

    List<Map<String, Object>> getPlaceByMeshId(JSONObject param);

    List<PlaceExtend> getPlacesAndGroups(List<Map<String, Object>> placeNum);

    Integer getPlaceByGroupIdAndMeshId(Integer dGroupId, String meshId);

    Integer SavePlace(String uid,String meshId,String pname);
}
