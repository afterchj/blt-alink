package com.tpadsz.after.service.impl;

import com.tpadsz.after.dao.PCFileDao;
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
    public Map getUser(String account) {
        return pcFileDao.getUser(account);
    }

    @Override
    public List getMesh(Map map) {
        return pcFileDao.getMesh(map);
    }

    @Override
    public List<Map> getProject(Map map) {
        return pcFileDao.getProject(map);
    }

    @Override
    public Map getFile(Map map) {
        return pcFileDao.getFile(map);
    }

    @Override
    public void saveMesh(Map map) {
        pcFileDao.saveFile(map);
    }

    @Override
    public void saveMesh(List map) {
        pcFileDao.saveMesh(map);
    }

    @Override
    public void saveUpdateProject(Map map) {
        pcFileDao.saveProject(map);
    }

    @Override
    public void saveFile(Map map) {
        pcFileDao.saveFile(map);
    }
}
