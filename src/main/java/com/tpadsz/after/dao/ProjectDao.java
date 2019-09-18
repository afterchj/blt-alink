package com.tpadsz.after.dao;

import com.tpadsz.after.entity.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by chenhao.lu on 2019/3/12.
 */
public interface ProjectDao {

    Mesh findRepeatIdByUid(@Param("mesh_id")String meshId, @Param("uid")String uid);

    int findFullyRepeatIdByUid(@Param("mesh_id")String meshId);

    int createMesh(Mesh mesh);

    String findMeshId(@Param("limitNum")int limitNum);

    void deleteMeshId(@Param("mesh_id")String meshId);

    void recordMeshId(@Param("mesh_id")String meshId);

    List<Project> findProListByUid(@Param("uid")String uid,@Param("flag")String flag);

    Project findOldProByUid(@Param("uid")String uid);

    int createProject(Project project);

    int createOldProject(Project project);

    void rename(@Param("id")int id, @Param("name")String name,@Param("renameFlag")int renameFlag);

    List<Mesh> findProDetailByUid(@Param("uid")String uid, @Param("projectId")int projectId);

    //删除部分
    int findLightByMid(@Param("id")int id);

    void deleteMeshByMid(@Param("id")int id);

    void deleteProByPid(@Param("id")int id, @Param("uid")String uid);

    void deleteMeshByPid(@Param("id")int id, @Param("uid")String uid);

    void freezing(@Param("id")int id, @Param("flag")String flag);

    void unfreezing(@Param("id")Integer id, @Param("flag")String flag);

    List<Integer> querySidByPid(@Param("id")int id, @Param("uid")String uid);

    void deleteSceneByPid(@Param("id")int id, @Param("uid")String uid);

    void deleteSceneSettingBySid(List<Integer> list);

    void deleteSceneSettingByMid(@Param("id")int id, @Param("uid")String uid);

    void deleteGroupSettingByMid(@Param("id")int id, @Param("uid")String uid);

    void deletePlaceByPid(@Param("id")int id, @Param("uid")String uid);

    void deleteGroupByPid(@Param("id")int id, @Param("uid")String uid);

    void deleteGroupSettingByPid(@Param("id")int id, @Param("uid")String uid);

    void deleteSceneByMid(@Param("id")int id, @Param("uid")String uid);

    void deleteGroupByMid(@Param("id")int id);

    void deletePlaceByMid(@Param("id")int id);

    int findLightByPid(@Param("id")int id, @Param("uid")String uid);

    String findNewMeshIdByMid(@Param("id")int id);

    List<String> findNewMeshIdByPid(@Param("id")int id, @Param("uid")String uid);

    void insertMeshId(List<String> list);

    void freezingOld(@Param("id")int id, @Param("flag")String flag);

    void unfreezingOld(@Param("id")Integer id,@Param("flag")String flag);

    Project findProjectById(@Param("projectId")Integer project_id);

    Mesh findMeshById(@Param("id")int id);

    void saveDeleteLog(DeleteLog deleteLog);
    //删除部分

    void createOldMesh(Mesh mesh);

    void createOldDuplicatedMesh(Mesh mesh);

    void oldMove(@Param("projectId")int projectId, @Param("meshId")String meshId, @Param("uid")String uid);

    String findRepeatNameByUid(@Param("uid")String uid,@Param("mname")String mname,@Param("projectId")Integer projectId);

    String findRepeatPnameByUid(@Param("uid")String uid,@Param("name")String name);

    void createPlace(AdjustPlace place);

    int findRepeatMid(AdjustPlace place);

    void savePcInfo(@Param("meshId")String meshId, @Param("meshName")String meshName,@Param("info")String info);

    List findMeshList();

    String getInfoByMeshId(@Param("meshId") String meshId);
}
