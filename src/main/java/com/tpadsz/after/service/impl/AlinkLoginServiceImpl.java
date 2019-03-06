package com.tpadsz.after.service.impl;

import com.tpadsz.after.dao.AlinkLoginDao;
import com.tpadsz.after.service.AlinkLoginService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @program: blt-light
 * @description:
 * @author: Mr.Ma
 * @create: 2019-03-06 11:24
 **/
@Service("alinkLoginService")
public class AlinkLoginServiceImpl implements AlinkLoginService {

    @Resource
    private AlinkLoginDao alinkLoginDao;

    @Override
    public void loginOut(String uid) {
        alinkLoginDao.saveLoginOutLog(uid);
    }
}
