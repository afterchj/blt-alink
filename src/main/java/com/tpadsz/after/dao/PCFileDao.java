package com.tpadsz.after.dao;


import java.util.List;
import java.util.Map;

/**
 * Created by hongjian.chen on 2019/3/7.
 */
public interface PCFileDao {

    Map getFile(Map map);

    void deleteProject(Map map);

    void saveFile(Map map);

    void saveProject(Map map);

    void saveMesh(List map);

    Map getUser(String account);

    List getMesh(Map map);

    List<Map> getProject(Map map);

    void saveMesh(Map map);

}
