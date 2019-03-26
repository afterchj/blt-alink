package com.tpadsz.after.service;

import java.util.List;
import java.util.Map;

/**
 * Created by hongjian.chen on 2019/3/7.
 */
public interface BltConsoleService {

    List getScenes(Map map);

    List getGroups(Map map);

    List getAll(Map map);

    List getLights(Map map);

    void saveApplyScene(Map map);

    void saveSceneName(Map map);

    void deleteScene(Map map);

    void saveOperation(Map map);
}
