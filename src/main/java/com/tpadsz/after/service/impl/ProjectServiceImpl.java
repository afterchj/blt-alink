package com.tpadsz.after.service.impl;

import com.tpadsz.after.dao.GroupOperationDao;
import com.tpadsz.after.dao.LightAjustDao;
import com.tpadsz.after.dao.ProjectDao;
import com.tpadsz.after.dao.SceneAjustDao;
import com.tpadsz.after.entity.*;
import com.tpadsz.after.exception.RepetitionException;
import com.tpadsz.after.service.ProjectService;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by chenhao.lu on 2019/3/8.
 */
@Service("projectService")
public class ProjectServiceImpl implements ProjectService {
    @Resource
    private ProjectDao projectDao;


    @Override
    public Mesh findRepeatIdByUid(String preId, String uid) {
        return projectDao.findRepeatIdByUid(preId, uid);
    }

    @Override
    public List<Project> findProListByUid(String uid) {
        return projectDao.findProListByUid(uid);
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
    public void rename(Integer id, String name, Integer renameFlag) {
        projectDao.rename(id, name, renameFlag);
    }

    @Override
    public void createMesh(Mesh mesh) throws RepetitionException {
        Mesh mesh2 = projectDao.findRepeatIdByUid(mesh.getMesh_id().substring(0, 4), mesh.getUid());
        if (mesh2 == null) {
            projectDao.createMesh(mesh);
        } else {
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

    @Override
    public List<Mesh> findProDetailByUid(String uid, int projectId) {
        return projectDao.findProDetailByUid(uid, projectId);
    }

    @Override
    public int findLightByMid(int id) {
        return projectDao.findLightByMid(id);
    }

    @Override
    public void delete(int id, String uid, String deleteFlag) {
        if ("0".equals(deleteFlag)) {
            List<String> list = projectDao.findNewMeshIdByPid(id, uid);
            projectDao.deleteProByPid(id, uid);
            projectDao.deleteMeshByPid(id, uid);
            if (list.size() != 0) {
                projectDao.insertMeshId(list);
            }
        } else if ("1".equals(deleteFlag)) {
            String meshId = projectDao.findNewMeshIdByMid(id);
            projectDao.deleteByMid(id);
            if (meshId != null) {
                projectDao.recordMeshId(meshId);
            }
        }
    }

    @Override
    public int findLightByPid(int id, String uid) {
        return projectDao.findLightByPid(id, uid);
    }

    @Override
    public List<Mesh> oldMeshCommit(List<Mesh> list, String uid) {
        Project project = new Project();
        project.setUid(uid);
        project.setName("老项目");
        projectDao.createOldProject(project);
        for (int i = 0; i < list.size(); i++) {
            try {
                list.get(i).setProject_id(project.getId());
                list.get(i).setUid(uid);
                projectDao.createOldMesh(list.get(i));
            } catch (DuplicateKeyException e) {
                projectDao.createOldDuplicatedMesh(list.get(i));
            }
        }
        return list;
    }


    @Override
    public void oldMove(int projectId, String meshId, String uid) {
        projectDao.oldMove(projectId, meshId, uid);
    }

}
