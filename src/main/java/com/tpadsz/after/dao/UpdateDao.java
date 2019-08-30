package com.tpadsz.after.dao;

import com.tpadsz.after.entity.FileRecord;
import com.tpadsz.after.entity.NewestFile;
import org.apache.ibatis.annotations.Param;


public interface UpdateDao {
    NewestFile getNewestFile(@Param("appId") String appId,@Param("pkg") String pkg);

    FileRecord getFileRecords(@Param("appId") String appId, @Param("versionCode") int versionCode,@Param("pkg") String pkg);

    String findAccountByUid(@Param("uid")String uid);
}
