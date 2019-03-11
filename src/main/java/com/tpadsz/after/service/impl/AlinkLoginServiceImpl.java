package com.tpadsz.after.service.impl;

import com.tpadsz.after.dao.AlinkLoginDao;
import com.tpadsz.after.entity.AppUser;
import com.tpadsz.after.entity.LoginLog;
import com.tpadsz.after.exception.AccountNotCorrectException;
import com.tpadsz.after.exception.PasswordNotCorrectException;
import com.tpadsz.after.exception.SystemAlgorithmException;
import com.tpadsz.after.service.AlinkLoginService;
import com.tpadsz.after.util.Encryption;
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

    @Resource
    private AlinkLoginDao alinkLoginDao;

    @Override
    public AppUser loginByTpad(String appid, String account, String password) throws SystemAlgorithmException,
            AccountNotCorrectException, PasswordNotCorrectException {
        AppUser appUser = null;
        try {
            appUser = alinkLoginDao.findUserByAccount(account);
        } catch (Exception e) {
            throw new SystemAlgorithmException();
        }
        if (appUser == null) {
            throw new AccountNotCorrectException();
        }
        boolean isCorrect = checkPassword(password, appUser.getPwd(), appUser.getSalt());
        if (!isCorrect) {
            throw new PasswordNotCorrectException();
        }
        appUser.setPwd(null);
        appUser.setSalt(null);
        return appUser;
    }


    @Override
    public boolean checkPassword(String actual, String expected, String salt) {
        if (StringUtils.isBlank(actual) || StringUtils.isBlank(expected) || StringUtils.isBlank(salt)) {
            return false;
        }
        String actualEncodingPassword = Encryption.encrypt(actual, salt);
        if (StringUtils.equals(actualEncodingPassword, expected)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void saveLoginLog(LoginLog loginLog) {
        alinkLoginDao.saveLoginLog(loginLog);
    }

    @Override
    public void loginOut(String uid) throws Exception {
        alinkLoginDao.saveLoginOutLog(uid);
    }

    @Override
    public AppUser findUserByMobile(String mobile) {
        return alinkLoginDao.findUserByMobile(mobile);
    }

    @Override
    public void insert() {
        alinkLoginDao.insert("网络","11222345","2345","2345","1");
    }
}
