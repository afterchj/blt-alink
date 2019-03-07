package com.tpadsz.after.controller;

import com.alibaba.fastjson.JSONObject;
import com.tpadsz.after.entity.dd.ResultDict;
import com.tpadsz.after.service.ConsoleLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

/**
 * Created by hongjian.chen on 2019/3/7.
 */

@Controller
@RequestMapping("/lightConsole")
public class AlinkConsoleController extends BaseDecodedController {

    @Autowired
    private ConsoleLogService logService;

    @RequestMapping("/show")
    public void bltInfo(@ModelAttribute("decodedParams") JSONObject param, ModelMap model) {
        model.put("result", ResultDict.SUCCESS.getCode());
        model.put("result_message", ResultDict.SUCCESS.getValue());
    }

    @RequestMapping("/switch")
    public void consoleSwitch(HttpSession session, @ModelAttribute("decodedParams") JSONObject param, ModelMap model) {
        session.setAttribute("param", param);
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
