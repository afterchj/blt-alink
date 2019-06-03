package com.tpadsz.after.service;

import com.tpadsz.after.entity.AppUser;
import com.tpadsz.after.entity.LoginLog;
import com.tpadsz.after.exception.AccountNotCorrectException;
import com.tpadsz.after.exception.AdminNotAllowedException;
import com.tpadsz.after.exception.PasswordNotCorrectException;
import com.tpadsz.after.exception.SystemAlgorithmException;

/**
 * @program: blt-light
 * @description:
 * @author: Mr.Ma
 * @create: 2019-03-06 11:23
 **/
public interface AlinkLoginService {


    AppUser loginByTpad(String input, String password, String inputFlag) throws SystemAlgorithmException, AccountNotCorrectException,PasswordNotCorrectException,AdminNotAllowedException;

    boolean checkPassword(String actual, String expected, String salt);

    void saveLoginLog(LoginLog loginLog);

    void loginOut(String uid)throws Exception;

    AppUser findUserByMobile(String mobile);

    String generateToken(String uid) throws SystemAlgorithmException;

    Integer findRoleIdByUid(String uid);
}
