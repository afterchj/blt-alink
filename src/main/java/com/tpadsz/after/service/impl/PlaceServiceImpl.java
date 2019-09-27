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
import com.tpadsz.after.util.factory.PlaceBeanUtils;
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

    @Resource
    private PlaceBeanUtils placeBeanUtils;

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
        PlaceSave placeSave = SavePlaceFactory.savePlace(uid,meshId,pname,placeId);
        placeDao.savePlace(placeSave);
        placeMap.put("pid",placeSave.getPid());
        placeMap.put("placeId",placeId);
        placeMap.put("pname",pname);
        return placeMap;
    }

    @Override
    public void delete(JSONObject params){
//        String uid = params.getString("uid");
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
    public List<PlaceExtend> getPlacesAndGroups(Integer mid) {
        return placeDao.getPlacesAndGroups(mid);
    }

    @Override
    public Integer getPlaceByGroupIdAndMeshId(Integer dGroupId, String meshId,String uid) {
        return placeDao.getPlaceByGroupIdAndMeshId(dGroupId,meshId, uid);
    }

    /**
     * 服务端根据pid自动创建区域
     * @param uid
     * @param meshId
     * @param pname
     * @return
     */
    @Override
    public Integer SavePlace(String uid,String meshId,String pname){
        pname = createPname(uid,meshId,pname);//赋予不重复的区域名
        //创建恢复区
        PlaceSave placeSave = placeBeanUtils.setPlaceSave(uid,meshId,pname,-1);
        placeDao.savePlace(placeSave);
        return placeSave.getPid();
    }

    @Override
    public Integer getVersionCode(JSONObject params) {

        return placeDao.getVersionCode(params.getString("meshId"));
    }

    /**
     * 为创建区域赋予一个名称不重复的区域名
     * @param uid
     * @param meshId
     * @param pname
     * @return
     */
    public String createPname(String uid,String meshId,String pname){
        int pnameNum = placeDao.getPname(uid,meshId,pname);
        int num = 1;
        StringBuffer sb = new StringBuffer();
        StringBuffer preSb = new StringBuffer();
        preSb.append("恢复区");
        sb.append("恢复区");
        while (pnameNum>0){//存在名称叫"恢复区"
            pname = sb.append(num).toString();
            sb = preSb;
            pnameNum = placeDao.getPname(uid,meshId,pname);
            num++;
        }
        return pname;
    }


}
