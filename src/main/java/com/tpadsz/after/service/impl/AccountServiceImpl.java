package com.tpadsz.after.service.impl;

import com.tpadsz.after.dao.AccountDao;
import com.tpadsz.after.exception.RepetitionException;
import com.tpadsz.after.service.AccountService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by hongjian.chen on 2019/3/6.
 */

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountDao accountDao;

    @Override
    public void updateAccount(Map map) throws RepetitionException {
        String username = (String) map.get("uname");
        if (StringUtils.isNotEmpty(username)) {
            int count = findByUser(username);
            if (count > 0) {
                throw new RepetitionException(11, "用户名已存在！");
            }
        }
        accountDao.updateAccount(map);
    }

    @Override
    public int findByUser(String username) {
        return accountDao.findByUser(username);
    }

    @Override
    public int findByMobile(String mobile) {
        return accountDao.findByMobile(mobile);
    }

    @Override
    public List<Map> getFirms() {
        return accountDao.getFirms();
    }

    @Override
    public void saveUser(Map map) {
        accountDao.saveUser(map);
    }
}
