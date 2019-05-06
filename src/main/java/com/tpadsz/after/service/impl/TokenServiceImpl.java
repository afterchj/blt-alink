package com.tpadsz.after.service.impl;

import com.tpadsz.after.constants.MemcachedObjectType;
import com.tpadsz.after.exception.AccountDisabledException;
import com.tpadsz.after.exception.SystemAlgorithmException;
import com.tpadsz.after.exception.TokenNotEffectiveException;
import com.tpadsz.after.exception.TokenReplacedException;
import com.tpadsz.after.service.TokenService;
import net.rubyeye.xmemcached.XMemcachedClient;
import net.rubyeye.xmemcached.exception.MemcachedException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeoutException;


/**
 * Created by chenhao.lu on 2019/3/17.
 */
@Service("tokenService")
public class TokenServiceImpl implements TokenService {

    @Resource
    private TokenService tokenService;

    @Autowired
    private XMemcachedClient client;

    @Override
    public void verifyToken(String uid, String token) throws SystemAlgorithmException, TokenNotEffectiveException,
            InterruptedException, MemcachedException, TimeoutException, AccountDisabledException {
        try {
            if (StringUtils.isBlank(token)) {
                throw new TokenNotEffectiveException();
            }
            String expected = null;
                String key = MemcachedObjectType.CACHE_TOKEN.getPrefix() + uid;
                expected = (String) client.get(key);
            if (StringUtils.equals(token, expected)) {
                    return;
            } else {
                if(StringUtils.isBlank(expected)) {
                    throw new TokenNotEffectiveException();
                }else if("disabled".equals(expected)){
                    throw new AccountDisabledException();
                }else {
                    throw new TokenReplacedException();
                }
            }
        } catch (RuntimeException e) {
            throw new SystemAlgorithmException();
        }
    }

}
