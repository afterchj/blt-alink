package com.tpadsz.after.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;

public class BaseDecodedController{

    @ModelAttribute("decodedParams")
    public JSONObject beforeInvokingHandlerMethod(HttpServletRequest request) {
        return (JSONObject) request.getAttribute("decodedParams");
    }

}
