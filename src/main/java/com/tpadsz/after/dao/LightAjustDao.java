package com.tpadsz.after.dao;

import com.tpadsz.after.entity.LightList;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @program: blt-alink
 * @description:
 * @author: Mr.Ma
 * @create: 2019-03-07 14:23
 **/
@Repository
public interface LightAjustDao {
    void saveLightAjustLog(@Param("meshId") String meshId, @Param("bltFlag") String bltFlag, @Param("operation") String operation);//保存灯操作日志

    void updateLightName(@Param("lmac") String lmac, @Param("lname") String lname);//重命名灯

    void saveLight(LightList lightList);//扫描灯

    void deleteLight(@Param("lmac") String lmac);//删除灯

    void updateLightGid(@Param("lmac") String lmac,@Param("gid") Integer gid);//移动灯

//    void saveLightColor(Param("uid") String uid,@Param("meshId") String meshId,@Param("lmac")String lmac,@Param("x")String x, @Param("y") String y);//扫描灯时，保存灯的x,y值，开关状态

//    String getLightOff(@Param("lmac") String lmac);//获取灯开关状态

    Integer getLid(@Param("lmac") String lmac);//获取lid

    void saveTempLight(LightList lightList);//创建临时灯
}
