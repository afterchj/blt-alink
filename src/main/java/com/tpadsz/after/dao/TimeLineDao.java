package com.tpadsz.after.dao;

import com.tpadsz.after.entity.TimeBean;
import com.tpadsz.after.entity.TimeLine;
import com.tpadsz.after.entity.TimePointParams;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @program: blt-alink
 * @description:
 * @author: Mr.Ma
 * @create: 2019-06-03 11:20
 **/
@Component
public interface TimeLineDao {
    Integer createTimeLine(TimeLine timeLine);

    Integer createTimePoint(List<TimePointParams> timePointParamsList);

    void updateTname(@Param("tid") Integer tid, @Param("meshId") String meshId, @Param("tname") String tname,@Param("uid") String uid);

    List<TimeBean> getByUidAndMeshId(@Param("uid") String uid, @Param("meshId") String meshId);

    Integer delete(@Param("uid") String uid, @Param("meshId") String meshId, @Param("tid") Integer tid);

    Map<String,Object> getOneMeshId();

    int deleteOneMeshId(@Param("id") Integer id);

    void insertRepeated( @Param("mesh_id") String mesh_id);

    void updateTimeLineState(TimePointParams timePointParams);

    void insertRolePermission(List<Map<String,Integer>> list);

    void insertTimeDatail(TimePointParams timePointParams);

    void createTimeJson(@Param("tid") Integer tid,@Param("meshId") String meshId,@Param("uid") String uid,@Param
            ("jsonString")
            String
            jsonString);

    void deleteTimeLine(@Param("uid") String uid, @Param("meshId") String meshId, @Param("tid") Integer tid);

    void deleteTimePoint(@Param("uid") String uid, @Param("meshId") String meshId, @Param("tid") Integer tid);

    void deleteTimeDetail(@Param("uid") String uid, @Param("meshId") String meshId, @Param("tid") Integer tid);

    void deleteTimeJson(@Param("uid") String uid, @Param("meshId") String meshId, @Param("tid") Integer tid);

    TimeBean getTimeJson(@Param("tid") Integer tid, @Param("meshId") String meshId, @Param("uid") String uid);

    void updateTimeJson(@Param("tid") Integer tid, @Param("meshId") String meshId, @Param("uid") String uid, @Param
            ("json") String json);

    int getTimeLine(TimeLine timeLine);

//    void updateTimeJson(@Param("tid") Integer tid, @Param("") String meshId, String uid, String json);
}
