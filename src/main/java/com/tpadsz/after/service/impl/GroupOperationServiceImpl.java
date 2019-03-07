package com.tpadsz.after.service.impl;

import com.tpadsz.after.dao.GroupOperationDao;
import com.tpadsz.after.entity.Group;
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
    public void saveGroupLog(String uid, String operation, String bltFlag) {
        groupOperationDao.saveGroupLog(uid,operation,bltFlag);
    }


    @Override
    public Integer getMeshSerialNo(String meshId) {
        return groupOperationDao.getMeshSerialNo(meshId);
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
    public void updateGroupNameByGroupId(String groupId, String gname) {
        groupOperationDao.updateGroupNameByGroupId(groupId,gname);
    }

    @Override
    public List<Map<String, Object>> getGroupsByMeshId(String meshId) {
        return groupOperationDao.getGroupsByMeshId(meshId);
    }


}
