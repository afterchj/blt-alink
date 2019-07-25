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
    public Map uploadFile(String params, MultipartFile file) {
        JSONObject object = JSON.parseObject(params);
        Map map = new HashMap();
        logger.warn("params=" + params);
        String path = PropertiesUtil.getPath();
        String str = file.getOriginalFilename();
        String fileName = str;
        int prefix = str.lastIndexOf("_");
        if (prefix != -1) {
            fileName = str.substring(0, prefix);
        }
        File targetFile = new File(path, str);
        if (!targetFile.getParentFile().exists()) {
            targetFile.getParentFile().mkdirs();
        }
        try {
            if (StringUtils.isNotEmpty(fileName)) {
                file.transferTo(targetFile);
            }
            object.put("file_name", fileName);
            object.put("file_path", "http://iotsztp.com/file/ota/" + str);
            map.put("code", "000");
            map.put("result", "success");
            fileService.saveFile(object);
        } catch (Exception e) {
            map.put("code", "500");
            map.put("result", "fail");
            logger.error("error:" + e.getMessage());
        }
        return map;
    }

    @RequestMapping("/fileInfo")
    public Map getFile() {
        logger.warn("download file...");
        Map map = new HashMap();
        try {
            map = fileService.getFile(null);
            map.put("code", "000");
            map.put("result", "success");
        } catch (Exception e) {
            map.put("code", "500");
            map.put("result", "fail");
        }
        return map;
    }
}
