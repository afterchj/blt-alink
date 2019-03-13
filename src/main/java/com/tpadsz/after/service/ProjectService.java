package com.tpadsz.after.service;

import com.tpadsz.after.entity.Project;
import com.tpadsz.after.exception.RepetitionException;

import java.util.List;

/**
 * Created by chenhao.lu on 2019/3/8.
 */
public interface ProjectService {

    List<Project> findProListByUid(String uid, String preId);

    Project findOldProByUid(String uid);

    void  createMesh(String mname,String meshId,String pwd,String uid,String projectId) throws RepetitionException;

    String findMeshId(int limitNum);

    void deleteMeshId(int limitNum);

    void recordMeshId(String meshId);

}
