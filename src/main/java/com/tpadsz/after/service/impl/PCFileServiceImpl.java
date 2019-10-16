package com.tpadsz.after.service.impl;

import com.tpadsz.after.dao.PCFileDao;
import com.tpadsz.after.entity.AppUser;
import com.tpadsz.after.service.PCFileService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author hongjian.chen
 * @date 2019/7/25 15:52
 */

@Service
public class PCFileServiceImpl implements PCFileService {
    @Resource
    private PCFileDao pcFileDao;

    @Override
    public AppUser getUser(String account) {
        return pcFileDao.getUser(account);
    }

    @Override
    public List getMesh(Map map) {
        return pcFileDao.getMesh(map);
    }

    @Override
    public Map getProject() {
        return pcFileDao.getProject();
    }

    @Override
    public Map getFile(Map map) {
        return pcFileDao.getFile(map);
    }

    @Override
    public void saveFile(Map map) {
        pcFileDao.saveFile(map);
    }
}
