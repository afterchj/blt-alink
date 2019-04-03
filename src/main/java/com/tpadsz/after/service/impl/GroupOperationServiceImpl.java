package com.tpadsz.after.service.impl;

import com.tpadsz.after.dao.GroupOperationDao;
import com.tpadsz.after.entity.*;
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
    }

    @Override
    public Integer getSceneSerialNo(Integer mid, Integer sceneId,String uid) {
        return groupOperationDao.getSceneSeriaNo(mid, sceneId, uid);
    }

    @Override
    public GroupConsoleLog getGroupConsoleLogByGid(Integer groupId,String uid,String meshId) {
        return groupOperationDao.getGroupConsoleLogByGid(groupId,uid,meshId);
    }

    @Override
    public Integer getPid(Integer placeId, Integer mid) {
        return groupOperationDao.getPid(placeId,mid);
    }

    @Override
    public void savePlace(AdjustPlace adjustPlace) {
        groupOperationDao.savePlace(adjustPlace);
    }

//    @Override
//    public List<GroupList> getGroups(Integer mid) {
//        return groupOperationDao.getGroups(mid);
//    }
}
