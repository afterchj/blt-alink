package com.tpadsz.after.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tpadsz.after.entity.Group;
import com.tpadsz.after.entity.GroupConsoleLog;
import com.tpadsz.after.entity.GroupList;
import com.tpadsz.after.entity.LightList;
import com.tpadsz.after.entity.dd.ResultDict;
import com.tpadsz.after.service.GroupOperationService;
import com.tpadsz.after.service.LightAjustService;
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
            //组内有灯 ??
            if (lightLists.size()>0){
                //最新调光记录
                groupConsoleLog = groupOperationService.getGroupConsoleLogByGid(groupList.getId());
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
        if (StringUtils.isBlank(meshId)||sceneId==null||bltFlag==null||StringUtils.isBlank(uid)){
            model.put("result", ResultDict.PARAMS_BLANK.getCode());
            model.put("result_message",ResultDict.PARAMS_BLANK.getValue());
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
        //sid 为空判断 ??
        List<GroupList> groupLists = groupOperationService.getGroupAll(mid);
        GroupConsoleLog groupConsoleLog;
        List<LightList> lightLists;
        Map<String,Object> map;
        for (GroupList groupList:groupLists){
            //组内有灯
            if (groupList.getLightLists().size()>0){
               groupConsoleLog = groupOperationService.getGroupConsoleLogByGid(groupList.getId());
                //最新调光记录是对灯调光
                if (groupConsoleLog.getLmac()!=null){
                    lightLists = groupList.getLightLists();
                    groupList.setStatus("0");
                    for (LightList lightList:lightLists){
                        map = groupOperationService.getLightColor(lightList.getLmac());
                        lightList.setX((String) map.get("x"));
                        lightList.setY((String) map.get("y"));
                        //保存单灯场景
                        lightAjustService.saveLightSetting(lightList);
                    }
                    continue;
                }
                //最新调光记录是对组调光
                groupList.setStatus("1");
                groupList.setX(groupConsoleLog.getX());
                groupList.setY(groupConsoleLog.getY());
            }
        }
        String x = groupLists.get(0).getX();
        String y = groupLists.get(0).getY();
        for (GroupList groupList:groupLists){
            //对组调光
            if ("1".equals(groupList.getStatus())){

            }
        }

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
