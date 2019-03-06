package com.tpadsz.after.service;


import com.tpadsz.after.constants.MemcachedObjectType;
import com.tpadsz.after.exception.InvalidCodeException;
import com.tpadsz.after.util.HttpUtils;
import net.rubyeye.xmemcached.XMemcachedClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

/**
 * Created by after on 2018/12/12.
 */
@Service
public class ValidationService{

    @Autowired
    XMemcachedClient client;

    public String sendCode(String appid, String mobile) throws Exception {
        String key = String.format(MemcachedObjectType.CACHE_MESSAGE_VERIFICATION.getPrefix(), mobile);
        String code = getCode(key);
        if (StringUtils.isEmpty(code)) {
            code = prepare(key, code);
        }
        HttpUtils.sendSms(appid, mobile, code);
        return code;
    }

    public void checkCode(String code, String mobile) throws InvalidCodeException {
        String key = String.format(MemcachedObjectType.CACHE_MESSAGE_VERIFICATION.getPrefix(), mobile);
        String value = getCode(key);
        System.out.println("code=" + code + ",value=" + value);
        if (!StringUtils.equals(code, value)) {
            throw new InvalidCodeException("验证码不正确！");
        }
    }

    private String prepare(String key, String code) throws Exception {
        code = StringUtils.isEmpty(code) ? getRandomNum(6) : code;
        client.set(key, MemcachedObjectType.CACHE_MESSAGE_VERIFICATION.getExpiredTime(), code);
        return code;
    }

    private String getRandomNum(int len) {
        Random r = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            sb.append(r.nextInt(10));
        }
        return sb.toString();
    }

    private String getCode(String key) {
        String code = "";
        try {
            code = client.get(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return code;
    }
}
