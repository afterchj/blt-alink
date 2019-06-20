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

    TimeLineService timeLineService = ac.getBean("timeLineService",TimeLineService.class);

    @Test
    public void createTest(){
        String data="{\"uid\":\"7\",\"tname\":\"test1\",\"tid\":\"115\",\"week\":\"周日\",\"state\":\"0\"," +
                "\"repetition\":\"0\",\"meshId\":\"46280379\",\"timePointList\":[{\"time\":\"13:30\",\"state\":\"3\"},{\"time\":\"00:30\",\"state\":\"1\",\"sceneId\":1},{\"time\":\"21:30\"," +
                "\"state\":\"0\",\"sceneId\":1}]}";
        JSONObject jsonObject = JSONObject.parseObject(data);
        boolean b = timeLineService.create(jsonObject);
        System.out.println(b);
    }

    @Test
    public void renameTest(){
        String data="{\"uid\":\"7\",\"meshId\":\"46280379\",\"tname\":\"test3\",\"tid\":115}";
        JSONObject jsonObject = JSONObject.parseObject(data);
        timeLineService.rename(jsonObject);
        System.out.println("success");
    }

    @Test
    public void getTest(){
        String data="{\"uid\":\"7\",\"meshId\":\"46280379\"}";
        JSONObject jsonObject = JSONObject.parseObject(data);
        List<TimeLineList> timeLineLists = timeLineService.get(jsonObject);
        timeLineLists.stream().forEach(System.out::println);
    }

    @Test
    public void deleteTest(){
        String data="{\"uid\":\"7\",\"meshId\":\"46280379\",\"timeLineLists\":[{\"tid\":0},{\"tid\":4}]}";
        JSONObject jsonObject = JSONObject.parseObject(data);
        timeLineService.delete(jsonObject);
    }

    @Test
    public void createMeshToPCTest(){
        String data = "{\"count\":\"7\"}";
        JSONObject jsonObject = JSONObject.parseObject(data);
        String meshToPC = timeLineService.createMeshToPC(jsonObject);
        System.out.println(meshToPC);
    }

    @Test
    public void updateTimePointStateTest(){
        String data = "{\"uid\":\"7\",\"meshId\":\"46280379\",\"timeLineList\":[{\"tid\":\"51\",\"state\":\"2\"}]}";
        JSONObject jsonObject = JSONObject.parseObject(data);
        timeLineService.updateTimePointState(jsonObject);

    }

    @Test
    public void insertRolePermissionTest(){
        List<Map<String,Integer>> maps = new ArrayList<>();
        Map<String,Integer> map;
        //1 6 11 15 20 25 28 30
        for (int i=1;i<48;i++) {
            switch (i) {
                case 1:
                    map = new HashMap<>();
                    map.put("role_id", 4);
                    map.put("permission_id", 1);
                    maps.add(map);
                    break;
                case 6:
                    map = new HashMap<>();
                    map.put("role_id", 4);
                    map.put("permission_id", 6);
                    maps.add(map);
                    break;
                case 11:
                    map = new HashMap<>();
                    map.put("role_id", 4);
                    map.put("permission_id", 11);
                    maps.add(map);
                    break;
                case 15:
                    map = new HashMap<>();
                    map.put("role_id", 4);
                    map.put("permission_id", 20);
                    maps.add(map);
                    break;
                case 20:
                    map = new HashMap<>();
                    map.put("role_id", 4);
                    map.put("permission_id", 20);
                    maps.add(map);
                    break;
                case 28:
                    map = new HashMap<>();
                    map.put("role_id", 4);
                    map.put("permission_id", 28);
                    maps.add(map);
                    break;
                case 25:
                    map = new HashMap<>();
                    map.put("role_id", 4);
                    map.put("permission_id", 25);
                    maps.add(map);
                    break;
                case 30:
                    map = new HashMap<>();
                    map.put("role_id", 4);
                    map.put("permission_id", 30);
                    maps.add(map);
                    break;
            }
        }

//        System.out.println(maps.toString());
        getSession().getMapper(TimeLineDao.class).insertRolePermission(maps);
    }

    @Test
    public void deleteOneTest(){
        timeLineService.deleteOne("7","46280379",2);
    }


}
