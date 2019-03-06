package com.tpadsz.after.service.impl;

import com.tpadsz.after.dao.AlinkLoginDao;
import com.tpadsz.after.entity.AppUser;
import com.tpadsz.after.entity.LoginLog;
import com.tpadsz.after.exception.AccountNotCorrectException;
import com.tpadsz.after.exception.SystemAlgorithmException;
import com.tpadsz.after.service.AlinkLoginService;
import com.tpadsz.after.util.Digests;
import com.tpadsz.after.util.Encodes;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @program: blt-light
 * @description:
 * @author: Mr.Ma
 * @create: 2019-03-06 11:24
 **/
@Service("alinkLoginService")
public class AlinkLoginServiceImpl implements AlinkLoginService {

    private static final int INTERATIONS = 1024;

    @Resource
    private AlinkLoginDao alinkLoginDao;

    @Override
    public AppUser loginByTpad(String appid, String account, String password)  throws SystemAlgorithmException, AccountNotCorrectException {
        AppUser appUser = null;
        try {
            appUser = alinkLoginDao.findByAccount(account);
            if(appUser == null){
                throw new AccountNotCorrectException();
            }
        } catch (Exception e) {
            throw new SystemAlgorithmException();
        }
        boolean isCorrect = checkPassword(password, appUser.getPsd(), appUser.getSalt());
        if(!isCorrect){
            throw new AccountNotCorrectException();
        }
        return appUser;
    }


    @Override
    public boolean checkPassword(String actual, String expected, String salt) {
        if(StringUtils.isBlank(actual) || StringUtils.isBlank(expected) || StringUtils.isBlank(salt)){
            return false;
        }
        String actualEncodingPassword = encrypt(actual, salt);
        if(StringUtils.equals(actualEncodingPassword, expected)){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public void saveLoginLog(LoginLog loginLog) {
        alinkLoginDao.saveLoginLog(loginLog);
    }

    private String encrypt(String actual, String salt){
        return encrypt(actual, Encodes.decodeHex(salt));
    }

    private String encrypt(String actual, byte[] salt){
        byte[] hashPassword = Digests.sha1(actual.getBytes(), salt, INTERATIONS);
        return Encodes.encodeHex(hashPassword);
    }

    @Override
    public void loginOut(String uid) {
        alinkLoginDao.saveLoginOutLog(uid);
    }
}
