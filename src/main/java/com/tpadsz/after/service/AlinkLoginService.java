package com.tpadsz.after.service;

import com.tpadsz.after.entity.AppUser;
import com.tpadsz.after.entity.LoginLog;
import com.tpadsz.after.exception.AccountNotCorrectException;
import com.tpadsz.after.exception.PasswordNotCorrectException;
import com.tpadsz.after.exception.SystemAlgorithmException;

/**
 * @program: blt-light
 * @description:
 * @author: Mr.Ma
 * @create: 2019-03-06 11:23
 **/
public interface AlinkLoginService {


    AppUser loginByTpad(String appid, String account, String password) throws SystemAlgorithmException, AccountNotCorrectException,PasswordNotCorrectException;

    boolean checkPassword(String actual, String expected, String salt);

    void saveLoginLog(LoginLog loginLog);

    void loginOut(String uid) throws Exception;

    AppUser findUserByMobile(String mobile);
}
