package com.tpadsz.after.service;

import com.tpadsz.after.exception.AccountDisabledException;
import com.tpadsz.after.exception.InvalidCodeException;
import com.tpadsz.after.exception.SystemAlgorithmException;
import com.tpadsz.after.exception.TokenNotEffectiveException;
import net.rubyeye.xmemcached.exception.MemcachedException;

import java.util.concurrent.TimeoutException;

/**
 * Created by chenhao.lu on 2019/3/17.
 */
public interface TokenService {

    void verifyToken(String uid, String token) throws SystemAlgorithmException, TokenNotEffectiveException,AccountDisabledException,InterruptedException, MemcachedException, TimeoutException;
}
