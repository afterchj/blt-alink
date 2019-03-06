package com.tpadsz.after.dao;


import java.util.Map;

/**
 * Created by hongjian.chen on 2019/3/6.
 */
public interface AccountDao {

    void updateAccount(Map map);

    int findByUser(String uname);

}
