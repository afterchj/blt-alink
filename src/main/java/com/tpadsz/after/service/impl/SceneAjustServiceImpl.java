package com.tpadsz.after.service.impl;

import com.tpadsz.after.dao.SceneAjustDao;
import com.tpadsz.after.entity.*;
import com.tpadsz.after.service.SceneAjustService;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @program: blt-alink
 * @description:
 * @author: Mr.Ma
 * @create: 2019-03-13 17:21
 **/
@Service("sceneAjustService")
public class SceneAjustServiceImpl implements SceneAjustService {

    @Resource
    private SceneAjustDao sceneAjustDao;

    @Resource
    private SqlSessionTemplate sqlSessionTemplate;

    @Override
    public void saveSceneLog(SceneLog sceneLog) {
        sceneAjustDao.saveSceneLog(sceneLog);
    }

    @Override
    public void saveScene(SceneAjust sceneAjust) {
        sceneAjustDao.saveScene(sceneAjust);
    }

    @Override
    public void saveLightSetting(LightSetting lightSetting) {
        sceneAjustDao.saveLightSetting(lightSetting);
    }

    @Override
    public void saveSceneSetting(SceneSetting sceneSetting) {
        sceneAjustDao.saveSceneSetting(sceneSetting);
    }

    @Override
    public void saveGroupSetting(GroupSetting groupSetting) {
        sceneAjustDao.saveGroupSetting(groupSetting);
    }

    @Override
    public void deleteLightSetting(Integer sid) {
        sceneAjustDao.deleteLightSetting(sid);
    }
}
