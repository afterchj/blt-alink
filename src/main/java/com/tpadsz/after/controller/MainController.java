package com.tpadsz.after.controller;


import com.tpadsz.after.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by hongjian.chen on 2019/5/5.
 */
@Controller
public class MainController {

    private Logger logger = Logger.getLogger(MainController.class);
    @Resource
    private UserService userService;

    @ResponseBody
    @RequestMapping(value = "/getFirms", method = RequestMethod.GET)
    public List<Map> getFirms(ModelMap model) {
        List<Map> firms = userService.getFirms();
        model.put("firms", firms);
        return firms;
    }
}
