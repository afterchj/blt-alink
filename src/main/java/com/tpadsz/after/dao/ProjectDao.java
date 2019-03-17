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

    int createMesh(Mesh mesh);

    String findMeshId(@Param("limitNum")int limitNum);

    void deleteMeshId(@Param("limitNum")int limitNum);

    void recordMeshId(@Param("mesh_id")String meshId);

    List<Project> findProListByUid(@Param("uid")String uid,@Param("id")int id);

    Project findOldProByUid(@Param("uid")String uid);

    int createProject(Project project);

    void rename(@Param("id")int id, @Param("name")String name,@Param("renameFlag")int renameFlag);

    List<Mesh> findProDetailByUid(@Param("uid")String uid, @Param("projectId")String projectId);

    int findLightByMid(@Param("id")int id);

    void deleteByMid(@Param("id")int id);

    void deleteProByPid(@Param("id")int id, @Param("uid")String uid);

    void deleteMeshByPid(@Param("id")int id, @Param("uid")String uid);

    int findLightByPid(@Param("id")int id, @Param("uid")String uid);

    String findMeshIdByMid(@Param("id")int id);

    List findMeshIdByPid(@Param("id")int id, @Param("uid")String uid);

    void insertMeshId(List<String> list);

    void createDefaultScene(SceneAjust scene);

    void createDefaultSceneSetting(@Param("sid")Integer sid, @Param("x")String x, @Param("y")String y);
}
