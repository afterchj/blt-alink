package com.tpadsz.after.controller;


import com.alibaba.fastjson.JSONObject;
import com.tpadsz.after.entity.dd.ResultDict;
import com.tpadsz.after.exception.InvalidCodeException;
import com.tpadsz.after.service.UserService;
import com.tpadsz.after.service.ValidationService;
import com.tpadsz.after.util.Encryption;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by hongjian.chen on 2019/5/5.
 */
@Controller
public class MainController extends BaseDecodedController {

    private Logger logger = Logger.getLogger(MainController.class);
    @Resource
    private UserService userService;
    @Resource
    private ValidationService validationService;


    @RequestMapping("/restPwd")
    public void updateUser(@ModelAttribute("decodedParams") JSONObject param, ModelMap model) {
        String code = param.getString("code");
        String mobile = param.getString("mobile");
        String plainPwd = param.getString("pwd");
        String md5Pwd = Encryption.getMD5Str(plainPwd);
        Encryption.HashPassword password = Encryption.encrypt(md5Pwd);
        param.put("pwd", password.getPassword());
        param.put("salt", password.getSalt());
        try {
            validationService.checkCode(code, mobile);
        } catch (InvalidCodeException e) {
            model.put("result", ResultDict.VERIFY_ERROR.getCode());
            model.put("result_message", ResultDict.VERIFY_ERROR.getValue());
        }
        userService.updateUser(param);
        model.put("result", ResultDict.SUCCESS.getCode());
        model.put("result_message", ResultDict.SUCCESS.getValue());
    }
}
