package com.tpadsz.after.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.tpadsz.after.dao.GroupOperationDao;
import com.tpadsz.after.entity.*;
import com.tpadsz.after.exception.GroupDuplicateException;
import com.tpadsz.after.exception.PlaceNotFoundException;
import com.tpadsz.after.service.GroupOperationService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @program: blt-alink
 * @description:
 * @author: Mr.Ma
 * @create: 2019-03-06 14:48
 **/
@Service("groupOperationService")
public class GroupOperationServiceImpl implements GroupOperationService {

    @Resource
    private GroupOperationDao groupOperationDao;

    @Override
    public void saveGroupLog(String uid, String meshId,String operation, String bltFlag, Integer groupId) {
        groupOperationDao.saveGroupLog(uid,meshId,operation,bltFlag, groupId);
    }

    @Override
    public Integer getMeshSerialNo(String meshId,String uid) {
        return groupOperationDao.getMeshSerialNo(meshId, uid);
    }

    @Override
    public String getGroupIdById(Integer id) {
        return groupOperationDao.getGroupIdById(id);
    }

    @Override
    public Integer saveGroup(Group group) {
        return groupOperationDao.saveGroup(group);
    }


    @Override
    public void updateGroupNameByMid(Group group) {
        groupOperationDao.updateGroupNameByMid(group);
    }

    @Override
    public List<GroupList> getGroupAll(Integer mid) {
        return groupOperationDao.getGroupAll(mid);
    }

    @Override
    public Map<String, Object> getLightColor(String lmac) {
        return groupOperationDao.getLightColor(lmac);
    }

    @Override
    public Integer getGid(Group group) {
        return groupOperationDao.getGid(group);
    }

    @Override
    public Integer getLightNum(Group group) {
        return groupOperationDao.getLightNum(group);
    }

    @Override
    public void updateGidInLight(Group group) {
        groupOperationDao.updateGidInLight(group);
    }

//    @Override
//    public List<LightList> getLightList(Group group) {
//        return groupOperationDao.getLightList(group);
//    }

    @Override
    public void deleteGroup(Group group) {
        groupOperationDao.deleteGroup(group);
        groupOperationDao.deleteLight(group);
    }

    @Override
    public Integer getSceneSerialNo(Integer mid, Integer sceneId,String uid) {
        return groupOperationDao.getSceneSeriaNo(mid, sceneId, uid);
    }

    @Override
    public GroupConsoleLog getGroupConsoleLogByGid(Integer groupId,String uid,String meshId) {
        return groupOperationDao.getGroupConsoleLogByGid(groupId,uid,meshId);
    }

//    @Override
//    public Integer getPid(Integer placeId, Integer mid) {
//        return groupOperationDao.getPid(placeId,mid);
//    }

    @Override
    public void savePlace(AdjustPlace adjustPlace) {
        groupOperationDao.savePlace(adjustPlace);
    }

    @Override
    public String getGname(Group group) {
        return groupOperationDao.getGname(group);
    }

    @Override
    public Integer getDefaultPlace(Integer pid,String uid, Integer mid) throws PlaceNotFoundException {
//        if (pid==null){
//            //v2.0版本 获取默认区域
//            pid=1;
//            pid = groupOperationDao.getDefaultPlace(uid,mid);
//            if (pid == null){
//                //未发现默认区域
//                throw new DefaultPlaceNotFoundException();
//            }
//        }

//        if (pid == null){
//            pid = 1;//v2.0版本 获取默认区域
//        }
//        Integer id = groupOperationDao.getPlaceId(pid,uid,mid);
//        if (id == null){
//            //未发现区域
//            throw new PlaceNotFoundException();
//        }
        return pid;
    }

    @Override
    public void saveGroupSetting(GroupSetting groupSetting) {
        groupOperationDao.saveGroupSetting(groupSetting);
    }

    @Override
    public void deleteGroupSetting(Integer sid) {
        groupOperationDao.deleteGroupSetting(sid);
    }

    @Override
    public void moveGroup(JSONObject params) throws GroupDuplicateException, PlaceNotFoundException {
        Integer pid = params.getInteger("pid");//目标区域id
        String meshId = params.getString("meshId");
        String uid = params.getString("uid");
        Integer groupId = params.getInteger("groupId");
        String gname = params.getString("gname");//组名
        pid = groupOperationDao.getPid(pid,meshId);
        if (pid == null){
            throw new PlaceNotFoundException();
        }
        Integer oldPid = groupOperationDao.getPlaceIdByGroup(groupId,meshId);
        if (pid.intValue() != oldPid.intValue()){
            String oldGname = groupOperationDao.getGnameByPidAndMeshId(pid, meshId, gname);
            if (oldGname!=null){
                //组名重复
                throw new GroupDuplicateException();
            }
            //不同区域之间移动
            groupOperationDao.moveGroup(pid,meshId,groupId);
            groupOperationDao.updateLightPid(groupId,pid,meshId);//修改灯信息的pid
        }
    }

//    @Override
//    public List<GroupList> getGroups(Integer mid) {
//        return groupOperationDao.getGroups(mid);
//    }
}
