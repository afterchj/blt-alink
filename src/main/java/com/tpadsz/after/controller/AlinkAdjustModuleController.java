package com.tpadsz.after.controller;

import com.alibaba.fastjson.JSONObject;
import com.tpadsz.after.entity.Group;
import com.tpadsz.after.entity.GroupReturn;
import com.tpadsz.after.entity.ResultDict;
import com.tpadsz.after.service.GroupOperationService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

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
        String groupId;
        String groupId2;
        String gname = params.getString("gname");//组名(重命名组时是重命名后的组名)
        String meshId = params.getString("meshId");//网络id
        Integer mid;
        GroupReturn groupReturn;
        GroupReturn groupReturn2;
        Group group;
        if (StringUtils.isBlank(operation)||StringUtils.isBlank(bltFlag)||StringUtils.isBlank(meshId)||StringUtils.isBlank(uid)||StringUtils.isBlank(gname)){
            model.put("result", ResultDict.PARAMS_BLANK.getCode());
            model.put("result_message",ResultDict.PARAMS_BLANK.getValue());
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
                groupOperationService.saveGroup(group);
                groupOperationService.saveGroupLog(uid,operation,bltFlag);
                groupId2 = groupOperationService.getGroupIdById(group.getId());
                groupReturn = new GroupReturn();
                groupReturn.setUid(uid);
                groupReturn.setGname(gname);
                groupReturn.setGroupId(groupId2);
                groupReturn.setMeshId(meshId);
                model.put("result",ResultDict.SUCCESS.getCode());
                model.put("result_message",ResultDict.SUCCESS.getValue());
                model.put("data",groupReturn);
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

}
