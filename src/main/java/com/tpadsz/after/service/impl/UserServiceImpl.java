package com.tpadsz.after.service.impl;

import com.tpadsz.after.dao.UserDao;
import com.tpadsz.after.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by hongjian.chen on 2019/5/5.
 */

@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserDao userDao;

    @Override
    public List<Map> getFirms(Map keyword) {
        return userDao.getFirms(keyword);
    }

    @Override
    public void saveUser(Map map) {
        userDao.saveUser(map);
    }

    @Override
    public void updateUser(Map map) {
        userDao.updateUser(map);
    }
}
