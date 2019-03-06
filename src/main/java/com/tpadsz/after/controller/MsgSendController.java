package com.tpadsz.after.controller;


import com.tpadsz.after.exception.InvalidCodeException;
import com.tpadsz.after.service.ValidationService;
import com.tpadsz.after.util.Constants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by after on 2018/12/12.
 */

@RestController
@RequestMapping("/msg")
public class MsgSendController {

    @Autowired
    ValidationService validationService;

    @RequestMapping("/verify")
    public Map testSend(String appid, String mobile) {
        appid = StringUtils.isEmpty(appid) ? "12" : appid;
        Map map = new HashMap();
        String str = Constants.PROPERTIES_LOADER.getProperty("message.sender.appid" + appid + ".msg");
        String code = null;
        try {
            code = validationService.sendCode(appid, mobile);
        } catch (Exception e) {
            map.put("value", e.getMessage());
        }
        map.put("content", str);
        map.put("code", code);
        return map;
    }

    @RequestMapping("/login")
    public Map verifyCode(String code, String mobile) {
        Map map = new HashMap();
        try {
            validationService.checkCode(code, mobile);
            map.put("result", "000");
        } catch (InvalidCodeException e) {
            map.put("error", e.getMessage());
        }
        return map;
    }
}
