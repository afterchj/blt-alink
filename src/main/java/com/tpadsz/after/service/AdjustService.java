package com.tpadsz.after.service;

import com.alibaba.fastjson.JSONObject;
import com.tpadsz.after.entity.Group;
import com.tpadsz.after.entity.LightReturn;
import com.tpadsz.after.exception.GroupDuplicateException;
import com.tpadsz.after.exception.NameDuplicateException;
import com.tpadsz.after.exception.NotExitException;
import com.tpadsz.after.exception.SystemAlgorithmException;

import java.util.List;

/**
 * @program: blt-alink
 * @description:
 * @author: Mr.Ma
 * @create: 2019-08-16 10:11
 **/
public interface AdjustService {

    void groupOperation(Group group,String operation) throws NameDuplicateException, GroupDuplicateException,
            NotExitException;

    List<LightReturn> lightOperation(Group group, JSONObject params) throws SystemAlgorithmException, NotExitException;
}
