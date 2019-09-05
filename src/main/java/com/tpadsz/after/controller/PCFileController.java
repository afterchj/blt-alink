package com.tpadsz.after.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tpadsz.after.service.PCFileService;
import com.tpadsz.after.util.FileReadUtils;
import com.tpadsz.after.util.PropertiesUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
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
    private org.apache.log4j.Logger logger = Logger.getLogger(this.getClass());

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
        saveFile(txt, downloadPath + str);
        return "000";
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

    public void saveFile(String str, String path) {
        Map map = new HashMap();
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
        } catch (Exception e) {
            map.put("File_Path", path);
            fileService.saveFile(map);
            logger.error(e.getCause());
        }
    }
}
