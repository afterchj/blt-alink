package com.tpadsz.after.controller;

import com.alibaba.fastjson.JSONObject;
import com.tpadsz.after.entity.AppUser;
import com.tpadsz.after.entity.LoginLog;
import com.tpadsz.after.entity.dd.ResultDict;
import com.tpadsz.after.exception.AccountNotCorrectException;
import com.tpadsz.after.exception.SystemAlgorithmException;
import com.tpadsz.after.service.AlinkLoginService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @program: blt-light
 * @description: 登录界面接口
 * @author: Mr.Ma
 * @create: 2019-03-06 11:12
 **/
@Controller
@RequestMapping("/alink")
public class AlinkLoginController extends BaseDecodedController {

    @Resource
    private AlinkLoginService alinkLoginService;

    static final String URL = "http://odelic.com.cn:8080/blt-alink/alink/";

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(@ModelAttribute("decodedParams") JSONObject params, ModelMap model) {
        ResultDict result = ResultDict.SUCCESS;
        try {
            String account = params.getString("account");
            String pwd = params.getString("pwd");
            if (StringUtils.isBlank(account) || StringUtils.isBlank(pwd)) {
                result = ResultDict.PARAMS_BLANK;
            } else {
                AppUser appUser = alinkLoginService.loginByTpad("13", account, pwd);
                model.put("user", appUser);
                LoginLog loginLog = new LoginLog();
                loginLog.setUid(appUser.getId());
                loginLog.setAccount(account);
                loginLog.setLogin_date(new Date());
                loginLog.setBehavior(URL + "login.json");
                alinkLoginService.saveLoginLog(loginLog);
            }
        } catch (AccountNotCorrectException e) {
            result = ResultDict.ACCOUNT_NOT_CORRECT;
        } catch (SystemAlgorithmException e) {
            result = ResultDict.SYSTEM_ERROR;
        }
        model.put("result", result.getCode());
        model.put("result_message", result.getValue());
        return null;
    }


    @RequestMapping(value = "loginOut", method = RequestMethod.POST)
    public void loginOut(@ModelAttribute("decodedParams") JSONObject params, ModelMap model) {
        String uid = params.getString("uid");
        alinkLoginService.loginOut(uid);
        model.put("result", ResultDict.SUCCESS.getCode());

    }
}
