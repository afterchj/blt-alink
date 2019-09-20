package com.tpadsz.after.dao;

import java.util.List;
import java.util.Map;

/**
 * Created by hongjian.chen on 2019/3/7.
 */
public interface BltConsoleDao {

    List getScenes(Map map);

    List getGroups(Map map);

    List getGroupsInfo(Map map);

    List getLightsInfo(Map map);

    List getAll(Map map);
    String selectById(String uid);
    Integer getPid(Map map);

    Integer getCount(Map map);

    int getTotal(Map map);

    void saveApplyScene(Map map);

    void restScene(Map map);

    void saveSceneName(Map map);

    void deleteScene(Map map);

    void saveOperation(Map map);
}
