package com.tpadsz.after.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tpadsz.after.entity.*;
import com.tpadsz.after.entity.dd.ResultDict;
import com.tpadsz.after.exception.GroupDuplicateException;
import com.tpadsz.after.exception.NameDuplicateException;
import com.tpadsz.after.exception.NotExitException;
import com.tpadsz.after.exception.SystemAlgorithmException;
import com.tpadsz.after.service.*;
import com.tpadsz.after.util.factory.AdjustBeanUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
 * @program: blt-alink
 * @description: 场景和组调节模块
 * @author: Mr.Ma
 * @create: 2019-03-06 13:27
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

    @Resource
    private PlaceService placeService;

    @Resource
    private AdjustService adjustService;

    private final AdjustBeanUtils adjustBeanUtils;

    private Logger logger = LoggerFactory.getLogger(AlinkAdjustModuleController.class);

    @Autowired//构造器注入
    public AlinkAdjustModuleController(AdjustBeanUtils adjustBeanUtils) {
        this.adjustBeanUtils = adjustBeanUtils;
    }


    @RequestMapping(value = "/groupOperation2")
    public void groupOperation2(@ModelAttribute("decodedParams") JSONObject params, ModelMap model) {
        String uid = params.getString("uid");
        String operation = params.getString("operation");
        //"0":创建组；"1":删除组；"2":重命名组；
        String bltFlag = params.getString("bltFlag");//"1":连接蓝牙；"0":未连蓝牙；
        Integer groupId = params.getInteger("groupId");//组id(客户端生成)
        String gname = params.getString("gname");//组名(重命名组时是重命名后的组名)
        String meshId = params.getString("meshId");//网络id
        Integer mid;
        Group group;
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
                group.setPid(pid);
                String dbGname = groupOperationService.getGname(group);
                if (StringUtils.isNotBlank(dbGname)) {
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
                    if (lightNum != null) {
                        groupOperationService.deleteGroup(group);
                    }
                    groupOperationService.saveGroupLog(uid, meshId, operation, bltFlag, groupId);
                } else {
                    //不存在组
                    logger.info("method:groupOperation cannot find the group:{},meshId:{}", groupId, meshId);
                    model.put("result", ResultDict.NO_GROUP.getCode());
                    model.put("result_message", ResultDict.NO_GROUP.getValue());
                    return;
                }
                model.put("result", ResultDict.SUCCESS.getCode());
                model.put("result_message", ResultDict.SUCCESS.getValue());
            }
        }

    }

    /**
     * group操作(创建/删除/重命名组)接口
     * TODO groupOperation
     * @param params uid,gname,groupId,operate,bltFlag
     */
    @RequestMapping(value = "/groupOperation")
    public void groupOperation(@ModelAttribute("decodedParams") JSONObject params, ModelMap model) {
        String uid = params.getString("uid");
        String operation = params.getString("operation");//"0":创建组；"1":删除组；"2":重命名组；
        String bltFlag = params.getString("bltFlag");//"1":连接蓝牙；"0":未连蓝牙；
        Integer groupId = params.getInteger("groupId");//组id(客户端生成)
        String meshId = params.getString("meshId");//网络id
        if (bltFlag.equals("0")) {//未连蓝牙
            saveLog(uid, meshId, operation, bltFlag, model, groupId, null);
        } else if ("1".equals(bltFlag)) {//连接蓝牙
            Group group = adjustBeanUtils.setGroup(params);
            try {
                adjustService.groupOperation(group, operation);
                groupOperationService.saveGroupLog(uid, meshId, operation, bltFlag, groupId);
                model.put("result", ResultDict.SUCCESS.getCode());
                model.put("result_message", ResultDict.SUCCESS.getValue());
            } catch (NameDuplicateException e) {//组名重复
                logger.error("method:groupOperation group name duplicate;groupId:{},meshId:{}", groupId, meshId);
                model.put("result", ResultDict.GROUP_NAME_DUPLICATE.getCode());
                model.put("result_message", ResultDict.GROUP_NAME_DUPLICATE.getValue());
            } catch (GroupDuplicateException e) {//存在组
                logger.error("method:groupOperation group duplicate;groupId:{},meshId:{}", groupId, meshId);
                model.put("result", ResultDict.DUPLICATE_GID.getCode());
                model.put("result_message", ResultDict.DUPLICATE_GID.getValue());
            } catch (NotExitException e) {//不存在组
                logger.error("method:groupOperation cannot find the group:{},meshId:{}", groupId, meshId);
                model.put("result", ResultDict.NO_GROUP.getCode());
                model.put("result_message", ResultDict.NO_GROUP.getValue());
            }
        }
    }

    /**
     * 查看group列表、group内设备列表和信息状态
     *
     * @param params uid,meshId
     */
    @RequestMapping(value = "/groupsLists", method = RequestMethod.POST)
    public void groupsLists(@ModelAttribute("decodedParams") JSONObject
                                    params, ModelMap model) {
        try {
            Map<String, Object> map = adjustService.getGroupList(params);
            model.put("result", ResultDict.SUCCESS.getCode());
            model.put("result_message", ResultDict.SUCCESS.getValue());
            model.put("data", map.get("data"));
        } catch (NotExitException e) {
            model.put("result", ResultDict.MESHID_NOT_NULL.getCode());
            model.put("result_message", ResultDict.MESHID_NOT_NULL.getValue());
            logger.error("method groupsLists; meshid is null; meshId is {}, uid is {}", params.getString("meshId"),
                    params.getString("uid"));
        }
    }

    /**
     * 重命名灯
     *
     * @param params |lmac|String|灯mac地址|
     *               |lname|String|重命名的灯名称|
     *               |bltFlag|String|"1":连接蓝牙；"0":未连蓝牙|
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
        Integer mid = groupOperationService.getMeshSerialNo(meshId, uid);
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
                LightList lightList = adjustBeanUtils.setLightList(mid, lmac, groupId, productId, pid);
                //创建灯
                lightAjustService.saveTempLight(lightList);
            } else {
                //数据库中的mid和当前扫描入网的mid不是同一网络//Integer默认比较[-128,128]
                if (lightMap.get("mid").intValue() != mid.intValue()) {
                    lightAjustService.deleteLightSettingByLmac(lmac);//删除灯的场景
                    //更新 lname,gid,mid,update_date,pid
                    lightAjustService.updateLight(lmac, groupId, mid, pid);
                } else {
                    //只更新灯名和pid
                    lightAjustService.updateLightName(lmac, lname, pid);
                }
            }
            //记录日志
            lightAjustService.saveLightAjustLog(meshId, bltFlag, operation, lmac);
            model.put("result", ResultDict.SUCCESS.getCode());
            model.put("result_message", ResultDict.SUCCESS.getValue());
        }
    }

    /**
     * 保存默认场景
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
                sceneAjust = adjustBeanUtils.setSceneAjust(sceneId, uid, mid, sname);
                //创建场景
                sceneAjustService.saveScene(sceneAjust);
                SceneLog sceneLog = adjustBeanUtils.setSceneLog(uid, "1", meshId, sceneId);
                sceneAjustService.saveSceneLog(sceneLog);//保存场景日志
            }
        }
        model.put("result", ResultDict.SUCCESS.getCode());
        model.put("result_message", ResultDict.SUCCESS.getValue());
    }

    /**
     * 保存更新场景
     * TODO saveScene
     */
    @RequestMapping(value = "/saveScene", method = RequestMethod.POST)//TODO saveScene
    public void saveScene(@ModelAttribute("decodedParams") JSONObject params,
                          ModelMap model) {
        String meshId = params.getString("meshId");
        Integer sceneId = params.getInteger("sceneId");
        String bltFlag = params.getString("bltFlag");
        String uid = params.getString("uid");
        String sname = params.getString("sname");
        Integer pid = params.getInteger("pid");
        //未连蓝牙
        if ("0".equals(bltFlag)) {
            //记录日志
            sceneAjustService.saveSceneLog(adjustBeanUtils.setSceneLog(uid, bltFlag, meshId, sceneId));
            model.put("result", ResultDict.SUCCESS.getCode());
            model.put("result_message", ResultDict.SUCCESS.getValue());
            return;
        }

        Integer mid = groupOperationService.getMeshSerialNo(meshId, uid);
        Integer sid = groupOperationService.getSceneSerialNo(mid, sceneId, uid);
        //sid 为空判断
        if (sid == null) {
            //创建场景
            SceneAjust sceneAjust = adjustBeanUtils.setSceneAjust(sceneId, uid, mid, sname);
            sceneAjustService.saveScene(sceneAjust);
            sid = sceneAjust.getId();
        }
        JSONArray lightArray = params.getJSONArray("lightList");
        JSONArray groupList = params.getJSONArray("groupList");
        String lmac;
        Integer groupId;
        String productId;
        LightList lightList;
        List<LightSetting> lightSettingList = new ArrayList<>(lightArray.size() + 1);//配置list容量为jsonArray的size()
        String x;
        String y;
        String off;
        Map<String, Integer> lightMap;
        if (groupList != null) {
            if (groupList.size() > 0) {
                //删除group_setting信息
                groupOperationService.deleteGroupSetting(sid);
                //v2.1.0新版本添加groupList集合
                for (int i = 0; i < groupList.size(); i++) {
                    groupId = groupList.getJSONObject(i).getInteger("groupId");
                    x = groupList.getJSONObject(i).getString("x");
                    y = groupList.getJSONObject(i).getString("y");
                    groupOperationService.saveGroupSetting(adjustBeanUtils.setGroupSetting(x, y, sid, mid, groupId));
                }
            }
        }
        if (lightArray.isEmpty() || lightArray.size() < 1) {
            model.put("result", ResultDict.PARAMS_BLANK.getCode());
            model.put("result_message", ResultDict.PARAMS_BLANK.getValue());
            return;
        }

        for (int i = 0; i < lightArray.size(); i++) {
            lmac = lightArray.getJSONObject(i).getString("lmac");
            productId = lightArray.getJSONObject(i).getString("productId");
            groupId = lightArray.getJSONObject(i).getInteger("groupId");
            productId = productId.split(" ")[0];
            lightMap = lightAjustService.getLid(lmac);
            //服务端未找到该灯
            if (lightMap == null || lightMap.size() == 0) {
                //Map<String, Integer> lightMap默认不会分配内存空间需要实例化
                //否则lightMap.put会报空指针
                lightMap = new HashedMap();
                lightList = adjustBeanUtils.setLightList(mid, lmac, groupId, productId, pid);
                //创建灯
                lightAjustService.saveTempLight(lightList);
                lightMap.put("id", lightList.getId());
            } else {
                //服务端有灯
                //mid不一致
                if (lightMap.get("mid").intValue() != mid.intValue()) {
                    lightAjustService.updateLight(lmac, groupId, mid, pid);//更新灯信息
                    sceneAjustService.deleteLightSettingByLmac(lmac);//删除灯的场景信息
                }
            }
            x = lightArray.getJSONObject(i).getString("x");
            y = lightArray.getJSONObject(i).getString("y");
            off = lightArray.getJSONObject(i).getString("off");
            lightSettingList.add(adjustBeanUtils.setLightSetting(sid, lightMap, x, y, off));
        }

        //批量插入到light_setting
        try {
            sceneAjustService.saveLightSetting(lightSettingList);
            SceneLog sceneLog = adjustBeanUtils.setSceneLog(uid, bltFlag, meshId, sceneId);
            sceneAjustService.saveSceneLog(sceneLog);//保存场景日志
            model.put("result", ResultDict.SUCCESS.getCode());
            model.put("result_message", ResultDict.SUCCESS.getValue());
        } catch (SystemAlgorithmException e) {
            model.put("result", ResultDict.SYSTEM_ERROR.getCode());
            model.put("result_message", ResultDict.SYSTEM_ERROR.getValue());
            logger.error("method:saveScene; service:saveLightSetting(); db rollback;sid:{},mid:{}", sid, mid);
        }
    }

    @RequestMapping(value = "/saveScene2", method = RequestMethod.POST)//TODO saveScene
    public void saveScene2(@ModelAttribute("decodedParams") JSONObject params,
                           ModelMap model) {
        String meshId = params.getString("meshId");
        Integer sceneId = params.getInteger("sceneId");
        String bltFlag = params.getString("bltFlag");
        String uid = params.getString("uid");
        //未连蓝牙
        if ("0".equals(bltFlag)) {
            //记录日志
            sceneAjustService.saveSceneLog(adjustBeanUtils.setSceneLog(uid, bltFlag, meshId, sceneId));
            model.put("result_message", ResultDict.SUCCESS.getValue());
            model.put("result", ResultDict.SUCCESS.getCode());
            return;
        }
        try {
            adjustService.saveLightSetting(params);
            model.put("result", ResultDict.SUCCESS.getCode());
            model.put("result_message", ResultDict.SUCCESS.getValue());
        } catch (NotExitException e) {
            model.put("result", ResultDict.PARAMS_BLANK.getCode());
            model.put("result_message", ResultDict.PARAMS_BLANK.getValue());
            logger.error("method:saveScene; service:saveLightSetting(); PARAMS BLANK;sceneId:{},meshId:{}", sceneId,
                    meshId);
        } catch (SystemAlgorithmException e) {
            model.put("result", ResultDict.SYSTEM_ERROR.getCode());
            model.put("result_message", ResultDict.SYSTEM_ERROR.getValue());
            logger.error("method:saveScene; service:saveLightSetting(); db rollback;sceneId:{},meshId:{}", sceneId,
                    meshId);
        }


    }

    /**
     * 删除灯前进行通信
     */
    @RequestMapping(value = "/communicationTest", method = RequestMethod.POST)
    public void communicationTest(@ModelAttribute("decodedParams") JSONObject params, ModelMap model) {
        model.put("result", ResultDict.SUCCESS.getCode());
        model.put("result_message", ResultDict.SUCCESS.getValue());
    }

    /**
     * 添加/删除设备
     * @param params |operation|String|"0":扫描灯，1:删除灯，2:已分组中移除灯,3:组之间移动灯
     *               |bltFlag|String|1:连接蓝牙，0:未连蓝牙|
     *               |productId|Stirng|产品id(产品类型)|
     *               |meshId|String|网络Id|
     *               |groupId|int|组id|
     *               |dGroupId|int|目标组id|
     */
    @RequestMapping(value = "/lightAjust", method = RequestMethod.POST)
    public void lightAjust(@ModelAttribute("decodedParams") JSONObject params, ModelMap model) {
        String operation = params.getString("operation");
        String bltFlag = params.getString("bltFlag");
        String uid = params.getString("uid");
        String meshId = params.getString("meshId");
        String lmac;
        Group group;
        //未连蓝牙
        if ("0".equals(bltFlag)) {
            saveLog(null, meshId, operation, bltFlag, model, null, null);
            return;
        }
        Integer mid = groupOperationService.getMeshSerialNo(meshId, uid);
        group = adjustBeanUtils.setGroup(params);
//        group = new Group();
//        group.setMid(mid);
//        group.setMeshId(meshId);
//        group.setUid(uid);
        List<LightList> lightLists;
        String result;
        String lmacs;
        //连接蓝牙
        if ("1".equals(bltFlag)) {
            //扫描灯
            if ("0".equals(operation)) {
                lightLists = getLightList(model, group, params);
                result = (String) model.get("result");
                if (result != null) {//出現異常
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
            }
            //移动灯，包括组之间移动 已分组移除灯 未分组移动到已分组
            if ("2".equals(operation)) {
                lightLists = getLightList(model, group, params);
                result = (String) model.get("result");
                if (result != null) {
                    //出现异常
                    return;
                }
                try {
                    lightAjustService.updateLightGid(lightLists, meshId, bltFlag, operation);//更新light表
//                    lightAjustService.saveLightAjustLog(meshId, bltFlag, operation, lmacs);//记录日志
                    model.put("result", ResultDict.SUCCESS.getCode());
                    model.put("result_message", ResultDict.SUCCESS.getValue());
                } catch (SystemAlgorithmException e) {
                    model.put("result", ResultDict.SYSTEM_ERROR.getCode());
                    model.put("result_message", ResultDict.SYSTEM_ERROR.getValue());
                    logger.error("method:lightAjust; service:saveLight(); db rollback; meshId:{},groupId:{}",
                            meshId, params.getInteger("dGroupId"));
                }

            }
        }
    }

    @RequestMapping(value = "/lightAjust2", method = RequestMethod.POST)
    public void lightAjust2(@ModelAttribute("decodedParams") JSONObject params, ModelMap model) {
        String operation = params.getString("operation");
        String bltFlag = params.getString("bltFlag");
        String meshId = params.getString("meshId");
        //未连蓝牙
        if ("0".equals(bltFlag)) {
            saveLog(null, meshId, operation, bltFlag, model, null, null);
        } else if ("1".equals(bltFlag)) {//连接蓝牙
            Group group = adjustBeanUtils.setGroup(params);
            try {
                List<LightReturn> lightReturns = adjustService.lightOperation(group, params);
                model.put("result", ResultDict.SUCCESS.getCode());
                model.put("result_message", ResultDict.SUCCESS.getValue());
                if (lightReturns.size() > 0) {
                    model.put("lightLists", lightReturns);
                }
            } catch (SystemAlgorithmException e) {
                model.put("result", ResultDict.SYSTEM_ERROR.getCode());
                model.put("result_message", ResultDict.SYSTEM_ERROR.getValue());
                logger.error("method:lightAjust; service:saveLight(); db rollback; meshId:{},groupId:{}",
                        meshId, params.getInteger("dGroupId"));
            } catch (NotExitException e) {
                System.out.println(e.getMessage());
                model.put("result", ResultDict.PARAMS_BLANK.getCode());
                model.put("result_message", ResultDict.PARAMS_BLANK.getValue());
            }
        }
    }

    /**
     * "0":扫描灯
     * "2":移动灯
     *
     * @return 返回灯集合
     */
    private List<LightList> getLightList(ModelMap model, Group group, JSONObject params) {
        String operation = params.getString("operation");
        String lmac;
        String lname;
        String productId;
//        Integer pid = params.getInteger("pid");//区域序列号
        //扫描灯
        if ("0".equals(operation)) {
            //v2.0 2.1版本默认groupId为0
            if (group.getGroupId() == null) {
                group.setGroupId(0);
            }
        }
        if ("2".equals(operation)) {
            //移动灯
            if (group.getGroupId() == null) {//未找到组
                model.put("result", ResultDict.PARAMS_BLANK.getCode());
                model.put("result_message", ResultDict.PARAMS_BLANK.getValue());
                return null;
            }
        }
        Integer gid = groupOperationService.getGid(group);//目的组的gid
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
            productId = productId.split(" ")[0];
            lightList = new LightList();
            lightList.setGid(gid);
            lightList.setGroupId(group.getGroupId());
            lightList.setLmac(lmac);
            if (StringUtils.isBlank(lname)) {
                lightList.setLname(lmac);
            } else {
                lightList.setLname(lname);
            }
            lightList.setPid(group.getPid());
            lightList.setProductId(productId);
            lightList.setMid(group.getMid());
            lightLists.add(lightList);
        }
        return lightLists;
    }

    /**
     * 未连蓝牙记录日志表
     */
    private void saveLog(String uid, String meshId, String
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
    }

    /**
     * 移动组 v2.2.0
     */
    @RequestMapping(value = "/moveGroup", method = RequestMethod.POST) //TODO moveGroup
    public void moveGroup(@ModelAttribute("decodedParams") JSONObject params, ModelMap model) {
        Map<String, Object> placeMap = groupOperationService.moveGroup(params);
        model.put("result", ResultDict.SUCCESS.getCode());
        model.put("result_message", ResultDict.SUCCESS.getValue());
        model.put("place", placeMap);
    }

    /**
     * 移动灯到不同的组 v2.2.0
     */
    @RequestMapping(value = "/moveLightsToDiffGroups", method = RequestMethod.POST)//TODO moveLightsToDiffGroups
    public void moveLightsToDiffGroups(@ModelAttribute("decodedParams") JSONObject params, ModelMap model) {
        lightAjustService.moveLightsToDiffGroups(params);
        model.put("result", ResultDict.SUCCESS.getCode());
        model.put("result_message", ResultDict.SUCCESS.getValue());
    }

    /**
     * 进入网络更新灯的xy值 v2.2.0
     */
    @RequestMapping(value = "/accessNetUploadLightXY", method = RequestMethod.POST)//TODO accessNetUploadLightXY
    public void accessNetUploadLightXY(@ModelAttribute("decodedParams") JSONObject params, ModelMap model) {
        lightAjustService.updateLightXY(params);
        model.put("result", ResultDict.SUCCESS.getCode());
        model.put("result_message", ResultDict.SUCCESS.getValue());
    }
}
