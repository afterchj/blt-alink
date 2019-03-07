package com.tpadsz.after.dao;

import com.tpadsz.after.entity.AppUser;
import com.tpadsz.after.entity.LoginLog;

/**
 * @program: blt-light
 * @description:
 * @author: Mr.Ma
 * @create: 2019-03-06 11:32
 **/
public interface AlinkLoginDao {

    AppUser findUserByAccount(String account);

    void saveLoginLog(LoginLog loginLog);

    void saveLoginOutLog(String uid);

    AppUser findUserByMobile(String mobile);
}
