package com.tpadsz.after.dao;


import java.util.List;
import java.util.Map;

/**
 * Created by hongjian.chen on 2019/3/7.
 */
public interface UserDao {

    List<Map> getFirms();

    void saveUser(Map map);

    void updateUser(Map map);

}
