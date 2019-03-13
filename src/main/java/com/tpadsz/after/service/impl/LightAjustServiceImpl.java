package com.tpadsz.after.service.impl;

import com.tpadsz.after.dao.LightAjustDao;
import com.tpadsz.after.entity.Group;
import com.tpadsz.after.entity.LightList;
import com.tpadsz.after.service.LightAjustService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @program: blt-alink
 * @description:
 * @author: Mr.Ma
 * @create: 2019-03-07 14:19
 **/
@Service("lightAjustService")
public class LightAjustServiceImpl implements LightAjustService {

    @Resource
    private LightAjustDao lightAjustDao;

    @Override
    public void saveLightAjustLog(String meshId,String lmac, String bltFlag,
                               String operation) {
        lightAjustDao.saveLightAjustLog(meshId, lmac,bltFlag,operation);
    }

    @Override
    public void updateLightName(String lmac, String lname) {
        lightAjustDao.updateLightName(lmac,lname);
    }

    @Override
    public void saveLight(LightList lightList) {
        lightAjustDao.saveLight(lightList);
    }

    @Override
    public void deleteLight(String lmac) {
        lightAjustDao.deleteLight(lmac);
    }

    @Override
    public void updateLightGid(Group group) {
        lightAjustDao.updateLightGid(group);
    }
}
