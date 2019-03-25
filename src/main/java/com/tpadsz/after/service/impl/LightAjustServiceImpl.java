package com.tpadsz.after.service.impl;

import com.tpadsz.after.dao.LightAjustDao;
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
    public void saveLightAjustLog(String meshId, String bltFlag, String operation) {
        lightAjustDao.saveLightAjustLog(meshId, bltFlag, operation);
    }

    @Override
    public void updateLightName(String lmac, String lname) {
        lightAjustDao.updateLightName(lmac, lname);
    }

    /**
     * 批量插入ExecutorType.BATCH
     * 在Insert操作时，在事务没有提交之前，是没有办法获取到自增的id
     * @param lightLists
     * @throws Exception
     */
    @Override
    public void saveLight(List<LightList> lightLists) throws Exception {
        SqlSession sqlSession = sqlSessionTemplate.getSqlSessionFactory().openSession(ExecutorType.BATCH);
        LightAjustDao lightAjustDao1 = sqlSession.getMapper(LightAjustDao.class);
        Integer lid;
        try {
            for (int i = 1; i <= lightLists.size(); i++) {
                lid = lightAjustDao.getLid(lightLists.get(i-1).getLmac());
//                System.out.println("lid: " + lid + " lmac: " + lightLists.get(i).getLmac());
                //该灯数据库没有记录
                if (lid == null) {
                    //扫描到未分组
                    lightAjustDao1.saveLight(lightLists.get(i-1));
                }else {
                    //有灯 灯移网
                    lightAjustDao1.updateLightGidAndMid(lightLists.get(i-1).getLmac(),lightLists.get(i-1).getGid(),
                            lightLists.get(i-1).getMid(),lightLists.get(i-1).getLname());
                }
//                System.out.println("i % 500: "+i % 500);
                if (i % 500 == 0 || i == lightLists.size()) {
                    //手动每500个一提交，提交后无法回滚
                    sqlSession.commit();
                    //清理缓存，防止溢出
                    sqlSession.clearCache();
                }
            }
        } catch (Exception e) {
            //回滚
            sqlSession.rollback();
            throw e;
        } finally {
            sqlSession.close();
        }
    }

    @Override
    public Integer deleteLight(String lmac) {

        return lightAjustDao.deleteLight(lmac);
    }

    @Override
    public void updateLightGid(List<LightList> lightLists) throws Exception{
        SqlSession sqlSession = sqlSessionTemplate.getSqlSessionFactory().openSession(ExecutorType.BATCH);
        LightAjustDao lightAjustDao1 = sqlSession.getMapper(LightAjustDao.class);
        Integer lid;
        try {
            for (int i = 1; i <= lightLists.size(); i++) {
                lid = lightAjustDao.getLid(lightLists.get(i-1).getLmac());
//                System.out.println("lid: " + lid + " lmac: " + lightLists.get(i).getLmac());
                //该灯数据库有记录
                if (lid != null) {
                    //更新灯表中gid
                    lightAjustDao1.updateLightGid(lightLists.get(i-1).getLmac(),lightLists.get(i-1).getGid());
                }else {
                    //创建灯
                    lightAjustDao1.saveLight(lightLists.get(i-1));
                }
//                System.out.println("i % 500: "+i % 500);
                if (i % 500 == 0 || i == lightLists.size()) {
                    //手动每500个一提交，提交后无法回滚
                    sqlSession.commit();
                    //清理缓存，防止溢出
                    sqlSession.clearCache();
                }
            }
        } catch (Exception e) {
            //回滚
            sqlSession.rollback();
            throw e;
        } finally {
            sqlSession.close();
        }
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
