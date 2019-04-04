package com.after.controller;

import com.tpadsz.after.dao.GroupOperationDao;
import com.tpadsz.after.dao.LightAjustDao;
import com.tpadsz.after.entity.*;
import com.tpadsz.after.service.GroupOperationService;
import com.tpadsz.after.service.LightAjustService;
import com.tpadsz.after.service.SceneAjustService;
import com.tpadsz.after.util.Encryption;
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
        groupOperationService.saveGroupLog("aaaa", "12345678", "0", "1", 0);
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
        lightAjustService.saveLightAjustLog("12345678", "0",
                "4", null);
        System.out.println("success");
    }

    @Test
    public void updateLightNameTest() {
//        lightAjustService.updateLightName("aa-aa-aa-aa", "筒灯");
//        System.out.println("success");
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
        for (int i = 0; i < 10; i++) {
            lightList = new LightList();
            lightList.setLmac(i + "iii");
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
        Integer num = lightAjustService.deleteLight("ii-ii-ii-ii");
        System.out.println(num);
    }

    @Test
    public void updateLightGidTest() throws Exception {
        List<LightList> lightLists = new ArrayList<>();
        LightList lightList = new LightList();
        lightList.setGid(1);
        lightList.setLmac("aa-aa-aa");
        lightLists.add(lightList);
        lightAjustService.updateLightGid(lightLists);
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
        for (int i = 0; i <= 1000; i++) {
            lightSetting = new LightSetting();
            lightSetting.setX("21");
            lightSetting.setY("21%");
            lightSetting.setLid(i + 14);
            lightSetting.setSid(21);
            lightSetting.setOff("0");
//            getSession().getMapper(SceneAjustDao.class).saveLightSetting(lightSetting);
//            sceneAjustService.save(lightSetting);
            lightSettingList.add(i, lightSetting);
        }

//        System.out.println(lightSettingList.size());
        Long start = System.currentTimeMillis();
        sceneAjustService.saveLightSetting(lightSettingList);
        Long end = System.currentTimeMillis();
        System.out.println("耗时: " + (end - start));
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
    public void getLidTest() {
//        Integer lid = lightAjustService.getLid("ff-ff-ff-ff");
//        System.out.println("lid: " + lid);
    }

    @Test
    public void saveTempLightTest() {
        LightList lightList = new LightList();
        lightList.setMid(3);
        lightList.setLmac("1-1-1-1");
        lightList.setLname("1-1-1-1");
        lightList.setGroupId(0);
        lightList.setProductId("2018");
        //创建灯
        lightAjustService.saveTempLight(lightList);
        System.out.println("id: " + lightList.getId());
    }

    @Test
    public void deleteLightSettingTest() {
        sceneAjustService.deleteLightSetting(2);
    }

    @Test
    public void deleteLightSettingByLmacTest() {
        sceneAjustService.deleteLightSettingByLmac("a");
    }

    @Test
    public void test1() {
        long startTime;
        long endTime;
        List<LightList> lightLists;
        LightList lightList;
        for (int j = 0; j < 10; j++) {
            startTime = System.currentTimeMillis();
            lightLists = new ArrayList<>(50001);
            for (int i = 0; i < 50000; i++) {
                lightList = new LightList();
                lightList.setGid(1);
                lightList.setMid(1);
                lightList.setGroupId(0);
                lightList.setLmac("aa-aa-aa" + i);
                lightLists.add(lightList);
            }
            endTime = System.currentTimeMillis();
            System.out.println("j= "+j+",time= "+(endTime - startTime));
            lightLists.clear();
        }


//        System.out.println("lightLists: "+lightLists);
//        StringBuffer stringBuffer = new StringBuffer();
//        for (LightList lightList1:lightLists){
//            stringBuffer.append(lightList1.getLmac()).append(",");
//        }
//        String s = stringBuffer.toString();
//        s = s.substring(0,s.length()-",".length());
//        System.out.println("s: " + s);
    }

    @Test
    public void getLidTest2() {
        Map<String, Integer> maps =  getSession().getMapper(LightAjustDao.class).getLid("aa-aa-aa-a");
        System.out.println(maps!=null&& maps.size()>0);
//        Collections.synchronizedList();

    }
    @Test
    public void test(){
        String plainPwd = "00000001";
        plainPwd = Encryption.getMD5Str(Encryption.getMD5Str(plainPwd));
//        String actualEncodingPassword = Encryption.encrypt(plainPwd, "8e0a2885579d4f22");
//        Encryption.HashPassword password = Encryption.encrypt(md5Pwd);
//        md5Pwd = Encryption.getMD5Str(md5Pwd);
        Encryption.HashPassword password = Encryption.encrypt(plainPwd);
        System.out.println("pwd: "+password.getPassword()+" salt: "+password.getSalt());
        //pwd: 901665ecefcd54d031a7821e341912aa3aefdc99 salt: f0c35bf074e8c5b8
    }

    @Test
    public void test2(){
        String plainPwd = "a892155918";
        plainPwd = Encryption.getMD5Str(Encryption.getMD5Str(plainPwd));
        String actualEncodingPassword = Encryption.encrypt(plainPwd, "f09ed0bb56ad10df");
        System.out.println("pwd: "+actualEncodingPassword);
    }

    @Test
    public void getPidTest3(){
        Integer pid = groupOperationService.getPid(0, 3);
        if (pid==null){
            AdjustPlace adjustPlace = new AdjustPlace();
            adjustPlace.setUid("aaaa");
            adjustPlace.setMid(3);
            adjustPlace.setPlaceId(0);
            adjustPlace.setPname("区域1");
            groupOperationService.savePlace(adjustPlace);
            pid = adjustPlace.getId();
        }
        System.out.println("pid: "+pid);
    }

    @Test
    public void test3(){
     lightAjustService.updateLight("dd-dd-dd-dd",0,3);
    }
}
