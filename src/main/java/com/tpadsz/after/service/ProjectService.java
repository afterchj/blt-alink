package com.tpadsz.after.service;

import com.tpadsz.after.entity.Mesh;
import com.tpadsz.after.entity.Project;
import com.tpadsz.after.exception.RepetitionException;

import java.util.List;

/**
 * Created by chenhao.lu on 2019/3/8.
 */
public interface ProjectService {

    List<Project> findProListByUid(String uid, String preId);

    Project findOldProByUid(String uid);

    void createProject(Project project);

    void renameProject(Integer id, String name);

    void renameMesh(String id, String name);

    void  createMesh(Mesh mesh) throws RepetitionException;

    String findMeshId(int limitNum);

    void deleteMeshId(int limitNum);

    void recordMeshId(String meshId);

}
