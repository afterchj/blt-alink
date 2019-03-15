package com.tpadsz.after.service;

import com.tpadsz.after.entity.*;

/**
 * @program: blt-alink
 * @description:
 * @author: Mr.Ma
 * @create: 2019-03-13 17:20
 **/
public interface SceneAjustService {

    void saveSceneLog(SceneLog sceneLog);

    void saveScene(SceneAjust sceneAjust);

    void saveLightSetting(LightSetting lightSetting);

    void saveSceneSetting(SceneSetting sceneSetting);

    void saveGroupSetting(GroupSetting groupSetting);

    void deleteLightSetting(Integer sid);
}
