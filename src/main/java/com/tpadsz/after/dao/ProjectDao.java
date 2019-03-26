package com.tpadsz.after.dao;

import com.tpadsz.after.entity.Mesh;
import com.tpadsz.after.entity.Project;
import com.tpadsz.after.entity.SceneAjust;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by chenhao.lu on 2019/3/12.
 */
public interface ProjectDao {

    Mesh findRepeatIdByUid(@Param("mesh_id")String meshId, @Param("uid")String uid);

    int findFullyRepeatIdByUid(@Param("mesh_id")String meshId, @Param("uid")String uid);

    int createMesh(Mesh mesh);

    String findMeshId(@Param("limitNum")int limitNum);

    void deleteMeshId(@Param("limitNum")int limitNum);

    void recordMeshId(@Param("mesh_id")String meshId);

    List<Project> findProListByUid(@Param("uid")String uid);

    Project findOldProByUid(@Param("uid")String uid);

    int createProject(Project project);

    int createOldProject(Project project);

    void rename(@Param("id")int id, @Param("name")String name,@Param("renameFlag")int renameFlag);

    List<Mesh> findProDetailByUid(@Param("uid")String uid, @Param("projectId")int projectId);

    int findLightByMid(@Param("id")int id);

    void deleteMeshByMid(@Param("id")int id);

    void deleteProByPid(@Param("id")int id, @Param("uid")String uid);

    void deleteMeshByPid(@Param("id")int id, @Param("uid")String uid);

    void deleteSceneByPid(@Param("id")int id, @Param("uid")String uid);

    void deleteGroupByPid(@Param("id")int id, @Param("uid")String uid);

    void deleteSceneByMid(@Param("id")int id, @Param("uid")String uid);

    void deleteGroupByMid(@Param("id")int id);


    int findLightByPid(@Param("id")int id, @Param("uid")String uid);

    String findNewMeshIdByMid(@Param("id")int id);

    List<String> findNewMeshIdByPid(@Param("id")int id, @Param("uid")String uid);

    void insertMeshId(List<String> list);

    void createOldMesh(Mesh mesh);

    void createOldDuplicatedMesh(Mesh mesh);

    void oldMove(@Param("projectId")int projectId, @Param("meshId")String meshId, @Param("uid")String uid);

    void createDuplicatedMesh(Mesh mesh);


}
