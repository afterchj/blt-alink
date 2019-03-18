package com.tpadsz.after.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tpadsz.after.entity.Mesh;
import com.tpadsz.after.entity.Project;
import com.tpadsz.after.entity.dd.ResultDict;
import com.tpadsz.after.exception.LightExistedException;
import com.tpadsz.after.exception.RepetitionException;
import com.tpadsz.after.service.AlinkLoginService;
import com.tpadsz.after.service.ProjectService;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import java.util.ArrayList;
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
    private AlinkLoginService alinkLoginService;

    @RequestMapping(value = "/generator", method = RequestMethod.POST)
    public String generator(@ModelAttribute("decodedParams") JSONObject params, ModelMap model) {
        try {
            int[] meshIds = generateAppCode2Disk();
            List<String> list = new ArrayList<>();
            for (int i = 1; i < 10001; i++) {
                list.add(String.valueOf(meshIds[i]));
//                alinkLoginService.insert(String.valueOf(meshIds[i]));
            }
            alinkLoginService.insertForeach(list);
        } catch (Exception e) {
        }
        model.put("result", "123");
        model.put("result_message", "成功");
        return null;
    }


    @RequestMapping(value = "/pro_list", method = RequestMethod.POST)
    public String findProList(@ModelAttribute("decodedParams") JSONObject params, ModelMap model) {
        String uid = params.getString("uid");
        String preId = params.getString("preId");
        try {
            List<Project> list = projectService.findProListByUid(uid, preId);
            Project oldPro = projectService.findOldProByUid(uid);
            model.put("result", ResultDict.SUCCESS.getCode());
            model.put("myProjects", list);
            model.put("oldProject", oldPro);
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
            List<Mesh> list = projectService.findProDetailByUid(uid, projectId);
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
        try {
            if("0".equals(deleteFlag)) {
                int count = projectService.findLightByPid(Integer.parseInt(id),uid);
                if(count>0){

                    throw new LightExistedException();
                }
            }else if("1".equals(deleteFlag)){
                int count = projectService.findLightByMid(Integer.parseInt(id));
                if(count>0){

                    throw new LightExistedException();
                }
            }
            projectService.delete(Integer.parseInt(id), uid,deleteFlag);
            model.put("result", ResultDict.SUCCESS.getCode());
        }catch (LightExistedException e){
            model.put("result", ResultDict.LIGHT_EXISTED.getCode());
        }catch (Exception e) {
            model.put("result", ResultDict.SYSTEM_ERROR.getCode());
        }
        return null;
    }


    @RequestMapping(value = "/oldCommit", method = RequestMethod.POST)
    public String oldCommit(@ModelAttribute("decodedParams") JSONObject params, ModelMap model) {
        String meshData = params.getString("meshData");
        String uid = params.getString("uid");
        try {
            List<Mesh> list = JSONArray.parseArray(meshData, Mesh.class);
            projectService.oldCommit(list,uid);
            model.put("result", ResultDict.SUCCESS.getCode());
        } catch (Exception e) {
            model.put("result", ResultDict.SYSTEM_ERROR.getCode());
        }
        return null;
    }




    private static int[] generateAppCode2Disk() {
        int begin = 1;
        int end = 99999999;
        int count = begin + end;
        //生成1到99999999的所有整数
        int[] codes = new int[count + 1];
        for (int i = begin; i <= end; i++) {
            codes[i] = i;
        }
        //随机交换数据
        int index = 0;
        int tempCode = 0;
        Random random = new Random();
        for (int i = begin; i <= end; i++) {
            index = random.nextInt(count + 1);
            tempCode = codes[index];
            codes[index] = codes[i];
            codes[i] = tempCode;
        }
        //生成1000个文件,每个文件包含100000个appCode
        //生成1000个文件,每个文件包含100000个appCode
        StringBuilder sb = new StringBuilder();
        int flag = 100000;
//        System.out.println("***********开始**********");
//        try {
//            for(int i = 5000001; i <= end; i++){
//                sb.append(codes[i]).append("\n");
//                if(i == end || i%flag == 0){
//                    File folder = new File("C:/001work/meshId");
//                    if(!folder.isDirectory()){
//                        folder.mkdir();
//                    }
//                    if(i==end){
//                        i = end +1;
//                    }
//                    File file = new File("C:/001work/meshId/ID_"+(i/flag)+".txt");
//                    if (!file.exists()) {
//                        file.createNewFile();
//                    }
//                    BufferedWriter bw=new BufferedWriter(new FileWriter(file.getAbsoluteFile()));
//                    bw.write(sb.toString());
//                    bw.flush();
//                    bw.close();
//                    sb = new StringBuilder();
////                    System.out.println("当前i值："+i+"第"+(i/flag)+"个文件生成成功！");
//                }
//            }
////            System.out.println("***********结束**********");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        return codes;
    }


}
