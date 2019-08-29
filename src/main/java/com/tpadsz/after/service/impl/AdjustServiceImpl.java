package com.tpadsz.after.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tpadsz.after.dao.GroupOperationDao;
import com.tpadsz.after.dao.LightAjustDao;
import com.tpadsz.after.dao.SceneAjustDao;
import com.tpadsz.after.entity.*;
import com.tpadsz.after.exception.GroupDuplicateException;
import com.tpadsz.after.exception.NameDuplicateException;
import com.tpadsz.after.exception.NotExitException;
import com.tpadsz.after.exception.SystemAlgorithmException;
import com.tpadsz.after.service.AdjustService;
import com.tpadsz.after.service.GroupOperationService;
import com.tpadsz.after.service.PlaceService;
import com.tpadsz.after.service.SceneAjustService;
import com.tpadsz.after.util.factory.AdjustBeanUtils;
import com.tpadsz.after.util.factory.AdjustComponentUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: blt-alink
 * @description:
 * @author: Mr.Ma
 * @create: 2019-08-16 10:14
 **/
@Service("adjustService")
public class AdjustServiceImpl implements AdjustService {

    @Resource
    private GroupOperationDao groupOperationDao;

    private final AdjustBeanUtils adjustBeanUtils;

    @Resource
    private SceneAjustDao sceneAjustDao;

    @Resource
    private SceneAjustService sceneAjustService;

    @Resource
    private AdjustComponentUtils adjustComponentUtils;

    @Autowired
    public AdjustServiceImpl(AdjustBeanUtils adjustBeanUtils) {
        this.adjustBeanUtils = adjustBeanUtils;
    }

    @Resource
    private PlaceService placeService;

    @Resource
    private GroupOperationService groupOperationService;

    @Resource
    private LightAjustDao lightAjustDao;

    /**
     * 组操作
     * @param group 组信息
     * @param operation "0":创建组(包含未分组)；"1":删除组；"2":重命名组；
     * @throws NameDuplicateException 组名重复
     * @throws GroupDuplicateException 存在组
     * @throws NotExitException 组不存在
     */
    @Override
    public void groupOperation(Group group, String operation) throws NameDuplicateException, GroupDuplicateException,
            NotExitException {
        if ("0".equals(operation)) {//创建组
            adjustComponentUtils.createGroup(group);
        } else if ("2".equals(operation)) {//重命名组
            adjustComponentUtils.renameGroup(group);
        } else if ("1".equals(operation)) {//删除组
            adjustComponentUtils.deleteGroup(group);
        }
    }

    /**
     * 灯操作
     * @param group 组信息
     * @return List<LightReturn>
     * @throws SystemAlgorithmException 数据库回滚异常
     * @throws NotExitException 目标组id不能为空
     */
    @Override
    public List<LightReturn> lightOperation(Group group, JSONObject params) throws SystemAlgorithmException, NotExitException {
        String operation = params.getString("operation");
        List<LightReturn> lightLists = new ArrayList<>();
        if ("0".equals(operation)) {//扫描灯
            lightLists = adjustComponentUtils.scanLights(group,params);
        } else if ("1".equals(operation)) {//删除灯
            adjustComponentUtils.deleteLight(params);
        } else if ("2".equals(operation)) {//移动灯，包括组之间移动 已分组移除灯 未分组移动到已分组
            adjustComponentUtils.moveLights(group,params);
        }
        return lightLists;
    }

    /**
     * 保存场景
     * @throws NotExitException 不存在设备
     * @throws SystemAlgorithmException 数据库回滚异常
     */
    @Override
    public void saveScene(JSONObject params) throws NotExitException, SystemAlgorithmException {
        String meshId = params.getString("meshId");
        String uid = params.getString("uid");
        Integer sceneId = params.getInteger("sceneId");
        String sname = params.getString("sname");
        Integer pid = params.getInteger("pid");
        String bltFlag = params.getString("bltFlag");
        Integer mid = groupOperationDao.getMeshSerialNo(meshId, uid);
        Integer sid = groupOperationDao.getSceneSeriaNo(mid, sceneId, uid);
        //sid 为空判断
        if (sid == null) {
            //创建场景
            SceneAjust sceneAjust = adjustBeanUtils.setSceneAjust(sceneId,uid,mid,sname);
            sceneAjustDao.saveScene(sceneAjust);
            sid = sceneAjust.getId();
        }
        JSONArray lightArray = params.getJSONArray("lightList");
        JSONArray groupList = params.getJSONArray("groupList");
        adjustComponentUtils.saveGroupSetting(groupList,sid,mid);
        List<LightSetting> lightSettings = adjustComponentUtils.setLightSettings(lightArray, mid, pid, sid);
        sceneAjustService.saveLightSetting(lightSettings);
        SceneLog sceneLog = adjustBeanUtils.setSceneLog(uid,bltFlag,meshId,sceneId);
        sceneAjustDao.saveSceneLog(sceneLog);//保存场景日志
    }

    /**
     * 保存默认场景
     * @param params meshId:网络id;sceneId:场景id;sname:场景名
     * @throws NotExitException 数据为空
     */
    @Override
    public void saveDefaultScene(JSONObject params) throws NotExitException {
        String meshId = params.getString("meshId");
        String uid = params.getString("uid");
        Integer mid = groupOperationDao.getMeshSerialNo(meshId, uid);
        JSONArray sceneArray = params.getJSONArray("sceneArray");
        if (sceneArray.isEmpty() || sceneArray.size() < 1) {
            throw new NotExitException("数据为空");
        }
        SceneAjust sceneAjust;
        for (int i = 0; i < sceneArray.size(); i++) {
            Integer sceneId = sceneArray.getJSONObject(i).getInteger("sceneId");
            String sname = sceneArray.getJSONObject(i).getString("sname");
            Integer sid = groupOperationDao.getSceneSeriaNo(mid, sceneId, uid);
            if (sid == null) {
                sceneAjust = adjustBeanUtils.setSceneAjust(sceneId, uid, mid, sname);
                sceneAjustDao.saveScene(sceneAjust);//创建场景
                SceneLog sceneLog = adjustBeanUtils.setSceneLog(uid, "1", meshId, sceneId);
                sceneAjustDao.saveSceneLog(sceneLog);//保存场景日志
            }
        }
    }

    /**
     * 查看group列表
     * @param params jsonObject
     * @return group/place列表
     * @throws NotExitException 网络不存在
     */
    public Map<String,Object> getGroupList(JSONObject params) throws NotExitException {
        String meshId = params.getString("meshId");
        String uid = params.getString("uid");
        Integer mid = groupOperationDao.getMeshSerialNo(meshId, uid);
        if (mid == null){
            throw new NotExitException("网络不存在");
        }
        List<Map<String, Object>> placeNum = placeService.getPlaceByMeshId(params);
        Integer version = params.getInteger("version");
        List<PlaceExtend> places;
        List<GroupList> groupLists;
        Map<String,Object> map = new HashMap();
        if (version != null && 2 == version) {
            //v2.1.0版本
            places = placeService.getPlacesAndGroups(placeNum);
            map.put("data",places);

        } else if (version == null) {
            //v2.1.0版本
            groupLists = groupOperationService.getGroupAll(mid);
            map.put("data",groupLists);
        }
        return map;
    }

    /**
     * 重命名灯
     * @param params bltFlag:蓝牙连接标志;productId:产品id;pid:区域序列号
     */
    @Override
    public void renameLight(JSONObject params){
        String bltFlag = params.getString("bltFlag");
        String lmac = params.getString("lmac");
        String lname = params.getString("lname");
        String meshId = params.getString("meshId");
        Integer groupId = params.getInteger("groupId");
        String productId = params.getString("productId");
        productId = productId.split(" ")[0];
        String uid = params.getString("uid");
        Integer pid = params.getInteger("pid");
        String operation = "5";
        Integer mid = groupOperationService.getMeshSerialNo(meshId, uid);
        Map<String, Integer> lightMap = lightAjustDao.getLid(lmac);
        if (lightMap == null || lightMap.size() == 0) {
            //服务端未找到该灯
            LightList lightList = adjustBeanUtils.setLightList(mid, lmac, groupId, productId, pid);
            //创建灯
            lightAjustDao.saveTempLight(lightList);
        }else {
            //数据库中的mid和当前扫描入网的mid不是同一网络//Integer默认比较[-128,128]
            if (lightMap.get("mid").intValue() != mid.intValue()) {
                lightAjustDao.deleteLightSettingByLmac(lmac);//删除灯的场景
                //更新 lname,gid,mid,update_date,pid
                lightAjustDao.updateLight(lmac, groupId, mid, pid);
            } else {
                //只更新灯名和pid
                lightAjustDao.updateLightName(lmac, lname, pid);
            }
        }
        //记录日志
        lightAjustDao.saveLightAjustLog(meshId, bltFlag, operation, lmac);
    }
}
