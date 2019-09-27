package com.tpadsz.after.service.impl;

import com.tpadsz.after.dao.BltConsoleDao;
import com.tpadsz.after.exception.NotExitException;
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
    public List getGroupsInfo(Map map) {
        return bltConsoleDao.getGroupsInfo(map);
    }

    @Override
    public List getLightsInfo(Map map) {
        return bltConsoleDao.getLightsInfo(map);
    }

    @Override
    public List getAll(Map map) {
        return bltConsoleDao.getAll(map);
    }

    @Override
    public String selectById(String uid) {
        return bltConsoleDao.selectById(uid);
    }

    @Override
    public int getTotal(Map map) {
        return bltConsoleDao.getTotal(map);
    }

    @Override
    public Integer getPid(Map map) {
        return bltConsoleDao.getPid(map);
    }

    @Override
    public Integer getCount(Map map) throws NotExitException {
        int count = bltConsoleDao.getCount(map);
        if (count > 0) {
            throw new NotExitException("该场景在定时列表中，不可删除！");
        } else {
            return count;
        }
    }

    @Override
    public void saveApplyScene(Map map) {
        bltConsoleDao.saveApplyScene(map);
    }

    @Override
    public void restScene(Map map) {
        bltConsoleDao.restScene(map);
    }

    @Override
    public void saveSceneName(Map map) throws NotExitException {
        List list = getScenes(map);
        if (list.size() > 0) {
            bltConsoleDao.saveSceneName(map);
        } else {
            throw new NotExitException("场景不存在！");
        }
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
