package com.tpadsz.after.controller;

import com.alibaba.fastjson.JSONObject;
import com.tpadsz.after.entity.Group;
import com.tpadsz.after.entity.LightReturn;
import com.tpadsz.after.entity.dd.ResultDict;
import com.tpadsz.after.exception.GroupDuplicateException;
import com.tpadsz.after.exception.NameDuplicateException;
import com.tpadsz.after.exception.NotExitException;
import com.tpadsz.after.exception.SystemAlgorithmException;
import com.tpadsz.after.service.*;
import com.tpadsz.after.util.UrlUtils;
import com.tpadsz.after.util.factory.AdjustBeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

import static com.tpadsz.after.entity.dd.ResultDict.*;

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
    private AdjustService adjustService;

    private final AdjustBeanUtils adjustBeanUtils;

    private Logger logger = LoggerFactory.getLogger(AlinkAdjustModuleController.class);

    @Autowired//构造器注入
    public AlinkAdjustModuleController(AdjustBeanUtils adjustBeanUtils) {
        this.adjustBeanUtils = adjustBeanUtils;
    }

    /**
     * group操作(创建/删除/重命名组)接口
     * TODO groupOperation
     * @param params uid,gname,groupId,operate,bltFlag
     */
    @RequestMapping(value = "/groupOperation")
    public void groupOperation(@ModelAttribute("decodedParams") JSONObject params, ModelMap model, HttpServletRequest request) {
        String uid = params.getString("uid");
        String operation = params.getString("operation");//"0":创建组；"1":删除组；"2":重命名组；
        String bltFlag = params.getString("bltFlag");//"1":连接蓝牙；"0":未连蓝牙；
        Integer groupId = params.getInteger("groupId");//组id(客户端生成)
        String meshId = params.getString("meshId");//网络id
        String result = ResultDict.SUCCESS.getCode();
        String resultMessage = ResultDict.SUCCESS.getValue();
        if (bltFlag.equals("0")) {//未连蓝牙
            saveLog(uid, meshId, operation, bltFlag, model, groupId, null,request);
        } else if ("1".equals(bltFlag)) {//连接蓝牙
            Group group = adjustBeanUtils.setGroup(params);
            try {
                adjustService.groupOperation(group, operation);
                groupOperationService.saveGroupLog(uid, meshId, operation, bltFlag, groupId);
            } catch (NameDuplicateException e) {//组名重复
                logger.error("method:groupOperation; group name duplicate;groupId:{},meshId:{}", groupId, meshId);
                result = GROUP_NAME_DUPLICATE.getCode();
                resultMessage = GROUP_NAME_DUPLICATE.getValue();
            } catch (GroupDuplicateException e) {//存在组
                logger.error("method:groupOperation; group duplicate;groupId:{},meshId:{}", groupId, meshId);
                result = ResultDict.DUPLICATE_GID.getCode();
                resultMessage = ResultDict.DUPLICATE_GID.getValue();
            } catch (NotExitException e) {//不存在组
                logger.error("method:groupOperation; cannot find the group:{},meshId:{}", groupId, meshId);
                result = ResultDict.NO_GROUP.getCode();
                resultMessage = ResultDict.NO_GROUP.getValue();
            }
            model.put("result", result);
            model.put("result_message", resultMessage);
            model.putAll(UrlUtils.getModelUrl(request));
        }
    }

    /**
     * 查看group列表、group内设备列表和信息状态
     * @param params uid,meshId
     */
    @RequestMapping(value = "/groupsLists", method = RequestMethod.POST)
    public void groupsLists(@ModelAttribute("decodedParams") JSONObject
                                    params, ModelMap model, HttpServletRequest request) {
        String result = ResultDict.SUCCESS.getCode();
        String resultMessage = ResultDict.SUCCESS.getValue();
        try {
            Map<String, Object> map = adjustService.getGroupList(params);
            model.put("data", map.get("data"));
        } catch (NotExitException e) {
            result = MESHID_NOT_NULL.getCode();
            resultMessage = MESHID_NOT_NULL.getValue();
            logger.error("method groupsLists; meshid is null; meshId: {}, uid: {}", params.getString("meshId"),
                    params.getString("uid"));
        }
        model.put("result", result);
        model.put("result_message", resultMessage);
        model.putAll(UrlUtils.getModelUrl(request));
    }

    /**
     * 重命名灯
     * @param params |lmac|String|灯mac地址|
     *               |lname|String|重命名的灯名称|
     *               |bltFlag|String|"1":连接蓝牙；"0":未连蓝牙|
     */
    @RequestMapping(value = "/renameLight", method = RequestMethod.POST)
    public void renameLight(@ModelAttribute("decodedParams") JSONObject
                                    params, ModelMap model, HttpServletRequest request) {
        String bltFlag = params.getString("bltFlag");
        String lmac = params.getString("lmac");
        String meshId = params.getString("meshId");
        String operation = "4";
        if ("0".equals(bltFlag)) {//未连蓝牙
            //记录日志
            saveLog(null, meshId, operation, bltFlag, model, null, lmac, request);
        }else if ("1".equals(bltFlag)){//连接蓝牙
            adjustService.renameLight(params);
            model.put("result", ResultDict.SUCCESS.getCode());
            model.put("result_message", ResultDict.SUCCESS.getValue());
            model.putAll(UrlUtils.getModelUrl(request));
        }
    }

    /**
     * 保存默认场景
     */
    @RequestMapping(value = "/saveDefaultScene", method = RequestMethod.POST)
    public void saveDefaultScene(@ModelAttribute("decodedParams") JSONObject params,
                                 ModelMap model, HttpServletRequest request) {
        String result = ResultDict.SUCCESS.getCode();
        String resultMessage = ResultDict.SUCCESS.getValue();
        try {
            adjustService.saveDefaultScene(params);
        } catch (NotExitException e) {
            result = PARAMS_BLANK.getCode();
            resultMessage = PARAMS_BLANK.getValue();
        }
        model.put("result", result);
        model.put("result_message", resultMessage);
        model.putAll(UrlUtils.getModelUrl(request));
    }

    /**
     * 保存更新场景
     * v2.2.3 轨道灯场景类型 type=1
     * TODO saveScene
     */
    @RequestMapping(value = "/saveScene", method = RequestMethod.POST)//TODO saveScene
    public void saveScene(@ModelAttribute("decodedParams") JSONObject params,
                           ModelMap model, HttpServletRequest request) {
        String meshId = params.getString("meshId");
        Integer sceneId = params.getInteger("sceneId");
        String bltFlag = params.getString("bltFlag");
        String uid = params.getString("uid");
        String result = ResultDict.SUCCESS.getCode();
        String resultMessage = ResultDict.SUCCESS.getValue();
        //未连蓝牙
        if ("0".equals(bltFlag)) {
            //记录日志
            sceneAjustService.saveSceneLog(adjustBeanUtils.setSceneLog(uid, bltFlag, meshId, sceneId));
            model.put("result_message", ResultDict.SUCCESS.getValue());
            model.put("result", ResultDict.SUCCESS.getCode());
            model.putAll(UrlUtils.getModelUrl(request));
            return;
        }
        try {
            adjustService.saveScene(params);
        } catch (NotExitException e) {
            result = PARAMS_BLANK.getCode();
            resultMessage = PARAMS_BLANK.getValue();
            logger.error("method:saveScene; service:saveLightSetting(); PARAMS BLANK;sceneId:{},meshId:{}", sceneId,
                    meshId);
        } catch (SystemAlgorithmException e) {
            result = SYSTEM_ERROR.getCode();
            resultMessage = SYSTEM_ERROR.getValue();
            logger.error("method:saveScene; service:saveLightSetting(); db rollback;sceneId:{},meshId:{}", sceneId,
                    meshId);
        }
        model.put("result", result);
        model.put("result_message", resultMessage);
        model.putAll(UrlUtils.getModelUrl(request));
    }

    /**
     * 删除灯前进行通信
     */
    @RequestMapping(value = "/communicationTest", method = RequestMethod.POST)
    public void communicationTest(@ModelAttribute("decodedParams") JSONObject params, ModelMap model, HttpServletRequest request) {
        model.put("result", ResultDict.SUCCESS.getCode());
        model.put("result_message", ResultDict.SUCCESS.getValue());
        model.putAll(UrlUtils.getModelUrl(request));
    }

    /**
     * 添加/删除设备
     * @param params operation:"0":扫描灯，1:删除灯，2:已分组中移除灯,3:组之间移动灯;
     *               bltFlag:1:连接蓝牙，0:未连蓝牙;productId:产品id(产品类型);
     *               meshId:网络Id;groupId:组id;dGroupId:目标组id
     */
    @RequestMapping(value = "/lightAjust", method = RequestMethod.POST)
    public void lightAjust(@ModelAttribute("decodedParams") JSONObject params, ModelMap model, HttpServletRequest request) {
        String operation = params.getString("operation");
        String bltFlag = params.getString("bltFlag");
        String meshId = params.getString("meshId");
        String result = ResultDict.SUCCESS.getCode();
        String resultMessage = ResultDict.SUCCESS.getValue();
        //未连蓝牙
        if ("0".equals(bltFlag)) {
            saveLog(null, meshId, operation, bltFlag, model, null, null,request);
        } else if ("1".equals(bltFlag)) {//连接蓝牙
            Group group = adjustBeanUtils.setGroup(params);
            try {
                List<LightReturn> lightReturns = adjustService.lightOperation(group, params);
                if (lightReturns.size() > 0) {
                    model.put("lightLists", lightReturns);
                }
            } catch (SystemAlgorithmException e) {
                result = SYSTEM_ERROR.getCode();
                resultMessage = SYSTEM_ERROR.getValue();
                logger.error("method:lightAjust; db rollback; meshId:{},groupId:{}",
                        meshId, params.getInteger("dGroupId"));
            } catch (NotExitException e) {
                result = PARAMS_BLANK.getCode();
                resultMessage = PARAMS_BLANK.getValue();
                logger.error("method:lightAjust;AdjustComponentUtils.getLightList; 目标组id不能为空; meshId:{},groupId:{}",
                        meshId, params.getInteger("dGroupId"));
            }
            model.put("result", result);
            model.put("result_message", resultMessage);
            model.putAll(UrlUtils.getModelUrl(request));
        }
    }

    /**
     * 未连蓝牙记录日志表
     */
    private void saveLog(String uid, String meshId, String
            operation, String bltFlag, ModelMap model, Integer groupId, String lmacs, HttpServletRequest request) {
        if (uid != null) {
            groupOperationService.saveGroupLog(uid, meshId, operation, bltFlag, groupId);
            model.put("result", ResultDict.SUCCESS.getCode());
            model.put("result_message", ResultDict.SUCCESS.getValue());
            model.putAll(UrlUtils.getModelUrl(request));
            return;
        }
        lightAjustService.saveLightAjustLog(meshId, bltFlag, operation, lmacs);
        model.put("result", ResultDict.SUCCESS.getCode());
        model.put("result_message", ResultDict.SUCCESS.getValue());
        model.putAll(UrlUtils.getModelUrl(request));
    }

    /**
     * 移动组 v2.2.0
     */
    @RequestMapping(value = "/moveGroup", method = RequestMethod.POST) //TODO moveGroup
    public void moveGroup(@ModelAttribute("decodedParams") JSONObject params, ModelMap model, HttpServletRequest request) {
        Map<String, Object> placeMap = groupOperationService.moveGroup(params);
        model.put("result", ResultDict.SUCCESS.getCode());
        model.put("result_message", ResultDict.SUCCESS.getValue());
        model.put("place", placeMap);
        model.putAll(UrlUtils.getModelUrl(request));
    }

    /**
     * 移动灯到不同的组 v2.2.0
     */
    @RequestMapping(value = "/moveLightsToDiffGroups", method = RequestMethod.POST)//TODO moveLightsToDiffGroups
    public void moveLightsToDiffGroups(@ModelAttribute("decodedParams") JSONObject params, ModelMap model, HttpServletRequest request) {
        lightAjustService.moveLightsToDiffGroups(params);
        model.put("result", ResultDict.SUCCESS.getCode());
        model.put("result_message", ResultDict.SUCCESS.getValue());
        model.putAll(UrlUtils.getModelUrl(request));
    }

    /**
     * 进入网络更新灯的xy值 v2.2.0
     * 更新轨道灯值 v2.2.3
     */
    @RequestMapping(value = "/accessNetUploadLightXY", method = RequestMethod.POST)//TODO accessNetUploadLightXY
    public void accessNetUploadLightXY(@ModelAttribute("decodedParams") JSONObject params, ModelMap model, HttpServletRequest request) {
        String result = ResultDict.SUCCESS.getCode();
        String resultMessage = ResultDict.SUCCESS.getValue();
        try {
            lightAjustService.updateLightXY(params);
        } catch (NotExitException e) {
            result = ResultDict.NO_LIGHT.getCode();
            resultMessage = ResultDict.NO_LIGHT.getValue();
        }
        model.put("result", result);
        model.put("result_message", resultMessage);
        model.putAll(UrlUtils.getModelUrl(request));
    }
}
