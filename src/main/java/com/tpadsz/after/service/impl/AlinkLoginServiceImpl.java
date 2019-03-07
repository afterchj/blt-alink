package com.tpadsz.after.service.impl;

import com.tpadsz.after.dao.AlinkLoginDao;
import com.tpadsz.after.entity.AppUser;
import com.tpadsz.after.entity.LoginLog;
import com.tpadsz.after.exception.AccountNotCorrectException;
import com.tpadsz.after.exception.PasswordNotCorrectException;
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
    private static final int SALT_SIZE = 8;

    @Resource
    private AlinkLoginDao alinkLoginDao;

    public static class HashPassword {
        private String salt;
        private String password;

        public HashPassword(String salt, String password) {
            super();
            this.salt = salt;
            this.password = password;
        }

        public String getSalt() {
            return salt;
        }

        public String getPassword() {
            return password;
        }

    }

    @Override
    public AppUser loginByTpad(String appid, String account, String password)  throws SystemAlgorithmException, AccountNotCorrectException,PasswordNotCorrectException {
        AppUser appUser = null;
        try {
            appUser = alinkLoginDao.findUserByAccount(account);
        } catch (Exception e) {
            throw new SystemAlgorithmException();
        }
        if(appUser == null){
            throw new AccountNotCorrectException();
        }
        boolean isCorrect = checkPassword(password, appUser.getPwd(), appUser.getSalt());
        if(!isCorrect){
            throw new PasswordNotCorrectException();
        }
        appUser.setPwd(null);
        appUser.setSalt(null);
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

    public HashPassword encrypt(String plainText) {
        byte[] salt = Digests.generateSalt(SALT_SIZE);
        String hashsalt = Encodes.encodeHex(salt);
        String hashpassword = encrypt(plainText, salt);
        HashPassword result = new HashPassword(hashsalt, hashpassword);
        return result;
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
    public void loginOut(String uid) throws Exception {
        alinkLoginDao.saveLoginOutLog(uid);
    }

    @Override
    public AppUser findUserByMobile(String mobile) {
        return alinkLoginDao.findUserByMobile(mobile);
    }
}
