package com.tpadsz.after.service;

import com.tpadsz.after.exception.NotExitException;

import java.util.List;
import java.util.Map;

/**
 * Created by hongjian.chen on 2019/3/7.
 */
public interface PCFileService {

    Map getFile(Map map);
    void saveFile(Map map);
}
