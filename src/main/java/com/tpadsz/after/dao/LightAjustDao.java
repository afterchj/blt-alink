package com.tpadsz.after.dao;

import com.tpadsz.after.entity.Group;
import com.tpadsz.after.entity.LightList;
import org.apache.ibatis.annotations.Param;

/**
 * @program: blt-alink
 * @description:
 * @author: Mr.Ma
 * @create: 2019-03-07 14:23
 **/
public interface LightAjustDao {
    void saveLightAjustLog(@Param("meshId") String meshId,@Param("lmac") String lmac, @Param("bltFlag") String bltFlag, @Param("operation") String operation);//保存灯操作日志

    void updateLightName(@Param("lmac") String lmac, @Param("lname") String lname);//重命名灯

    void saveLight(LightList lightList);//扫描灯

    void deleteLight(@Param("lmac") String lmac);//删除灯

    void updateLightGid(Group group);//已分组中移除灯
}
