package com.after.controller;

import com.alibaba.fastjson.JSONObject;
import com.tpadsz.after.entity.TimeLineList;
import com.tpadsz.after.service.TimeLineService;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

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
        String data="{\"uid\":\"7\",\"meshId\":\"46280379\",\"tid\":1000}";
        JSONObject jsonObject = JSONObject.parseObject(data);
        Integer delete = timeLineService.delete(jsonObject);
        System.out.println(delete);
    }

    @Test
    public void createMeshToPCTest(){
        String data = "{\"count\":\"7\"}";
        JSONObject jsonObject = JSONObject.parseObject(data);
        String meshToPC = timeLineService.createMeshToPC(jsonObject);
        System.out.println(meshToPC);
    }
}
