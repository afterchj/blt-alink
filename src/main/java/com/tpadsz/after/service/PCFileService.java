package com.tpadsz.after.service;

import com.tpadsz.after.entity.AppUser;

import java.util.List;
import java.util.Map;

/**
 * Created by hongjian.chen on 2019/3/7.
 */
public interface PCFileService {

    AppUser getUser(String account);

    List getMesh(Map map);

    Map getProject();

    Map getFile(Map map);

    void saveFile(Map map);
}
