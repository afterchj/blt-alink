package com.tpadsz.after.service;

import com.alibaba.fastjson.JSONObject;
import com.tpadsz.after.exception.NameDuplicateException;

/**
 * @program: blt-alink
 * @description:
 * @author: Mr.Ma
 * @create: 2019-08-01 09:35
 **/
public interface PlaceService {
    void create(JSONObject params) throws NameDuplicateException;

    void delete(JSONObject params);

    void rename(JSONObject params);
}
