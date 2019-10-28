package com.tpadsz.after.controller;

import com.alibaba.fastjson.JSONObject;
import com.tpadsz.after.entity.dd.ResultDict;
import com.tpadsz.after.entity.time.ProjectTimer;
import com.tpadsz.after.service.TimeLineService;
import com.tpadsz.after.util.UrlUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
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

    Logger logger = LoggerFactory.getLogger(TimeLineController.class);

    /**
     * 创建定时TimeLine
     * @param params
     * @param model
     */
    @RequestMapping(value = "/create",method = RequestMethod.POST)
    public void create(@ModelAttribute("decodedParams") JSONObject params, ModelMap model, HttpServletRequest request) {
        boolean success = timeLineService.create(params);
        String result = ResultDict.SUCCESS.getCode();
        String resultMessage = ResultDict.SUCCESS.getValue();
        if (!success){
            result = ResultDict.SYSTEM_ERROR.getCode();
            resultMessage = ResultDict.SUCCESS.getValue();
            logger.error("controller:[TimeLineController],method:[create],result:[{}]",resultMessage);
        }
        model.put("result", result);
        model.put("result_message", resultMessage);
        model.put("url", UrlUtils.getModelUrl(request));
    }

    /**
     * 重命名TimeLine
     * @param params
     * @param model
     */
    @RequestMapping(value = "/rename",method = RequestMethod.POST)
    public void rename(@ModelAttribute("decodedParams") JSONObject params, ModelMap model, HttpServletRequest request){
        timeLineService.rename(params);
        model.put("result", ResultDict.SUCCESS.getCode());
        model.put("result_message", ResultDict.SUCCESS.getValue());
        model.put("url", UrlUtils.getModelUrl(request));
    }

    /**
     * 更新定时TimeLine
     * @param params
     * @param model
     */
    @RequestMapping(value = "/update",method = RequestMethod.POST)
    public void update(@ModelAttribute("decodedParams") JSONObject params, ModelMap model, HttpServletRequest request){
        String uid = params.getString("uid");
        String meshId = params.getString("meshId");
        Integer tid = params.getInteger("tid");
        timeLineService.deleteOne(uid,meshId,tid);
        timeLineService.create(params);
        model.put("result", ResultDict.SUCCESS.getCode());
        model.put("result_message", ResultDict.SUCCESS.getValue());
        model.put("url", UrlUtils.getModelUrl(request));
    }

    /**
     * 删除定时TimeLine(批量删除)
     * @param params
     * @param model
     */
    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    public void delete(@ModelAttribute("decodedParams") JSONObject params, ModelMap model, HttpServletRequest request){
        timeLineService.delete(params);
        model.put("result", ResultDict.SUCCESS.getCode());
        model.put("result_message", ResultDict.SUCCESS.getValue());
        model.put("url", UrlUtils.getModelUrl(request));
    }

    /**
     * 获取定时TimeLine
     * @param params
     * @param model
     */
    @RequestMapping(value = "/get",method = RequestMethod.POST)
    public void get(@ModelAttribute("decodedParams") JSONObject params, ModelMap model, HttpServletRequest request){
        List<JSONObject>  timeLineList = timeLineService.get(params);
        model.put("timeLineList",timeLineList);
        model.put("result", ResultDict.SUCCESS.getCode());
        model.put("result_message", ResultDict.SUCCESS.getValue());
        model.put("url", UrlUtils.getModelUrl(request));
    }

    /**
     * 全开全关接口
     * @param params
     * @param model
     */
    @RequestMapping(value = "/onOrOff", method = RequestMethod.POST)
    public void allOnOrAllOff(@ModelAttribute("decodedParams") JSONObject params, ModelMap model, HttpServletRequest request){
        timeLineService.updateTimeLineState(params);
        model.put("result", ResultDict.SUCCESS.getCode());
        model.put("result_message", ResultDict.SUCCESS.getValue());
        model.put("url", UrlUtils.getModelUrl(request));
    }

    /**
     *PC端补充网络ID数量的接口
     * @param params
     * @param model
     */
    @RequestMapping(value = "/createMeshId", method = RequestMethod.POST)
    public void createMeshIdToPC(@ModelAttribute("decodedParams") JSONObject params,ModelMap model, HttpServletRequest request){
        String meshIds = timeLineService.createMeshToPC(params);
        model.put("result", ResultDict.SUCCESS.getCode());
        model.put("result_message", ResultDict.SUCCESS.getValue());
        model.put("meshIds",meshIds);
        model.put("url", UrlUtils.getModelUrl(request));
    }

    /**
     * 获取一个项目下定时信息
     * @param params projectId:项目id uid:用户id
     * @param model projectTimer:包含项目-网络-定时层级的集合
     */
    @RequestMapping(value = "/getProjectTimers",method = RequestMethod.POST)
    public void getProjectTimers(@ModelAttribute("decodedParams") JSONObject params,ModelMap model, HttpServletRequest request){
        ProjectTimer projectTimer = timeLineService.getProjectTimers(params);
        model.put("result", ResultDict.SUCCESS.getCode());
        model.put("result_message", ResultDict.SUCCESS.getValue());
        model.put("data",projectTimer);
        model.put("url", UrlUtils.getModelUrl(request));
    }
}
