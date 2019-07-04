package com.tpadsz.after.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tpadsz.after.entity.*;
import com.tpadsz.after.entity.dd.ResultDict;
import com.tpadsz.after.exception.RepetitionException;
import com.tpadsz.after.service.GroupOperationService;
import com.tpadsz.after.service.LightAjustService;
import com.tpadsz.after.service.ProjectService;
import com.tpadsz.after.service.SceneAjustService;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

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
                        projectService.unfreezing(mesh.getId(), "1");
                    } else if ("freezingOld".equals(mesh.getOther())) {
                        projectService.unfreezingOld(mesh.getId(), "1");
                    }
                    if ("freezing".equals(project.getOther())) {
                        projectService.unfreezing(mesh.getProject_id(), "0");
                        model.put("unfreezingProject", project);
                    } else if ("freezingOld".equals(project.getOther())) {
                        projectService.unfreezingOld(mesh.getProject_id(), "0");
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
            int result = projectService.createProject(project);
            if (result == 1) {
                model.put("result", ResultDict.SUCCESS.getCode());
                model.put("projectId", project.getId());
            } else {
                model.put("result", ResultDict.DUPLICATE_NAME.getCode());
            }
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
        String meshType = params.getString("meshType");
        String meshId = "";
        int limitNum = 1;
        boolean isDuplicate;
        do {
            isDuplicate = false;
            try {
                meshId = projectService.findMeshId(limitNum);
                Mesh mesh = new Mesh();
                mesh.setMname(mname);
                mesh.setMesh_id(meshId);
                mesh.setMesh_type(meshType);
                mesh.setPwd(meshId.substring(4));
                mesh.setUid(uid);
                mesh.setProject_id(Integer.parseInt(projectId));
                int result = projectService.createMesh(mesh);
                if (result == 1) {
                    projectService.deleteMeshId(meshId);
                    AdjustPlace place = new AdjustPlace();
                    place.setPlaceId(1);
                    place.setPname("区域1");
                    place.setUid(uid);
                    place.setMid(mesh.getId());
                    projectService.createPlace(place);
                    model.put("result", ResultDict.SUCCESS.getCode());
                    model.put("meshId", meshId);
                    model.put("mid", mesh.getId());
                    model.put("pid", place.getId());
                } else {
                    model.put("result", ResultDict.DUPLICATE_NAME.getCode());
                }
            } catch (DuplicateKeyException e) {
                isDuplicate = true;
                projectService.recordMeshId(meshId);
                projectService.deleteMeshId(meshId);
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
            deleteLog.setDeleteId(Integer.valueOf(id));
            deleteLog.setDeleteFlag(deleteFlag);
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
        String uid = params.getString("uid");
        try {
            if (!"[]".equals(meshinfo)) {
                List<Mesh> meshInfoList = JSONArray.parseArray(meshinfo, Mesh.class);
                List<Mesh> meshInfoList2 = new ArrayList<>();
                for (int i = 0; i < meshInfoList.size(); i++) {
                    int count = projectService.findFullyRepeatIdByUid(meshInfoList.get(i).getMesh_id());
                    if (count == 0) {
                        meshInfoList2.add(meshInfoList.get(i));
                    }
                }
                Project oldPro = projectService.findOldProByUid(uid);
                if (oldPro != null) {
                    model.put("result", ResultDict.SUCCESS.getCode());
                    model.put("commitSuccess", "");
                    model.put("projectId", oldPro.getId());
                    model.put("isExist", true);
                    return null;
                } else {
                    List<Mesh> meshList = projectService.oldMeshCommit(meshInfoList2, uid, "0");
                    if (meshList != null) {
                        if (meshList.get(0).getMesh_id() != null) {
                            oldDeal(sceneinfo, groupinfo, meshList, uid);
                            model.put("commitSuccess", meshList);
                        } else {
                            model.put("commitSuccess", "");
                        }
                        model.put("projectId", meshList.get(0).getProject_id());
                    }
                    model.put("isExist", false);
                }
            }
            model.put("result", ResultDict.SUCCESS.getCode());
        } catch (Exception e) {
            model.put("result", ResultDict.SYSTEM_ERROR.getCode());
        }
        return null;
    }


    @RequestMapping(value = "/oldLight", method = RequestMethod.POST)
    public String oldLight(@ModelAttribute("decodedParams") JSONObject params, ModelMap model) {
        String lightinfo = params.getString("lightinfo");
        try {
            if (!"[]".equals(lightinfo)) {
                List<LightInfo> lightInfoList = JSONArray.parseArray(lightinfo, LightInfo.class);
                Map<String, LightInfo> lightMap = lightInfoList.stream().collect(Collectors.toMap(LightInfo::getLmac,
                        a ->
                                a, (k1, k2) -> k1));
                List<LightList> flightList = new ArrayList<>();
                for (Map.Entry<String, LightInfo> entry : lightMap.entrySet()) {
                    LightList lightList = new LightList();
                    Group group = new Group();
                    lightList.setMid(entry.getValue().getMid());
                    lightList.setLmac(entry.getValue().getLmac());
                    lightList.setLname(entry.getValue().getLname());
                    lightList.setProductId(entry.getValue().getProductId());
                    group.setMid(entry.getValue().getMid());
                    group.setGroupId(entry.getValue().getGroupId());
                    int gid = groupOperationService.getGid(group);
                    lightList.setGid(gid);
                    Integer pid = groupOperationService.getDefaultPlace(null, params.getString("uid"), group.getMid());
                    lightList.setPid(pid);
                    flightList.add(lightList);
                }
                lightAjustService.saveLight(flightList);
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
        String uid = params.getString("uid");
        try {
            List<Mesh> meshInfoList = JSONArray.parseArray(meshinfo, Mesh.class);
            List<Mesh> meshInfoList2 = new ArrayList<>();
            List<Mesh> meshInfoList3 = new ArrayList<>();
            for (int i = 0; i < meshInfoList.size(); i++) {
                int count = projectService.findFullyRepeatIdByUid(meshInfoList.get(i).getMesh_id());
                if (count == 0) {
                    meshInfoList2.add(meshInfoList.get(i));
                } else {
                    meshInfoList3.add(meshInfoList.get(i));
                }
            }
            List<Mesh> meshList = new ArrayList<>();
            if (meshInfoList2.size() != 0) {
                meshList = projectService.oldMeshCommit(meshInfoList2, uid, "1");
                oldDeal(sceneinfo, groupinfo, meshList, uid);
                model.put("projectId", meshList.get(0).getProject_id());
            } else {
                model.put("projectId", "");
            }
            model.put("result", ResultDict.SUCCESS.getCode());
            model.put("importSuccessId", meshList);
            model.put("repeatId", meshInfoList3);
        } catch (Exception e) {
            model.put("result", ResultDict.SYSTEM_ERROR.getCode());
        }
        return null;
    }


    private void oldDeal(String sceneinfo, String groupinfo, List<Mesh> meshList, String uid)
            throws Exception {
        for (int i = 0; i < meshList.size(); i++) {
            AdjustPlace place = new AdjustPlace();
            place.setPlaceId(1);
            place.setPname("区域1");
            place.setUid(uid);
            place.setMid(meshList.get(i).getId());
            projectService.createPlace(place);
        }

        if (!"[]".equals(sceneinfo)) {
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

        if (!"[]".equals(groupinfo)) {
            List<GroupInfo> groupInfoList = JSONArray.parseArray(groupinfo, GroupInfo.class);
            for (int i = 0; i < groupInfoList.size(); i++) {
                Group group = new Group();
                for (int j = 0; j < meshList.size(); j++) {
                    if (meshList.get(j).getMname().equals(groupInfoList.get(i).getMname())) {
                        group.setGname(groupInfoList.get(i).getGname());
                        group.setMid(meshList.get(j).getId());
                        group.setGroupId(groupInfoList.get(i).getGroupId());
                        Integer pid = groupOperationService.getDefaultPlace(null, uid, group.getMid());
                        group.setPid(pid);
                        groupOperationService.saveGroup(group);
                        groupInfoList.get(i).setGid(group.getId());
                        break;
                    }
                }
            }
        }
    }


    @RequestMapping(value = "/uploadFromPc", method = RequestMethod.POST)
    public String uploadFromPc(@RequestBody Map<String,Object> map, ModelMap model) {
        try {
            String meshId = String.valueOf(map.get("meshId"));
            projectService.savePcInfo(meshId, JSON.toJSONString(map.get("info")));
            model.put("result", ResultDict.SUCCESS.getCode());
        }catch (Exception e){
            model.put("result", ResultDict.SYSTEM_ERROR.getCode());
        }
        return null;
    }

    @RequestMapping(value = "/meshList", method = RequestMethod.POST)
    public String meshList(ModelMap model) {
        try {
            List<String> meshList = projectService.findMeshList();
            model.put("data", meshList);
            model.put("result", ResultDict.SUCCESS.getCode());
        }catch (Exception e){
            model.put("result", ResultDict.SYSTEM_ERROR.getCode());
        }
        return null;
    }


    @RequestMapping(value = "/download2", method = RequestMethod.POST)
    public String download2(String meshId, ModelMap model) {
        try {
            String info = projectService.getInfoByMeshId(meshId);
            model.put("data", info);
            model.put("result", ResultDict.SUCCESS.getCode());
        }catch (Exception e){
            model.put("result", ResultDict.SYSTEM_ERROR.getCode());
        }
        return null;
    }


    @RequestMapping(value = "/download", method = RequestMethod.POST)
    public String download(@RequestBody Map<String,Object> map, ModelMap model) {
        try {
            String meshId = String.valueOf(map.get("meshId"));
            String info = projectService.getInfoByMeshId(meshId);
            if (info.startsWith("\"")) {
                info = info.substring(1);
            }
            if (info.endsWith("\"")) {
                info = info.substring(0,info.length() - 1);
            }
            model.put("data", info);
            model.put("result", ResultDict.SUCCESS.getCode());
        }catch (Exception e){
            model.put("result", ResultDict.SYSTEM_ERROR.getCode());
        }
        return null;
    }

}
