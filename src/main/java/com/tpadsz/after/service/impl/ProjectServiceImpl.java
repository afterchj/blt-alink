package com.tpadsz.after.service.impl;

import com.tpadsz.after.dao.ProjectDao;
import com.tpadsz.after.entity.*;
import com.tpadsz.after.exception.RepetitionException;
import com.tpadsz.after.service.ProjectService;
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
    public Mesh findRepeatIdByUid(String preId, String uid,String flag) {
        return projectDao.findRepeatIdByUid(preId, uid,flag);
    }

    @Override
    public int findFullyRepeatIdByUid(String meshId) {
        return projectDao.findFullyRepeatIdByUid(meshId);
    }

    @Override
    public List<Project> findProListByUid(String uid,String flag) {
        return projectDao.findProListByUid(uid,flag);
    }

    @Override
    public Project findOldProByUid(String uid) {
        return projectDao.findOldProByUid(uid);
    }

    @Override
    public int createProject(Project project) {
        int result;
        String name = projectDao.findRepeatPnameByUid(project.getUid(), project.getName());
        if (name == null) {
            projectDao.createProject(project);
            result = 1;
        } else {
            result = 0;
        }
        return result;
    }

    @Override
    public void rename(Integer id, String name, Integer renameFlag) {
        projectDao.rename(id, name, renameFlag);
    }

    @Override
    public int createMesh(Mesh mesh) throws RepetitionException {
        int result;
        Mesh mesh2 = projectDao.findRepeatIdByUid(mesh.getMesh_id().substring(0, 4), mesh.getUid(),null);
        if (mesh2 == null) {
            String mname = projectDao.findRepeatNameByUid(mesh.getUid(), mesh.getMname(),mesh.getProject_id());
            if (mname == null) {
                projectDao.createMesh(mesh);
                result = 1;
            } else {
                result = 0;
            }
        } else {
            throw new RepetitionException();
        }
        return result;
    }

    @Override
    public String findMeshId(int limitNum) {
        return projectDao.findMeshId(limitNum);
    }

    @Override
    public void deleteMeshId(String meshId) {
        projectDao.deleteMeshId(meshId);
    }

    @Override
    public void recordMeshId(String meshId) {
        projectDao.recordMeshId(meshId);
    }

    @Override
    public List<Mesh> findProDetailByUid(int projectId) {
        return projectDao.findProDetailByUid(projectId);
    }

    @Override
    public int findLightByMid(int id) {
        return projectDao.findLightByMid(id);
    }

    @Override
    public void delete(int id, String uid, String deleteFlag, int lightFlag) {
        if (lightFlag == 0) {
            if ("0".equals(deleteFlag)) {
                List<String> list = projectDao.findNewMeshIdByPid(id, uid);
                List<Integer> sids = projectDao.querySidByPid(id, uid);
                if (sids.size() != 0) {
                    projectDao.deleteSceneByPid(id, uid);
                    projectDao.deleteSceneSettingBySid(sids);
                }
                projectDao.deletePlaceByPid(id, uid);
                projectDao.deleteGroupByPid(id, uid);
                projectDao.deleteGroupSettingByPid(id, uid);
                projectDao.deleteProByPid(id, uid);
                projectDao.deleteMeshByPid(id, uid);
                if (list.size() != 0) {
                    projectDao.insertMeshId(list);
                }
            } else if ("1".equals(deleteFlag)) {
                String meshId = projectDao.findNewMeshIdByMid(id);
                projectDao.deleteMeshByMid(id);
                projectDao.deleteSceneSettingByMid(id, uid);
                projectDao.deleteSceneByMid(id, uid);
                projectDao.deleteGroupSettingByMid(id, uid);
                projectDao.deleteGroupByMid(id);
                projectDao.deletePlaceByMid(id);
                if (meshId != null) {
                    projectDao.recordMeshId(meshId);
                }
            }
        } else if (lightFlag > 0) {
            if ("0".equals(deleteFlag)) {
                Project project = projectDao.findProjectById(id);
                if (!"old".equals(project.getOther())) {
                    projectDao.freezing(id, "0");
                } else {
                    projectDao.freezingOld(id, "0");
                }
            } else if ("1".equals(deleteFlag)) {
                Mesh mesh = projectDao.findMeshById(id);
                if (!"old".equals(mesh.getOther())) {
                    projectDao.freezing(id, "1");
                } else {
                    projectDao.freezingOld(id, "1");
                }
            }
        }
    }

    @Override
    public int findLightByPid(int id, String uid) {
        return projectDao.findLightByPid(id, uid);
    }

    @Override
    public List<Mesh> oldMeshCommit(List<Mesh> list, String uid, String flag) {
        Project project = new Project();
        if ("1".equals(flag)) {
            Project oldProject = projectDao.findOldProByUid(uid);
            if (oldProject == null) {
                project.setUid(uid);
                project.setName("老项目");
                projectDao.createOldProject(project);
            } else {
                project = oldProject;
            }
        } else {
            project.setUid(uid);
            project.setName("老项目");
            projectDao.createOldProject(project);
        }
        for (int i = 0; i < list.size(); i++) {
            try {
                list.get(i).setProject_id(project.getId());
                list.get(i).setUid(uid);
                projectDao.createOldMesh(list.get(i));
            } catch (Exception e) {
//                projectDao.createOldDuplicatedMesh(list.get(i));
            }
        }
        if (list.size() == 0) {
            Mesh mesh = new Mesh();
            mesh.setMesh_id(null);
            mesh.setProject_id(project.getId());
            list.add(mesh);
        }
        return list;
    }


    @Override
    public void oldMove(int projectId, String meshId, String uid) {
        projectDao.oldMove(projectId, meshId, uid);
    }

    @Override
    public Project findProjectById(Integer project_id) {
        return projectDao.findProjectById(project_id);
    }

    @Override
    public void unfreezing(Integer id, String flag) {
        projectDao.unfreezing(id, flag);
    }

    @Override
    public void unfreezingOld(Integer id, String flag) {
        projectDao.unfreezingOld(id, flag);
    }

    @Override
    public void createPlace(AdjustPlace place) {
        int count = projectDao.findRepeatMid(place);
        if(count==0) {
            projectDao.createPlace(place);
        }
    }

    @Override
    public void savePcInfo(String meshId,String meshName, String info) {
        projectDao.savePcInfo(meshId,meshName,info);
    }

    @Override
    public String getInfoByMeshId(String meshId) {
        return projectDao.getInfoByMeshId(meshId);
    }

    @Override
    public List findMeshList() {
        return projectDao.findMeshList();
    }


    @Override
    public void saveDeleteLog(DeleteLog deleteLog) {
        projectDao.saveDeleteLog(deleteLog);
    }


}
