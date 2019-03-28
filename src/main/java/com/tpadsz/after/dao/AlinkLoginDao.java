package com.tpadsz.after.dao;

import com.tpadsz.after.entity.AppUser;
import com.tpadsz.after.entity.LoginLog;

import java.util.List;

/**
 * @program: blt-light
 * @description:
 * @author: Mr.Ma
 * @create: 2019-03-06 11:32
 **/
public interface AlinkLoginDao {

    AppUser findUserByAccount(String account);

    void saveLoginLog(LoginLog loginLog);

    void saveLoginOutLog(String uid) throws Exception;

    AppUser findUserByMobile(String mobile);

    AppUser findUserByUname(String uname);

}
