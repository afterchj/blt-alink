package com.tpadsz.after.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tpadsz.after.service.PCFileService;
import com.tpadsz.after.util.PropertiesUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
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
    public String uploadFile(String params, MultipartFile file) {
        String path = PropertiesUtil.getPath("upload");
        String downloadPath = PropertiesUtil.getPath("otaPath");
        Map map = new HashMap();
        String str = file.getOriginalFilename();
        String fileName = str.substring(0,str.lastIndexOf("."));
        File targetFile = new File(path, str);
        if (!targetFile.getParentFile().exists()) {
            targetFile.getParentFile().mkdirs();
        }
        try {
            if (StringUtils.isNotEmpty(fileName)) {
                file.transferTo(targetFile);
            }
            map.put("file_name", fileName);
            map.put("file_path", downloadPath + str);
            map.put("code", "000");
            map.put("result", "success");
            fileService.saveFile(map);
            logger.warn("map=" + JSON.toJSONString(map));
        } catch (Exception e) {
            e.printStackTrace();
            map.put("code", "500");
            map.put("result", "fail");
            return "500";
        }
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
}
