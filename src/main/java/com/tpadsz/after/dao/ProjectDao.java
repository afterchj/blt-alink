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

    void createMesh(@Param("mname")String mname, @Param("mesh_id")String meshId, @Param("pwd")String pwd, @Param("uid")String uid, @Param("project_id")String project_id);

    String findMeshId(@Param("limitNum")int limitNum);

    void deleteMeshId(@Param("limitNum")int limitNum);

    void recordMeshId(@Param("mesh_id")String meshId);

    List<Project> findProListByUid(@Param("uid")String uid,@Param("id")int id);
}
