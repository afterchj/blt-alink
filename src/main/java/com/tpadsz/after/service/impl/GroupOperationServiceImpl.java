package com.tpadsz.after.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.tpadsz.after.dao.GroupOperationDao;
import com.tpadsz.after.dao.PlaceDao;
import com.tpadsz.after.entity.*;
import com.tpadsz.after.exception.GroupDuplicateException;
import com.tpadsz.after.exception.PlaceNotFoundException;
import com.tpadsz.after.service.GroupOperationService;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @program: blt-alink
 * @description:
 * @author: Mr.Ma
 * @create: 2019-03-06 14:48
 **/
@Service("groupOperationService")
public class GroupOperationServiceImpl implements GroupOperationService {

    @Resource
    private GroupOperationDao groupOperationDao;

    @Resource
    private PlaceDao placeDao;

    @Override
    public void saveGroupLog(String uid, String meshId, String operation, String bltFlag, Integer groupId) {
        groupOperationDao.saveGroupLog(uid, meshId, operation, bltFlag, groupId);
    }

    @Override
    public Integer getMeshSerialNo(String meshId, String uid) {
        return groupOperationDao.getMeshSerialNo(meshId, uid);
    }

    @Override
    public String getGroupIdById(Integer id) {
        return groupOperationDao.getGroupIdById(id);
    }

    @Override
    public Integer saveGroup(Group group) {
        return groupOperationDao.saveGroup(group);
    }

    @Override
    public void updateGroupNameByMid(Group group) {
        groupOperationDao.updateGroupNameByMid(group);
    }

    @Override
    public List<GroupList> getGroupAll(Integer mid) {
        return groupOperationDao.getGroupAll(mid);
    }

    @Override
    public Map<String, Object> getLightColor(String lmac) {
        return groupOperationDao.getLightColor(lmac);
    }

    @Override
    public Integer getGid(Group group) {
        return groupOperationDao.getGid(group);
    }

    @Override
    public Integer getLightNum(Group group) {
        return groupOperationDao.getLightNum(group);
    }

    @Override
    public void updateGidInLight(Group group) {
        groupOperationDao.updateGidInLight(group);
    }

    @Override
//    public void deleteGroup(Group group, Integer version) {
    public void deleteGroup(Group group) {
        //灯移动到未分组
        groupOperationDao.updateGidInLight(group);
        groupOperationDao.deleteGroup(group);
//        if (version==null ){
//        v2 .1 .0 灯移动到未分组
//            groupOperationDao.updateGidInLight(group);
//        }
    }

    @Override
    public Integer getSceneSerialNo(Integer mid, Integer sceneId, String uid) {
        return groupOperationDao.getSceneSeriaNo(mid, sceneId, uid);
    }

    @Override
    public GroupConsoleLog getGroupConsoleLogByGid(Integer groupId, String uid, String meshId) {
        return groupOperationDao.getGroupConsoleLogByGid(groupId, uid, meshId);
    }

//    @Override
//    public Integer getPid(Integer placeId, Integer mid) {
//        return groupOperationDao.getPid(placeId,mid);
//    }

    @Override
    public void savePlace(AdjustPlace adjustPlace) {
        groupOperationDao.savePlace(adjustPlace);
    }

    @Override
    public String getGname(Group group) {
        return groupOperationDao.getGname(group);
    }

    @Override
    public Integer getDefaultPlace(Integer pid, String uid, Integer mid) throws PlaceNotFoundException {
//        if (pid==null){
//            //v2.0版本 获取默认区域
//            pid=1;
//            pid = groupOperationDao.getDefaultPlace(uid,mid);
//            if (pid == null){
//                //未发现默认区域
//                throw new DefaultPlaceNotFoundException();
//            }
//        }

//        if (pid == null){
//            pid = 1;//v2.0版本 获取默认区域
//        }
//        Integer id = groupOperationDao.getPlaceId(pid,uid,mid);
//        if (id == null){
//            //未发现区域
//            throw new PlaceNotFoundException();
//        }
        return pid;
    }

    @Override
    public void saveGroupSetting(GroupSetting groupSetting) {
        groupOperationDao.saveGroupSetting(groupSetting);
    }

    @Override
    public void deleteGroupSetting(Integer sid) {
        groupOperationDao.deleteGroupSetting(sid);
    }

    @Override
    public Map<String, Object> moveGroup(JSONObject params) throws GroupDuplicateException {
        Map<String, Object> placeMap = new HashedMap();
        Integer pid = params.getInteger("pid");//目标区域序列号
        Integer placeId = params.getInteger("placeId");//目标区域id
        String meshId = params.getString("meshId");
        String uid = params.getString("uid");
        Integer groupId = params.getInteger("groupId");
        String gname = params.getString("gname");//组名
        String pname;
        pname = placeDao.getPlaceByPlaceIdAndMeshId(placeId, meshId);
        if (pname == null){
            //区域未创建 创建区域
            StringBuffer sb = new StringBuffer();
            StringBuffer preSb = new StringBuffer();
            sb.append("区域").append((placeId));
            preSb.append("区域").append((placeId));
            pname = sb.toString();
            Integer pnameCount = placeDao.getPname(uid, meshId, pname);
            int num = 1;
            while (pnameCount>0){
                //区域名重复
                pname = sb.append("(").append(num).append(")").toString();
                sb = preSb;
                pnameCount = placeDao.getPname(uid, meshId, pname);
                num++;
            }
            PlaceSave placeSave = SavePlaceFactory.savePlace(uid,meshId,pname,placeId);
            placeDao.savePlace(placeSave);
            pid  = placeSave.getPid();
        }
        String oldGname = groupOperationDao.getGnameByPidAndMeshId(pid, meshId, gname);
        if (oldGname != null) {
            //组名重复
            throw new GroupDuplicateException();
        }
        //不同区域之间移动
        groupOperationDao.moveGroup(pid, meshId, groupId);
        groupOperationDao.updateLightPid(groupId, pid, meshId);//修改灯信息的pid
        placeMap.put("pid",pid);
        placeMap.put("placeId",placeId);
        placeMap.put("pname",pname);
        return placeMap;
    }

    @Override
    public Map<String, Object> saveGroup(JSONObject params) {
        Map<String,Object> groupMap = new HashedMap();
        String uid = params.getString("uid");
        String meshId = params.getString("meshId");
        Integer pid = params.getInteger("pid");
        Integer mid = groupOperationDao.getMeshSerialNo(meshId,uid);
        Integer preGroupId = groupOperationDao.getLastGroup(mid);
        Integer groupId = preGroupId + 1;
        Group group = createGroup(mid,pid,groupId);
        groupMap.put("gname",group.getGname());
        groupMap.put("groupId",group.getGroupId());
        return groupMap;
    }

    public Group createGroup(Integer mid,Integer pid,Integer groupId){
        Group group = new Group();
        group.setMid(mid);
        group.setPid(pid);
        group.setGroupId(groupId);
        StringBuffer sb = new StringBuffer();
        StringBuffer preSb = new StringBuffer();
        sb.append("组").append(groupId);
        preSb.append("组").append(groupId);
        String gname;
        group.setGname(sb.toString());
        String dbGname = groupOperationDao.getGname(group);
        int count=0;
        //区域内组名重复 组名后添加"(1)"后缀
        while (dbGname!=null){
            count++;
            gname = sb.append("(").append(count).append(")").toString();
            group.setGname(gname);
            sb = preSb;
            dbGname = groupOperationDao.getGname(group);
        }
        //创建组
        groupOperationDao.saveGroup(group);
        return group;
    }
}
