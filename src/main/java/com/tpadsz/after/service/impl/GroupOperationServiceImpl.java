package com.tpadsz.after.service.impl;

import com.tpadsz.after.dao.GroupOperationDao;
import com.tpadsz.after.entity.Group;
import com.tpadsz.after.entity.GroupConsoleLog;
import com.tpadsz.after.entity.GroupList;
import com.tpadsz.after.entity.LightList;
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
    public void saveGroupLog(String uid, String meshId,String operation, String bltFlag) {
        groupOperationDao.saveGroupLog(uid,meshId,operation,bltFlag);
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

    @Override
    public List<LightList> getLightList(Group group) {
        return groupOperationDao.getLightList(group);
    }

    @Override
    public void deleteGroup(Group group) {
        groupOperationDao.deleteGroup(group);
    }

    @Override
    public Integer getSceneSerialNo(Integer mid, Integer sceneId) {
        return groupOperationDao.getSceneSeriaNo(mid, sceneId);
    }

    @Override
    public GroupConsoleLog getGroupConsoleLogByGid(Integer gid) {
        return groupOperationDao.getGroupConsoleLogByGid(gid);
    }

}
