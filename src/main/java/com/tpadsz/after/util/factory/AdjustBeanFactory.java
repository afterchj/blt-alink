package com.tpadsz.after.util.factory;

import com.alibaba.fastjson.JSONObject;
import com.tpadsz.after.entity.Group;
import com.tpadsz.after.entity.SceneLog;

import javax.annotation.Resource;

/**
 * @program: blt-alink
 * @description:
 * @author: Mr.Ma
 * @create: 2019-08-30 11:49
 **/
public class AdjustBeanFactory implements AdjustBeanInterfaceFactory {

    @Resource
    private AdjustBeanUtils adjustBeanUtils;

    @Override
    public Group createGroup(JSONObject params) {
        return adjustBeanUtils.setGroup(params);
    }

    @Override
    public SceneLog createSceneLog(String uid, String bltFlag, String meshId, Integer sceneId) {
        return adjustBeanUtils.setSceneLog(uid,bltFlag,meshId,sceneId);
    }

//    @Override
//    public SceneAjust createSceneAdjust(Integer sceneId, String uid, Integer mid, String sname) {
//        return adjustBeanUtils.setSceneAjust(sceneId,uid,mid,sname);
//    }
}
