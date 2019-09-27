package com.tpadsz.after.controller;

import com.alibaba.fastjson.JSONObject;
import com.tpadsz.after.entity.dd.ResultDict;
import com.tpadsz.after.exception.NameDuplicateException;
import com.tpadsz.after.service.GroupOperationService;
import com.tpadsz.after.service.PlaceService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
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

    @Resource
    private GroupOperationService groupOperationService;

    /**
     * 创建区域
     *
     * @param params
     * @param model
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public void create(@ModelAttribute("decodedParams") JSONObject params, ModelMap model) {
        try {
            Map<String, Object> placeMap = placeService.create(params);
            params.put("pid",placeMap.get("pid"));
//            Map<String,Object> groupMap = groupOperationService.saveGroup(params);
//            placeMap.put("group",groupMap);
            model.put("result", ResultDict.SUCCESS.getCode());
            model.put("result_message", ResultDict.SUCCESS.getValue());
            model.put("place",placeMap);
        } catch (NameDuplicateException e) {
//            e.printStackTrace(); 区域名重复
            model.put("result", ResultDict.DUPLICATE_PLACE_NAME.getCode());
            model.put("result_message", ResultDict.DUPLICATE_PLACE_NAME.getValue());
        }

    }

    /**
     * 删除区域
     *
     * @param params
     * @param model
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public void delete(@ModelAttribute("decodedParams") JSONObject params, ModelMap model) {
        placeService.delete(params);
        model.put("result", ResultDict.SUCCESS.getCode());
        model.put("result_message", ResultDict.SUCCESS.getValue());
    }

    /**
     * 重命名区域
     *
     * @param params
     * @param model
     */
    @RequestMapping(value = "/rename", method = RequestMethod.POST)
    public void rename(@ModelAttribute("decodedParams") JSONObject params, ModelMap model) {
        try {
            placeService.rename(params);
            model.put("result", ResultDict.SUCCESS.getCode());
            model.put("result_message", ResultDict.SUCCESS.getValue());
        } catch (NameDuplicateException e) {
            e.printStackTrace();//区域名重复
            model.put("result", ResultDict.DUPLICATE_PLACE_NAME.getCode());
            model.put("result_message", ResultDict.DUPLICATE_PLACE_NAME.getValue());
        }

    }

    /**
     * 获取服务端最新PC版本
     * @param params meshId
     * @param model 返回最新版本号
     */
    @RequestMapping(value = "/getNewestFileVersionCode", method = RequestMethod.POST)
    public void getNewestFileVersionCode(@ModelAttribute("decodedParams") JSONObject params, ModelMap model){
        Integer versionCode = placeService.getVersionCode(params);
        model.put("result", ResultDict.SUCCESS.getCode());
        model.put("result_message", ResultDict.SUCCESS.getValue());
        model.put("versionCode",versionCode);
    }

}
