package com.tpadsz.after.dao;

import com.tpadsz.after.entity.PlaceExtend;
import com.tpadsz.after.entity.PlaceSave;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @program: blt-alink
 * @description:
 * @author: Mr.Ma
 * @create: 2019-08-01 09:37
 **/
@Component
public interface PlaceDao {
    Integer getLastPlace(@Param("uid") String uid, @Param("meshId") String meshId);//获取网络中区域id最大的区域

    void savePlace(PlaceSave placeSave);//创建区域

    Integer getPname(@Param("uid") String uid, @Param("meshId") String meshId, @Param("pname") String pname);//区域名是否重复

    void deletePlaceByPid(@Param("pid") Integer pid);

    void updatePname(@Param("pid") Integer pid, @Param("pname") String pname);//重命名区域

    List<Map<String, Object>> getPlaceByMeshId(@Param("meshId")String meshId, @Param("uid")String uid);

    List<PlaceExtend> getPlacesAndGroups(@Param("mid") int mid);

    String getPlaceByPlaceIdAndMeshId(@Param("placeId") Integer placeId, @Param("meshId")String meshId,@Param("uid")
            String uid);

    Integer getPlaceByGroupIdAndMeshId(@Param("groupId") Integer dGroupId, @Param("meshId") String meshId,@Param("uid")
            String uid);

    Integer getRecoverPlace(@Param("mid") Integer mid);

    Integer getVersionCode(@Param("meshId") String meshId);

    Map<String,Object> getPlace(@Param("placeId")Integer placeId, @Param("meshId")String meshId, @Param("uid")String uid);
}
