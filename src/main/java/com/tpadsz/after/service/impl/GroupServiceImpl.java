package com.tpadsz.after.service.impl;

import com.tpadsz.after.dao.GroupOperationDao;
import com.tpadsz.after.dao.LightAjustDao;
import com.tpadsz.after.entity.Group;
import com.tpadsz.after.exception.GroupDuplicateException;
import com.tpadsz.after.exception.NameDuplicateException;
import com.tpadsz.after.exception.NotExitException;
import com.tpadsz.after.service.GroupService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @program: blt-alink
 * @description:
 * @author: Mr.Ma
 * @create: 2019-08-16 10:14
 **/
@Service
public class GroupServiceImpl implements GroupService {

    @Resource
    private GroupOperationDao groupOperationDao;

    @Resource
    private LightAjustDao lightAjustDao;

    public void createGroup(Group group) throws NameDuplicateException, GroupDuplicateException {
        String dbGname = groupOperationDao.getGname(group);
        if (StringUtils.isNotBlank(dbGname)){
            throw new NameDuplicateException("组名重复");
        }
        if (group.getGid() == null){
            groupOperationDao.saveGroup(group);
        }else {
            throw new GroupDuplicateException("存在组");
        }
    }

    public void deleteGroup(Group group) throws NotExitException {
        if (group.getGid() != null){
            groupOperationDao.updateGroupNameByMid(group);
        }else {
            throw new NotExitException("不存在组");
        }
    }

    public void renameGroup(Group group) throws NotExitException {
        if (group.getGid() != null){
            /* TODO 查询组内灯的数量 */
            Integer lightNum = groupOperationDao.getLightNum(group);
            if (lightNum != null){
                // TODO 存在设备 转移到未分组
                groupOperationDao.updateGidInLight(group);
            }
            // TODO 删除组
            groupOperationDao.deleteGroup(group);
        }else {
            throw new NotExitException("不存在组");
        }
    }

    @Override
    public void groupOperation(Group group,String operation) throws NameDuplicateException, GroupDuplicateException, NotExitException {
        if ("0".equals(operation)) {//创建组
            createGroup(group);
        } else if ("2".equals(operation)) {//重命名组
            deleteGroup(group);
        } else if ("1".equals(operation)) {//删除组
            renameGroup(group);
        }
    }
}
