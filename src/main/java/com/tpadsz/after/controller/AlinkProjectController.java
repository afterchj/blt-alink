package com.tpadsz.after.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tpadsz.after.entity.*;
import com.tpadsz.after.entity.dd.ResultDict;
import com.tpadsz.after.exception.LightExistedException;
import com.tpadsz.after.exception.RepetitionException;
import com.tpadsz.after.service.GroupOperationService;
import com.tpadsz.after.service.LightAjustService;
import com.tpadsz.after.service.ProjectService;
import com.tpadsz.after.service.SceneAjustService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by chenhao.lu on 2019/3/8.
 */

@Controller
@RequestMapping("/project")
public class AlinkProjectController extends BaseDecodedController {
    @Resource
    private ProjectService projectService;

    @Resource
    private SceneAjustService sceneAjustService;

    @Resource
    private GroupOperationService groupOperationService;

    @Resource
    private LightAjustService lightAjustService;


    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @RequestMapping(value = "/pro_list", method = RequestMethod.POST)
    public String findProList(@ModelAttribute("decodedParams") JSONObject params, ModelMap model) {
        String uid = params.getString("uid");
        String preId = params.getString("preId");
        List<ProjectVo> listVo = new ArrayList<>();
        try {
            if ("".equals(preId)) {
                List<Project> list = projectService.findProListByUid(uid);
                if (list.size() != 0) {
                    for (Project project : list) {
                        ProjectVo projectVo = new ProjectVo();
                        projectVo.setId(String.valueOf(project.getId()));
                        projectVo.setName(project.getName());
                        projectVo.setAdd(project.getAdd());
                        projectVo.setUid(project.getUid());
                        projectVo.setIsConnected("0");
                        if (project.getCreate_date() != null) {
                            projectVo.setCreate_date(sdf.format(project.getCreate_date()));
                        }
                        if (project.getUpdate_date() != null) {
                            projectVo.setUpdate_date(sdf.format(project.getUpdate_date()));
                        }
                        listVo.add(projectVo);
                    }
                    model.put("myProjects", listVo);
                } else {
                    model.put("myProjects", "");
                }
                Project oldPro = projectService.findOldProByUid(uid);
                if (oldPro != null) {
                    ProjectVo projectVo2 = new ProjectVo();
                    projectVo2.setId(String.valueOf(oldPro.getId()));
                    projectVo2.setName(oldPro.getName());
                    projectVo2.setAdd(oldPro.getAdd());
                    projectVo2.setUid(oldPro.getUid());
                    projectVo2.setIsConnected("0");
                    if (oldPro.getCreate_date() != null) {
                        projectVo2.setCreate_date(sdf.format(oldPro.getCreate_date()));
                    }
                    if (oldPro.getUpdate_date() != null) {
                        projectVo2.setUpdate_date(sdf.format(oldPro.getUpdate_date()));
                    }
                    model.put("oldProject", projectVo2);
                } else {
                    model.put("oldProject", "");
                }
                model.put("result", ResultDict.SUCCESS.getCode());
            } else {
                Mesh mesh = projectService.findRepeatIdByUid(preId, uid);
                if (mesh != null) {
                    Project project = projectService.findProjectById(mesh.getProject_id());
                    if ("freezing".equals(mesh.getOther())) {
                        projectService.unfreezing(mesh.getId(),"1");
                    }else if("freezingOld".equals(mesh.getOther())){
                        projectService.unfreezingOld(mesh.getId(),"1");
                    }
                    if ("freezing".equals(project.getOther())) {
                        projectService.unfreezing(mesh.getProject_id(),"0");
                        model.put("isUnfreezingProject", true);
                        model.put("unfreezingProject", project);
                    }else if("freezingOld".equals(project.getOther())){
                        projectService.unfreezingOld(mesh.getProject_id(),"0");
                        model.put("isUnfreezingProject", true);
                        model.put("unfreezingProject", project);
                    }
                    model.put("result", ResultDict.SUCCESS.getCode());
                    model.put("connectedProjectId", String.valueOf(mesh.getProject_id()));
                } else {
                    model.put("result", ResultDict.SUCCESS.getCode());
                    model.put("connectedProjectId", "");
                }
            }
        } catch (Exception e) {
            model.put("result", ResultDict.SYSTEM_ERROR.getCode());
        }
        return null;
    }

    @RequestMapping(value = "/proDetail", method = RequestMethod.POST)
    public String proDetail(@ModelAttribute("decodedParams") JSONObject params, ModelMap model) {
        String uid = params.getString("uid");
        String projectId = params.getString("projectId");
        try {
            List<Mesh> list = projectService.findProDetailByUid(uid, Integer.parseInt(projectId));
            model.put("result", ResultDict.SUCCESS.getCode());
            model.put("meshList", list);
        } catch (Exception e) {
            model.put("result", ResultDict.SYSTEM_ERROR.getCode());
        }
        return null;
    }


    @RequestMapping(value = "/createProject", method = RequestMethod.POST)
    public String createProject(@ModelAttribute("decodedParams") JSONObject params, ModelMap model) {
        String uid = params.getString("uid");
        String projectName = params.getString("projectName");
        try {
            Project project = new Project();
            project.setUid(uid);
            project.setName(projectName);
            projectService.createProject(project);
            model.put("result", ResultDict.SUCCESS.getCode());
            model.put("projectId", project.getId());
        } catch (Exception e) {
            model.put("result", ResultDict.SYSTEM_ERROR.getCode());
        }
        return null;
    }

    @RequestMapping(value = "/rename", method = RequestMethod.POST)
    public String rename(@ModelAttribute("decodedParams") JSONObject params, ModelMap model) {
        String id = params.getString("id");
        String name = params.getString("name");
        String renameFlag = params.getString("renameFlag");
        try {
            projectService.rename(Integer.parseInt(id), name, Integer.parseInt(renameFlag));
            model.put("result", ResultDict.SUCCESS.getCode());
        } catch (Exception e) {
            model.put("result", ResultDict.SYSTEM_ERROR.getCode());
        }
        return null;
    }

    @RequestMapping(value = "/createMesh", method = RequestMethod.POST)
    public String createMesh(@ModelAttribute("decodedParams") JSONObject params, ModelMap model) {
        String uid = params.getString("uid");
        String projectId = params.getString("projectId");
        String mname = params.getString("mname");
        String prex = "00000000";
        String meshId = "";
        int limitNum = 1;
        boolean isDuplicate;
        do {
            isDuplicate = false;
            try {
                meshId = projectService.findMeshId(limitNum);
                meshId = prex.substring(0, prex.length() - meshId.length()) + meshId;
                Mesh mesh = new Mesh();
                mesh.setMname(mname);
                mesh.setMesh_id(meshId);
                mesh.setPwd(meshId.substring(4));
                mesh.setUid(uid);
                mesh.setProject_id(Integer.parseInt(projectId));
                projectService.createMesh(mesh);
                projectService.deleteMeshId(limitNum);
                model.put("result", ResultDict.SUCCESS.getCode());
                model.put("meshId", meshId);
                model.put("mid", mesh.getId());
            } catch (DuplicateKeyException e) {
                isDuplicate = true;
                projectService.recordMeshId(meshId);
                projectService.deleteMeshId(limitNum);
            } catch (RepetitionException e) {
                isDuplicate = true;
                limitNum++;
            } catch (Exception e) {
                model.put("result", ResultDict.SYSTEM_ERROR.getCode());
            }
        } while (isDuplicate);
        return null;
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public String delete(@ModelAttribute("decodedParams") JSONObject params, ModelMap model) {
        String id = (String) params.get("id");
        String uid = (String) params.get("uid");
        String deleteFlag = (String) params.get("deleteFlag");
        int lightFlag = 0;
        try {
            if ("0".equals(deleteFlag)) {
                lightFlag = projectService.findLightByPid(Integer.parseInt(id), uid);
            } else if ("1".equals(deleteFlag)) {
                lightFlag = projectService.findLightByMid(Integer.parseInt(id));
            }
            projectService.delete(Integer.parseInt(id), uid, deleteFlag, lightFlag);
            DeleteLog deleteLog = new DeleteLog();
            deleteLog.setUid(uid);
            deleteLog.setLightFlag(lightFlag);
            deleteLog.setDelete_date(new Date());
            projectService.saveDeleteLog(deleteLog);
            model.put("result", ResultDict.SUCCESS.getCode());
        } catch (Exception e) {
            model.put("result", ResultDict.SYSTEM_ERROR.getCode());
        }
        return null;
    }

    @RequestMapping(value = "/oldMove", method = RequestMethod.POST)
    public String oldMove(@ModelAttribute("decodedParams") JSONObject params, ModelMap model) {
        String projectId = params.getString("projectId");
        String meshId = params.getString("meshId");
        String uid = params.getString("uid");
        try {
            projectService.oldMove(Integer.parseInt(projectId), meshId, uid);
            model.put("result", ResultDict.SUCCESS.getCode());
        } catch (Exception e) {
            model.put("result", ResultDict.SYSTEM_ERROR.getCode());
        }
        return null;
    }

    @RequestMapping(value = "/oldCommit", method = RequestMethod.POST)
    public String oldCommit(@ModelAttribute("decodedParams") JSONObject params, ModelMap model) {
        String meshinfo = params.getString("meshinfo");
        String sceneinfo = params.getString("sceneinfo");
        String groupinfo = params.getString("groupinfo");
        String lightinfo = params.getString("lightinfo");
        String uid = params.getString("uid");
        try {
            if (!"[]".equals(meshinfo)) {
                List<Mesh> meshInfoList = JSONArray.parseArray(meshinfo, Mesh.class);
                List<Mesh> meshList = projectService.oldMeshCommit(meshInfoList, uid, "0");
                if (meshList != null) {
                    if (meshList.get(0).getMesh_id() != null) {
                        oldDeal(sceneinfo, groupinfo, lightinfo, meshList, uid);
                        model.put("commitSuccess", meshList);
                    } else {
                        model.put("commitSuccess", "");
                    }
                    model.put("projectId", meshList.get(0).getProject_id());
                }
            }
            model.put("result", ResultDict.SUCCESS.getCode());
        } catch (Exception e) {
            model.put("result", ResultDict.SYSTEM_ERROR.getCode());
        }
        return null;
    }

    @RequestMapping(value = "/oldImport", method = RequestMethod.POST)
    public String oldImport(@ModelAttribute("decodedParams") JSONObject params, ModelMap model) {
        String meshinfo = params.getString("meshinfo");
        String sceneinfo = params.getString("sceneinfo");
        String groupinfo = params.getString("groupinfo");
        String lightinfo = params.getString("lightinfo");
        String uid = params.getString("uid");
        try {
            List<Mesh> meshInfoList = JSONArray.parseArray(meshinfo, Mesh.class);
            List<Mesh> meshInfoList2 = new ArrayList<>();
            for (int i = 0; i < meshInfoList.size(); i++) {
                int count = projectService.findFullyRepeatIdByUid(meshInfoList.get(i).getMesh_id(), uid);
                if (count == 0) {
                    meshInfoList2.add(meshInfoList.get(i));
                    meshInfoList.remove(i);
                }
            }
            List<Mesh> meshList = new ArrayList<>();
            if (meshInfoList2.size() != 0) {
                meshList = projectService.oldMeshCommit(meshInfoList2, uid, "1");
                model.put("projectId", meshList.get(0).getProject_id());
            }
            oldDeal(sceneinfo, groupinfo, lightinfo, meshList, uid);
            model.put("result", ResultDict.SUCCESS.getCode());
            model.put("importSuccessId", meshList);
            model.put("repeatId", meshInfoList);
        } catch (Exception e) {
            model.put("result", ResultDict.SYSTEM_ERROR.getCode());
        }
        return null;
    }


    private void oldDeal(String sceneinfo, String groupinfo, String lightinfo, List<Mesh> meshList, String uid)
            throws Exception {
        if (StringUtils.isNotBlank(sceneinfo)) {
            List<SceneInfo> sceneInfoList = JSONArray.parseArray(sceneinfo, SceneInfo.class);
            for (int i = 0; i < sceneInfoList.size(); i++) {
                SceneAjust sceneAjust = new SceneAjust();
                for (int j = 0; j < meshList.size(); j++) {
                    if (meshList.get(j).getMname().equals(sceneInfoList.get(i).getMname())) {
                        sceneAjust.setSceneId(sceneInfoList.get(i).getSceneId());
                        sceneAjust.setSname(sceneInfoList.get(i).getSname());
                        sceneAjust.setMid(meshList.get(j).getId());
                        sceneAjust.setUid(uid);
                        sceneAjustService.saveScene(sceneAjust);
                        break;
                    }
                }
            }
        }

        if (StringUtils.isNotBlank(groupinfo)) {
            List<GroupInfo> groupInfoList = JSONArray.parseArray(groupinfo, GroupInfo.class);
            for (int i = 0; i < groupInfoList.size(); i++) {
                Group group = new Group();
                for (int j = 0; j < meshList.size(); j++) {
                    if (meshList.get(j).getMname().equals(groupInfoList.get(i).getMname())) {
                        group.setGname(groupInfoList.get(i).getGname());
                        group.setMid(meshList.get(j).getId());
                        group.setGroupId(groupInfoList.get(i).getGroupId());
                        groupOperationService.saveGroup(group);
                        groupInfoList.get(i).setGid(group.getId());
                        break;
                    }
                }
            }

            if (StringUtils.isNotBlank(lightinfo)) {
                List<LightList> flightList = new ArrayList<>();
                List<LightInfo> lightInfoList = JSONArray.parseArray(lightinfo, LightInfo.class);
                for (int i = 0; i < lightInfoList.size(); i++) {
                    LightList lightList = new LightList();
                    for (int j = 0; j < meshList.size(); j++) {
                        if (meshList.get(j).getMname().equals(lightInfoList.get(i).getMname())) {
                            lightList.setMid(meshList.get(j).getId());
                            lightList.setLmac(lightInfoList.get(i).getLmac());
                            lightList.setGroupId(lightInfoList.get(i).getGroupId());
                            lightList.setLname(lightInfoList.get(i).getLname());
                            lightList.setProductId(lightInfoList.get(i).getProductId());
                            for (int k = 0; k < groupInfoList.size(); k++) {
                                if (groupInfoList.get(k).getMname().equals(lightInfoList.get(i).getMname()) &&
                                        groupInfoList.get(k).getGroupId().equals(lightInfoList.get(i).getGroupId())) {
                                    lightList.setGid(groupInfoList.get(k).getGid());
                                    break;
                                }
                            }
                            flightList.add(lightList);
                            break;
                        }
                    }
                }
                lightAjustService.saveLight(flightList);
            }
        }
    }

}
