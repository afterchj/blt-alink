package com.tpadsz.after.controller;

import com.alibaba.fastjson.JSONObject;
import com.tpadsz.after.entity.dd.ResultDict;
import com.tpadsz.after.exception.InvalidCodeException;
import com.tpadsz.after.exception.RepetitionException;
import com.tpadsz.after.service.AccountService;
import com.tpadsz.after.service.ValidationService;
import com.tpadsz.after.util.Encryption;
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
            }
            accountService.updateAccount(param);
            code = ResultDict.SUCCESS.getCode();
            msg = ResultDict.SUCCESS.getValue();
        } catch (RepetitionException e) {
            int flag = e.getCode();
            logger.info("flag=" + flag);
            if (flag == 12) {
                code = ResultDict.MOBILE_REPET.getCode();
                msg = ResultDict.MOBILE_REPET.getValue();
            }
            if (flag == 11) {
                code = ResultDict.UNAME_REPET.getCode();
                msg = ResultDict.UNAME_REPET.getValue();
            }
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

    @RequestMapping("/verify")
    public void pushCode(@ModelAttribute("decodedParams") JSONObject param, ModelMap model) {

        try {
            validationService.sendCode(param.getString("appid"), param.getString("mobile"));
            model.put("result", ResultDict.SUCCESS.getCode());
        } catch (Exception e) {
            model.put("result", ResultDict.SYSTEM_ERROR.getCode());
        }
    }
}
