package com.tpadsz.after.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tpadsz.after.dao.GroupOperationDao;
import com.tpadsz.after.dao.LightAjustDao;
import com.tpadsz.after.dao.SceneAjustDao;
import com.tpadsz.after.entity.Group;
import com.tpadsz.after.entity.LightList;
import com.tpadsz.after.entity.LightReturn;
import com.tpadsz.after.exception.GroupDuplicateException;
import com.tpadsz.after.exception.NameDuplicateException;
import com.tpadsz.after.exception.NotExitException;
import com.tpadsz.after.exception.SystemAlgorithmException;
import com.tpadsz.after.service.AdjustService;
import com.tpadsz.after.service.LightAjustService;
import com.tpadsz.after.util.factory.AdjustBeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

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

    @Resource
    private LightAjustDao lightAjustDao;

    @Resource
    private LightAjustService lightAjustService;

    private final AdjustBeanUtils adjustBeanUtils;

    @Resource
    private SceneAjustDao sceneAjustDao;

    @Autowired
    public AdjustServiceImpl(AdjustBeanUtils adjustBeanUtils) {
        this.adjustBeanUtils = adjustBeanUtils;
    }

    @Override
    public void groupOperation(Group group, String operation) throws NameDuplicateException, GroupDuplicateException,
            NotExitException {
        if ("0".equals(operation)) {//创建组
            createGroup(group);
        } else if ("2".equals(operation)) {//重命名组
            renameGroup(group);
        } else if ("1".equals(operation)) {//删除组
            deleteGroup(group);
        }
    }

    @Override
    public List<LightReturn> lightOperation(Group group, JSONObject params) throws SystemAlgorithmException, NotExitException {
        String operation = params.getString("operation");
        List<LightReturn> lightLists = new ArrayList<>();
        if ("0".equals(operation)) {//扫描灯
            lightLists = scanLights(group,params);
        } else if ("1".equals(operation)) {//删除灯
            deleteLight(params);
        } else if ("2".equals(operation)) {//移动灯，包括组之间移动 已分组移除灯 未分组移动到已分组
            moveLights(group,params);
        }
        return lightLists;
    }

    private void saveGroupSetting(JSONArray groupList){
        if (groupList != null){
            if (groupList.size()>0){

            }
        }
    }

    //扫描灯
    private List<LightReturn> scanLights(Group group, JSONObject params) throws SystemAlgorithmException,
            NotExitException {
        List<LightList> lightLists = getLightList(group, params);
        String lmacs = lightAjustService.saveLight(lightLists);
        lightAjustDao.saveLightAjustLog(params.getString("meshId"), params.getString("bltFlag"), params.getString
                ("operation"), lmacs);
        return lightAjustDao.getAllByMid(group.getMid());
    }

    private void deleteLight(JSONObject params) throws NotExitException, SystemAlgorithmException {
        String lmac = params.getString("lmac");
        lightAjustDao.deleteLight(lmac);
        //删除light_setting中的记录
        sceneAjustDao.deleteLightSettingByLmac(lmac);
        lightAjustDao.saveLightAjustLog(params.getString("meshId"), params.getString("bltFlag"), params.getString
                ("operation"), lmac);
    }

    private void moveLights(Group group, JSONObject params) throws NotExitException, SystemAlgorithmException {
        List<LightList> lightLists = getLightList(group, params);
        lightAjustService.updateLightGid(lightLists, params.getString("meshId"), params.getString("bltFlag"), params
                .getString("operation"));//更新light表
    }


    private List<LightList> getLightList(Group group, JSONObject params) throws NotExitException {
        String operation = params.getString("operation");
        if ("0".equals(operation)) {//扫描灯
            //v2.0 2.1版本默认groupId为0
            if (group.getGroupId() == null) {
                group.setGroupId(0);
            }
        } else if ("2".equals(operation)) {//移动灯
            if (group.getGroupId() == null) {//未找到组
                throw new NotExitException("目标组id不能为空");
            }
        }
        return adjustBeanUtils.setLightList(params, group);
    }

    private void createGroup(Group group) throws NameDuplicateException, GroupDuplicateException {
        String dbGname = groupOperationDao.getGname(group);
        if (StringUtils.isNotBlank(dbGname)) {
            throw new NameDuplicateException("组名重复");
        }
        if (group.getGid() == null) {
            groupOperationDao.saveGroup(group);
        } else {
            throw new GroupDuplicateException("存在组");
        }
    }

    private void renameGroup(Group group) throws NotExitException {
        if (group.getGid() != null) {
            groupOperationDao.updateGroupNameByMid(group);
        } else {
            throw new NotExitException("不存在组");
        }
    }

    private void deleteGroup(Group group) throws NotExitException {
        if (group.getGid() != null) {
            /* TODO 查询组内灯的数量 */
            Integer lightNum = groupOperationDao.getLightNum(group);
            if (lightNum != null) {
                // TODO 存在设备 转移到未分组
                groupOperationDao.updateGidInLight(group);
            }
            // TODO 删除组
            groupOperationDao.deleteGroup(group);
        } else {
            throw new NotExitException("不存在组");
        }
    }
}
