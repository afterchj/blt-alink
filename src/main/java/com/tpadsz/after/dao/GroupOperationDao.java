package com.tpadsz.after.dao;

import com.tpadsz.after.entity.Group;
import com.tpadsz.after.entity.GroupConsoleLog;
import com.tpadsz.after.entity.GroupList;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @program: blt-alink
 * @description: 组操作
 * @author: Mr.Ma
 * @create: 2019-03-06 14:51
 **/
@Repository
public interface GroupOperationDao {

    void saveGroupLog(@Param("uid") String uid,@Param("meshId")String meshId, @Param("operation") String operation, @Param("bltFlag") String bltFlag, @Param("groupId") Integer groupId);//创建组日志

    Integer getMeshSerialNo(@Param("meshId") String meshId, @Param("uid") String uid);//查询mesh序列号

    String getGroupIdById(@Param("id") Integer id);//获取组id

    Integer saveGroup(Group group);//创建组

    void updateGroupNameByMid(Group group);//重命名组名

    List<GroupList> getGroupAll(@Param("mid") Integer mid);//获取所有组的信息

    Map<String,Object> getLightColor(@Param("lmac") String lmac);//根据mac查询灯x，y值

    Integer getGid(Group group);//获取gid

    Integer getLightNum(Group group);//获取组内灯数量

    void updateGidInLight(Group group);//更新灯表gid

//    List<LightList> getLightList(Group group);//查询未分组内灯信息

    void deleteGroup(Group group);//删除组

    Integer getSceneSeriaNo(@Param("mid") Integer mid, @Param("sceneId") Integer sceneId, @Param("uid") String uid);//获取sid

    GroupConsoleLog getGroupConsoleLogByGid(@Param("groupId") Integer groupId,@Param("uid") String uid,@Param("meshId") String meshId);//获取lmac地址

//    List<GroupList> getGroups(@Param("mid") Integer mid);//获取所有组的信息(比getGroupAll更简单，不包含灯的产品类型)
}
