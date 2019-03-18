package com.tpadsz.after.service;

import com.tpadsz.after.entity.AppUser;
import com.tpadsz.after.entity.LoginLog;
import com.tpadsz.after.exception.AccountNotCorrectException;
import com.tpadsz.after.exception.PasswordNotCorrectException;
import com.tpadsz.after.exception.SystemAlgorithmException;

import java.util.List;

/**
 * @program: blt-light
 * @description:
 * @author: Mr.Ma
 * @create: 2019-03-06 11:23
 **/
public interface AlinkLoginService {


    AppUser loginByTpad(String input, String password,String inputFlag) throws SystemAlgorithmException, AccountNotCorrectException,PasswordNotCorrectException;

    boolean checkPassword(String actual, String expected, String salt);

    void saveLoginLog(LoginLog loginLog);

<<<<<<< HEAD
    void loginOut(String uid)throws Exception;
=======
    void loginOut(String uid) throws Exception;
>>>>>>> origin/ji.ma

    AppUser findUserByMobile(String mobile);

    void insertForeach(List<String> list);
}
