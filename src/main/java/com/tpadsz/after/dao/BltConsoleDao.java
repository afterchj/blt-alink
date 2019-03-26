package com.tpadsz.after.dao;

import java.util.List;
import java.util.Map;

/**
 * Created by hongjian.chen on 2019/3/7.
 */
public interface BltConsoleDao {

    List getScenes(Map map);

    List getGroups(Map map);

    List getLights(Map map);

    List getAll(Map map);

    void saveApplyScene(Map map);

    void saveSceneName(Map map);

    void deleteScene(Map map);

    void saveOperation(Map map);
}
