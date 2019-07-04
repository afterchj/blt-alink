package com.after.controller;

import com.alibaba.fastjson.JSONObject;
import com.tpadsz.after.dao.TimeLineDao;
import com.tpadsz.after.service.TimeLineService;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: blt-alink
 * @description:
 * @author: Mr.Ma
 * @create: 2019-03-06 16:24
 **/
public class TimeLineControllerTest {

    static ApplicationContext ac = null;

    static {
        ac = new ClassPathXmlApplicationContext("applicationContext.xml");
    }

    public static SqlSession getSession() {
        SqlSessionFactory factory = (SqlSessionFactory) ac.getBean
                ("sqlSessionFactory");
        return factory.openSession();
    }

    TimeLineService timeLineService = ac.getBean("timeLineService", TimeLineService.class);


    @Test
    public void renameTest() {
        String data = "{\"uid\":\"7\",\"meshId\":\"84137526\",\"tname\":\"ceshi\",\"tid\":300}";
        JSONObject jsonObject = JSONObject.parseObject(data);
        timeLineService.rename(jsonObject);
        System.out.println("success");
    }

    @Test
    public void getTest() {
        String data = "{\"uid\":\"7\",\"meshId\":\"84137526\"}";
        JSONObject jsonObject = JSONObject.parseObject(data);
        List<JSONObject> timeLineLists = timeLineService.get(jsonObject);
        timeLineLists.stream().forEach(System.out::println);
    }

    @Test
    public void deleteTest() {
        String data = "{\"uid\":\"7\",\"meshId\":\"84137526\",\"timeLineLists\":[{\"tid\":300}]}";
        JSONObject jsonObject = JSONObject.parseObject(data);
        timeLineService.delete(jsonObject);
    }

    @Test
    public void createMeshToPCTest() {
        String data = "{\"count\":\"7\"}";
        JSONObject jsonObject = JSONObject.parseObject(data);
        String meshToPC = timeLineService.createMeshToPC(jsonObject);
        System.out.println(meshToPC);
    }

    @Test
    public void updateTimePointStateTest() {
        String data = "{\"uid\":\"7\",\"meshId\":\"84137526\",\"tid\":2,\"state\":\"1\"}";
        JSONObject jsonObject = JSONObject.parseObject(data);
        timeLineService.updateTimeLineState(jsonObject);

    }

    @Test
    public void insertRolePermissionTest() {
        List<Map<String, Integer>> maps = new ArrayList<>();
        Map<String, Integer> map;
        //1 6 11 15 20 25 28 30
        for (int i = 1; i < 48; i++) {
            map = new HashMap<>();
            map.put("role_id", 3);
            map.put("permission_id", i);
            maps.add(map);
        }

//        System.out.println(maps.toString());
        getSession().getMapper(TimeLineDao.class).insertRolePermission(maps);
    }

    @Test
    public void deleteOneTest() {
        timeLineService.deleteOne("7", "46280379", 2);
    }

    @Test
    public void createTest(){
        String data = "{\"item_set\":0,\"week\":\"周日、周一\",\"ischoose\":true,\"item_desc\":\"重复,周日,周一,周二,周三,周四,周五," +
                "周六\",\"timePointList\":[{\"pos_x\":482,\"hour\":3,\"light_status\":0,\"sence_index\":1,\"time\":0," +
                "\"minute\":30},{\"pos_x\":659,\"hour\":5,\"light_status\":0,\"sence_index\":1,\"time\":0," +
                "\"minute\":50},{\"pos_x\":482,\"hour\":3,\"light_status\":0,\"detailValueList\":[{\"pos_x\":659," +
                "\"hour\":3,\"light_status\":0,\"sence_index\":1,\"minute\":0},{\"pos_x\":659,\"hour\":3," +
                "\"light_status\":0,\"sence_index\":1,\"time\":0,\"minute\":30}],\"sence_index\":23,\"time\":0," +
                "\"minute\":30}],\"dayObj\":{\"tus\":1,\"loop\":1,\"sat\":1,\"wed\":1,\"fri\":1,\"mon\":1,\"sun\":1," +
                "\"thr\":1},\"tname\":\"ceshi\",\"repetition\":\"0\",\"meshId\":\"84137526\",\"tid\":400," +
                "\"uid\":\"7\",\"item_tag\":0,\"item_title\":\"ceshi\"}";
        JSONObject jsonObject = JSONObject.parseObject(data);
        String s = jsonObject.toJSONString();
        System.out.println(s);
        timeLineService.create(jsonObject);

    }

    @Test
    public void test(){
        String data = "{\"uid\":\"7\",\"tname\":\"test1\",\"tid\":300,\"week\":\"周日、周一\",\"state\":\"0\"," +
                "\"repetition\":\"0\",\"meshId\":\"84137526\",\"dayObj\":{\"fri\":1,\"loop\":1,\"mon\":1,\"sat\":1," +
                "\"sun\":1,\"thr\":1,\"tus\":1,\"wed\":1},\"ischoose\":true,\"item_desc\":\"重复,周日,周一,周二,周三,周四,周五," +
                "周六\",\"item_set\":0,\"item_tag\":0,\"timePointList\":[{\"time\":\"13:30\"," +
                "\"hour\":3,\"minute\":30,\"sceneId\":1,\"pos_x\":482,\"time\":0,\"light_status\":0}," +
                "{\"hour\":5,\"minute\":50,\"sceneId\":1,\"ttime\":0," +
                "\"pos_x\":659,\"light_status\":0},{\"hour\":3,\"minute\":30," +
                "\"sceneId\":23,\"pos_x\":482,\"time\":0,\"light_status\":0," +
                "\"detailValueList\":[{\"hour\":3,\"minute\":0,\"sceneId\":23," +
                "\"pos_x\":659,\"light_status\":0},{\"hour\":3," +
                "\"minute\":30,\"sceneId\":23,\"time\":0,\"pos_x\":659,\"light_status\":0}]}]}";
        JSONObject jsonObject = JSONObject.parseObject(data);
        timeLineService.createTimeLine(jsonObject);
    }




}
