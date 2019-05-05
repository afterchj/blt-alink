package com.tpadsz.after.dao;


import java.util.List;
import java.util.Map;

/**
 * Created by hongjian.chen on 2019/3/6.
 */
public interface AccountDao {

    void updateAccount(Map map);

    int findByUser(String uname);

    int findByMobile(String mobile);

    List<Map> getFirms();

    void saveUser(Map map);

}
