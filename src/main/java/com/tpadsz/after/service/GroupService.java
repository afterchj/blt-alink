package com.tpadsz.after.service;

import com.tpadsz.after.entity.Group;
import com.tpadsz.after.exception.GroupDuplicateException;
import com.tpadsz.after.exception.NameDuplicateException;
import com.tpadsz.after.exception.NotExitException;

/**
 * @program: blt-alink
 * @description:
 * @author: Mr.Ma
 * @create: 2019-08-16 10:11
 **/
public interface GroupService {

    void groupOperation(Group group,String operation) throws NameDuplicateException, GroupDuplicateException,
            NotExitException;
}
