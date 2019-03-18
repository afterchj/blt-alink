package com.after.controller;

import com.tpadsz.after.dao.GroupOperationDao;
import com.tpadsz.after.service.GroupOperationService;
import com.tpadsz.after.service.LightAjustService;
import com.tpadsz.after.service.SceneAjustService;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.ArrayList;
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
        SqlSessionFactory factory = (SqlSessionFactory) ac.getBean
                ("sqlSessionFactory");
        return factory.openSession();
    }

    GroupOperationService groupOperationService = ac.getBean
            ("groupOperationService", GroupOperationService.class);
    LightAjustService lightAjustService = ac.getBean("lightAjustService",
            LightAjustService.class);
    SceneAjustService sceneAjustService = ac.getBean("sceneAjustService",
            SceneAjustService.class);

    @Test
    public void getMeshSerialNoTest() {
        Integer mid = groupOperationService.getMeshSerialNo
                ("12345678", "aaaa");
        System.out.println("mid:" + mid);
    }

    @Test
    public void saveGroupTest() {
        Group group = new Group();
        group.setGname("组4");
        group.setMid(1);
        groupOperationService.saveGroup(group);
        System.out.println("id:" + group.getId());
        String groupId = groupOperationService.getGroupIdById(group.getId());
        System.out.println("groupId:" + groupId);
    }

    @Test
    public void saveGroupLogTest() {
        groupOperationService.saveGroupLog("aaaa", "12345678", "0", "1");
        System.out.println("success");
    }

    @Test
    public void updateGroupNameByMidTest() {
        Group group = new Group();
        group.setGname("灯100");
        group.setGroupId(1);
        group.setMid(1);
        groupOperationService.updateGroupNameByMid(group);
        System.out.println("success");
    }

    @Test
    public void saveLightAjustLogTest() {
        lightAjustService.saveLightAjustLog("12345678", "aa-aa-aa-aa", "0",
                "4");
        System.out.println("success");
    }

    @Test
    public void updateLightNameTest() {
        lightAjustService.updateLightName("aa-aa-aa-aa", "筒灯");
        System.out.println("success");
    }

    @Test
    public void getMapperTest() {
        SqlSession session = getSession();
        List<GroupList> groupLists = session.getMapper(GroupOperationDao
                .class).getGroupAll(3);
        for (GroupList groupList : groupLists) {
            System.out.println(groupList.toString());
        }
    }

    @Test
    public void getLightColorTest() {
        Map<String, Object> map = groupOperationService.getLightColor
                ("red");
        System.out.println(map);
//        System.out.println("x: " + map.get("x") + ", y:" + map.get("y"));
    }

    @Test
    public void getGroupAllTest() {
//        Integer mid = 1;
        List<GroupList> groupLists = groupOperationService.getGroupAll(3);
        System.out.println(groupLists.toString());
    }

    @Test
    public void getGidTest() {
        Group group = new Group();
//        group.setGid(19);
        group.setMid(1);
        group.setGroupId(1);
        Integer gid = groupOperationService.getGid(group);
        System.out.println(gid);
    }

    @Test
    public void saveLightTest() throws Exception {
        List<LightList> lightLists = new ArrayList<>();
        LightList lightList;
        for (int i=0;i<10;i++){
            lightList = new LightList();
            lightList.setLmac(i+"iii");
            lightList.setLname("iii");
            lightList.setMid(3);
            lightList.setGid(17);
            lightList.setProductId("1234");
            lightLists.add(lightList);
        }
        lightAjustService.saveLight(lightLists);
    }

    @Test
    public void deleteLightTest() {
        lightAjustService.deleteLight("rr-rr-rr-rr");
        System.out.println("success");
    }

    @Test
    public void updateLightGidTest() {
        Group group = new Group();
        group.setGid(21);
        group.setDgid(17);
        lightAjustService.updateLightGid(group);
        System.out.println("success");
    }

    @Test
    public void getSceneSerialNoTest() {
        Integer sid = groupOperationService.getSceneSerialNo(3, 8888, "aaaa");
        System.out.println("sid: " + sid);
    }

    @Test
    public void getGroupConsoleLogByGidTest() {
        GroupConsoleLog groupConsoleLog = groupOperationService
                .getGroupConsoleLogByGid(2, "aaaa", "12345678");
        System.out.println(groupConsoleLog.toString());
        if (groupConsoleLog.getLmac() == null) {
            System.out.println("lmac is null");
        }
    }

    @Test
    public void saveLightSettingTest() throws Exception {
        List<LightSetting> lightSettingList = new ArrayList<>();
        LightSetting lightSetting;
        for (int i=0;i<=1000;i++){
            lightSetting = new LightSetting();
            lightSetting.setX("21");
            lightSetting.setY("21%");
            lightSetting.setLid(i + 14);
            lightSetting.setSid(21);
            lightSetting.setOff("0");
//            getSession().getMapper(SceneAjustDao.class).saveLightSetting(lightSetting);
//            sceneAjustService.save(lightSetting);
            lightSettingList.add(i,lightSetting);
        }

//        System.out.println(lightSettingList.size());
        Long start  = System.currentTimeMillis();
        sceneAjustService.saveLightSetting( lightSettingList);
        Long end = System.currentTimeMillis();
        System.out.println("耗时: "+(end-start));
    }

    @Test
    public void saveSceneSettingTest() {
        SceneSetting sceneSetting = new SceneSetting();
        sceneSetting.setSid(1);
        sceneSetting.setX("20");
        sceneSetting.setY("20%");
        sceneAjustService.saveSceneSetting(sceneSetting);
    }

    @Test
    public void saveGroupSettingTest() {
        GroupSetting groupSetting = new GroupSetting();
        groupSetting.setMid(4);
        groupSetting.setGid(21);
        groupSetting.setSid(1);
        groupSetting.setX("11");
        groupSetting.setY("21%");
        //保存单组场景
        sceneAjustService.saveGroupSetting(groupSetting);
    }

    @Test
    public void saveSceneTest() {
        SceneAjust sceneAjust = new SceneAjust();
        sceneAjust.setUid("aaaa");
        sceneAjust.setMid(3);
        sceneAjust.setSceneId(8888);
        sceneAjust.setSname("8888");
        sceneAjustService.saveScene(sceneAjust);
        Integer sid = sceneAjust.getId();
        System.out.println("sid: " + sid);//17
    }

    @Test
    public void saveSceneLogTest() {
        SceneLog sceneLog = new SceneLog();
        sceneLog.setSceneId(7777);
        sceneLog.setUid("aaaa");
        sceneLog.setBltFlag("0");
        sceneLog.setMeshId("12345678");
        sceneLog.setOperation("0");
        sceneAjustService.saveSceneLog(sceneLog);
    }

    @Test
    public void saveLightColorTest() {
        lightAjustService.saveLightColor("0000", "00000000", "00-00-00-00",
                "0", "0%");
    }

    @Test
    public void getLidTest() {
        Integer lid = lightAjustService.getLid("ff-ff-ff-ff");
        System.out.println("lid: " + lid);
    }

    @Test
    public void saveTempLightTest() {
        LightList lightList = new LightList();
        lightList.setMid(3);
        lightList.setLmac("1-1-1-1");
        lightList.setLname("1-1-1-1");
        //临时创建灯
        lightAjustService.saveTempLight(lightList);
        System.out.println("id: " + lightList.getId());
    }

    @Test
    public void deleteLightSettingTest() {
        sceneAjustService.deleteLightSetting(2);
    }

    @Test
    public void test(){
        sceneAjustService.deleteLightSettingByLmac("a");
    }

}
