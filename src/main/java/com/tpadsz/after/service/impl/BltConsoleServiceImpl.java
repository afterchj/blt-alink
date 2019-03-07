package com.tpadsz.after.service.impl;

import com.tpadsz.after.dao.BltConsoleDao;
import com.tpadsz.after.service.BltConsoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by hongjian.chen on 2019/3/7.
 */

@Service
public class BltConsoleServiceImpl implements BltConsoleService {

    @Autowired
    private BltConsoleDao bltConsoleDao;

    @Override
    public List getScenes(Map map) {
        return bltConsoleDao.getScenes(map);
    }

    @Override
    public List getGroups(Map map) {
        return bltConsoleDao.getGroups(map);
    }

    @Override
    public List getLights(Map map) {
        return bltConsoleDao.getLights(map);
    }

    @Override
    public void saveApplyScene(Map map) {
        bltConsoleDao.saveApplyScene(map);
    }

    @Override
    public void saveSceneName(Map map) {
        bltConsoleDao.saveSceneName(map);
    }

    @Override
    public void deleteScene(Map map) {
        bltConsoleDao.deleteScene(map);
    }

    @Override
    public void saveOperation(Map map) {
        bltConsoleDao.saveOperation(map);
    }
}
