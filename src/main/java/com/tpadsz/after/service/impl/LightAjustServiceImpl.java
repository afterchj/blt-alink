package com.tpadsz.after.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tpadsz.after.dao.GroupOperationDao;
import com.tpadsz.after.dao.LightAjustDao;
import com.tpadsz.after.dao.PlaceDao;
import com.tpadsz.after.entity.Group;
import com.tpadsz.after.entity.LightList;
import com.tpadsz.after.entity.LightReturn;
import com.tpadsz.after.exception.NotExitException;
import com.tpadsz.after.exception.SystemAlgorithmException;
import com.tpadsz.after.service.GroupOperationService;
import com.tpadsz.after.service.LightAjustService;
import com.tpadsz.after.service.PlaceService;
import com.tpadsz.after.util.factory.AdjustBeanUtils;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

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

    @Resource
    private GroupOperationDao groupOperationDao;

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    @Resource
    private GroupOperationService groupOperationService;

    @Resource
    private PlaceDao placeDao;

    @Resource
    private PlaceService placeService;

    @Resource
    private AdjustBeanUtils adjustBeanUtils;

    @Override
    public void saveLightAjustLog(String meshId, String bltFlag, String operation, String lmacs) {
        lightAjustDao.saveLightAjustLog(meshId, bltFlag, operation, lmacs);
    }

    /**
     * 批量插入ExecutorType.BATCH
     * 在Insert操作时，在事务没有提交之前，是没有办法获取到自增的id
     * @param lightLists
     * @throws Exception
     */
    @Override
    public String saveLight(List<LightList> lightLists) throws SystemAlgorithmException {
        SqlSession sqlSession = sqlSessionTemplate.getSqlSessionFactory().openSession(ExecutorType.BATCH);
        LightAjustDao lightAjustDao1 = sqlSession.getMapper(LightAjustDao.class);
        Integer nowMid;
        Map<String,Integer> lightMap;
        StringJoiner sj = new StringJoiner(",");//分隔符分割的字符串拼接
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
                sj.add(lightLists.get(i-1).getLmac());
                if (i % 500 == 0 || i == lightLists.size()) {
                    //手动每500个一提交，提交后无法回滚
                    sqlSession.commit();
                    //清理缓存，防止溢出
                    sqlSession.clearCache();
                }
            }
            lmacs = sj.toString();
            return lmacs;
        } catch (Exception e) {
            //回滚
            sqlSession.rollback();
            throw new SystemAlgorithmException("数据提交错误");
        } finally {
            sqlSession.close();
        }
    }


    @Override
    public Integer deleteLight(String lmac) {

        return lightAjustDao.deleteLight(lmac);
    }

    @Override
    public void updateLightGid(List<LightList> lightLists, String meshId, String bltFlag, String operation) throws SystemAlgorithmException {
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
                        //更新灯表 更新 gid,update_date,mid,lmac,lname,pid
                        lightAjustDao1.deleteLightSettingByLmac(lightLists.get(i-1).getLmac());
                        lightAjustDao1.updateLightGidAndMid(lightLists.get(i-1));
                    }else {
                        //在同一网络中
                        //更新灯表 更新 gid,update_date,pid
                        lightAjustDao1.updateLightGidAndLmac(lightLists.get(i-1));
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
            throw new SystemAlgorithmException("数据提交异常");
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

    @Override
    public void moveLightsToDiffGroups(JSONObject params) {
        String uid = params.getString("uid");
        String meshId = params.getString("meshId");
        JSONArray lights =  params.getJSONArray("lightList");
        Integer version = params.getInteger("version");
        Integer mid = groupOperationDao.getMeshSerialNo(meshId,uid);
        Integer pid = groupOperationDao.getDefaultPlace(uid,mid);
        LightList lightList;
        String lmac;
        Integer groupId;
        String productId;
        Group group = new Group();
        for (int i=0;i<lights.size();i++){
            lightList = new LightList();
            lmac = lights.getJSONObject(i).getString("lmac");
            groupId = lights.getJSONObject(i).getInteger("groupId");
            productId = lights.getJSONObject(i).getString("productId");
            productId = productId.split(" ")[0];
            Integer gid = groupOperationDao.getGidByGroupIdAndMeshId(groupId,meshId,uid);
            Map<String,Integer> groupMap= groupOperationDao.getGidAndPid(groupId,meshId,uid);
            if (gid == null){//创建组
                //2.1.2 版本
                if (version == null){
                    //组放到扫描区中
                    group = groupOperationService.createGroup(mid,pid,groupId);
                }else if (version == 2){//2.2.0版本
                    //组放到恢复区中
                    pid = placeDao.getRecoverPlace(mid);//获取恢复区
                    if (pid == null){//创建恢复区
                        String pname = "恢复区";
                        pid = placeService.SavePlace(uid,meshId,pname);
                    }
                    group = groupOperationService.createGroup(mid,pid,groupId);
                }
                gid = group.getId();
            }else {//存在组 设置pid为对应组对应的区域
                pid = groupMap.get("pid");
            }
            lightList.setGid(gid);
            lightList.setLmac(lmac);
            lightList.setPid(pid);
            lightList.setLname(lmac);
            lightList.setMid(mid);
            lightList.setProductId(productId);
            Map<String, Integer> lightMap = lightAjustDao.getLid(lmac);
            //移动灯到对应的组
            if (lightMap==null){
                //数据库中没有该灯 创建灯
                lightAjustDao.saveLight(lightList);
            }else {
                //不在当前网络
                if (lightMap.get("mid").intValue()!=mid.intValue()){
                    //删除light_setting中的场景记录
                    lightAjustDao.deleteLightSettingByLmac(lmac);
                    lightAjustDao.updateLightGidAndMid(lightList);
                }else {
                    lightAjustDao.updateLightGidAndLmac(lightList);
                }
            }
        }
    }

    @Override
    public void updateLightXY(JSONObject params) throws NotExitException {
        JSONArray lights =  params.getJSONArray("lightList");
        LightList lightList;
        JSONObject light;
        for (int i=0;i<lights.size();i++){
            light = lights.getJSONObject(i);
            lightList = adjustBeanUtils.setLightList(light);
            String lmac = lights.getJSONObject(i).getString("lmac");
            Map<String, Integer> lightMap = lightAjustDao.getLid(lmac);
            if (lightMap == null || lightMap.size() == 0){
                throw new NotExitException("未发现灯");
            }
            Integer count = lightAjustDao.getLightAdjust(lmac);
            if (count>0){
                lightAjustDao.updateLightXY(lightList);
            }else {
                lightAjustDao.insertLightXY(lightList);
            }
        }
    }
}