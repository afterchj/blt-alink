package com.tpadsz.after.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSONObject;
import com.tpadsz.after.dao.PlaceDao;
import com.tpadsz.after.exception.NameDuplicateException;
import com.tpadsz.after.service.PlaceService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @program: blt-alink
 * @description:
 * @author: Mr.Ma
 * @create: 2019-08-01 09:35
 **/
@Component
@Service
public class PlaceServiceImpl implements PlaceService {

    @Resource
    private PlaceDao placeDao;

    @Override
    public void create(JSONObject params) throws NameDuplicateException {
        String uid = params.getString("uid");
        String meshId = params.getString("meshId");
        String pname = params.getString("pname");
        Integer count = placeDao.getPname(uid,meshId,pname);
        if (count>0){
            //区域名重复
            throw new NameDuplicateException("区域名重复");
        }
        Integer placeId = placeDao.getLastPlace(uid,meshId);
        if (placeId==null){
            //没有区域
            placeId=0;
        }
        placeId = placeId+1;
        placeDao.savePlace(uid,meshId,placeId,pname);
    }

    @Override
    public void delete(JSONObject params) {

    }

    @Override
    public void rename(JSONObject params) {

    }
}
