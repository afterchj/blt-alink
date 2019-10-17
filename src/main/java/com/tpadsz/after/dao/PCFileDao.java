package com.tpadsz.after.dao;

import com.tpadsz.after.entity.AppUser;

import java.util.List;
import java.util.Map;

/**
 * Created by hongjian.chen on 2019/3/7.
 */
public interface PCFileDao {
    Map getFile(Map map);

    void saveFile(Map map);

    AppUser getUser(String account);

    List getMesh(Map map);

    Map getProject();
}
