package com.tpadsz.after.dao;

import com.tpadsz.after.entity.Mesh;
import com.tpadsz.after.entity.Project;
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

    void rename(@Param("id")Integer id, @Param("name")String name);

}
