package com.tpadsz.after.service;

import com.tpadsz.after.entity.*;

import java.util.List;

/**
 * @program: blt-alink
 * @description:
 * @author: Mr.Ma
 * @create: 2019-03-13 17:20
 **/
public interface SceneAjustService {

    void saveSceneLog(SceneLog sceneLog);

    void saveScene(SceneAjust sceneAjust);

    void saveLightSetting(List<LightSetting> lightSettingList) throws Exception;

    void saveSceneSetting(SceneSetting sceneSetting);

    void saveGroupSetting(GroupSetting groupSetting);

    void deleteLightSetting(Integer sid);

    void deleteLightSettingByLmac(String lmac);
}
