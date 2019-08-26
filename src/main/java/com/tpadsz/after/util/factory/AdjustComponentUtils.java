package com.tpadsz.after.util.factory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tpadsz.after.dao.GroupOperationDao;
import com.tpadsz.after.dao.LightAjustDao;
import com.tpadsz.after.dao.SceneAjustDao;
import com.tpadsz.after.entity.Group;
import com.tpadsz.after.entity.LightList;
import com.tpadsz.after.entity.LightReturn;
import com.tpadsz.after.entity.LightSetting;
import com.tpadsz.after.exception.GroupDuplicateException;
import com.tpadsz.after.exception.NameDuplicateException;
import com.tpadsz.after.exception.NotExitException;
import com.tpadsz.after.exception.SystemAlgorithmException;
import com.tpadsz.after.service.LightAjustService;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @program: blt-alink
 * @description:
 * @author: Mr.Ma
 * @create: 2019-08-26 09:37
 **/
@Component
public class AdjustComponentUtils {

    @Resource
    private GroupOperationDao groupOperationDao;

    @Resource
    private LightAjustDao lightAjustDao;

    @Resource
    private LightAjustService lightAjustService;

    @Resource
    private AdjustBeanUtils adjustBeanUtils;

    @Resource
    private SceneAjustDao sceneAjustDao;


    public void createGroup(Group group) throws NameDuplicateException, GroupDuplicateException {
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

    public void renameGroup(Group group) throws NotExitException, NameDuplicateException {
        if (group.getGid() == null) {
            throw new NotExitException("不存在组");
        }
        String gname = groupOperationDao.getGname(group);
        if (gname != null) {
            throw new NameDuplicateException("组名重复");
        }
        groupOperationDao.updateGroupNameByMid(group);
    }

    public void deleteGroup(Group group) throws NotExitException {
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

    //扫描灯
    public List<LightReturn> scanLights(Group group, JSONObject params) throws SystemAlgorithmException,
            NotExitException {
        List<LightList> lightLists = getLightList(group, params);
        String lmacs = lightAjustService.saveLight(lightLists);
        lightAjustDao.saveLightAjustLog(params.getString("meshId"), params.getString("bltFlag"), params.getString
                ("operation"), lmacs);
        return lightAjustDao.getAllByMid(group.getMid());
    }

    public List<LightList> getLightList(Group group, JSONObject params) throws NotExitException {
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

    public void deleteLight(JSONObject params) throws NotExitException, SystemAlgorithmException {
        String lmac = params.getString("lmac");
        lightAjustDao.deleteLight(lmac);
        //删除light_setting中的记录
        sceneAjustDao.deleteLightSettingByLmac(lmac);
        lightAjustDao.saveLightAjustLog(params.getString("meshId"), params.getString("bltFlag"), params.getString
                ("operation"), lmac);
    }

    public void moveLights(Group group, JSONObject params) throws NotExitException, SystemAlgorithmException {
        List<LightList> lightLists = getLightList(group, params);
        lightAjustService.updateLightGid(lightLists, params.getString("meshId"), params.getString("bltFlag"), params
                .getString("operation"));//更新light表
    }

    public void saveGroupSetting(JSONArray groupList, Integer sid, Integer mid) {
        String x;
        String y;
        Integer groupId;
        if (groupList != null && groupList.size() > 0) {
            //删除group_setting信息
            groupOperationDao.deleteGroupSetting(sid);
            //v2.1.0新版本添加groupList集合
            for (int i = 0; i < groupList.size(); i++) {
                groupId = groupList.getJSONObject(i).getInteger("groupId");
                y = groupList.getJSONObject(i).getString("y");
                x = groupList.getJSONObject(i).getString("x");
                groupOperationDao.saveGroupSetting(adjustBeanUtils.setGroupSetting(x, y, sid, mid, groupId));
            }
        }
    }

    public List<LightSetting> setLightSettings(JSONArray lightArray, Integer mid, Integer pid, Integer sid) throws
            NotExitException {
        String x;
        String y;
        String off;
        String lmac;
        Integer groupId;
        String productId;
        Map<String, Integer> lightMap;
        LightList lightList;
        if (lightArray.isEmpty() || lightArray.size() < 1) {
            throw new NotExitException("不存在设备");
        }
        List<LightSetting> lightSettingList = new ArrayList<>(lightArray.size() + 1);//配置list容量为jsonArray的size()
        for (int i = 0; i < lightArray.size(); i++) {
            lmac = lightArray.getJSONObject(i).getString("lmac");
            groupId = lightArray.getJSONObject(i).getInteger("groupId");
            productId = lightArray.getJSONObject(i).getString("productId");
            productId = productId.split(" ")[0];
            lightMap = lightAjustDao.getLid(lmac);
            //服务端未找到该灯
            if (lightMap == null || lightMap.size() == 0) {
                //Map<String, Integer> lightMap默认不会分配内存空间需要实例化
                //否则lightMap.put会报空指针
                lightMap = new HashedMap();
                lightList = adjustBeanUtils.setLightList(mid, lmac, groupId, productId, pid);
                //创建灯
                lightAjustDao.saveTempLight(lightList);
                lightMap.put("id", lightList.getId());
            } else {
                //服务端有灯
                //mid不一致
                if (lightMap.get("mid").intValue() != mid.intValue()) {
                    lightAjustDao.updateLight(lmac, groupId, mid, pid);//更新灯信息
                    sceneAjustDao.deleteLightSettingByLmac(lmac);//删除灯的场景信息
                }
            }
            x = lightArray.getJSONObject(i).getString("x");
            y = lightArray.getJSONObject(i).getString("y");
            off = lightArray.getJSONObject(i).getString("off");
            lightSettingList.add(adjustBeanUtils.setLightSetting(sid, lightMap, x, y, off));
        }
        return lightSettingList;
    }
}
