package com.tpadsz.after.service.impl;

import com.tpadsz.after.dao.PCFileDao;
import com.tpadsz.after.service.PCFileService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
    public Map getFile(Map map) {
        return pcFileDao.getFile(map);
    }

    @Override
    public void saveFile(Map map) {
        pcFileDao.saveFile(map);
    }
}
