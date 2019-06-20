package com.tpadsz.after.controller;

import com.alibaba.fastjson.JSONObject;
import com.tpadsz.after.entity.TimeLineList;
import com.tpadsz.after.entity.dd.ResultDict;
import com.tpadsz.after.service.TimeLineService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import java.util.List;

/**
 * @program: blt-alink
 * @description: 定时任务
 * @author: Mr.Ma
 * @create: 2019-05-27 16:34
 **/
@Controller
@RequestMapping("/timeLine")
public class TimeLineController extends BaseDecodedController{

    @Resource
    private TimeLineService timeLineService;

    /**
     * 创建定时TimeLine
     * @param params
     * @param model
     */
    @RequestMapping(value = "/create",method = RequestMethod.POST)
    public void create(@ModelAttribute("decodedParams") JSONObject params, ModelMap model) {
        boolean success = timeLineService.create(params);
        if (success){
            model.put("result", ResultDict.SUCCESS.getCode());
            model.put("result_message", ResultDict.SUCCESS.getValue());
        }else {
            model.put("result", ResultDict.SYSTEM_ERROR.getCode());
            model.put("result_message", ResultDict.SYSTEM_ERROR.getValue());
        }
    }

    /**
     * 重命名TimeLine
     * @param params
     * @param model
     */
    @RequestMapping(value = "/rename",method = RequestMethod.POST)
    public void rename(@ModelAttribute("decodedParams") JSONObject params, ModelMap model){

        timeLineService.rename(params);
        model.put("result", ResultDict.SUCCESS.getCode());
        model.put("result_message", ResultDict.SUCCESS.getValue());
    }

    /**
     * 更新定时TimeLine
     * @param params
     * @param model
     */
    @RequestMapping(value = "/update",method = RequestMethod.POST)
    public void update(@ModelAttribute("decodedParams") JSONObject params, ModelMap model){
        String uid = params.getString("uid");
        String meshId = params.getString("meshId");
        Integer tid = params.getInteger("tid");
        timeLineService.deleteOne(uid,meshId,tid);
        timeLineService.create(params);
        model.put("result", ResultDict.SUCCESS.getCode());
        model.put("result_message", ResultDict.SUCCESS.getValue());

    }

    /**
     * 删除定时TimeLine(批量删除)
     * @param params
     * @param model
     */
    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    public void delete(@ModelAttribute("decodedParams") JSONObject params, ModelMap model){

        timeLineService.delete(params);
        model.put("result", ResultDict.SUCCESS.getCode());
        model.put("result_message", ResultDict.SUCCESS.getValue());
    }

    /**
     * 获取定时TimeLine
     * @param params
     * @param model
     */
    @RequestMapping(value = "/get",method = RequestMethod.POST)
    public void get(@ModelAttribute("decodedParams") JSONObject params, ModelMap model){

        List<TimeLineList> timeLineLists = timeLineService.get(params);
        model.put("timeLineLists",timeLineLists);
        model.put("result", ResultDict.SUCCESS.getCode());
        model.put("result_message", ResultDict.SUCCESS.getValue());
    }

    /**
     * 全开全关接口
     * @param params
     * @param model
     */
    @RequestMapping(value = "/allOnOrAllOff", method = RequestMethod.POST)
    public void allOnOrAllOff(@ModelAttribute("decodedParams") JSONObject params, ModelMap model){
        timeLineService.updateTimePointState(params);
        model.put("result", ResultDict.SUCCESS.getCode());
        model.put("result_message", ResultDict.SUCCESS.getValue());
    }

    /**
     *PC端补充网络ID数量的接口
     * @param params
     * @param model
     */
    @RequestMapping(value = "/createMeshId", method = RequestMethod.POST)
    public void createMeshIdToPC(@ModelAttribute("decodedParams") JSONObject params,ModelMap model){

        String meshIds = timeLineService.createMeshToPC(params);
        model.put("result", ResultDict.SUCCESS.getCode());
        model.put("result_message", ResultDict.SUCCESS.getValue());
        model.put("meshIds",meshIds);
    }

}
