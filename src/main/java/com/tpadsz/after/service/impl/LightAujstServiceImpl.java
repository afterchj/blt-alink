package com.tpadsz.after.service.impl;

import com.tpadsz.after.dao.LightAujstDao;
import com.tpadsz.after.service.LightAujstService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @program: blt-alink
 * @description:
 * @author: Mr.Ma
 * @create: 2019-03-07 14:19
 **/
@Service("lightAujstService")
public class LightAujstServiceImpl implements LightAujstService {

    @Resource
    private LightAujstDao lightAujstDao;

    @Override
    public void saveLightAjust(String lmac, String bltFlag, String lname,
                               String operation) {
        lightAujstDao.saveLightAjust(lmac,bltFlag,lname,operation);
    }
}
