package com.tpadsz.after.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * @program: blt-alink
 * @description:
 * @author: Mr.Ma
 * @create: 2019-08-01 09:37
 **/
@Component
public interface PlaceDao {
    Integer getLastPlace(@Param("uid") String uid, @Param("meshId") String meshId);//获取网络中区域id最大的区域

    void savePlace(@Param("uid") String uid, @Param("meshId") String meshId, @Param("placeId") Integer placeId,@Param("pname") String pname);
    //创建区域

    Integer getPname(@Param("uid") String uid, @Param("meshId") String meshId, @Param("pname") String pname);//区域名是否重复
}
