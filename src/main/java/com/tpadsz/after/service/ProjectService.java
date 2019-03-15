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

    void rename(Integer id, String name,Integer renameFlag);

    void  createMesh(Mesh mesh) throws RepetitionException;

    String findMeshId(int limitNum);

    void deleteMeshId(int limitNum);

    void recordMeshId(String meshId);

    List<Mesh> findProDetailByUid(String uid, String projectId);

    int findLightByMid(int id);

    void delete(int id, String uid, String deleteFlag);

    int findLightByPid(int id, String uid);
}
