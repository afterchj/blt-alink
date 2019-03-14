package com.tpadsz.after.service.impl;

import com.tpadsz.after.dao.ProjectDao;
import com.tpadsz.after.entity.Mesh;
import com.tpadsz.after.entity.Project;
import com.tpadsz.after.exception.RepetitionException;
import com.tpadsz.after.service.ProjectService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenhao.lu on 2019/3/8.
 */
@Service("projectService")
public class ProjectServiceImpl implements ProjectService {
    @Resource
    private ProjectDao projectDao;

    @Override
    public List<Project> findProListByUid(String uid, String preId) {
        List<Project> list = new ArrayList<>();
        Mesh mesh = projectDao.findRepeatIdByUid(preId,uid);
        if(mesh!=null){
            list = projectDao.findProListByUid(uid,mesh.getId());
        }else {
            list = projectDao.findProListByUid(uid,0);
        }
        return list;
    }

    @Override
    public void createMesh(String mname, String meshId, String pwd, String uid, String projectId) throws RepetitionException{
        Mesh mesh = projectDao.findRepeatIdByUid(meshId.substring(0,4),uid);
        if(mesh==null) {
            projectDao.createMesh(mname, meshId, pwd, uid, projectId);
        }else {
            throw new RepetitionException();
        }
    }

    @Override
    public String findMeshId(int limitNum) {
        String meshId = projectDao.findMeshId(limitNum);
        return meshId;
    }

    @Override
    public void deleteMeshId(int limitNum) {
        projectDao.deleteMeshId(limitNum);
    }

    @Override
    public void recordMeshId(String meshId) {
        projectDao.recordMeshId(meshId);
    }



}
