package com.tpadsz.after.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSONObject;
import com.tpadsz.after.dao.GroupOperationDao;
import com.tpadsz.after.dao.LightAjustDao;
import com.tpadsz.after.dao.PlaceDao;
import com.tpadsz.after.entity.PlaceExtend;
import com.tpadsz.after.exception.NameDuplicateException;
import com.tpadsz.after.exception.NotExitException;
import com.tpadsz.after.service.PlaceService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

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

    @Resource
    private LightAjustDao lightAjustDao;

    @Resource
    private GroupOperationDao groupOperationDao;

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
    public void delete(JSONObject params) throws NotExitException {
        String uid = params.getString("uid");
        String meshId = params.getString("meshId");
        Integer pid = params.getInteger("pid");//区域序列号
        Integer count = lightAjustDao.getLightByPid(pid);
        if (count>0){
            throw new NotExitException("该区域中存在设备");
        }
        groupOperationDao.deleteGroupByPid(pid);//删除组
        placeDao.deletePlaceByPid(pid);//删除区域
    }

    @Override
    public void rename(JSONObject params) throws NameDuplicateException {
        String uid = params.getString("uid");
        String meshId = params.getString("meshId");
        Integer pid = params.getInteger("pid");//区域序列号
        String pname = params.getString("pname");
        Integer count = placeDao.getPname(uid,meshId,pname);
        if (count>0){
            //区域名重复
            throw new NameDuplicateException("区域名重复");
        }
        placeDao.updatePname(pid,pname);
    }

    @Override
    public List<Map<String, Object>> getPlaceByMeshId(JSONObject param) {
        String meshId = param.getString("meshId");
        String uid = param.getString("uid");
        return placeDao.getPlaceByMeshId(meshId,uid);
    }

    @Override
    public List<PlaceExtend> getPlacesAndGroups(List<Map<String, Object>> placeNum) {
        int mid = (int) placeNum.get(0).get("mid");
        return placeDao.getPlacesAndGroups(mid);
    }
}
