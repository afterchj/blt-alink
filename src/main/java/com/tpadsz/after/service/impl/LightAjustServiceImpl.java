package com.tpadsz.after.service.impl;

import com.tpadsz.after.dao.LightAjustDao;
import com.tpadsz.after.entity.Group;
import com.tpadsz.after.entity.LightList;
import com.tpadsz.after.service.LightAjustService;
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
 * @create: 2019-03-07 14:19
 **/
@Service("lightAjustService")
public class LightAjustServiceImpl implements LightAjustService {

    @Resource
    private LightAjustDao lightAjustDao;

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    @Override
    public void saveLightAjustLog(String meshId,String lmac, String bltFlag,
                               String operation) {
        lightAjustDao.saveLightAjustLog(meshId, lmac,bltFlag,operation);
    }

    @Override
    public void updateLightName(String lmac, String lname) {
        lightAjustDao.updateLightName(lmac,lname);
    }

    /**
     * 批量插入ExecutorType.BATCH
     * 在Insert操作时，在事务没有提交之前，是没有办法获取到自增的id
     * @param lightLists
     * @throws Exception
     */
    @Override
    public void saveLight(List<LightList> lightLists) throws Exception{
        SqlSession sqlSession = sqlSessionTemplate.getSqlSessionFactory().openSession(ExecutorType.BATCH);
        LightAjustDao lightAjustDao1 = sqlSession.getMapper(LightAjustDao.class);
        try {
            for (int i=0;i<lightLists.size();i++){
                lightAjustDao1.saveLight(lightLists.get(i));
                if (i%500==0||i==(lightLists.size()-1)){
                    //手动每500个一提交，提交后无法回滚
                    sqlSession.commit();
                    //清理缓存，防止溢出
                    sqlSession.clearCache();
                }
            }
        }catch (Exception e){
            //回滚
            sqlSession.rollback();
            throw e;
        }finally {
            sqlSession.close();
        }
    }

    @Override
    public void deleteLight(String lmac) {

        lightAjustDao.deleteLight(lmac);
    }

    @Override
    public void updateLightGid(Group group) {
        lightAjustDao.updateLightGid(group);
    }

    @Override
    public void saveLightColor(String uid,String meshId,String lmac, String x, String y) {
        lightAjustDao.saveLightColor(uid,meshId,lmac,x,y);
    }

    @Override
    public String getLightOff(String lmac) {
        return lightAjustDao.getLightOff(lmac);
    }

    @Override
    public Integer getLid(String lmac) {
        return lightAjustDao.getLid(lmac);
    }

    @Override
    public void saveTempLight(LightList lightList) {
        lightAjustDao.saveTempLight(lightList);
    }
}
