package com.tpadsz.after.controller;

import com.alibaba.fastjson.JSONObject;
import com.tpadsz.after.entity.dd.ResultDict;
import com.tpadsz.after.exception.NameDuplicateException;
import com.tpadsz.after.service.PlaceService;
import com.tpadsz.after.util.UrlUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: blt-alink
 * @description:
 * @author: Mr.Ma
 * @create: 2019-08-01 09:29
 **/
@Controller
@RequestMapping("/place")
public class PlaceController extends BaseDecodedController {

    @Resource
    private PlaceService placeService;

    Logger logger = LoggerFactory.getLogger(PlaceController.class);

    /**
     * 创建区域
     *
     * @param params
     * @param model
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public void create(@ModelAttribute("decodedParams") JSONObject params, ModelMap model, HttpServletRequest request) {
        String result = ResultDict.SUCCESS.getCode();
        String resultMessage = ResultDict.SUCCESS.getValue();
        try {
            Map<String, Object> placeMap = placeService.create(params);
//            params.put("pid",placeMap.get("pid"));
            model.put("place",placeMap);
        } catch (NameDuplicateException e) {
//            e.printStackTrace(); 区域名重复
            result = ResultDict.DUPLICATE_PLACE_NAME.getCode();
            resultMessage = ResultDict.DUPLICATE_PLACE_NAME.getValue();
            String method = Thread.currentThread().getStackTrace()[1].getMethodName();
            errorMessageLogger(method,e);
        }
        model.put("result", result);
        model.put("result_message", resultMessage);
        model.put("url",UrlUtils.getModelUrl(request));
    }

    /**
     * 删除区域
     *
     * @param params
     * @param model
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public void delete(@ModelAttribute("decodedParams") JSONObject params, ModelMap model, HttpServletRequest request) {
        placeService.delete(params);
        model.put("result", ResultDict.SUCCESS.getCode());
        model.put("result_message", ResultDict.SUCCESS.getValue());
        model.put("url",UrlUtils.getModelUrl(request));
    }

    /**
     * 重命名区域
     *
     * @param params
     * @param model
     */
    @RequestMapping(value = "/rename", method = RequestMethod.POST)
    public void rename(@ModelAttribute("decodedParams") JSONObject params, ModelMap model, HttpServletRequest request) {
        String result = ResultDict.SUCCESS.getCode();
        String resultMessage = ResultDict.SUCCESS.getValue();
        try {
            placeService.rename(params);
        } catch (NameDuplicateException e) {
//            e.printStackTrace();//区域名重复
            result = ResultDict.DUPLICATE_PLACE_NAME.getCode();
            resultMessage = ResultDict.DUPLICATE_PLACE_NAME.getValue();
            String method = Thread.currentThread().getStackTrace()[1].getMethodName();
            errorMessageLogger(method,e);
        }
        model.put("result", result);
        model.put("result_message", resultMessage);
        model.put("url",UrlUtils.getModelUrl(request));
    }

    /**
     * 获取服务端最新PC版本
     */
    @RequestMapping(value = "/getNewestFileVersionCode", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> getNewestFileVersionCode( @RequestBody JSONObject params, HttpServletRequest request){
        Map<String, Object> map = new HashMap<>();
        String meshId = params.getString("meshId");
        Integer versionCode = placeService.getVersionCode(meshId);
        map.put("result", ResultDict.SUCCESS.getCode());
        map.put("result_message", ResultDict.SUCCESS.getValue());
        map.put("versionCode",versionCode);
        map.put("url",UrlUtils.getModelUrl(request));
        return map;
    }

    private void errorMessageLogger(String method,Exception e){
        Object[] errArr = {this.getClass().getSimpleName(),method,e.getMessage()};
        logger.error("controller:[{}],method:[{}],result:[{}]", errArr);
    }
}
