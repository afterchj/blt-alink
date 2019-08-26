package com.tpadsz.after.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tpadsz.after.dao.GroupOperationDao;
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
    public void saveLightSetting(JSONObject params) throws NotExitException, SystemAlgorithmException {
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
        sceneAjustService.saveSceneLog(sceneLog);//保存场景日志
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
}
