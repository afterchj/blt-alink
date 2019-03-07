package com.tpadsz.after.dao;

import com.tpadsz.after.entity.Group;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @program: blt-alink
 * @description: 组操作
 * @author: Mr.Ma
 * @create: 2019-03-06 14:51
 **/
public interface GroupOperationDao {

    void saveGroupLog(@Param("uid") String uid, @Param("operation") String operation, @Param("bltFlag") String bltFlag);//创建组日志

    Integer getMeshSerialNo(@Param("meshId") String meshId);//查询mesh序列号
    //获取组id
    String getGroupIdById(@Param("id") Integer id);

    Integer saveGroup(Group group);//创建组
    //重命名组名
    void updateGroupNameByGroupId(@Param("groupId") String groupId, @Param("gname") String gname);

    List<Map<String,Object>> getGroupsByMeshId(String meshId);
}
