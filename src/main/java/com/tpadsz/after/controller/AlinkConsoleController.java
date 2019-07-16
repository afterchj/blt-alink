package com.tpadsz.after.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tpadsz.after.entity.dd.ResultDict;
import com.tpadsz.after.exception.NotExitException;
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
        List scenes = bltConsoleService.getScenes(param);
        List groups = bltConsoleService.getGroups(param);
        int total = bltConsoleService.getTotal(param);
        Integer pid = bltConsoleService.getPid(param);
        Map info = new HashMap();
        info.put("total", total);
        info.put("pid", pid);
        if (scenes.size() == 0) {
            model.put("result", ResultDict.NO_SCENE.getCode());
            model.put("result_message", ResultDict.NO_SCENE.getValue());
        } else {
            info.put("scenes", scenes);
        }
        if (groups.size() == 0) {
            model.put("result", ResultDict.NO_GROUP.getCode());
            model.put("result_message", ResultDict.NO_GROUP.getValue());
        } else {
            info.put("groups", groups);
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
        JSONArray gids = param.getJSONArray("gids");
        if (gids != null) {
            for (Object gid : gids) {
                param.put("gid", gid);
                bltConsoleService.saveOperation(param);
            }
        } else {
            bltConsoleService.saveOperation(param);
        }
        logger.info("result=" + param.getString("result"));
        model.put("result", ResultDict.SUCCESS.getCode());
        model.put("result_message", ResultDict.SUCCESS.getValue());
    }

    @RequestMapping("/rename")
    public void consoleRenameScene(HttpSession session, @ModelAttribute("decodedParams") JSONObject param, ModelMap model) {
        session.setAttribute("param", param);
        try {
            bltConsoleService.saveSceneName(param);
            model.put("result", ResultDict.SUCCESS.getCode());
            model.put("result_message", ResultDict.SUCCESS.getValue());
        } catch (NotExitException e) {
            model.put("result", 601);
            model.put("result_message", e.getMessage());
        }
    }

    @RequestMapping("/clean")
    public void consoleCleanScene(HttpSession session, @ModelAttribute("decodedParams") JSONObject param, ModelMap model) {
        session.setAttribute("param", param);
        String flag = param.getString("bltFlag");
        int sceneId = param.getInteger("sceneId");
        if ("1".equals(flag)) {
            try {
                bltConsoleService.getCount(param);
                if (sceneId > 3) {
                    bltConsoleService.deleteScene(param);
                } else {
                    String sname = "场景" + (sceneId + 1);
                    param.put("sname", sname);
                    bltConsoleService.saveSceneName(param);
                    bltConsoleService.restScene(param);
                }
                model.put("result", ResultDict.SUCCESS.getCode());
                model.put("result_message", ResultDict.SUCCESS.getValue());
            } catch (NotExitException e) {
                model.put("result", ResultDict.SYSTEM_ERROR.getCode());
                model.put("result_message", e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @RequestMapping("/apply")
    public void consoleApplyScene(HttpSession session, @ModelAttribute("decodedParams") JSONObject param, ModelMap model) {
        session.setAttribute("param", param);
//        String x = param.getString("x");
//        String y = param.getString("y");
//        if (StringUtils.isNotEmpty(x) && StringUtils.isNotEmpty(y)) {
//            bltConsoleService.saveApplyScene(param);
//        }
        List lights = bltConsoleService.getLightsInfo(param);
        List groups = bltConsoleService.getGroupsInfo(param);
        if (groups.size() > 0) {
            model.put("groups", groups);
        }
        if (lights.size() > 0) {
            model.put("lights", lights);
        }
        model.put("result", ResultDict.SUCCESS.getCode());
        model.put("result_message", ResultDict.SUCCESS.getValue());
    }
}
