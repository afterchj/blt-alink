package com.tpadsz.after.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tpadsz.after.entity.*;
import com.tpadsz.after.entity.dd.ResultDict;
import com.tpadsz.after.exception.DefaultPlaceNotFoundException;
import com.tpadsz.after.service.GroupOperationService;
import com.tpadsz.after.service.LightAjustService;
import com.tpadsz.after.service.SceneAjustService;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 客户端和服务端可能有数据不一致情况
 *
 * @program: blt-alink
 * @description: 场景和组调节模块
 * @author: Mr.Ma
 * @create: 2019-03-06 13:27
 * @see
 **/
@Controller
@RequestMapping("/adjust")
public class AlinkAdjustModuleController extends BaseDecodedController {

    @Resource
    private GroupOperationService groupOperationService;

    @Resource
    private LightAjustService lightAjustService;

    @Resource
    private SceneAjustService sceneAjustService;

    private Logger logger = LoggerFactory.getLogger(AlinkAdjustModuleController.class);

    /**
     * group操作(创建/删除/重命名组)接口
     *
     * @param params uid,gname,groupId,operate,bltFlag
     * @param model
     */
    @RequestMapping(value = "/groupOperation")
    public void groupOperation(@ModelAttribute("decodedParams") JSONObject
                                       params, ModelMap model) {
        String uid = params.getString("uid");
        String operation = params.getString("operation");
        //"0":创建组；"1":删除组；"2":重命名组；
        String bltFlag = params.getString("bltFlag");//"1":连接蓝牙；"0":未连蓝牙；
        Integer groupId = params.getInteger("groupId");//组id(客户端生成)
        String gname = params.getString("gname");//组名(重命名组时是重命名后的组名)
        String meshId = params.getString("meshId");//网络id
        Integer mid;
        Group group;
        if (StringUtils.isBlank(operation) || StringUtils.isBlank(bltFlag) || StringUtils.isBlank(meshId) ||
                StringUtils.isBlank(gname) || groupId == null) {
            model.put("result", ResultDict.PARAMS_BLANK.getCode());
            model.put("result_message", ResultDict.PARAMS_BLANK.getValue());
            return;
        }
        //未连蓝牙
        if (bltFlag.equals("0")) {
            saveLog(uid, meshId, operation, bltFlag, model, groupId, null);
            return;
        }
        //根据mesh id查询mesh序列号
        mid = groupOperationService.getMeshSerialNo(meshId, uid);
        if (mid == null) {
            logger.info("method:groupOperation meshId：{} mid is null", meshId);
//            System.out.println("method:groupOperation" + "mid is null");
            model.put("result", ResultDict.MESHID_NOT_NULL.getCode());
            model.put("result_message", ResultDict.MESHID_NOT_NULL.getValue());
            return;
        }

        group = new Group();
        group.setGname(gname);
        group.setMid(mid);
        group.setGroupId(groupId);
        group.setUid(uid);
        group.setMeshId(meshId);
        Integer gid = groupOperationService.getGid(group);
        //连接蓝牙
        if ("1".equals(bltFlag)) {
            //创建组
            if ("0".equals(operation)) {
                Integer pid = params.getInteger("pid");
                try {
                    pid = groupOperationService.getDefaultPlace(pid,uid,mid);
                } catch (DefaultPlaceNotFoundException e) {

                    model.put("result", ResultDict.NO_DEFAULT_PLACE.getCode());
                    model.put("result_message", ResultDict.NO_DEFAULT_PLACE.getValue());
                    return;
                }
                group.setPid(pid);
                String dbGname = groupOperationService.getGname(group);
                if (StringUtils.isNotBlank(dbGname)){
                    //组名重复
                    model.put("result", ResultDict.DUPLICATE_NAME.getCode());
                    model.put("result_message", ResultDict.DUPLICATE_NAME.getValue());
                    return;
                }
                if (gid == null) {
                    groupOperationService.saveGroup(group);//创建组
                } else {
                    //数据库存在该组,只改名 组中的灯???
//                    groupOperationService.updateGroupNameByMid(group);
                    logger.info("method:groupOperation db has the group: {},meshId: {}", groupId, meshId);
                    model.put("result", ResultDict.DUPLICATE_GID.getCode());
                    model.put("result_message", ResultDict.DUPLICATE_GID.getValue());
                    return;
                }
                groupOperationService.saveGroupLog(uid, meshId, operation,
                        bltFlag, groupId);//创建组日志
                model.put("result", ResultDict.SUCCESS.getCode());
                model.put("result_message", ResultDict.SUCCESS.getValue());
                return;
            }
            //重命名组
            if ("2".equals(operation)) {
                if (gid != null) {
                    groupOperationService.updateGroupNameByMid(group);
                } else {
                    //不存在组 ???
                    logger.info("method:groupOperation cannot find the group:{},meshId:{}", groupId, meshId);
                    model.put("result", ResultDict.NO_GROUP.getCode());
                    model.put("result_message", ResultDict.NO_GROUP.getValue());
                    return;
                }
                groupOperationService.saveGroupLog(uid, meshId, operation, bltFlag, groupId);
                model.put("result", ResultDict.SUCCESS.getCode());
                model.put("result_message", ResultDict.SUCCESS.getValue());
                return;
            }
            //删除组
            if ("1".equals(operation)) {
                //存在该组
                if (gid != null) {
                    group.setGid(gid);
                    //查询组内灯的数量
                    Integer lightNum = groupOperationService.getLightNum(group);
                    //组内有灯
                    if (lightNum != null) {
                        //移动组中的灯到未分组中
                        groupOperationService.updateGidInLight(group);
                    }
                    //删除组表
                    groupOperationService.deleteGroup(group);
                    groupOperationService.saveGroupLog(uid, meshId, operation, bltFlag, groupId);
                } else {
                    //不存在改组
                    logger.info("method:groupOperation cannot find the group:{},meshId:{}", groupId, meshId);
                    model.put("result", ResultDict.NO_GROUP.getCode());
                    model.put("result_message", ResultDict.NO_GROUP.getValue());
                    return;
                }
                model.put("result", ResultDict.SUCCESS.getCode());
                model.put("result_message", ResultDict.SUCCESS.getValue());
                return;
            }
        }

    }

    /**
     * 查看group列表、group内设备列表和信息状态
     *
     * @param params uid,meshId
     * @param model
     */
    @RequestMapping(value = "/groupsLists", method = RequestMethod.POST)
    public void groupsLists(@ModelAttribute("decodedParams") JSONObject
                                    params, ModelMap model) {
        String meshId = params.getString("meshId");
        String uid = params.getString("uid");
        if (StringUtils.isBlank(meshId)) {
            model.put("result", ResultDict.PARAMS_BLANK.getCode());
            model.put("result_message", ResultDict.PARAMS_BLANK.getValue());
            return;
        }
        Integer mid = groupOperationService.getMeshSerialNo(meshId, uid);
        if (mid == null) {
            model.put("result", ResultDict.MESHID_NOT_NULL.getCode());
            model.put("result_message", ResultDict.MESHID_NOT_NULL.getValue());
//            System.out.println("method:groupsLists" + "mid is null");
            return;
        }
        List<GroupList> groupLists = groupOperationService.getGroupAll(mid);
        model.put("result", ResultDict.SUCCESS.getCode());
        model.put("result_message", ResultDict.SUCCESS.getValue());
        model.put("data", groupLists);
        return;
    }


    /**
     * 重命名灯
     * @param params |lmac|String|灯mac地址|
     *               |lname|String|重命名的灯名称|
     *               |bltFlag|String|"1":连接蓝牙；"0":未连蓝牙|
     * @param model
     */
    @RequestMapping(value = "/renameLight", method = RequestMethod.POST)
    public void renameLight(@ModelAttribute("decodedParams") JSONObject
                                    params, ModelMap model) {
        String bltFlag = params.getString("bltFlag");
        String lmac = params.getString("lmac");
        String lname = params.getString("lname");
        String meshId = params.getString("meshId");
        Integer groupId = params.getInteger("groupId");
        String productId = params.getString("productId");
        productId = productId.split(" ")[0];
        String uid = params.getString("uid");
        Integer pid = params.getInteger("pid");
        String operation = "4";
        if (StringUtils.isBlank(bltFlag) || StringUtils.isBlank(lmac) || StringUtils.isBlank(lname)) {
            model.put("result", ResultDict.PARAMS_BLANK.getCode());
            model.put("result_message", ResultDict.PARAMS_BLANK.getValue());
            return;
        }

        Integer mid = groupOperationService.getMeshSerialNo(meshId, uid);
        if (mid == null) {
            model.put("result", ResultDict.MESHID_NOT_NULL.getCode());
            model.put("result_message", ResultDict.MESHID_NOT_NULL.getValue());
//            System.out.println("method:renameLight" + "mid is null");
            return;
        }
        try {
            pid = groupOperationService.getDefaultPlace(pid,params.getString("uid"),mid);
        } catch (DefaultPlaceNotFoundException e) {
            model.put("result_message", ResultDict.NO_DEFAULT_PLACE.getValue());
            model.put("result", ResultDict.NO_DEFAULT_PLACE.getCode());
            return;
        }

        //未连蓝牙
        if ("0".equals(bltFlag)) {
            //记录日志
            saveLog(null, meshId, operation, bltFlag, model, null, lmac);
            return;
        }
        //连接蓝牙
        if ("1".equals(bltFlag)) {
            Map<String, Integer> lightMap = lightAjustService.getLid(lmac);
            if (lightMap == null || lightMap.size() == 0) {
                //服务端未找到该灯
                operation = "5";
//                logger.info("method:renameLight cannot find the light {}", lmac);
                LightList lightList = new LightList();
                lightList.setLmac(lmac);
                lightList.setLname(lname);
                lightList.setMid(mid);
                lightList.setProductId(productId);
                lightList.setGroupId(groupId);
                lightList.setPid(pid);
                //创建灯
                lightAjustService.saveTempLight(lightList);
//                System.out.println("method:renameLight cannot find the light:"+lmac);
            } else {
                //数据库中的mid和当前扫描入网的mid不是同一网络//Integer默认比较[-128,128]
                if (lightMap.get("mid").intValue() != mid.intValue()) {
                    lightAjustService.deleteLightSettingByLmac(lmac);//删除灯的场景
                    //更新 lname,gid,mid,update_date,pid
                    lightAjustService.updateLight(lmac, groupId, mid, pid);
                } else {
                    //只更新灯名
                    lightAjustService.updateLightName(lmac, lname);
                }
            }
            //记录日志
            lightAjustService.saveLightAjustLog(meshId, bltFlag, operation, lmac);
            model.put("result", ResultDict.SUCCESS.getCode());
            model.put("result_message", ResultDict.SUCCESS.getValue());
            return;
        }
    }

    /**
     * 保存默认场景
     * @param params
     * @param model
     */
    @RequestMapping(value = "/saveDefaultScene", method = RequestMethod.POST)
    public void saveDefaultScene(@ModelAttribute("decodedParams") JSONObject params,
                                 ModelMap model) {

        String meshId = params.getString("meshId");
        String uid = params.getString("uid");
        if (StringUtils.isBlank(meshId)) {
            model.put("result", ResultDict.PARAMS_BLANK.getCode());
            model.put("result_message", ResultDict.PARAMS_BLANK.getValue());
            return;
        }
        Integer mid = groupOperationService.getMeshSerialNo(meshId, uid);
        if (mid == null) {
            model.put("result", ResultDict.MESHID_NOT_NULL.getCode());
            model.put("result_message", ResultDict.MESHID_NOT_NULL.getValue());
            logger.error("method:saveDefaultScene meshId:{} mid is null", meshId);
            return;
        }
        JSONArray sceneArray = params.getJSONArray("sceneArray");
        if (sceneArray.isEmpty() || sceneArray.size() < 1) {
            model.put("result", ResultDict.PARAMS_BLANK.getCode());
            model.put("result_message", ResultDict.PARAMS_BLANK.getValue());
            return;
        }
        SceneAjust sceneAjust;
        for (int i = 0; i < sceneArray.size(); i++) {
            String sname = sceneArray.getJSONObject(i).getString("sname");
            Integer sceneId = sceneArray.getJSONObject(i).getInteger("sceneId");
            if (sceneId == null || StringUtils.isBlank(sname)) {
                model.put("result", ResultDict.PARAMS_BLANK.getCode());
                model.put("result_message", ResultDict.PARAMS_BLANK.getValue());
                return;
            }
            Integer sid = groupOperationService.getSceneSerialNo(mid, sceneId, uid);
            if (sid == null) {
                sceneAjust = new SceneAjust();
                sceneAjust.setSceneId(sceneId);
                sceneAjust.setUid(uid);
                sceneAjust.setMid(mid);
                sceneAjust.setSname(sname);
                //创建场景
                sceneAjustService.saveScene(sceneAjust);
                SceneLog sceneLog = new SceneLog();
                sceneLog.setOperation("0");
                sceneLog.setUid(uid);
                sceneLog.setMeshId(meshId);
                sceneLog.setBltFlag("1");
                sceneLog.setSceneId(sceneId);
                sceneAjustService.saveSceneLog(sceneLog);//保存场景日志
            }
        }
        model.put("result", ResultDict.SUCCESS.getCode());
        model.put("result_message", ResultDict.SUCCESS.getValue());
        return;
    }


    /**
     * 保存更新场景
     *
     * @param params
     * @param model
     */
    @RequestMapping(value = "/saveScene", method = RequestMethod.POST)
    public void saveScene(@ModelAttribute("decodedParams") JSONObject params,
                          ModelMap model) {
        String meshId = params.getString("meshId");
        Integer sceneId = params.getInteger("sceneId");
        String bltFlag = params.getString("bltFlag");
        String uid = params.getString("uid");
        String sname = params.getString("sname");
        Integer pid = params.getInteger("pid");
        if (StringUtils.isBlank(meshId) || sceneId == null || bltFlag == null ) {
            model.put("result", ResultDict.PARAMS_BLANK.getCode());
            model.put("result_message", ResultDict.PARAMS_BLANK.getValue());
            return;
        }

        //未连蓝牙
        if ("0".equals(bltFlag)) {
            SceneLog sceneLog = new SceneLog();
            sceneLog.setUid(uid);
            sceneLog.setBltFlag(bltFlag);
            sceneLog.setMeshId(meshId);
            sceneLog.setOperation("0");
            sceneLog.setSceneId(sceneId);
            //记录日志
            sceneAjustService.saveSceneLog(sceneLog);
            model.put("result", ResultDict.SUCCESS.getCode());
            model.put("result_message", ResultDict.SUCCESS.getValue());
            return;
        }

        Integer mid = groupOperationService.getMeshSerialNo(meshId, uid);
        Integer sid = groupOperationService.getSceneSerialNo(mid, sceneId, uid);
        if (mid == null) {
            model.put("result", ResultDict.MESHID_NOT_NULL.getCode());
            model.put("result_message", ResultDict.MESHID_NOT_NULL.getValue());
//            System.out.println("method:saveScene" + "mid is null");
            logger.error("method:saveScene meshId:{} mid is null", meshId);
            return;
        }
        try {
            pid = groupOperationService.getDefaultPlace(pid,uid,mid);
        } catch (DefaultPlaceNotFoundException e) {
            model.put("result_message", ResultDict.NO_DEFAULT_PLACE.getValue());
            model.put("result", ResultDict.NO_DEFAULT_PLACE.getCode());
            return;
        }
        //sid 为空判断
        if (sid == null) {
            SceneAjust sceneAjust = new SceneAjust();
            sceneAjust.setSceneId(sceneId);
            sceneAjust.setUid(uid);
            sceneAjust.setMid(mid);
            sceneAjust.setSname(sname);
            //创建场景
            sceneAjustService.saveScene(sceneAjust);
            sid = sceneAjust.getId();
        }
        JSONArray array = params.getJSONArray("lightList");
        JSONArray groupList = params.getJSONArray("groupList");
        String lmac;
        Integer groupId;
        String productId;
        LightList lightList;
        List<LightSetting> lightSettingList = new ArrayList<>(array.size() + 1);//配置list容量为jsonArray的size()
        LightSetting lightSetting;
        String x;
        String y;
        String off;
        Map<String, Integer> lightMap;
        GroupSetting groupSetting;
        if (!groupList.isEmpty()&&array.size()>0){
            //删除group_setting信息
            groupOperationService.deleteGroupSetting(sid);
            //v2.1.0新版本添加groupList集合
            for (int i=0; i<groupList.size(); i++){
                groupSetting = new GroupSetting();
                groupId = groupList.getJSONObject(i).getInteger("groupId");
                x =  groupList.getJSONObject(i).getString("x");
                y = groupList.getJSONObject(i).getString("y");
                groupSetting.setX(x);
                groupSetting.setY(y);
                groupSetting.setSid(sid);
                groupSetting.setMid(mid);
                groupSetting.setGroupId(groupId);
                groupOperationService.saveGroupSetting(groupSetting);
            }
        }
        if (array.isEmpty() || array.size() < 1) {
            model.put("result", ResultDict.PARAMS_BLANK.getCode());
            model.put("result_message", ResultDict.PARAMS_BLANK.getValue());
            return;
        }

        for (int i = 0; i < array.size(); i++) {
            lmac = array.getJSONObject(i).getString("lmac");
            productId = array.getJSONObject(i).getString("productId");
            groupId = array.getJSONObject(i).getInteger("groupId");
            if (StringUtils.isBlank(lmac) || groupId == null || StringUtils.isBlank(productId)) {
                model.put("result", ResultDict.PARAMS_BLANK.getCode());
                model.put("result_message", ResultDict.PARAMS_BLANK.getValue());
                return;
            }
            productId = productId.split(" ")[0];
            lightMap = lightAjustService.getLid(lmac);
            //服务端未找到该灯
            if (lightMap == null || lightMap.size() == 0) {
                //Map<String, Integer> lightMap默认不会分配内存空间需要实例化
                //否则lightMap.put会报空指针
                lightMap = new HashedMap();
                lightList = new LightList();
                lightList.setMid(mid);
                lightList.setLmac(lmac);
                lightList.setLname(lmac);
                lightList.setGroupId(groupId);
                lightList.setProductId(productId);
                lightList.setPid(pid);
                //创建灯
                lightAjustService.saveTempLight(lightList);
                lightMap.put("id", lightList.getId());
//                logger.info("method:saveScene lmac:{} cannot find the light", lmac);
            }else {
                //服务端有灯
                //mid不一致
                if (lightMap.get("mid").intValue() != mid.intValue()) {
                    lightAjustService.updateLight(lmac, groupId, mid, pid);//更新灯信息
                    sceneAjustService.deleteLightSettingByLmac(lmac);//删除灯的场景信息
                }
            }
            x = array.getJSONObject(i).getString("x");
            y = array.getJSONObject(i).getString("y");
            off = array.getJSONObject(i).getString("off");
            lightSetting = new LightSetting();
            lightSetting.setSid(sid);
            lightSetting.setLid(lightMap.get("id"));
            lightSetting.setX(x);
            lightSetting.setY(y);
            lightSetting.setOff(off);
            lightSettingList.add(lightSetting);
        }

        //批量插入到light_setting
        try {
            sceneAjustService.saveLightSetting(lightSettingList);
            SceneLog sceneLog = new SceneLog();
            sceneLog.setOperation("0");
            sceneLog.setUid(uid);
            sceneLog.setMeshId(meshId);
            sceneLog.setBltFlag(bltFlag);
            sceneLog.setSceneId(sceneId);
            sceneAjustService.saveSceneLog(sceneLog);//保存场景日志
            model.put("result", ResultDict.SUCCESS.getCode());
            model.put("result_message", ResultDict.SUCCESS.getValue());
            return;
        } catch (Exception e) {
            model.put("result", ResultDict.SYSTEM_ERROR.getCode());
            model.put("result_message", ResultDict.SYSTEM_ERROR.getValue());
//            System.out.println("method:saveScene" + "批量插入灯场景异常");
            logger.error("method:saveScene; service:saveLightSetting(); db rollback;sid:{},mid:{}", sid, mid);
//            e.printStackTrace();
        }
    }

    /**
     * 删除灯前进行通信
     */
    @RequestMapping(value = "/communicationTest", method = RequestMethod.POST)
    public void communicationTest(@ModelAttribute("decodedParams") JSONObject params, ModelMap model ){
        model.put("result", ResultDict.SUCCESS.getCode());
        model.put("result_message", ResultDict.SUCCESS.getValue());
    }

    /**
     * 添加/删除设备
     *
     * @param params |operation|String|"0":扫描灯，1:删除灯，2:已分组中移除灯,3:组之间移动灯
     *               |bltFlag|String|1:连接蓝牙，0:未连蓝牙|
     *               |productId|Stirng|产品id(产品类型)|
     *               |meshId|String|网络Id|
     *               |groupId|int|组id|
     *               |dGroupId|int|目标组id|
     * @param model
     */
    @RequestMapping(value = "/lightAjust", method = RequestMethod.POST)
    public void lightAjust(@ModelAttribute("decodedParams") JSONObject params, ModelMap model) {
        String operation = params.getString("operation");
        String bltFlag = params.getString("bltFlag");
        String uid = params.getString("uid");
        String meshId = params.getString("meshId");
        String lmac;
        Group group;
//        Integer gid;
        if (StringUtils.isBlank(operation) || StringUtils.isBlank(bltFlag) || StringUtils.isBlank(meshId)) {
            model.put("result", ResultDict.PARAMS_BLANK.getCode());
            model.put("result_message", ResultDict.PARAMS_BLANK.getValue());
            return;
        }
        //未连蓝牙
        if ("0".equals(bltFlag)) {
            saveLog(null, meshId, operation, bltFlag, model, null, null);
            return;
        }
        Integer mid = groupOperationService.getMeshSerialNo(meshId, uid);
        if (mid == null) {
            model.put("result", ResultDict.MESHID_NOT_NULL.getCode());
            model.put("result_message", ResultDict.MESHID_NOT_NULL.getValue());
//            System.out.println("method:lightAjust" + "mid is null");
            logger.error("method:lightAjust; mid:{}; mid is null", mid);
            return;
        }
        group = new Group();
        group.setMid(mid);
        group.setMeshId(meshId);
        group.setUid(uid);
        List<LightList> lightLists;
        String result;
        StringBuffer sb;
        String lmacs;
        //连接蓝牙
        if ("1".equals(bltFlag)) {
            //扫描灯
            if ("0".equals(operation)) {
                lightLists = GetLightList(model, operation, group, params);
                result = (String) model.get("result");
//                System.out.println("result: "+result);
                if (result != null) {
                    //出現異常
                    return;
                }
                try {
                    //mybatis 批量添加灯
                    lmacs = lightAjustService.saveLight(lightLists);
                    lightAjustService.saveLightAjustLog(meshId, bltFlag, operation, lmacs);
                    List<LightReturn> lightReturns = lightAjustService.getAllByMid(mid);
                    model.put("result", ResultDict.SUCCESS.getCode());
                    model.put("result_message", ResultDict.SUCCESS.getValue());
                    model.put("lightLists", lightReturns);
                } catch (Exception e) {
                    model.put("result", ResultDict.SYSTEM_ERROR.getCode());
                    model.put("result_message", ResultDict.SYSTEM_ERROR.getValue());
                    logger.error("method:lightAjust; service:saveLight(); db rollback; meshId:{},groupId:{}",
                            meshId, 0);
                }

            }
            //删除灯
            if ("1".equals(operation)) {
                lmac = params.getString("lmac");
                if (StringUtils.isBlank(lmac)) {
                    model.put("result", ResultDict.PARAMS_BLANK.getCode());
                    model.put("result_message", ResultDict.PARAMS_BLANK.getValue());
                    return;
                }
                Integer num = lightAjustService.deleteLight(lmac);//删除
                //刪除成功
                if (num > 0) {
                    //删除light_setting中的记录
                    sceneAjustService.deleteLightSettingByLmac(lmac);
                }
                lightAjustService.saveLightAjustLog(meshId, bltFlag, operation, lmac);//记录日志
                model.put("result", ResultDict.SUCCESS.getCode());
                model.put("result_message", ResultDict.SUCCESS.getValue());
                return;
            }
            //移动灯，包括组之间移动 已分组移除灯 未分组移动到已分组
            if ("2".equals(operation)) {
                lightLists = GetLightList(model, operation, group, params);
                result = (String) model.get("result");
                if (result != null) {
                    //出现异常
                    return;
                }
                try {
                    lightAjustService.updateLightGid(lightLists,meshId,bltFlag,operation);//更新light表
//                    lightAjustService.saveLightAjustLog(meshId, bltFlag, operation, lmacs);//记录日志
                    model.put("result", ResultDict.SUCCESS.getCode());
                    model.put("result_message", ResultDict.SUCCESS.getValue());
                } catch (Exception e) {
                    model.put("result", ResultDict.SYSTEM_ERROR.getCode());
                    model.put("result_message", ResultDict.SYSTEM_ERROR.getValue());
                    logger.error("method:lightAjust; service:saveLight(); db rollback; meshId:{},groupId:{}",
                            meshId, params.getInteger("dGroupId"));
                }

            }
        }
    }

    /**
     * "0":扫描灯
     * "2":移动灯
     * @param
     * @return 返回灯集合
     */
    public List<LightList> GetLightList(ModelMap model, String
            operation, Group group, JSONObject params) {
        String lmac;
        String lname;
        String productId;
        Integer dGroupId = 0;
        Integer pid;
        if ("0".equals(operation)) {
            //扫描灯
            group.setGroupId(0);
        }
        if ("2".equals(operation)) {
            //移动灯
            dGroupId = params.getInteger("dGroupId");
            if (dGroupId == null) {
                model.put("result", ResultDict.PARAMS_BLANK.getCode());
                model.put("result_message", ResultDict.PARAMS_BLANK.getValue());
                return null;
            }
            group.setGroupId(dGroupId);
        }
        Integer gid = groupOperationService.getGid(group);//目的组的gid
        if (gid == null) {
            //目标组未创建
            model.put("result", ResultDict.NO_GROUP.getCode());
            model.put("result_message", ResultDict.NO_GROUP.getValue());
            return null;
        }
        pid = params.getInteger("pid");
        try {
            pid = groupOperationService.getDefaultPlace(pid,group.getUid(),group.getMid());
        } catch (DefaultPlaceNotFoundException e) {
            model.put("result_message", ResultDict.NO_DEFAULT_PLACE.getValue());
            model.put("result", ResultDict.NO_DEFAULT_PLACE.getCode());
            return null;
        }

        LightList lightList;
        JSONArray array = params.getJSONArray("lightGroup");
        if (array.isEmpty() || array.size() < 1) {
            //参数中没有灯的信息
            model.put("result", ResultDict.PARAMS_BLANK.getCode());
            model.put("result_message", ResultDict.PARAMS_BLANK.getValue());
            return null;
        }
        List<LightList> lightLists = new ArrayList<>(array.size() + 1);//配置list容量为jsonarray的size()
        for (int i = 0; i < array.size(); i++) {
            lmac = array.getJSONObject(i).getString("lmac");
            lname = array.getJSONObject(i).getString("lname");
            productId = array.getJSONObject(i).getString("productId");
            if (StringUtils.isBlank(lmac) || StringUtils.isBlank(productId)) {
                model.put("result", ResultDict.PARAMS_BLANK.getCode());
                model.put("result_message", ResultDict.PARAMS_BLANK.getValue());
                return null;
            }
            productId = productId.split(" ")[0];
            lightList = new LightList();
            lightList.setGid(gid);
            lightList.setGroupId(dGroupId);
            lightList.setLmac(lmac);
            if (StringUtils.isBlank(lname)){
                lightList.setLname(lmac);
            }else {
                lightList.setLname(lname);
            }
            lightList.setPid(pid);
            lightList.setProductId(productId);
            lightList.setMid(group.getMid());
            lightLists.add(lightList);
        }
        return lightLists;
    }

    /**
     * 未连蓝牙记录日志表
     *
     * @param uid
     * @param meshId
     * @param operation
     * @param bltFlag
     * @param model
     */

    public void saveLog(String uid, String meshId, String
            operation, String bltFlag, ModelMap model, Integer groupId, String lmacs) {
        if (uid != null) {
            groupOperationService.saveGroupLog(uid, meshId, operation, bltFlag, groupId);
            model.put("result", ResultDict.SUCCESS.getCode());
            model.put("result_message", ResultDict.SUCCESS.getValue());
            return;
        }
        lightAjustService.saveLightAjustLog(meshId, bltFlag, operation, lmacs);
        model.put("result", ResultDict.SUCCESS.getCode());
        model.put("result_message", ResultDict.SUCCESS.getValue());
        return;
    }

}
