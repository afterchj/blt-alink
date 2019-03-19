package com.tpadsz.after.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tpadsz.after.entity.*;
import com.tpadsz.after.entity.dd.ResultDict;
import com.tpadsz.after.service.GroupOperationService;
import com.tpadsz.after.service.LightAjustService;
import com.tpadsz.after.service.SceneAjustService;
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
            saveLog(uid, meshId, null, operation, bltFlag, model);
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
                if (gid == null) {
                    groupOperationService.saveGroup(group);//创建组
                } else {
                    //数据库存在该组,只改名 组中的灯???
                    groupOperationService.updateGroupNameByMid(group);
                    logger.info("method:groupOperation db has the group: {},meshId: {}", groupId, meshId);
                }
                groupOperationService.saveGroupLog(uid, meshId, operation,
                        bltFlag);//创建组日志
                model.put("result", ResultDict.SUCCESS.getCode());
                model.put("result_message", ResultDict.SUCCESS.getValue());
                return;
            }
            //重命名组
            if ("2".equals(operation)) {
                if (gid != null) {
                    groupOperationService.updateGroupNameByMid(group);
                } else {
                    //不存在灯 ???
                    logger.info("method:groupOperation cannot find the group:{},meshId:{}", groupId, meshId);
                }
                groupOperationService.saveGroupLog(uid, meshId, operation, bltFlag);
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
                    groupOperationService.saveGroupLog(uid, meshId, operation, bltFlag);
                } else {
                    logger.info("method:groupOperation cannot find the group:{},meshId:{}", groupId, meshId);
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
            System.out.println("method:groupsLists" + "mid is null");
            return;
        }
        List<GroupList> groupLists = groupOperationService.getGroupAll(mid);
        String lmac;
        GroupConsoleLog groupConsoleLog;
        List<LightList> lightLists;
        Map<String, Object> map;
        for (GroupList groupList : groupLists) {
            groupList.setMeshId(meshId);
            lightLists = groupList.getLightLists();
            //组内有灯
            if (lightLists.size() > 0) {
                //最新调光记录
                groupConsoleLog = groupOperationService
                        .getGroupConsoleLogByGid(groupList.getGroupId(), uid,
                                meshId);
                //最新调光记录是对灯调光 开关状态???
                if (groupConsoleLog.getLmac() != null) {
                    for (LightList lightList : lightLists) {
                        lmac = lightList.getLmac();
                        //获取单灯最近调光记录
                        map = groupOperationService.getLightColor(lmac);
                        //新扫描的灯没有调光记录
                        if (map != null) {
                            lightList.setX((String) map.get("x"));
                            lightList.setY((String) map.get("y"));
                        }
                    }
                    continue;
                }
                //最新调光记录是对组调光 lmac == null
                groupList.setX(groupConsoleLog.getX());
                groupList.setY(groupConsoleLog.getY());
            }
        }
        model.put("result", ResultDict.SUCCESS.getCode());
        model.put("result_message", ResultDict.SUCCESS.getValue());
        model.put("data", groupLists);
        return;
    }


    /**
     * 重命名灯
     *
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
        String operation = "4";
        if (StringUtils.isBlank(bltFlag) || StringUtils.isBlank(lmac) || StringUtils.isBlank(lname)) {
            model.put("result", ResultDict.PARAMS_BLANK.getCode());
            model.put("result_message", ResultDict.PARAMS_BLANK.getValue());
            return;
        }
        //未连蓝牙
        if ("0".equals(bltFlag)) {
            //记录日志
            saveLog(null, meshId, lmac, operation, bltFlag, model);
            return;
        }
        //连接蓝牙
        if ("1".equals(bltFlag)) {
            Integer lid = lightAjustService.getLid(lmac);
            if (lid == null) {
                //服务端未找到该灯
                operation = "5";
                logger.info("method:renameLight cannot find the light {}", lmac);
//                System.out.println("method:renameLight cannot find the light:"+lmac);
            } else {
                lightAjustService.updateLightName(lmac, lname);
            }
            //记录日志
            lightAjustService.saveLightAjustLog(meshId, lmac, bltFlag, operation);
            model.put("result", ResultDict.SUCCESS.getCode());
            model.put("result_message", ResultDict.SUCCESS.getValue());
            return;
        }
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
        if (StringUtils.isBlank(meshId) || sceneId == null || bltFlag == null) {
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
        if (array.isEmpty() || array.size() < 1) {
            model.put("result", ResultDict.PARAMS_BLANK.getCode());
            model.put("result_message", ResultDict.PARAMS_BLANK.getValue());
            return;
        }
        String lmac;
        Integer lid;
        LightList lightList;
        List<LightSetting> lightSettingList = new ArrayList<>();
        LightSetting lightSetting;
        String x;
        String y;
        String off;
        for (int i = 0; i < array.size(); i++) {
            lmac = array.getJSONObject(i).getString("lmac");
            if (StringUtils.isBlank(lmac)) {
                model.put("result", ResultDict.PARAMS_BLANK.getCode());
                model.put("result_message", ResultDict.PARAMS_BLANK.getValue());
                return;
            }
            lid = lightAjustService.getLid(lmac);
            //服务端未找到该灯
            if (lid == null) {
                lightList = new LightList();
                lightList.setMid(mid);
                lightList.setLmac(lmac);
                lightList.setLname(lmac);
                //临时创建灯
                lightAjustService.saveTempLight(lightList);
                lid = lightList.getId();
                logger.info("method:saveScene lmac:{} cannot find the light", lmac);
            }
            x = array.getJSONObject(i).getString("x");
            y = array.getJSONObject(i).getString("y");
            off = array.getJSONObject(i).getString("off");
            lightSetting = new LightSetting();
            lightSetting.setSid(sid);
            lightSetting.setLid(lid);
            lightSetting.setX(x);
            lightSetting.setY(y);
            lightSetting.setOff(off);
            lightSettingList.add(lightSetting);
        }

        //删除light_setting中之前保存的场景
        sceneAjustService.deleteLightSetting(sid);
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

    //    @RequestMapping(value = "/saveScene",method = RequestMethod.POST)
    public void saveScene2(@ModelAttribute("decodedParams") JSONObject
                                   params, ModelMap model) {

//        List<GroupList> groupLists = groupOperationService.getGroups(mid);
//        GroupConsoleLog groupConsoleLog;
//        List<LightList> lightLists;
//        Map<String,Object> map;
//        LightSetting lightSetting;
//        GroupSetting groupSetting;
//        String off;
//        for (GroupList groupList:groupLists){
//            lightLists = groupList.getLightLists();
//            //组内有灯
//            if (groupList.getLightLists().size()>0){
//                for (LightList lightList:lightLists){
//                    off = lightAjustService.getLightOff(lightList.getLmac());
//                }
//               groupConsoleLog = groupOperationService
// .getGroupConsoleLogByGid(groupList.getGroupId(),uid,meshId);
//                //最新调光记录是对灯调光
//                if (groupConsoleLog.getLmac()!=null){
//                    groupList.setX("-1");
//                    groupList.setY("-1%");
//
//                    for (LightList lightList:lightLists){
//                        map = groupOperationService.getLightColor(lightList
// .getLmac());
//                        lightSetting = new LightSetting();
//                        lightSetting.setX((String) map.get("x"));
//                        lightSetting.setY((String) map.get("y"));
//                        lightSetting.setLid(lightList.getLid());
//                        lightSetting.setSid(sid);
//                        //保存单灯场景
//                        sceneAjustService.saveLightSetting(lightSetting);
//                    }
//                    continue;
//                }
//                //最新调光记录是对组调光
//                groupList.setX(groupConsoleLog.getX());
//                groupList.setY(groupConsoleLog.getY());
//                groupSetting = new GroupSetting();
//                groupSetting.setMid(mid);
//                groupSetting.setGid(groupList.getId());
//                groupSetting.setSid(sid);
//                groupSetting.setX(groupConsoleLog.getX());
//                groupSetting.setY(groupConsoleLog.getY());
//                //保存单组场景
//                sceneAjustService.saveGroupSetting(groupSetting);
//            }
//        }
//        int status=0;
//        String x = groupLists.get(0).getX();
//        String y = groupLists.get(0).getY();
//        for (GroupList groupList:groupLists){
//            //组内有灯
//            if (groupList.getLightLists().size()>0){
//                //该组xy值和第1组xy值不相同或者x值为"-1"或者y值为"-1%"
//                if (!x.equals(groupList.getX())||!y.equals(groupList.getY()
// )||"-1".equals(groupList.getX())||"-1%".equals(groupList.getY())){
//                    status=1;
//                    break;
//                }
//            }
//            continue;
//        }
//        //最新调光记录是对全组调光
//        if (0==status){
//            SceneSetting sceneSetting = new SceneSetting();
//            sceneSetting.setSid(sid);
//            sceneSetting.setX(x);
//            sceneSetting.setY(y);
//            //记录到scene_setting表中
//            sceneAjustService.saveSceneSetting(sceneSetting);
//        }
//        SceneLog sceneLog = new SceneLog();
//        sceneLog.setOperation("0");
//        sceneLog.setUid(uid);
//        sceneLog.setMeshId(meshId);
//        sceneLog.setBltFlag(bltFlag);
//        sceneLog.setSceneId(sceneId);
//        sceneAjustService.saveSceneLog(sceneLog);
//        model.put("result",ResultDict.SUCCESS.getCode());
//        model.put("result_message",ResultDict.SUCCESS.getValue());
//        return;

    }

    /**
     * 添加/删除设备
     *
     * @param params |operation|String|"0":扫描灯，1:删除灯，2:已分组中移除灯,3:组之间移动灯
     *               |bltFlag|String|1:连接蓝牙，0:未连蓝牙|
     *               |productId|Stirng|产品id(产品类型)|
     *               |meshId|String|网络Id|
     *               |groupId|String|组id|
     *               |dGroupId|String|目标组id|
     * @param model
     */
    @RequestMapping(value = "/lightAjust", method = RequestMethod.POST)
    public void lightAjust(@ModelAttribute("decodedParams") JSONObject params, ModelMap model) {
        String operation = params.getString("operation");
        String bltFlag = params.getString("bltFlag");
        String uid = params.getString("uid");
        String meshId = params.getString("meshId");
        String lmac = null;
        String lname;
        String productId;//产品类型
        Group group;
        Integer gid;
        Integer groupId;
        Integer dGroupId;
        if (StringUtils.isBlank(operation) || StringUtils.isBlank(bltFlag) || StringUtils.isBlank(meshId)) {
            model.put("result", ResultDict.PARAMS_BLANK.getCode());
            model.put("result_message", ResultDict.PARAMS_BLANK.getValue());
            return;
        }
        //未连蓝牙
        if ("0".equals(bltFlag)) {
            saveLog(null, meshId, lmac, operation, bltFlag, model);
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
        //连接蓝牙
        if ("1".equals(bltFlag)) {
            //扫描灯
            if ("0".equals(operation)) {
                group.setGroupId(0);//未分组的组id
                gid = groupOperationService.getGid(group);//未分组的gid
                group.setGid(gid);
                JSONArray array = params.getJSONArray("lightGroup");
                if (array.isEmpty() || array.size() < 1) {
                    model.put("result", ResultDict.PARAMS_BLANK.getCode());
                    model.put("result_message", ResultDict.PARAMS_BLANK.getValue());
                    return;
                }
                List<LightList> lightLists = new ArrayList<>();
                LightList lightList;
                for (int i = 0; i < array.size(); i++) {
                    lmac = array.getJSONObject(i).getString("lmac");
                    lname = array.getJSONObject(i).getString("lname");
                    productId = array.getJSONObject(i).getString("productId");
                    if (StringUtils.isBlank(lmac) || StringUtils.isBlank(lname)) {
                        model.put("result", ResultDict.PARAMS_BLANK.getCode());
                        model.put("result_message", ResultDict.PARAMS_BLANK.getValue());
                        return;
                    }
                    lightList = new LightList();
                    lightList.setLmac(lmac);
                    lightList.setLname(lname);
                    lightList.setMid(mid);
                    lightList.setGid(gid);
                    if (!StringUtils.isBlank(productId)) {
                        lightList.setProductId(productId);
                    }
                    lightLists.add(lightList);
                }
                try {
                    //mybatis 批量添加灯
                    lightAjustService.saveLight(lightLists);
                } catch (Exception e) {
                    model.put("result", ResultDict.SYSTEM_ERROR.getCode());
                    model.put("result_message", ResultDict.SYSTEM_ERROR.getValue());
                    logger.error("method:lightAjust; service:saveLight(); db rollback; mid:{},gid:{}", mid, gid);
//                    System.out.println("method:lightAjust service:saveLight exception:rollback");
                }
                lightAjustService.saveLightAjustLog(meshId, null, bltFlag, operation);
                model.put("result", ResultDict.SUCCESS.getCode());
                model.put("result_message", ResultDict.SUCCESS.getValue());
                return;
            }
            //删除灯
            if ("1".equals(operation)) {
                group.setGroupId(0);//未分组的组id
                gid = groupOperationService.getGid(group);//未分组的gid
                group.setGid(gid);
                lmac = params.getString("lmac");
                if (StringUtils.isBlank(lmac)) {
                    model.put("result", ResultDict.PARAMS_BLANK.getCode());
                    model.put("result_message", ResultDict.PARAMS_BLANK.getValue());
                    return;
                }
                lightAjustService.deleteLight(lmac);//删除
                //删除light_setting中的记录
                sceneAjustService.deleteLightSettingByLmac(lmac);
                lightAjustService.saveLightAjustLog(meshId, lmac, bltFlag, operation);//记录日志
                model.put("result", ResultDict.SUCCESS.getCode());
                model.put("result_message", ResultDict.SUCCESS.getValue());
                return;
            }
            groupId = params.getInteger("groupId");
            lmac = params.getString("lmac");
            //已分组中移除灯 移除到未分组中
            if ("2".equals(operation)) {
                dGroupId = 0;
                moveLight(model, groupId, dGroupId, lmac, meshId, bltFlag,
                        operation, mid);
                return;
            }
            //组之间移动灯
            if ("3".equals(operation)) {
                dGroupId = params.getInteger("dGroupId");
                moveLight(model, groupId, dGroupId, lmac, meshId, bltFlag,
                        operation, mid);
                return;
            }
        }
    }


    /**
     * 2:已分组中移除灯,3:组之间移动灯
     *
     * @param model    返回给客户端的相应码
     * @param groupId  当前组的组id
     * @param dGroupId 需要移动到目的组的组id
     * @param lmac     灯的mac地址
     */
    public void moveLight(ModelMap model, Integer groupId, Integer dGroupId,
                          String lmac, String meshId, String bltFlag, String
                                  operation, Integer mid) {
        Group group = new Group();
        group.setMeshId(meshId);
        group.setMid(mid);
        if (groupId == null || StringUtils.isBlank(lmac)) {
            model.put("result", ResultDict.PARAMS_BLANK.getCode());
            model.put("result_message", ResultDict.PARAMS_BLANK.getValue());
            return;
        }
        group.setGroupId(groupId);
        Integer gid = groupOperationService.getGid(group);//当前灯的gid
        group.setGid(gid);
        group.setdGroupId(dGroupId);//未分组的组id
        group.setGroupId(dGroupId);
        Integer dgid = groupOperationService.getGid(group);//未分组的gid
        group.setGroupId(groupId);
        group.setDgid(dgid);
        lightAjustService.updateLightGid(group);//更新light表中的gid
        lightAjustService.saveLightAjustLog(meshId, lmac, bltFlag, operation)
        ;//记录日志
        model.put("result", ResultDict.SUCCESS.getCode());
        model.put("result_message", ResultDict.SUCCESS.getValue());
        return;
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
    public void saveLog(String uid, String meshId, String lmac, String
            operation, String bltFlag, ModelMap model) {
        if (uid != null) {
            groupOperationService.saveGroupLog(uid, meshId, operation, bltFlag);
            model.put("result", ResultDict.SUCCESS.getCode());
            model.put("result_message", ResultDict.SUCCESS.getValue());
            return;
        }
        lightAjustService.saveLightAjustLog(meshId, lmac, bltFlag, operation);
        model.put("result", ResultDict.SUCCESS.getCode());
        model.put("result_message", ResultDict.SUCCESS.getValue());
        return;
    }

}
