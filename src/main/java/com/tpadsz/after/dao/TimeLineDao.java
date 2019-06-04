package com.tpadsz.after.dao;

import com.tpadsz.after.entity.TimeLine;
import com.tpadsz.after.entity.TimeLineList;
import com.tpadsz.after.entity.TimePointParams;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

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

    List<TimeLineList> getByUidAndMeshId(@Param("uid") String uid, @Param("meshId") String meshId);

    Integer delete(@Param("uid") String uid, @Param("meshId") String meshId, @Param("tid") Integer tid);
}
