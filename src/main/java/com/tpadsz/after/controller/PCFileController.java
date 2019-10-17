package com.tpadsz.after.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tpadsz.after.entity.dd.ResultDict;
import com.tpadsz.after.service.PCFileService;
import com.tpadsz.after.util.*;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author hongjian.chen
 * @date 2019/7/25 13:43
 */

@RequestMapping("/pc")
@RestController
public class PCFileController {

    @Resource
    private PCFileService fileService;
    private Logger logger = Logger.getLogger(this.getClass());

    @RequestMapping("/login")
    public Map login(@RequestBody JSONObject params) {
        logger.warn("params =" + params);
        Map map = new HashMap();
        Map data = new HashMap();
        Map<String, String> appUser = fileService.getUser(params.getString("User_Name"));
        map.put("uid", appUser.get("User_ID"));
        String confirm = Encryption.encrypt(Encryption.getMD5Str(params.getString("User_Pwd")), appUser.get("salt"));
        boolean flag = appUser.get("pwd").equals(confirm);
        if (!flag) {
            data.put("code", ResultDict.PASSWORD_NOT_CORRECT.getCode());
            data.put("msg", ResultDict.PASSWORD_NOT_CORRECT.getValue());
            return data;
        }
        appUser = MapUtil.removeEntries(appUser, new String[]{"pwd", "salt"});
        data.put("User_Info", appUser);
        List<Map> maps = fileService.getProject(map);
        List pList = new ArrayList();
        for (Map p : maps) {
            Map project = new HashMap();
            List mesh = fileService.getMesh(p);
            project.put("Project_Mesh", mesh);
            project.putAll(p);
            pList.add(project);
        }
        data.put("User_Project", pList);
        return data;
    }

    @RequestMapping("/backup")
    public Map submit(@RequestBody JSONObject params) {
        logger.warn("params =" + params);
        Map data = new HashMap();
        try {
            String uid = params.getString("User_ID");
            Map project = new HashMap();
            project.put("uid", uid);
            JSONArray jsonArray = params.getJSONArray("User_Project");
            for (int i = 0; i < jsonArray.size(); i++) {
                List list = new ArrayList();
                JSONObject object = (JSONObject) jsonArray.get(i);
                String pid = object.getString("Project_ID");
                String name = object.getString("Project_Name");
                project.put("name", name);
                List mesh = object.getJSONArray("Project_Mesh");
                for (int j = 0; j < mesh.size(); j++) {
                    Map map = new HashMap();
                    map.put("pid", pid);
                    map.put("meshId", mesh.get(j));
                    list.add(map);
                }
                fileService.saveUpdateProject(project);
                fileService.saveMesh(list);
            }
        } catch (Exception e) {
            data.put("code", ResultDict.PASSWORD_NOT_CORRECT.getCode());
            data.put("msg", ResultDict.PASSWORD_NOT_CORRECT.getValue());
            return data;
        }
        data.put("code", ResultDict.SUCCESS.getCode());
        data.put("msg", ResultDict.SUCCESS.getValue());
        return data;
    }

    @RequestMapping("/upload")
    public String uploadFile(MultipartFile file) {
        String downloadPath = PropertiesUtil.getPath("otaPath");
        String path = PropertiesUtil.getPath("upload");
        String str = file.getOriginalFilename();
        File targetFile = new File(path, str);
        if (StringUtils.isNotEmpty(str)) {
            try {
                file.transferTo(targetFile);
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        }
        String txt = FileReadUtils.parseTxtFile(targetFile);
        Map map = saveFile(txt, downloadPath + str);
        JSONObject object = new JSONObject();
        object.put("code", "000");
        if (map != null) {
            object.put("meshId", map.get("Mesh_ID"));
            object.put("versionCode", map.get("result"));
        } else {
            object.put("code", "200");
        }
        String result = object.toJSONString();
        new Thread(() -> WSClientUtil.sendMsg(result)).start();
        return result;
    }

    @RequestMapping("/fileInfo")
    public Map getFile(String params) {
        JSONObject object = new JSONObject();
        try {
            object = JSON.parseObject(params);
        } catch (Exception e) {
            object.put("fileName", params);
        }
        logger.warn("download file..." + object.toJSONString());
        Map map = new HashMap();
        try {
            map.putAll(fileService.getFile(object));
            map.put("code", "000");
            map.put("result", "success");
        } catch (Exception e) {
            map.put("code", "500");
            map.put("result", "fail");
        }
        return map;
    }

    public Map saveFile(String str, String path) {
        Map map;
        JSONObject jsonObject;
        try {
            jsonObject = JSONObject.parseObject(str);
            JSONObject mesh = jsonObject.getJSONObject("Project_Mesh");
            map = jsonObject.getJSONObject("Head");
            map.put("File_Path", path);
            map.put("Author_User_ID", 1);
            map.put("Mesh_ID", mesh.getString("Mesh_ID"));
            map.put("Mesh_Name", mesh.getString("Mesh_Name"));
            map.put("info", str);
            fileService.saveFile(map);
            return map;
        } catch (Exception e) {
//            map.put("File_Path", path);
//            fileService.saveFile(map);
//            logger.error(e.getCause());
            return null;
        }
    }
}
