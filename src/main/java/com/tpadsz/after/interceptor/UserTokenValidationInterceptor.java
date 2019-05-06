package com.tpadsz.after.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.tpadsz.after.entity.dd.ResultDict;
import com.tpadsz.after.exception.AccountDisabledException;
import com.tpadsz.after.exception.TokenNotEffectiveException;
import com.tpadsz.after.exception.TokenReplacedException;
import com.tpadsz.after.service.TokenService;
import com.tpadsz.after.util.Encryption;
import com.tpadsz.after.util.HttpServletUtil;
import com.tpadsz.after.util.LoggerUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;


public class UserTokenValidationInterceptor extends HandlerInterceptorAdapter {
    private Logger logger = LoggerUtils.SYSTEM;

    @Resource
    private TokenService tokenService;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
        JSONObject params = (JSONObject) request.getAttribute("decodedParams");
        String token = params.getString("token");
        String uid = params.getString("uid");
        Map<String, String> data = new HashMap<>();
        if (StringUtils.isNotBlank(uid)) {
            if (StringUtils.isBlank(token)) {
                data.put("result", ResultDict.TOKEN_NOT_SUBMIT.getCode());
                HttpServletUtil.renderText(response, Encryption.encode(JSONObject.toJSONString(data)));
                return false;
            } else {
                try {
                    tokenService.verifyToken(uid, token);
                    return true;
                } catch (TokenReplacedException e) {
                    data.put("result", ResultDict.TOKEN_REPLACED.getCode());
                } catch (AccountDisabledException e) {
                    data.put("result", ResultDict.ACCOUNT_IS_DISABLED.getCode());
                } catch (TokenNotEffectiveException e) {
                    data.put("result", ResultDict.TOKEN_NOT_CORRECT.getCode());
                }  catch (Exception e) {
                    data.put("result", ResultDict.SYSTEM_ERROR.getCode());
//					logger.error("严重 :: ", e);
                }
                HttpServletUtil.renderText(response, Encryption.encode(JSONObject.toJSONString(data)));
                return false;
            }
        } else {
            return false;
        }
    }

}
