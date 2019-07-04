package com.tpadsz.after.dao;

import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by hongjian.chen on 2019/3/7.
 */
@Component
public interface ConsoleLogDao {

    void saveLog(Map map);
}
