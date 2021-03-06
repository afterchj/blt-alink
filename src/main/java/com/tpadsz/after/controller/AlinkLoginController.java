package com.tpadsz.after.controller;

import com.alibaba.fastjson.JSONObject;
import com.tpadsz.after.constants.MemcachedObjectType;
import com.tpadsz.after.entity.AppUser;
import com.tpadsz.after.entity.LoginLog;
import com.tpadsz.after.entity.dd.ResultDict;
import com.tpadsz.after.exception.*;
import com.tpadsz.after.service.AlinkLoginService;
import com.tpadsz.after.service.ValidationService;
import net.rubyeye.xmemcached.XMemcachedClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/account")
public class AlinkLoginController extends BaseDecodedController {

    @Resource
    private AlinkLoginService alinkLoginService;

    @Autowired
    ValidationService validationService;

    @Autowired
    private XMemcachedClient client;

    static final String URL = "http://odelic.cn/blt_alink/account";

    @RequestMapping(value = "/pwd/login", method = RequestMethod.POST)
    public String pwdLogin(@ModelAttribute("decodedParams") JSONObject params, ModelMap model) {
        ResultDict result = ResultDict.SUCCESS;
        try {
            String input = params.getString("input");
            String pwd = params.getString("pwd");
            String inputFlag = params.getString("inputFlag");
            if (StringUtils.isBlank(input) || StringUtils.isBlank(pwd)) {
                result = ResultDict.PARAMS_BLANK;
            } else {
                AppUser appUser = alinkLoginService.loginByTpad(input, pwd, inputFlag);
                if (appUser.getStatus() == 0) {
                    result = ResultDict.ACCOUNT_DISABLED;
                } else {
                    String token = alinkLoginService.generateToken(appUser.getId());
                    model.put("user", appUser);
                    model.put("token", token);
                    LoginLog loginLog = new LoginLog();
                    loginLog.setUid(appUser.getId());
                    loginLog.setAccount(input);
                    loginLog.setLogin_date(new Date());
                    loginLog.setBehavior(URL + "/pwd/login.json");
                    alinkLoginService.saveLoginLog(loginLog);
                }
            }
        } catch (PasswordNotCorrectException e) {
            result = ResultDict.PASSWORD_NOT_CORRECT;
        } catch (AccountNotCorrectException e) {
            result = ResultDict.ACCOUNT_NOT_CORRECT;
        } catch (AdminNotAllowedException e) {
            result = ResultDict.ADMIN_NOT_ALLOWED;
        } catch (SystemAlgorithmException e) {
            result = ResultDict.SYSTEM_ERROR;
        }
        model.put("result", result.getCode());
        model.put("result_message", result.getValue());
        return null;
    }

    @RequestMapping(value = "/code/login", method = RequestMethod.POST)
    public String codeLogin(@ModelAttribute("decodedParams") JSONObject params, ModelMap model) {
        String mobile = params.getString("mobile");
        String code = params.getString("code");
        try {
            if (StringUtils.isBlank(code) || StringUtils.isBlank(mobile)) {
                model.put("result", ResultDict.PARAMS_BLANK.getCode());
                model.put("result_message", ResultDict.PARAMS_BLANK.getValue());
            } else {
                validationService.checkCode(code, mobile);
                AppUser appUser = alinkLoginService.findUserByMobile(mobile);
                if (appUser == null) {
                    model.put("result", ResultDict.MOBILE_NOT_EXISTED.getCode());
                    model.put("result_message", ResultDict.MOBILE_NOT_EXISTED.getValue());
                } else if (appUser.getStatus() == 0) {
                    model.put("result", ResultDict.ACCOUNT_DISABLED.getCode());
                    model.put("result_message", ResultDict.ACCOUNT_DISABLED.getValue());
                } else {
                    Integer role_id = alinkLoginService.findRoleIdByUid(appUser.getId());
                    if(role_id==3){
                        appUser.setUtype("manager");
                    }
                    if(role_id==14){
                        appUser.setUtype("salesman");
                    }
                    String token = alinkLoginService.generateToken(appUser.getId());
                    model.put("token", token);
                    appUser.setPwd(null);
                    appUser.setSalt(null);
                    LoginLog loginLog = new LoginLog();
                    loginLog.setUid(appUser.getId());
                    loginLog.setMobile(mobile);
                    loginLog.setLogin_date(new Date());
                    loginLog.setBehavior(URL + "/code/login.json");
                    alinkLoginService.saveLoginLog(loginLog);
                    model.put("user", appUser);
                    model.put("result", ResultDict.SUCCESS.getCode());
                    model.put("result_message", ResultDict.SUCCESS.getValue());
                }
            }
        } catch (InvalidCodeException e) {
            model.put("result", ResultDict.VERIFY_ERROR.getCode());
            model.put("result_message", ResultDict.VERIFY_ERROR.getValue());
        } catch (Exception e) {
            model.put("result", ResultDict.SYSTEM_ERROR.getCode());
            model.put("result_message", ResultDict.SYSTEM_ERROR.getValue());
        }
        return null;
    }


    /**
     * 退出登录接口
     *
     * @param params uid:用户id
     * @param model
     */
    @RequestMapping(value = "loginOut", method = RequestMethod.POST)
    public void loginOut(@ModelAttribute("decodedParams") JSONObject params, ModelMap model) {
        String uid = params.getString("uid");
        String key = MemcachedObjectType.CACHE_TOKEN.getPrefix() + uid;
        if (StringUtils.isBlank(uid)) {
            //参数不能为空
            model.put("result", ResultDict.PARAMS_BLANK.getCode());
            model.put("result_message", ResultDict.PARAMS_BLANK.getCode());
            return;
        }
        try {
            client.delete(key);
            alinkLoginService.loginOut(uid);
            model.put("result", ResultDict.SUCCESS.getCode());
            model.put("result_message", ResultDict.SUCCESS.getCode());
            return;
        } catch (Exception e) {
            e.printStackTrace();
            //系统错误
            model.put("result", ResultDict.SYSTEM_ERROR.getCode());
            model.put("result_message", ResultDict.SYSTEM_ERROR.getCode());
        }
    }

}
