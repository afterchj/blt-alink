package com.tpadsz.after.controller;

import com.alibaba.fastjson.JSONObject;
import com.tpadsz.after.entity.AppUser;
import com.tpadsz.after.entity.dd.ResultDict;
import com.tpadsz.after.exception.InvalidCodeException;
import com.tpadsz.after.exception.MobileNotExistedException;
import com.tpadsz.after.exception.RepetitionException;
import com.tpadsz.after.service.AccountService;
import com.tpadsz.after.service.AlinkLoginService;
import com.tpadsz.after.service.ValidationService;
import com.tpadsz.after.util.Encryption;
import com.tpadsz.after.util.GenerateUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by hongjian.chen on 2019/3/6.
 */

@Controller
@RequestMapping("/user")
public class AccountController extends BaseDecodedController {

    private Logger logger = Logger.getLogger(this.getClass());
    @Autowired
    private AccountService accountService;
    @Autowired
    private ValidationService validationService;
    @Autowired
    private AlinkLoginService alinkLoginService;

    @RequestMapping("/fillAccount")
    public void fillAccount(@ModelAttribute("decodedParams") JSONObject param, ModelMap model) {
        String msg = param.getString("mobile");
        String code = param.getString("code");
        String plainPwd = param.getString("pwd");
        try {
            if (StringUtils.isNotEmpty(code) && StringUtils.isNotEmpty(msg)) {
                validationService.checkCode(code, msg);
            }
            if (StringUtils.isNotEmpty(plainPwd)) {
                String md5Pwd = Encryption.getMD5Str(plainPwd);
                Encryption.HashPassword password = Encryption.encrypt(md5Pwd);
                param.put("pwd", password.getPassword());
                param.put("salt", password.getSalt());
                param.put("other", "1");
            }
            accountService.updateAccount(param);
            code = ResultDict.SUCCESS.getCode();
            msg = ResultDict.SUCCESS.getValue();
        } catch (RepetitionException e) {
            code = ResultDict.UNAME_REPET.getCode();
            msg = ResultDict.UNAME_REPET.getValue();
        } catch (InvalidCodeException e1) {
            code = ResultDict.VERIFY_ERROR.getCode();
            msg = ResultDict.VERIFY_ERROR.getValue();
        } catch (Exception e) {
            code = ResultDict.SYSTEM_ERROR.getCode();
            msg = ResultDict.SYSTEM_ERROR.getValue();
        } finally {
            model.put("result", code);
            model.put("result_message", msg);
        }
    }

    @RequestMapping("/register")
    public void saveUser(@ModelAttribute("decodedParams") JSONObject param, ModelMap model) {
        String mobile = param.getString("mobile");
        String code = param.getString("code");
        String plainPwd = "00000000";
        String md5Pwd = Encryption.getMD5Str(plainPwd);
        String account = GenerateUtils.getCharAndNumr(8);
        while (!GenerateUtils.check(account)) {
            account = GenerateUtils.getCharAndNumr(8);
        }
        param.put("account", account);
        Encryption.HashPassword password = Encryption.encrypt(md5Pwd);
        param.put("pwd", password.getPassword());
        param.put("salt", password.getSalt());
        try {
            validationService.checkCode(code, mobile);
            try {
                accountService.saveUser(param);
                logger.info("result=" + param.getString("result"));
            } catch (RepetitionException e) {
                model.put("result", e.getCode());
                model.put("result_message", e.getMessage());
            }
            AppUser appUser = alinkLoginService.findUserByMobile(mobile);
            String token = alinkLoginService.generateToken(appUser.getId());
            model.put("token", token);
            appUser.setPwd(null);
            appUser.setSalt(null);
            model.put("user", appUser);
            model.put("result", ResultDict.SUCCESS.getCode());
            model.put("result_message", ResultDict.SUCCESS.getValue());
        } catch (InvalidCodeException e) {
            model.put("result", ResultDict.VERIFY_ERROR.getCode());
            model.put("result_message", ResultDict.VERIFY_ERROR.getValue());
        } catch (Exception e) {
            model.put("result", ResultDict.SYSTEM_ERROR.getCode());
            model.put("result_message", ResultDict.SYSTEM_ERROR.getValue());
        }
    }

    @RequestMapping("/verify")
    public void pushCode(@ModelAttribute("decodedParams") JSONObject param, ModelMap model) {
        String mobile = (String) param.get("mobile");
        String flag = (String) param.get("flag");
        try {
            if (StringUtils.isNotEmpty(mobile)) {
                int count = accountService.getCount(param);
                if ("0".equals(flag)) {
                    if (count == 0) {
                        throw new MobileNotExistedException();
                    }
                } else if ("1".equals(flag)) {
                    if (count > 0) {
                        throw new RepetitionException(12, "该手机号已被绑定！");
                    }
                }
            }
            validationService.sendCode(param.getString("appid"), param.getString("mobile"));
            model.put("result", ResultDict.SUCCESS.getCode());
        } catch (RepetitionException e) {
            model.put("result", ResultDict.MOBILE_REPET.getCode());
            model.put("result_message", ResultDict.MOBILE_REPET.getValue());
        } catch (MobileNotExistedException e) {
            model.put("result", ResultDict.MOBILE_NOT_EXISTED.getCode());
            model.put("result_message", ResultDict.MOBILE_NOT_EXISTED.getValue());
        } catch (Exception e) {
            model.put("result", ResultDict.SYSTEM_ERROR.getCode());
            model.put("result_message", ResultDict.SYSTEM_ERROR.getValue());
        }
    }
}
