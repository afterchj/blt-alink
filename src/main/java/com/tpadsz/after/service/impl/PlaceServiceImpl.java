package com.tpadsz.after.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSONObject;
import com.tpadsz.after.dao.GroupOperationDao;
import com.tpadsz.after.dao.LightAjustDao;
import com.tpadsz.after.dao.PlaceDao;
import com.tpadsz.after.entity.PlaceExtend;
import com.tpadsz.after.entity.PlaceSave;
import com.tpadsz.after.entity.SavePlaceFactory;
import com.tpadsz.after.exception.NameDuplicateException;
import com.tpadsz.after.service.PlaceService;
import org.apache.commons.collections.map.HashedMap;
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
    public Map<String,Object> create(JSONObject params) throws NameDuplicateException {
        Map<String,Object> placeMap = new HashedMap();
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
//        PlaceSave placeSave = new PlaceSave();
//        placeSave.setMeshId(meshId);
//        placeSave.setPlace_id(placeId);
//        placeSave.setUid(uid);
//        placeSave.setPname(pname);
        PlaceSave placeSave = SavePlaceFactory.savePlace(uid,meshId,pname,placeId);
        placeDao.savePlace(placeSave);
        placeMap.put("pid",placeSave.getId());
        placeMap.put("placeId",placeId);
        placeMap.put("pname",pname);
        return placeMap;
    }

    @Override
    public void delete(JSONObject params){
        String uid = params.getString("uid");
        String meshId = params.getString("meshId");
        Integer pid = params.getInteger("pid");//区域序列号
        Integer count = lightAjustDao.getLightByPid(pid);
        if (count>0){
            //区域内存在设备 将设备放到未分组
            lightAjustDao.updateLightByPidAndMeshId(pid,meshId);
//            throw new NotExitException("该区域中存在设备");
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
