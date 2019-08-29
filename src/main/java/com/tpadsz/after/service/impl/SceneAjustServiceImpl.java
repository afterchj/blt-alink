package com.tpadsz.after.service.impl;

import com.tpadsz.after.dao.SceneAjustDao;
import com.tpadsz.after.entity.LightSetting;
import com.tpadsz.after.entity.SceneAjust;
import com.tpadsz.after.entity.SceneLog;
import com.tpadsz.after.exception.SystemAlgorithmException;
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
    public void saveLightSetting(List<LightSetting> lightSettingList) throws SystemAlgorithmException {
        SqlSession sqlSession = sqlSessionTemplate.getSqlSessionFactory().openSession(ExecutorType.BATCH,false);
        SceneAjustDao sceneAjustDao1 = sqlSession.getMapper(SceneAjustDao.class);
        try {
            //删除light_setting中之前保存的场景
//            sceneAjustDao1.deleteLightSetting(lightSettingList.get(0).getSid());
            for (int i=1;i<=lightSettingList.size();i++){
                sceneAjustDao1.saveLightSetting(lightSettingList.get(i-1));
                if (i%500==0||i==lightSettingList.size()){
                    //手动每500个一提交，提交后无法回滚
                    sqlSession.commit();
                    //清理缓存，防止溢出
                    sqlSession.clearCache();
                }
            }
        }catch (Exception e){
            sqlSession.rollback();
            throw new SystemAlgorithmException("数据提交错误");
        }finally {
            sqlSession.close();
        }
    }

//    @Override
//    public void saveSceneSetting(SceneSetting sceneSetting) {
//        sceneAjustDao.saveSceneSetting(sceneSetting);
//    }

//    @Override
//    public void saveGroupSetting(GroupSetting groupSetting) {
//        sceneAjustDao.saveGroupSetting(groupSetting);
//    }

    @Override
    public void deleteLightSetting(Integer sid) {
        sceneAjustDao.deleteLightSetting(sid);
    }

    @Override
    public void deleteLightSettingByLmac(String lmac) {
        sceneAjustDao.deleteLightSettingByLmac(lmac);
    }
}
