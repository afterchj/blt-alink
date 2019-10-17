package com.tpadsz.after.service;


import java.util.List;
import java.util.Map;

/**
 * Created by hongjian.chen on 2019/3/7.
 */
public interface PCFileService {

    Map getUser(String account);

    List getMesh(Map map);

    List<Map> getProject(Map map);

    Map getFile(Map map);

    void saveMesh(Map map);
    void saveMesh(List map);

    void saveUpdateProject(Map map);

    void saveFile(Map map);
}
