package com.tpadsz.after.controller;

import com.alibaba.fastjson.JSONObject;
import com.tpadsz.after.entity.AppUser;
import com.tpadsz.after.entity.LoginLog;
import com.tpadsz.after.entity.dd.ResultDict;
import com.tpadsz.after.exception.AccountNotCorrectException;
import com.tpadsz.after.exception.InvalidCodeException;
import com.tpadsz.after.exception.PasswordNotCorrectException;
import com.tpadsz.after.exception.SystemAlgorithmException;
import com.tpadsz.after.service.AlinkLoginService;
import com.tpadsz.after.service.ValidationService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import java.util.Date;

import static com.tpadsz.after.entity.dd.ResultDict.SYSTEM_ERROR;

/**
 * @program: blt-light
 * @description: 登录界面接口
 * @author: Mr.Ma
 * @create: 2019-03-06 11:12
 **/
@Controller
@RequestMapping("/account")
public class AlinkLoginController extends BaseDecodedController {

    @Resource
    private AlinkLoginService alinkLoginService;

    @Autowired
    ValidationService validationService;

    static final String URL = "http://odelic.com.cn:8080/blt-alink/account";

    @RequestMapping(value = "/pwd/login", method = RequestMethod.POST)
    public String pwdLogin(@ModelAttribute("decodedParams") JSONObject params, ModelMap model) {
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
                loginLog.setBehavior(URL + "/pwd/login.json");
                alinkLoginService.saveLoginLog(loginLog);
            }
        } catch (PasswordNotCorrectException e) {
            result = ResultDict.PASSWORD_NOT_CORRECT;
        } catch (AccountNotCorrectException e) {
            result = ResultDict.ACCOUNT_NOT_CORRECT;
        } catch (SystemAlgorithmException e) {
            result = SYSTEM_ERROR;
        }
        model.put("result", result.getCode());
        model.put("result_message", result.getValue());
        return null;
    }

    @RequestMapping(value = "/verify", method = RequestMethod.POST)
    public String verify(@ModelAttribute("decodedParams") JSONObject params, ModelMap model) {
        String mobile = params.getString("mobile");
        if (StringUtils.isBlank(mobile)) {
            model.put("result", ResultDict.PARAMS_BLANK.getCode());
            model.put("result_message", ResultDict.PARAMS_BLANK.getValue());
        }
        try {
            AppUser appUser = alinkLoginService.findUserByMobile(mobile);
            if (appUser == null) {
                model.put("result", ResultDict.MOBILE_NOT_EXISTED.getCode());
                model.put("result_message", ResultDict.MOBILE_NOT_EXISTED.getValue());
            } else {
                validationService.sendCode("12", mobile);
                model.put("result", ResultDict.SUCCESS.getCode());
                model.put("result_message", ResultDict.SUCCESS.getValue());
            }
        } catch (Exception e) {
            model.put("result", SYSTEM_ERROR.getCode());
            model.put("result_message", SYSTEM_ERROR.getValue());
        }
        return null;
    }


    @RequestMapping(value = "/code/login", method = RequestMethod.POST)
    public String codeLogin(@ModelAttribute("decodedParams") JSONObject params, ModelMap model) {
        String mobile = params.getString("mobile");
        String code = params.getString("code");
        try {
            if (StringUtils.isNotEmpty(code) && StringUtils.isNotEmpty(mobile)) {
                validationService.checkCode(code, mobile);
            }
            AppUser appUser = alinkLoginService.findUserByMobile(mobile);
            LoginLog loginLog = new LoginLog();
            loginLog.setUid(appUser.getId());
            loginLog.setMobile(mobile);
            loginLog.setLogin_date(new Date());
            loginLog.setBehavior(URL + "/code/login.json");
            alinkLoginService.saveLoginLog(loginLog);
            model.put("user", appUser);
            model.put("result", ResultDict.SUCCESS.getCode());
            model.put("result_message", ResultDict.SUCCESS.getValue());
        }catch (InvalidCodeException e) {
            model.put("result", ResultDict.VERIFY_ERROR.getCode());
            model.put("result_message", ResultDict.VERIFY_ERROR.getValue());
        } catch (Exception e) {
            model.put("result", SYSTEM_ERROR.getCode());
            model.put("result_message", SYSTEM_ERROR.getValue());
        }
        return null;
    }


    /**
     * 退出登录接口
     * @param params uid:用户id
     * @param model
     */
    @RequestMapping(value = "loginOut",method = RequestMethod.POST)
    public void loginOut(@ModelAttribute("decodedParams") JSONObject params, ModelMap model){
        String uid = params.getString("uid");
        if (StringUtils.isBlank(uid)){
            //参数不能为空
            model.put("result", ResultDict.PARAMS_BLANK.getCode());
            model.put("result_message",ResultDict.PARAMS_BLANK.getCode());
            return;
        }
        try {
            alinkLoginService.loginOut(uid);
            model.put("result", ResultDict.SUCCESS.getCode());
            model.put("result_message",ResultDict.SUCCESS.getCode());
            return;
        } catch (Exception e) {
            e.printStackTrace();
            //系统错误
            model.put("result", SYSTEM_ERROR.getCode());
            model.put("result_message", SYSTEM_ERROR.getCode());
        }
    }

    @RequestMapping(value = "/generator", method = RequestMethod.POST)
    public String generator(@ModelAttribute("decodedParams") JSONObject params, ModelMap model) {
        try{
            String meshId = "11112345";
            alinkLoginService.insert();
        }catch (DuplicateKeyException e){
            System.out.println();
        }

        model.put("result", "123");
        model.put("result_message", "成功");
        return null;
    }

}
