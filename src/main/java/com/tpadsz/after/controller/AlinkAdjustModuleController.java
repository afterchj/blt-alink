package com.tpadsz.after.controller;

import com.alibaba.fastjson.JSONObject;
import com.tpadsz.after.entity.Group;
import com.tpadsz.after.entity.GroupReturn;
import com.tpadsz.after.entity.dd.ResultDict;
import com.tpadsz.after.service.GroupOperationService;
import com.tpadsz.after.service.LightAujstService;
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
 **/
@Controller
public class AlinkAdjustModuleController extends BaseDecodedController {

    @Resource
    private GroupOperationService groupOperationService;

    @Resource
    private LightAujstService lightAujstService;

    /**
     * group操作(创建/删除/重命名组)接口
     * @param params uid,gname,groupId,operate,bltFlag
     * @param model
     */
    @RequestMapping(value = "groupOperation")
    public void groupOperation(@ModelAttribute("decodedParams") JSONObject params, ModelMap model){
        String uid = params.getString("uid");
        String operation = params.getString("operation");//"0":创建组；"1":删除组；"2":重命名组；
        String bltFlag = params.getString("bltFlag");//"1":连接蓝牙；"0":未连蓝牙；
        String groupId = params.getString("groupId");
        String groupId2;
        String gname = params.getString("gname");//组名(重命名组时是重命名后的组名)
        String meshId = params.getString("meshId");//网络id
        Integer mid;
        GroupReturn groupReturn;
        GroupReturn groupReturn2;
        Group group;
        if (StringUtils.isBlank(operation)||StringUtils.isBlank(bltFlag)||StringUtils.isBlank(meshId)||StringUtils.isBlank(uid)||StringUtils.isBlank(gname)){
            model.put("result", ResultDict.PARAMS_BLANK.getCode());
            model.put("result_message", ResultDict.PARAMS_BLANK.getValue());
            return;
        }
        //未连蓝牙
        if (bltFlag.equals("0")){
            groupOperationService.saveGroupLog(uid,operation,bltFlag);
            model.put("result",ResultDict.SUCCESS.getCode());
            model.put("result_message",ResultDict.SUCCESS.getValue());
            return;
        }
        //根据mesh id查询mesh序列号
        mid = groupOperationService.getMeshSerialNo(meshId);
        if (mid==null){
            System.out.println("method:groupOperation"+"mid is null");
            model.put("result",ResultDict.MESHID_NOT_NULL.getCode());
            model.put("result_message",ResultDict.MESHID_NOT_NULL.getValue());
            return;
        }
        //创建组
        if (operation.equals("0")){
            //连接蓝牙
            if (bltFlag.equals("1")){
                group = new Group();
                group.setGname(gname);
                group.setMid(mid);
                group.setGroupId(groupId);
                groupOperationService.saveGroup(group);
                groupOperationService.saveGroupLog(uid,operation,bltFlag);
//                groupId2 = groupOperationService.getGroupIdById(group.getId());
//                groupReturn = new GroupReturn();
//                groupReturn.setUid(uid);
//                groupReturn.setGname(gname);
//                groupReturn.setGroupId(groupId);
//                groupReturn.setMeshId(meshId);
                group.setMeshId(meshId);
                model.put("result",ResultDict.SUCCESS.getCode());
                model.put("result_message",ResultDict.SUCCESS.getValue());
                model.put("data",group);
                return;
            }
        }
        //重命名组
        if (operation.equals("2")){
            groupId = params.getString("groupId");
            if (StringUtils.isBlank(groupId)){
                model.put("result", ResultDict.PARAMS_BLANK.getCode());
                model.put("result_message",ResultDict.PARAMS_BLANK.getValue());
                return;
            }
            groupOperationService.updateGroupNameByGroupId(groupId,gname);
            groupOperationService.saveGroupLog(uid,operation,bltFlag);
            model.put("result",ResultDict.SUCCESS.getCode());
            model.put("result_message",ResultDict.SUCCESS.getValue());
            groupReturn2 = new GroupReturn();
            groupReturn2.setGname(gname);
            groupReturn2.setUid(uid);
            groupReturn2.setGroupId(groupId);
            groupReturn2.setMeshId(meshId);
            model.put("result",ResultDict.SUCCESS.getCode());
            model.put("result_message",ResultDict.SUCCESS.getValue());
            model.put("data",groupReturn2);
            return;
        }

        //删除组
        if (operation.equals("1")){

        }
    }

    /**
     *
     * SELECT
     l.lmac,
     l.lname,
     l.create_date,
     l.update_date,
     p.product_id,
     p.product_name,
     p.irr_eff,
     p.power,
     p.voltage,
     p.current,
     t.tname,
     t.tnum
     FROM
     alink.f_light l,
     alink.f_product p,
     alink.f_type t
     WHERE
     gid = 7
     AND l.product_id = p.product_id
     AND l.type_id = t.id
     * 查看group列表、group内设备列表和信息状态
     * @param params uid,meshId
     * @param model
     */
    @RequestMapping(value = "groupsLists",method = RequestMethod.POST)
    public void groupsLists(@ModelAttribute("decodedParams") JSONObject params, ModelMap model){
        String meshId = params.getString("meshId");
        String uid = params.getString("uid");
        List<Map<String,Object>> groupLists;

        if (StringUtils.isBlank(meshId)||StringUtils.isBlank(uid)){
            model.put("result", ResultDict.PARAMS_BLANK.getCode());
            model.put("result_message",ResultDict.PARAMS_BLANK.getValue());
            return;
        }
        groupLists = groupOperationService.getGroupsByMeshId(meshId);

    }

    /**
     * 重命名灯
     * @param params
     * |lmac|String|灯mac地址|
     *|lname|String|重命名的灯名称|
     *|bltFlag|String|"1":连接蓝牙；"0":未连蓝牙|
     * @param model
     */
    @RequestMapping(value = "renameLight",method = RequestMethod.POST)
    public void renameLight(@ModelAttribute("decodedParams") JSONObject params, ModelMap model){
        String bltFlag = params.getString("bltFlag");
        String lmac = params.getString("lmac");
        String lname = params.getString("lname");
        String operation = "4";
        if (StringUtils.isBlank(bltFlag)||StringUtils.isBlank(lmac)||StringUtils.isBlank(lname)){
            model.put("result", ResultDict.PARAMS_BLANK.getCode());
            model.put("result_message",ResultDict.PARAMS_BLANK.getValue());
            return;
        }
        //未连蓝牙
        if ("0".equals(bltFlag)){
            lightAujstService.saveLightAjust(lmac,bltFlag,lname,operation);
        }
    }

    /**
     * 保存更新场景
     * @param params
     * @param model
     */
    @RequestMapping(value = "saveScene",method = RequestMethod.POST)
    public void saveScene(@ModelAttribute("decodedParams") JSONObject params, ModelMap model){

    }

}
