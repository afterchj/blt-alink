package com.tpadsz.after.util.factory;

import com.alibaba.fastjson.JSONObject;
import com.tpadsz.after.entity.Group;
import com.tpadsz.after.entity.SceneLog;

/**
 * @program: blt-alink
 * @description:
 * @author: Mr.Ma
 * @create: 2019-08-30 11:46
 **/
public interface AdjustBeanInterfaceFactory {

    Group createGroup(JSONObject params);

    SceneLog createSceneLog(String uid, String bltFlag, String meshId, Integer sceneId);

//    SceneAjust createSceneAdjust(Integer sceneId, String uid, Integer mid, String sname);


}
