package com.tpadsz.after.service.impl;

import com.tpadsz.after.dao.ConsoleLogDao;
import com.tpadsz.after.service.ConsoleLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by hongjian.chen on 2019/3/7.
 */

@Service
public class ConsoleLogServiceImpl implements ConsoleLogService {

    @Autowired
    private ConsoleLogDao consoleLogDao;

    @Override
    public void saveLog(Map map) {
        consoleLogDao.saveLog(map);
    }
}
