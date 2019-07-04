package com.tpadsz.after.service;

import com.tpadsz.after.exception.NotExitException;

import java.util.List;
import java.util.Map;

/**
 * Created by hongjian.chen on 2019/3/7.
 */
public interface BltConsoleService {

    List getScenes(Map map);

    List getGroups(Map map);

    List getGroupsInfo(Map map);

    List getLightsInfo(Map map);

    List getAll(Map map);

    int getTotal(Map map);

    Integer getPid(Map map);

    Integer getCount(Map map)throws NotExitException;

    void saveApplyScene(Map map);

    void restScene(Map map);

    void saveSceneName(Map map) throws NotExitException;

    void deleteScene(Map map);

    void saveOperation(Map map);
}
