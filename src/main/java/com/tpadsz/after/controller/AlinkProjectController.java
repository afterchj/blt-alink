package com.tpadsz.after.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by chenhao.lu on 2019/3/8.
 */

@Controller
@RequestMapping("/project")
public class AlinkProjectController {

    @RequestMapping(value = "/pro_list", method = RequestMethod.POST)
    public String find(@ModelAttribute("decodedParams") JSONObject params, ModelMap model) {
        String uid = params.getString("uid");
        String meshId = params.getString("meshId");
        try{


        }catch (Exception e){

        }
        model.put("result", "123");
        model.put("result_message", "成功");
        return null;
    }


    @RequestMapping(value = "/generator", method = RequestMethod.POST)
    public String pwdLogin(@ModelAttribute("decodedParams") JSONObject params, ModelMap model) {
        try{
            String meshId = "11112345";

        }catch (Exception e){

        }
        model.put("result", "123");
        model.put("result_message", "成功");
        return null;
    }
}
