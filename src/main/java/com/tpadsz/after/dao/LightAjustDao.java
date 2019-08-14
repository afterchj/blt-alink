package com.tpadsz.after.dao;

import com.tpadsz.after.entity.LightList;
import com.tpadsz.after.entity.LightReturn;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @program: blt-alink
 * @description:
 * @author: Mr.Ma
 * @create: 2019-03-07 14:23
 **/
@Repository
public interface LightAjustDao {
    void saveLightAjustLog(@Param("meshId") String meshId, @Param("bltFlag") String bltFlag, @Param("operation") String operation, @Param("lmacs") String lmacs);//保存灯操作日志

    void updateLightName(@Param("lmac") String lmac, @Param("lname") String lname, @Param("pid") Integer pid);//重命名灯

    void saveLight(LightList lightList);//扫描灯

    Integer deleteLight(@Param("lmac") String lmac);//删除灯

    Map<String,Integer> getLid(@Param("lmac") String lmac);//获取mid 查询灯是否存在

    void saveTempLight(LightList lightList);//创建临时灯

    void updateLightGidAndMid(LightList lightList);//更新灯信息

    void deleteLightSettingByLmac(@Param("lmac") String lmac);//删除light_setting

    void updateLight(@Param("lmac") String lmac, @Param("groupId") Integer groupId, @Param("mid") Integer mid, @Param("pid") Integer pid);

    List<LightReturn> getAllByMid(@Param("mid") Integer mid);//查询网络下所有灯的信息

    void updateLightGidAndLmac(LightList lightList);

    void updateLightXY(LightList lightList);

    Integer getLightAdjust(@Param("lmac") String lmac);

    void insertLightXY(LightList lightList);

    Integer getLightByPid(@Param("pid") Integer pid);

    void updateLightByPidAndMeshId(@Param("pid") Integer pid, @Param("meshId") String meshId);
}
