package com.tpadsz.after.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tpadsz.after.entity.LightActive;
import com.tpadsz.after.entity.dd.ResultDict;
import com.tpadsz.after.service.PairingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletException;
import java.io.IOException;

/**
 * Created by Administrator on 2016/8/28.
 */
@Controller
public class LightController extends BaseDecodedController {

    private PairingService pairingService;

    @RequestMapping(value = "/pairing", method = RequestMethod.POST)
    private String pairing(@ModelAttribute("decodedParams") JSONObject params, ModelMap model) throws ServletException, IOException {
//        String searchName = params.getString("searchName");
        System.out.println("params=" + params);
//        PrintWriter out = res.getWriter();
        LightActive lightActive = pairingService.findMacAddress("FC-AA-14-2B-22-FB");
        model.put("result", ResultDict.SUCCESS.getCode());
        System.out.println("show=" + JSON.toJSONString(lightActive));
        return null;
    }

    @Autowired
    public void setPairingService(PairingService pairingService) {
        this.pairingService = pairingService;
    }

}
