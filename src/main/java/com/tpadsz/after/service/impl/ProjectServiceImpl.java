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
            list = projectDao.findProListByUid(uid,mesh.getProject_id());
        }else {
            list = projectDao.findProListByUid(uid,0);
        }
        return list;
    }

    @Override
    public Project findOldProByUid(String uid) {
        return projectDao.findOldProByUid(uid);
    }

    @Override
    public void createProject(Project project) {
        projectDao.createProject(project);
    }

    @Override
    public void renameProject(Integer id, String name) {
        projectDao.rename(id,name);
    }

    @Override
    public void renameMesh(String id, String name) {
//        projectDao.renameMesh(id,name);
    }

    @Override
    public void createMesh(Mesh mesh) throws RepetitionException{
        Mesh mesh2 = projectDao.findRepeatIdByUid(mesh.getMesh_id().substring(0,4),mesh.getUid());
        if(mesh2==null) {
            projectDao.createMesh(mesh);
        }else {
            throw new RepetitionException();
        }
    }

    @Override
    public String findMeshId(int limitNum) {
        return projectDao.findMeshId(limitNum);
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
