package com.tpadsz.after.service;

import com.alibaba.fastjson.JSONObject;
import com.tpadsz.after.entity.PlaceExtend;
import com.tpadsz.after.exception.NameDuplicateException;
import com.tpadsz.after.exception.NotExitException;

import java.util.List;
import java.util.Map;

/**
 * @program: blt-alink
 * @description:
 * @author: Mr.Ma
 * @create: 2019-08-01 09:35
 **/
public interface PlaceService {
    void create(JSONObject params) throws NameDuplicateException;

    void delete(JSONObject params) throws NotExitException;

    void rename(JSONObject params) throws NameDuplicateException;

    List<Map<String, Object>> getPlaceByMeshId(JSONObject param);

    List<PlaceExtend> getPlacesAndGroups(List<Map<String, Object>> placeNum);
}
