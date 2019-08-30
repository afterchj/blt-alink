package com.tpadsz.after.service;


import com.tpadsz.after.entity.FileRecord;
import com.tpadsz.after.entity.NewestFile;

public interface UpdateService {

    NewestFile getNewestFile(String appId,String pkg);

    FileRecord getFileRecords(String appId, int versionCode,String pkg);

    String findAccountByUid(String uid);
}
