package com.tpadsz.after.service.impl;

import com.tpadsz.after.dao.SceneAjustDao;
import com.tpadsz.after.entity.*;
import com.tpadsz.after.service.SceneAjustService;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

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

    @Autowired
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
    public void saveLightSetting(List<LightSetting> lightSettingList)throws Exception {
        SqlSession sqlSession = sqlSessionTemplate.getSqlSessionFactory().openSession(ExecutorType.BATCH,false);
        SceneAjustDao sceneAjustDao1 = sqlSession.getMapper(SceneAjustDao.class);
        try {
            for (int i=0;i<lightSettingList.size();i++){
                sceneAjustDao1.saveLightSetting(lightSettingList.get(i));
                if (i%500==0||i==(lightSettingList.size()-1)){
                    //手动每500个一提交，提交后无法回滚
                    sqlSession.commit();
                    //清理缓存，防止溢出
                    sqlSession.clearCache();
                }
            }
        }catch (Exception e){
            sqlSession.rollback();
            throw e;
        }finally {
            sqlSession.close();
        }
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

    @Override
    public void deleteLightSettingByLmac(String lmac) {
        sceneAjustDao.deleteLightSettingByLmac(lmac);
    }
}
