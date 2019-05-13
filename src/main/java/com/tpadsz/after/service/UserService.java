package com.tpadsz.after.service;

import java.util.List;
import java.util.Map;

/**
 * Created by hongjian.chen on 2019/5/5.
 */
public interface UserService {

    List<Map> getFirms(Map keyword);

    void saveUser(Map map);

    void updateUser(Map map);
}
