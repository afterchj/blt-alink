package com.tpadsz.after.service;

import com.tpadsz.after.exception.RepetitionException;

import java.util.Map;

/**
 * Created by hongjian.chen on 2019/3/6.
 */
public interface AccountService {

    void updateAccount(Map map) throws RepetitionException;

    int findByUser(String uname);

    int findByMobile(String mobile);

    void saveUser(Map map);
}
