package com.tpadsz.after.controller;

import com.alibaba.fastjson.JSONObject;
import com.tpadsz.after.entity.dd.ResultDict;
import com.tpadsz.after.exception.InvalidCodeException;
import com.tpadsz.after.exception.RepetitionException;
import com.tpadsz.after.service.AccountService;
import com.tpadsz.after.service.ValidationService;
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

    @Autowired
    private AccountService accountService;
    @Autowired
    private ValidationService validationService;

    public void fillAccount(@ModelAttribute("decodedParams") JSONObject param, ModelMap model) {
        String msg = "";
        String code = "";
        try {
            validationService.checkCode(param.getString("code"), param.getString("mobile"));
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
