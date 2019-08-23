package com.tpadsz.after.util.factory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tpadsz.after.entity.*;
import com.tpadsz.after.exception.NotExitException;
import com.tpadsz.after.service.GroupOperationService;
import com.tpadsz.after.service.PlaceService;
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
 * @create: 2019-08-23 11:09
 **/
@Component
public class AdjustBeanUtils {

    @Resource
    private GroupOperationService groupOperationService;

    @Resource
    private PlaceService placeService;

    public Group setGroup(JSONObject params) {
        String uid = params.getString("uid");
        Integer groupId = params.getInteger("groupId");//组id(客户端生成)
        String gname = params.getString("gname");//组名(重命名组时是重命名后的组名)
        String meshId = params.getString("meshId");//网络id
        Integer pid = params.getInteger("pid");
        Integer dGroupId = params.getInteger("dGroupId");
        Integer mid = groupOperationService.getMeshSerialNo(meshId, uid);
        Group group = new Group();
        if (pid != null) {
            group.setPid(pid);
        }
        if (StringUtils.isNotBlank(gname)){
            group.setGname(gname);
        }
        group.setMid(mid);
        if (groupId != null){
            group.setGroupId(groupId);
        }
        if (dGroupId != null){
            group.setGroupId(dGroupId);//存在dGroup是对灯操作
            pid = placeService.getPlaceByGroupIdAndMeshId(dGroupId,meshId);
            group.setPid(pid);//移动灯后目标组的pid
        }
        group.setUid(uid);
        group.setMeshId(meshId);
        Integer gid = groupOperationService.getGid(group);
        if (gid != null) {
            group.setGid(gid);
        }
        return group;
    }

    public SceneLog setSceneLog(String uid, String bltFlag, String meshId, Integer sceneId){
        SceneLog sceneLog = new SceneLog();
        sceneLog.setUid(uid);
        sceneLog.setBltFlag(bltFlag);
        sceneLog.setMeshId(meshId);
        sceneLog.setOperation("0");
        sceneLog.setSceneId(sceneId);
        return sceneLog;
    }

    public SceneAjust setSceneAjust(Integer sceneId, String uid, Integer mid, String sname){
        SceneAjust sceneAjust = new SceneAjust();
        sceneAjust.setSceneId(sceneId);
        sceneAjust.setUid(uid);
        sceneAjust.setMid(mid);
        sceneAjust.setSname(sname);
        return sceneAjust;
    }

    public GroupSetting setGroupSetting(String x, String y, Integer sid, Integer mid, Integer groupId){
        GroupSetting groupSetting = new GroupSetting();
        groupSetting.setX(x);
        groupSetting.setY(y);
        groupSetting.setSid(sid);
        groupSetting.setMid(mid);
        groupSetting.setGroupId(groupId);
        return groupSetting;
    }

    public LightList setLightList(Integer mid, String lmac, Integer groupId, String productId, Integer pid){
        LightList lightList = new LightList();
        lightList.setMid(mid);
        lightList.setLmac(lmac);
        lightList.setLname(lmac);
        lightList.setGroupId(groupId);
        lightList.setProductId(productId);
        lightList.setPid(pid);
        return lightList;
    }

    public LightSetting setLightSetting(Integer sid, Map<String, Integer> lightMap, String x, String y, String off){
        LightSetting lightSetting = new LightSetting();
        lightSetting.setSid(sid);
        lightSetting.setLid(lightMap.get("id"));
        lightSetting.setX(x);
        lightSetting.setY(y);
        lightSetting.setOff(off);
        return lightSetting;
    }

    public List<LightList> setLightList(JSONObject params, Group group) throws NotExitException {
        String lmac;
        String lname;
        String productId;
        JSONArray array = params.getJSONArray("lightGroup");
        if (array.isEmpty() || array.size() < 1) {
            //参数中没有灯的信息
            throw new NotExitException("不存在灯信息");
        }
        List<LightList> lightLists = new ArrayList<>(array.size() + 1);//配置list容量为jsonarray的size()
        LightList lightList;
        for (int i = 0; i < array.size(); i++) {
            lmac = array.getJSONObject(i).getString("lmac");
            lname = array.getJSONObject(i).getString("lname");
            productId = array.getJSONObject(i).getString("productId");
            productId = productId.split(" ")[0];
            lightList = new LightList();
            lightList.setGid(group.getGid());
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
}
