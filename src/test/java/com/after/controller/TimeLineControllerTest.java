package com.after.controller;

import com.alibaba.fastjson.JSONObject;
import com.tpadsz.after.dao.TimeLineDao;
import com.tpadsz.after.entity.TimeLineList;
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
    public void createTest() {
        String data = "{\"uid\":\"10\",\"token\":\"c1e1fc6d3fb54eb5a759a4bfad5c8c26\",\"meshId\":\"28650319\"," +
                "\"tname\":\"Timeline_00\",\"tid\":2,\"state\":\"0\",\"repetition\":\"1\",\"week\":\"重复,周日,周一,周二,周三," +
                "周四,周五,周六\",\"timePointList\":[{\"sceneId\":2,\"state\":\"1\",\"time\":\"2:31\"}]}";
        JSONObject jsonObject = JSONObject.parseObject(data);
        boolean b = timeLineService.create(jsonObject);
        System.out.println(b);
    }

    @Test
    public void renameTest() {
        String data = "{\"uid\":\"7\",\"meshId\":\"46280379\",\"tname\":\"test3\",\"tid\":115}";
        JSONObject jsonObject = JSONObject.parseObject(data);
        timeLineService.rename(jsonObject);
        System.out.println("success");
    }

    @Test
    public void getTest() {
        String data = "{\"uid\":\"7\",\"meshId\":\"46280379\"}";
        JSONObject jsonObject = JSONObject.parseObject(data);
        List<TimeLineList> timeLineLists = timeLineService.get(jsonObject);
        timeLineLists.stream().forEach(System.out::println);
    }

    @Test
    public void deleteTest() {
        String data = "{\"uid\":\"7\",\"meshId\":\"46280379\",\"timeLineLists\":[{\"tid\":0},{\"tid\":4}]}";
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
        String data = "{\"uid\":\"7\",\"meshId\":\"46280379\",\"timeLineList\":[{\"tid\":\"51\",\"state\":\"2\"}]}";
        JSONObject jsonObject = JSONObject.parseObject(data);
        timeLineService.updateTimePointState(jsonObject);

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
    public void test(){
        String data = "{\"uid\":\"7\",\"tname\":\"test1\",\"tid\":200,\"week\":\"周日、周一\",\"state\":\"0\"," +
                "\"repetition\":\"0\",\"meshId\":\"84137526\",\"dayObj\":{\"fri\":1,\"loop\":1,\"mon\":1,\"sat\":1," +
                "\"sun\":1,\"thr\":1,\"tus\":1,\"wed\":1},\"ischoose\":true,\"item_desc\":\"重复,周日,周一,周二,周三,周四,周五," +
                "周六\",\"item_set\":0,\"item_tag\":0,\"timePointList\":[{\"time\":\"13:30\",\"state\":\"0\"," +
                "\"hour\":3,\"minute\":30,\"sceneId\":1,\"pos_x\":482,\"ttime\":0,\"light_status\":0}," +
                "{\"time\":\"00:30\",\"state\":\"1\",\"hour\":5,\"minute\":50,\"sceneId\":1,\"ttime\":0," +
                "\"pos_x\":659,\"light_status\":0},{\"time\":\"13:30\",\"state\":\"0\",\"hour\":3,\"minute\":30," +
                "\"sceneId\":23,\"pos_x\":482,\"ttime\":0,\"light_status\":0," +
                "\"detailValueList\":[{\"time\":\"00:30\",\"state\":\"1\",\"hour\":3,\"minute\":0,\"sceneId\":1," +
                "\"tttime\":0,\"pos_x\":659,\"llight_status\":0},{\"time\":\"00:30\",\"state\":\"1\",\"hour\":3," +
                "\"minute\":30,\"sceneId\":1,\"tttime\":0,\"pos_x\":659,\"llight_status\":0}]}]}";
        JSONObject jsonObject = JSONObject.parseObject(data);
        timeLineService.create(jsonObject);

    }


}
