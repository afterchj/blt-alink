package com.tpadsz.after.dao;

import com.tpadsz.after.entity.*;

/**
 * @program: blt-alink
 * @description:
 * @author: Mr.Ma
 * @create: 2019-03-13 17:22
 **/
public interface SceneAjustDao {
    void saveSceneLog(SceneLog sceneLog);//保存场景日志

    void saveScene(SceneAjust sceneAjust);//创建场景

    void saveLightSetting(LightSetting lightSetting);//单灯场景设置

    void saveSceneSetting(SceneSetting sceneSetting);//场景设置

    void saveGroupSetting(GroupSetting groupSetting);//保存单组场景
}
