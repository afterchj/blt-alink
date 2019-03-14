package com.tpadsz.after.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tpadsz.after.entity.*;
import com.tpadsz.after.entity.dd.ResultDict;
import com.tpadsz.after.service.GroupOperationService;
import com.tpadsz.after.service.LightAjustService;
import com.tpadsz.after.service.SceneAjustService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
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

    /**
     * group操作(创建/删除/重命名组)接口
     * @param params uid,gname,groupId,operate,bltFlag
     * @param model
     */
    @RequestMapping(value = "/groupOperation")
    public void groupOperation(@ModelAttribute("decodedParams") JSONObject params, ModelMap model){
        String uid = params.getString("uid");
        String operation = params.getString("operation");//"0":创建组；"1":删除组；"2":重命名组；
        String bltFlag = params.getString("bltFlag");//"1":连接蓝牙；"0":未连蓝牙；
        Integer groupId = params.getInteger("groupId");//组id(客户端生成)
        String gname = params.getString("gname");//组名(重命名组时是重命名后的组名)
        String meshId = params.getString("meshId");//网络id
        Integer mid;
        Group group;
        if (StringUtils.isBlank(operation)||StringUtils.isBlank(bltFlag)||StringUtils.isBlank(meshId)||StringUtils.isBlank(uid)||StringUtils.isBlank(gname)||groupId==null){
            model.put("result", ResultDict.PARAMS_BLANK.getCode());
            model.put("result_message", ResultDict.PARAMS_BLANK.getValue());
            return;
        }
        //未连蓝牙
        if (bltFlag.equals("0")){
//            groupOperationService.saveGroupLog(uid,meshId,operation,bltFlag);
//            model.put("result",ResultDict.SUCCESS.getCode());
//            model.put("result_message",ResultDict.SUCCESS.getValue());
            saveLog(uid,meshId,null,operation,bltFlag,model);
            return;
        }
        //根据mesh id查询mesh序列号
        mid = groupOperationService.getMeshSerialNo(meshId,uid);
        if (mid==null){
            System.out.println("method:groupOperation"+"mid is null");
            model.put("result",ResultDict.MESHID_NOT_NULL.getCode());
            model.put("result_message",ResultDict.MESHID_NOT_NULL.getValue());
            return;
        }
        group = new Group();
        group.setGname(gname);
        group.setMid(mid);
        group.setGroupId(groupId);
        group.setUid(uid);
        group.setMeshId(meshId);
        //连接蓝牙
        if ("1".equals(bltFlag)){
            //创建组
            if ("0".equals(operation)){
                //连接蓝牙
                groupOperationService.saveGroup(group);
                groupOperationService.saveGroupLog(uid,meshId,operation,bltFlag);
                model.put("result",ResultDict.SUCCESS.getCode());
                model.put("result_message",ResultDict.SUCCESS.getValue());
//                model.put("data",group);
                return;
            }
            //重命名组
            if ("2".equals(operation)){
                groupOperationService.updateGroupNameByMid(group);
                groupOperationService.saveGroupLog(uid,meshId,operation,bltFlag);
                model.put("result",ResultDict.SUCCESS.getCode());
                model.put("result_message",ResultDict.SUCCESS.getValue());
//                model.put("data",group);
                return;
            }
            //删除组
            if ("1".equals(operation)){
                //查询gid
                Integer gid = groupOperationService.getGid(group);
                group.setGid(gid);
                //查询组内灯的数量
                Integer lightNum = groupOperationService.getLightNum(group);
//                List<LightList> lightLists = new ArrayList<>();
                //组内有灯
                if (lightNum!=null){
                    //移动组中的灯到未分组中
                    groupOperationService.updateGidInLight(group);
                    //返回未分组内的灯信息
//                     lightLists = groupOperationService.getLightList(group);
                }
                //删除组表
                groupOperationService.deleteGroup(group);
                groupOperationService.saveGroupLog(uid,meshId,operation,bltFlag);
                model.put("result",ResultDict.SUCCESS.getCode());
                model.put("result_message",ResultDict.SUCCESS.getValue());
//                model.put("data",lightLists);
                return;
            }
        }

    }

    /**
     * 查看group列表、group内设备列表和信息状态
     * @param params uid,meshId
     * @param model
     */
    @RequestMapping(value = "/groupsLists",method = RequestMethod.POST)
    public void groupsLists(@ModelAttribute("decodedParams") JSONObject params, ModelMap model){
        String meshId = params.getString("meshId");
        String uid = params.getString("uid");
        if (StringUtils.isBlank(meshId)||StringUtils.isBlank(uid)){
            model.put("result", ResultDict.PARAMS_BLANK.getCode());
            model.put("result_message",ResultDict.PARAMS_BLANK.getValue());
            return;
        }
        Integer mid = groupOperationService.getMeshSerialNo(meshId,uid);
        if (mid==null){
            model.put("result",ResultDict.MESHID_NOT_NULL.getCode());
            model.put("result_message",ResultDict.MESHID_NOT_NULL.getValue());
            System.out.println("method:groupsLists"+"mid is null");
            return;
        }
        List<GroupList> groupLists = groupOperationService.getGroupAll(mid);
        String lmac;
        GroupConsoleLog groupConsoleLog;
        List<LightList> lightLists;
        Map<String,Object> map;
        for (GroupList groupList:groupLists){
            groupList.setMeshId(meshId);
            lightLists = groupList.getLightLists();
            //组内有灯
            if (lightLists.size()>0){
                //最新调光记录
                groupConsoleLog = groupOperationService.getGroupConsoleLogByGid(groupList.getGroupId(),uid,meshId);
                //最新调光记录是对灯调光
                if (groupConsoleLog.getLmac()!=null){
                    for (LightList lightList:lightLists){
                        lmac = lightList.getLmac();
                        map = groupOperationService.getLightColor(lmac);
                        //灯日志表有数据 mybatis map判空使用null
//                        if (map!=null){
                            lightList.setX((String) map.get("x"));
                            lightList.setY((String) map.get("y"));
//                        }
                    }
                }
                //最新调光记录是对组调光 lmac == null
                groupList.setX(groupConsoleLog.getX());
                groupList.setY(groupConsoleLog.getY());
            }

        }
        model.put("result",ResultDict.SUCCESS.getCode());
        model.put("result_message",ResultDict.SUCCESS.getValue());
        model.put("data",groupLists);
        return;
    }


    /**
     * 重命名灯
     * @param params
     * |lmac|String|灯mac地址|
     *|lname|String|重命名的灯名称|
     *|bltFlag|String|"1":连接蓝牙；"0":未连蓝牙|
     * @param model
     */
    @RequestMapping(value = "/renameLight",method = RequestMethod.POST)
    public void renameLight(@ModelAttribute("decodedParams") JSONObject params, ModelMap model){
        String bltFlag = params.getString("bltFlag");
        String lmac = params.getString("lmac");
        String lname = params.getString("lname");
        String meshId = params.getString("meshId");
        final String operation = "4";
        if (StringUtils.isBlank(bltFlag)||StringUtils.isBlank(lmac)||StringUtils.isBlank(lname)){
            model.put("result", ResultDict.PARAMS_BLANK.getCode());
            model.put("result_message",ResultDict.PARAMS_BLANK.getValue());
            return;
        }
        //未连蓝牙
        if ("0".equals(bltFlag)){
            //记录日志
//            lightAjustService.saveLightAjustLog(meshId,lmac,bltFlag,operation);
//            model.put("result",ResultDict.SUCCESS.getCode());
//            model.put("result_message",ResultDict.SUCCESS.getValue());
            saveLog(null,meshId,lmac,operation,bltFlag,model);
            return;
        }
        //连接蓝牙
        if ("1".equals(bltFlag)){
            lightAjustService.updateLightName(lmac,lname);
            //记录日志
            lightAjustService.saveLightAjustLog(meshId,lmac,bltFlag,operation);
            model.put("result",ResultDict.SUCCESS.getCode());
            model.put("result_message",ResultDict.SUCCESS.getValue());
            return;
        }
    }

    /**
     * 保存场景设置
     * @param groupLists meshId中组和灯集合
     */
    public void saveSceneSetting(List<GroupList> groupLists ){

    }
    /**
     * 保存更新场景
     * @param params
     * @param model
     */
    @RequestMapping(value = "/saveScene",method = RequestMethod.POST)
    public void saveScene(@ModelAttribute("decodedParams") JSONObject params, ModelMap model){
        String meshId = params.getString("meshId");
        Integer sceneId = params.getInteger("sceneId");
        String bltFlag = params.getString("bltFlag");
        String uid = params.getString("uid");
        String sname = params.getString("sname");
        if (StringUtils.isBlank(meshId)||sceneId==null||bltFlag==null||StringUtils.isBlank(uid)){
            model.put("result", ResultDict.PARAMS_BLANK.getCode());
            model.put("result_message",ResultDict.PARAMS_BLANK.getValue());
            return;
        }

        //未连蓝牙
        if ("0".equals(bltFlag)){
            SceneLog sceneLog = new SceneLog();
            sceneLog.setUid(uid);
            sceneLog.setBltFlag(bltFlag);
            sceneLog.setMeshId(meshId);
            sceneLog.setOperation("0");
            sceneLog.setSceneId(sceneId);
            //记录日志
            sceneAjustService.saveSceneLog(sceneLog);
            model.put("result",ResultDict.SUCCESS.getCode());
            model.put("result_message",ResultDict.SUCCESS.getValue());
            return;
        }
        Integer mid = groupOperationService.getMeshSerialNo(meshId, uid);
        Integer sid = groupOperationService.getSceneSerialNo(mid, sceneId);
        if (mid==null){
            model.put("result",ResultDict.MESHID_NOT_NULL.getCode());
            model.put("result_message",ResultDict.MESHID_NOT_NULL.getValue());
            System.out.println("method:saveScene"+"mid is null");
            return;
        }
        //sid 为空判断
        if (sid==null){
            SceneAjust sceneAjust = new SceneAjust();
            sceneAjust.setSceneId(sceneId);
            sceneAjust.setUid(uid);
            sceneAjust.setMid(mid);
            sceneAjust.setSname(sname);
            //创建场景
            sceneAjustService.saveScene(sceneAjust);
            sid = sceneAjust.getId();
        }
        List<GroupList> groupLists = groupOperationService.getGroupAll(mid);
        GroupConsoleLog groupConsoleLog;
        List<LightList> lightLists;
        Map<String,Object> map;
        LightSetting lightSetting;
        GroupSetting groupSetting;
        for (GroupList groupList:groupLists){
            //组内有灯
            if (groupList.getLightLists().size()>0){
               groupConsoleLog = groupOperationService.getGroupConsoleLogByGid(groupList.getGroupId(),uid,meshId);
                //最新调光记录是对灯调光
                if (groupConsoleLog.getLmac()!=null){
                    groupList.setX("-1");
                    groupList.setY("-1%");
                    lightLists = groupList.getLightLists();
                    for (LightList lightList:lightLists){
                        map = groupOperationService.getLightColor(lightList.getLmac());
                        lightSetting = new LightSetting();
                        lightSetting.setX((String) map.get("x"));
                        lightSetting.setY((String) map.get("y"));
                        lightSetting.setLid(lightList.getLid());
                        lightSetting.setSid(sid);
                        //保存单灯场景
                        sceneAjustService.saveLightSetting(lightSetting);
                    }
                    continue;
                }
                //最新调光记录是对组调光
                groupList.setX(groupConsoleLog.getX());
                groupList.setY(groupConsoleLog.getY());
                groupSetting = new GroupSetting();
                groupSetting.setMid(mid);
                groupSetting.setGid(groupList.getId());
                groupSetting.setSid(sid);
                groupSetting.setX(groupConsoleLog.getX());
                groupSetting.setY(groupConsoleLog.getY());
                //保存单组场景
                sceneAjustService.saveGroupSetting(groupSetting);
            }
        }
        int status=0;
        String x = groupLists.get(0).getX();
        String y = groupLists.get(0).getY();
        for (GroupList groupList:groupLists){
            //组内有灯
            if (groupList.getLightLists().size()>0){
                //该组xy值和第1组xy值不相同或者x值为"-1"或者y值为"-1%"
                if (!x.equals(groupList.getX())||!y.equals(groupList.getY())||"-1".equals(groupList.getX())||"-1%".equals(groupList.getY())){
                    status=1;
                    break;
                }
            }
            continue;
        }
        //最新调光记录是对全组调光
        if (0==status){
            SceneSetting sceneSetting = new SceneSetting();
            sceneSetting.setSid(sid);
            sceneSetting.setX(x);
            sceneSetting.setY(y);
            //记录到scene_setting表中
            sceneAjustService.saveSceneSetting(sceneSetting);
        }
        SceneLog sceneLog = new SceneLog();
        sceneLog.setOperation("0");
        sceneLog.setUid(uid);
        sceneLog.setMeshId(meshId);
        sceneLog.setBltFlag(bltFlag);
        sceneLog.setSceneId(sceneId);
        sceneAjustService.saveSceneLog(sceneLog);
        model.put("result",ResultDict.SUCCESS.getCode());
        model.put("result_message",ResultDict.SUCCESS.getValue());
        return;

    }

    /**
     * 添加/删除设备
     * @param params
     * |operation|String|"0":扫描灯，1:删除灯，2:已分组中移除灯,3:组之间移动灯
     * |bltFlag|String|1:连接蓝牙，0:未连蓝牙|
     * |productId|Stirng|产品id(产品类型)|
     *|meshId|String|网络Id|
    |groupId|String|组id|
    |dGroupId|String|目标组id|
     * @param model
     */
    @RequestMapping(value = "/lightAjust",method = RequestMethod.POST)
    public void lightAjust(@ModelAttribute("decodedParams") JSONObject params, ModelMap model){
        String operation = params.getString("operation");
        String bltFlag = params.getString("bltFlag");
        String uid = params.getString("uid");
        String lmac = null;
        String lname ;
        String productId;//产品类型
        String meshId = params.getString("meshId");
        Group group;
        Integer gid;
        Integer groupId;
        Integer dGroupId;
        if (StringUtils.isBlank(operation)||StringUtils.isBlank(bltFlag)||StringUtils.isBlank(meshId)||StringUtils.isBlank(uid)){
            model.put("result", ResultDict.PARAMS_BLANK.getCode());
            model.put("result_message",ResultDict.PARAMS_BLANK.getValue());
            return;
        }
        //未连蓝牙
        if ("0".equals(bltFlag)){
            saveLog(null,meshId,lmac,operation,bltFlag,model);
            return;
        }

        Integer mid = groupOperationService.getMeshSerialNo(meshId, uid);
        if (mid==null){
            model.put("result",ResultDict.MESHID_NOT_NULL.getCode());
            model.put("result_message",ResultDict.MESHID_NOT_NULL.getValue());
            System.out.println("method:lightAjust"+"mid is null");
            return;
        }
        group = new Group();
        group.setMid(mid);
        group.setMeshId(meshId);
        //连接蓝牙
        if ("1".equals(bltFlag)){
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
                LightList lightList;
                for (int i = 0; i < array.size(); i++) {
                    lightList = new LightList();
                    lmac = array.getJSONObject(i).getString("lmac");
                    lname = array.getJSONObject(i).getString("lname");
                    productId = array.getJSONObject(i).getString("productId");
                    if (StringUtils.isBlank(lmac) || StringUtils.isBlank(lname) || StringUtils.isBlank(productId)) {
                        model.put("result", ResultDict.PARAMS_BLANK.getCode());
                        model.put("result_message", ResultDict.PARAMS_BLANK.getValue());
                        return;
                    }
                    lightList.setLmac(lmac);
                    lightList.setLname(lname);
                    lightList.setMid(mid);
                    lightList.setGid(gid);
                    lightList.setProductId(productId);
                    lightAjustService.saveLight(lightList);
                    //记录灯色温
                    //记录日志
//                    lightAjustService.saveLightAjustLog(meshId,lmac,bltFlag,operation);
                }
                lightAjustService.saveLightAjustLog(meshId,null,bltFlag,operation);
                model.put("result", ResultDict.SUCCESS.getCode());
                model.put("result_message", ResultDict.SUCCESS.getValue());
                return;
            }
                //删除灯
                if ("1".equals(operation)){
                    group.setGroupId(0);//未分组的组id
                    gid = groupOperationService.getGid(group);//未分组的gid
                    group.setGid(gid);
                    lmac = params.getString("lmac");
                    if (StringUtils.isBlank(lmac)){
                        model.put("result", ResultDict.PARAMS_BLANK.getCode());
                        model.put("result_message",ResultDict.PARAMS_BLANK.getValue());
                        return;
                    }
                    lightAjustService.deleteLight(lmac);//删除
                    lightAjustService.saveLightAjustLog(meshId,lmac,bltFlag,operation);//记录日志
                    model.put("result",ResultDict.SUCCESS.getCode());
                    model.put("result_message",ResultDict.SUCCESS.getValue());
                    return;
                }
            groupId = params.getInteger("groupId");
            lmac = params.getString("lmac");
            dGroupId = params.getInteger("dGroupId");
                //已分组中移除灯 移除到未分组中
                if ("2".equals(operation)){
                    moveLight(model,groupId,dGroupId,lmac,meshId,bltFlag,operation,mid);
                    return;
                }
                if ("3".equals(operation)){
//                    groupId = params.getInteger("groupId");
//                    lmac = params.getString("lmac");
//                    dGroupId = params.getInteger("dGroupId");
                    moveLight(model,groupId,dGroupId,lmac,meshId,bltFlag,operation,mid);
                    return;
                }
            }
        }


    /**
     *2:已分组中移除灯,3:组之间移动灯
      * @param model 返回给客户端的相应码
     * @param groupId 当前组的组id
     * @param dGroupId 需要移动到目的组的组id
     * @param lmac 灯的mac地址
     */
   public void moveLight(ModelMap model,Integer groupId,Integer dGroupId,String lmac,String meshId,String bltFlag,String operation,Integer mid){
       Group group = new Group();
       group.setMeshId(meshId);
       group.setMid(mid);
       if (groupId==null||StringUtils.isBlank(lmac)){
           model.put("result", ResultDict.PARAMS_BLANK.getCode());
           model.put("result_message",ResultDict.PARAMS_BLANK.getValue());
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
       lightAjustService.saveLightAjustLog(meshId,lmac,bltFlag,operation);//记录日志
       LightList lightList1 = new LightList();
       lightList1.setLmac(lmac);
       lightList1.setGroupId(dGroupId);
       lightList1.setGid(dgid);
       model.put("result",ResultDict.SUCCESS.getCode());
       model.put("result_message",ResultDict.SUCCESS.getValue());
       model.put("data",lightList1);
       return;
   }

    /**
     * 未连蓝牙记录日志表
     * @param uid
     * @param meshId
     * @param operation
     * @param bltFlag
     * @param model
     */
    public void saveLog(String uid, String meshId, String lmac,String operation, String bltFlag, ModelMap model){
            if (uid!=null){
                groupOperationService.saveGroupLog(uid,meshId,operation,bltFlag);
                model.put("result",ResultDict.SUCCESS.getCode());
                model.put("result_message",ResultDict.SUCCESS.getValue());
                return;
            }
            lightAjustService.saveLightAjustLog(meshId,lmac,bltFlag,operation);
            model.put("result",ResultDict.SUCCESS.getCode());
            model.put("result_message",ResultDict.SUCCESS.getValue());
            return;
    }

}
