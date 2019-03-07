package com.tpadsz.after.service;

import com.tpadsz.after.entity.Group;

import java.util.Map;

/**
 * @program: blt-alink
 * @description:
 * @author: Mr.Ma
 * @create: 2019-03-06 14:47
 **/
public interface GroupOperationService {

    void saveGroupLog(String uid, String operation, String bltFlag);

    Integer getMeshSerialNo(String meshId);

    String getGroupIdById(Integer id);

    Integer saveGroup(Group group);

    void updateGroupNameByGroupId(String groupId, String gname);

    Map<Integer,String> getGroupsByMeshId(String meshId);
}
