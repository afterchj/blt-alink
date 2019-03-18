package com.tpadsz.after.service.impl;

import com.tpadsz.after.dao.UpdateDao;
import com.tpadsz.after.entity.FileRecord;
import com.tpadsz.after.entity.NewestFile;
import com.tpadsz.after.service.UpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UpdateServiceImpl implements UpdateService {

    @Autowired
    UpdateDao updateDao;

    @Override
    public NewestFile getNewestFile(String appId) {
        return updateDao.getNewestFile(appId);
    }

    @Override
    public FileRecord getFileRecords(String appId, int versionCode) {
        return updateDao.getFileRecords(appId,versionCode);
    }
}
