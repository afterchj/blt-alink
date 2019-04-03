package com.tpadsz.after.service;

import com.tpadsz.after.entity.*;

import java.util.List;
import java.util.Map;

/**
 * @program: blt-alink
 * @description:
 * @author: Mr.Ma
 * @create: 2019-03-06 14:47
 **/
public interface GroupOperationService {

    void saveGroupLog(String uid, String meshId, String operation, String bltFlag, Integer groupId);

    Integer getMeshSerialNo(String meshId,String uid);

    String getGroupIdById(Integer id);

    Integer saveGroup(Group group);

    void updateGroupNameByMid(Group group);

    List<GroupList> getGroupAll(Integer mid);

    Map<String,Object> getLightColor(String lmac);

    Integer getGid(Group group);

    Integer getLightNum(Group group);

    void updateGidInLight(Group group);

//    List<LightList> getLightList(Group group);

    void deleteGroup(Group group);

    Integer getSceneSerialNo(Integer mid, Integer sceneId,String uid);

    GroupConsoleLog getGroupConsoleLogByGid(Integer groupId,String uid,String meshId);

    Integer getPid(Integer placeId, Integer mid);

    void savePlace(AdjustPlace adjustPlace);

//    List<GroupList> getGroups(Integer mid);
}
