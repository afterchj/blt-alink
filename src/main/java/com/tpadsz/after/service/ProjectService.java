package com.tpadsz.after.service;

import com.tpadsz.after.entity.*;
import com.tpadsz.after.exception.RepetitionException;

import java.util.List;

/**
 * Created by chenhao.lu on 2019/3/8.
 */
public interface ProjectService {

    Mesh findRepeatIdByUid(String preId,String uid);

    int findFullyRepeatIdByUid(String meshId,String uid);

    List<Project> findProListByUid(String uid);

    Project findOldProByUid(String uid);

    int createProject(Project project);

    void rename(Integer id, String name,Integer renameFlag);

    int createMesh(Mesh mesh) throws RepetitionException;

    String findMeshId(int limitNum);

    void deleteMeshId(int limitNum);

    void recordMeshId(String meshId);

    List<Mesh> findProDetailByUid(String uid, int projectId);

    //删除部分
    int findLightByMid(int id);

    void delete(int id, String uid, String deleteFlag,int lightFlag);

    int findLightByPid(int id, String uid);

    void saveDeleteLog(DeleteLog deleteLog);
    //删除部分

    List<Mesh> oldMeshCommit(List<Mesh> list, String uid,String flag);

    void oldMove(int projectId,String meshId, String uid);

    Project findProjectById(Integer project_id);

    void unfreezing(Integer id,String flag);

    void unfreezingOld(Integer id,String flag);

}
