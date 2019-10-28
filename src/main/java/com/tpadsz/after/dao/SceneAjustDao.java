package com.tpadsz.after.dao;

import com.tpadsz.after.entity.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @program: blt-alink
 * @description:
 * @author: Mr.Ma
 * @create: 2019-03-13 17:22
 **/
@Repository
public interface SceneAjustDao {
    void saveSceneLog(SceneLog sceneLog);//保存场景日志

    void saveScene(SceneAjust sceneAjust);//创建场景

    void saveLightSetting(LightSetting lightSetting);//单灯场景设置

    void deleteLightSetting(@Param("sid") Integer sid);//删除旧场景

    void deleteLightSettingByLmac(@Param("lmac") String lmac);//删除单灯lightsetting记录

    int getLightSetting(LightSetting lightSetting);

    void updateLightSetting(LightSetting lightSetting);
}
