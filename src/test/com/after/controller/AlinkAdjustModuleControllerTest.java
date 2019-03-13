package com.after.controller;

import com.tpadsz.after.dao.GroupOperationDao;
import com.tpadsz.after.entity.Group;
import com.tpadsz.after.entity.GroupConsoleLog;
import com.tpadsz.after.entity.GroupList;
import com.tpadsz.after.entity.LightList;
import com.tpadsz.after.service.GroupOperationService;
import com.tpadsz.after.service.LightAjustService;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;
import java.util.Map;

/**
 * @program: blt-alink
 * @description:
 * @author: Mr.Ma
 * @create: 2019-03-06 16:24
 **/
public class AlinkAdjustModuleControllerTest {

    static ApplicationContext ac = null;
    static {
        ac = new ClassPathXmlApplicationContext("applicationContext.xml");
    }
    public static SqlSession getSession() {
        SqlSessionFactory factory = (SqlSessionFactory) ac.getBean("sqlSessionFactory");
        return factory.openSession();
    }

    GroupOperationService groupOperationService = ac.getBean("groupOperationService",GroupOperationService.class);
    LightAjustService lightAjustService = ac.getBean("lightAjustService",LightAjustService.class);

    @Test
    public void getMeshSerialNoTest(){
        Integer mid = groupOperationService.getMeshSerialNo
                ("12345678","aaaa");
        System.out.println("mid:"+mid);
    }

    @Test
    public void saveGroupTest(){
        Group group = new Group();
        group.setGname("组4");
        group.setMid(1);
        groupOperationService.saveGroup(group);
        System.out.println("id:"+group.getId());
        String groupId = groupOperationService.getGroupIdById(group.getId());
        System.out.println("groupId:"+groupId);
    }

    @Test
    public void saveGroupLogTest(){
        groupOperationService.saveGroupLog("aaaa","12345678","0","1");
        System.out.println("success");
    }

    @Test
    public void updateGroupNameByMidTest(){
        Group group = new Group();
        group.setGname("灯100");
        group.setGroupId(1);
        group.setMid(1);
        groupOperationService.updateGroupNameByMid(group);
        System.out.println("success");
    }

    @Test
    public void saveLightAjustLogTest(){
        lightAjustService.saveLightAjustLog("12345678","aa-aa-aa-aa","0","4");
        System.out.println("success");
    }

    @Test
    public void updateLightNameTest(){
        lightAjustService.updateLightName("aa-aa-aa-aa","筒灯");
        System.out.println("success");
    }

    @Test
    public void getMapperTest(){
        SqlSession session=getSession();
        List<GroupList> groupLists = session.getMapper(GroupOperationDao.class).getGroupAll(1);
        for (GroupList groupList:groupLists){
            System.out.println(groupList.toString());
        }
    }

    @Test
    public void getLightColorTest(){
        Map<String,Object> map = groupOperationService.getLightColor("bb-bb-bb-bb");
        System.out.println("x: "+map.get("x")+", y:"+map.get("y"));
    }

    @Test
    public void getGroupAllTest(){
        Integer mid = 1;
        List<GroupList> groupLists = groupOperationService.getGroupAll(mid);
        for (GroupList groupList:groupLists){
            List<LightList> lightLists = groupList.getLightLists();
            groupList.setMeshId("12345678");
            if (lightLists.size()>0){
                for (LightList lightList:lightLists){
                    String lmac = lightList.getLmac();
                    Map<String, Object> map = groupOperationService.getLightColor(lmac);
                    if (map!=null){
                        lightList.setX((String) map.get("x"));
                        lightList.setY((String) map.get("y"));
                    }
                }
            }
        }
    }

    @Test
    public void getGidTest(){
        Group group = new Group();
//        group.setGid(19);
        group.setMid(1);
        group.setGroupId(1);
        Integer gid = groupOperationService.getGid(group);
        System.out.println(gid);
    }

    @Test
    public void saveLightTest(){
        LightList lightList = new LightList();
        lightList.setLmac("rr-rr-rr-rr");
        lightList.setLname("灯19");
        lightList.setMid(3);
        lightList.setGid(17);
        lightList.setProductId("1234");
        lightAjustService.saveLight(lightList);
    }

    @Test
    public void deleteLightTest(){
        lightAjustService.deleteLight("rr-rr-rr-rr");
        System.out.println("success");
    }

    @Test
    public void updateLightGidTest(){
        Group group = new Group();
        group.setGid(21);
        group.setDgid(17);
        lightAjustService.updateLightGid(group);
        System.out.println("success");
    }

    @Test
    public void getSceneSerialNoTest(){
        Integer sid = groupOperationService.getSceneSerialNo(3,1111);
        System.out.println("sid: "+sid);
    }

    @Test
    public void test(){
        GroupConsoleLog groupConsoleLog = groupOperationService.getGroupConsoleLogByGid(21);
        System.out.println(groupConsoleLog.toString());
        if (groupConsoleLog.getLmac()==null){
            System.out.println("lmac is null");
        }
    }
}
