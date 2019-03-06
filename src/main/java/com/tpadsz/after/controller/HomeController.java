package com.tpadsz.after.controller;



import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hongjian.chen on 2018/8/1.
 */
@Controller
@RequestMapping(value = "/home")
public class HomeController {


    private Logger log = Logger.getLogger(HomeController.class);

    @RequestMapping(value = "/toChat")
    public String toChat() {
        return "websocket";
    }

    @RequestMapping(value = "/login")
    public String login() {
        return "userInfo";
    }

    @RequestMapping(value = "/meshSystem")
    public String toMeshSystem(ModelMap modelMap) {
        List<Map<String, String>> items = new ArrayList<Map<String, String>>();
        for (int i = 0; i < 3; i++) {
            Map map = new HashMap();
            map.put("mid", "100" + (i + 1));
            map.put("mName", "test" + i);
            items.add(map);
        }
        modelMap.put("items", items);
        return "meshSystem";
    }

    @RequestMapping(value = "/productInfo")
    public String toProductInfo() {
        return "productInfo";
    }

    @RequestMapping(value = "/lightInfo")
    public String toLightInfo() {
        return "lightInfo";
    }

    @RequestMapping(value = "/meshInfo")
    public String toMeshInfo() {
        return "meshInfo";
    }

    @RequestMapping(value = "/userInfo")
    public String toUserInfo() {
        return "userInfo";
    }

    @RequestMapping(value = "/loginOut")
    public String loginOut() {
        return "forward:/toChat";
    }
}
