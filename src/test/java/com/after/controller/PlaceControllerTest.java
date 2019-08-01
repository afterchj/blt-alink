package com.after.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tpadsz.after.exception.NameDuplicateException;
import com.tpadsz.after.service.PlaceService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * @program: blt-alink
 * @description:
 * @authr: Mr.Ma
 * @create: 2019-08-01 10:55
 **/
//基于注解的junit
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class PlaceControllerTest {

    @Resource
    private PlaceService placeService;

    @Test
    public void createTest(){
        String jsonStr = "{\"uid\":\"1\",\"token\":\"aaa\",\"meshId\":\"45790182\",\"pname\":\"区域3\"}";
        JSONObject jsonObject = JSON.parseObject(jsonStr);
        try {
            placeService.create(jsonObject);
        } catch (NameDuplicateException e) {
            System.out.println(e.getMessage());
        }
    }
}
