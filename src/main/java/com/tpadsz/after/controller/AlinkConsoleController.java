package com.tpadsz.after.controller;

import com.alibaba.fastjson.JSONObject;
import com.tpadsz.after.entity.dd.ResultDict;
import com.tpadsz.after.service.BltConsoleService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hongjian.chen on 2019/3/7.
 */

@Controller
@RequestMapping("/lightConsole")
public class AlinkConsoleController extends BaseDecodedController {

    private Logger logger = Logger.getLogger(this.getClass());

    @Autowired
    private BltConsoleService bltConsoleService;

    @RequestMapping("/show")
    public void bltInfo(@ModelAttribute("decodedParams") JSONObject param, ModelMap model) {
        Map info = new HashMap();
        List scenes = bltConsoleService.getScenes(param);
        List groups = bltConsoleService.getGroups(param);
        if (scenes.size() > 0) {
            info.put("scenes", scenes);
        } else {
            model.put("result", ResultDict.NO_SCENE.getCode());
            model.put("result_message", ResultDict.NO_SCENE.getValue());
        }
        if (groups.size() > 0) {
            info.put("groups", groups);
        } else {
            model.put("result", ResultDict.NO_GROUP.getCode());
            model.put("result_message", ResultDict.NO_GROUP.getValue());
        }
        if (info.size() > 0) {
            model.put("data", info);
            model.put("result", ResultDict.SUCCESS.getCode());
            model.put("result_message", ResultDict.SUCCESS.getValue());
        }
    }

    @RequestMapping("/switch")
    public void consoleSwitch(HttpSession session, @ModelAttribute("decodedParams") JSONObject param, ModelMap model) {
        session.setAttribute("param", param);
        bltConsoleService.saveOperation(param);
        logger.info("result=" + param.getString("result"));
        model.put("result", ResultDict.SUCCESS.getCode());
        model.put("result_message", ResultDict.SUCCESS.getValue());
    }

    @RequestMapping("/rename")
    public void consoleRenameScene(HttpSession session, @ModelAttribute("decodedParams") JSONObject param, ModelMap model) {
        session.setAttribute("param", param);
        model.put("result", ResultDict.SUCCESS.getCode());
        model.put("result_message", ResultDict.SUCCESS.getValue());
    }

    @RequestMapping("/clean")
    public void consoleCleanScene(HttpSession session, @ModelAttribute("decodedParams") JSONObject param, ModelMap model) {
        session.setAttribute("param", param);
        model.put("result", ResultDict.SUCCESS.getCode());
        model.put("result_message", ResultDict.SUCCESS.getValue());
    }

    @RequestMapping("/apply")
    public void consoleApplyScene(HttpSession session, @ModelAttribute("decodedParams") JSONObject param, ModelMap model) {
        session.setAttribute("param", param);
        model.put("result", ResultDict.SUCCESS.getCode());
        model.put("result_message", ResultDict.SUCCESS.getValue());
    }
}
