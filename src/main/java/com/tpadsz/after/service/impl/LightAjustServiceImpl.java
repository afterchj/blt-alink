package com.tpadsz.after.service.impl;

import com.tpadsz.after.dao.LightAjustDao;
import com.tpadsz.after.entity.LightList;
import com.tpadsz.after.entity.LightReturn;
import com.tpadsz.after.service.LightAjustService;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

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
    public void saveLightAjustLog(String meshId, String bltFlag, String operation, String lmacs) {
        lightAjustDao.saveLightAjustLog(meshId, bltFlag, operation, lmacs);
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
    public String saveLight(List<LightList> lightLists) throws Exception {
        SqlSession sqlSession = sqlSessionTemplate.getSqlSessionFactory().openSession(ExecutorType.BATCH);
        LightAjustDao lightAjustDao1 = sqlSession.getMapper(LightAjustDao.class);
        Integer nowMid;
        Map<String,Integer> lightMap;
        StringBuffer sb = new StringBuffer();
        String lmacs;
        try {
            for (int i = 1; i <= lightLists.size(); i++) {
                lightMap = lightAjustDao.getLid(lightLists.get(i-1).getLmac());
                //该灯数据库没有记录
                if (lightMap == null||lightMap.size()==0) {
                    //扫描到未分组
                    lightAjustDao1.saveLight(lightLists.get(i-1));
                }else {
                    //有灯 更新灯记录
                    nowMid = lightLists.get(i-1).getMid();
                    //数据库中的mid和当前扫描入网的mid不是同一网络//Integer默认比较[-128,128]
                    if (lightMap.get("mid").intValue()!=nowMid.intValue()){
                        //删除light_setting中的场景记录
                        lightAjustDao1.deleteLightSettingByLmac(lightLists.get(i-1).getLmac());
                        lightAjustDao1.updateLightGidAndMid(lightLists.get(i-1));
                    }
                }
                sb.append(lightLists.get(i-1).getLmac()).append(",");
                if (i % 500 == 0 || i == lightLists.size()) {
                    //手动每500个一提交，提交后无法回滚
                    sqlSession.commit();
                    //清理缓存，防止溢出
                    sqlSession.clearCache();
                }
            }
            lmacs = sb.toString();
            lmacs = lmacs.substring(0,lmacs.length()-",".length());
            return lmacs;
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
    public void updateLightGid(List<LightList> lightLists, String meshId, String bltFlag, String operation) throws Exception{
        SqlSession sqlSession = sqlSessionTemplate.getSqlSessionFactory().openSession(ExecutorType.BATCH);
        LightAjustDao lightAjustDao1 = sqlSession.getMapper(LightAjustDao.class);
        Map<String,Integer> lightMap;
        StringBuffer sb = new StringBuffer();
        String lmacs;
        try {
            for (int i = 1; i <= lightLists.size(); i++) {
                lightMap = lightAjustDao.getLid(lightLists.get(i-1).getLmac());
                //该灯数据库有记录
                if (lightMap!=null&& lightMap.size()>0) {
                    if (lightMap.get("mid").intValue()!=lightLists.get(i-1).getMid().intValue()){
                        //mid不一致删除场景信息
                        //更新灯表 更新 gid,update_date,mid,lmac,lname
                        lightAjustDao1.deleteLightSettingByLmac(lightLists.get(i-1).getLmac());
                        lightAjustDao1.updateLightGidAndMid(lightLists.get(i-1));
                    }else {
                        //在同一网络中
                        //更新灯表 更新 gid,update_date
                        lightAjustDao1.updateLightGidAndLmac(lightLists.get(i-1).getLmac(),lightLists.get(i-1).getGid());
                    }
                }else {
                    //创建灯
                    lightAjustDao1.saveLight(lightLists.get(i-1));
                }
                sb.append(lightLists.get(i-1).getLmac()).append(",");
                if (i % 500 == 0 || i == lightLists.size()) {
                    lmacs = sb.toString();
                    lmacs = lmacs.substring(0,lmacs.length()-",".length());
                    lightAjustDao1.saveLightAjustLog(meshId, bltFlag, operation, lmacs);//记录日志
                    sb = new StringBuffer();//重置StringBuffer
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
    public Map<String, Integer> getLid(String lmac) {
        return lightAjustDao.getLid(lmac);
    }

    @Override
    public void saveTempLight(LightList lightList) {
        lightAjustDao.saveTempLight(lightList);
    }

    @Override
    public void deleteLightSettingByLmac(String lmac) {
        lightAjustDao.deleteLightSettingByLmac(lmac);
    }

    @Override
    public void updateLight(String lmac, Integer groupId, Integer mid, Integer pid) {
        lightAjustDao.updateLight(lmac,groupId,mid, pid);
    }

    @Override
    public List<LightReturn> getAllByMid(Integer mid) {
        return lightAjustDao.getAllByMid(mid);
    }
}
