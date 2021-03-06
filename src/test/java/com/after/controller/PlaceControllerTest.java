package com.after.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tpadsz.after.dao.PlaceDao;
import com.tpadsz.after.entity.PlaceExtend;
import com.tpadsz.after.exception.NameDuplicateException;
import com.tpadsz.after.exception.NotExitException;
import com.tpadsz.after.service.PlaceService;
import org.apache.commons.lang.text.StrBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

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

    @Resource
    private PlaceDao placeDao;

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

    @Test
    public void getPlaceByMeshIdTest(){
        String jsonStr = "{\"uid\":\"1\",\"token\":\"aaa\",\"meshId\":\"45790182\"}";
        JSONObject jsonObject = JSON.parseObject(jsonStr);
        List<Map<String, Object>> placeByMeshId = placeService.getPlaceByMeshId(jsonObject);
        System.out.println(placeByMeshId.toString());
    }

    @Test
    public void getPlacesAndGroupsTest(){
        Integer mid=20066;
        List<PlaceExtend> places = placeService.getPlacesAndGroups(mid);
        System.out.println(places.toString());

    }

    @Test
    public void DeleteTest() throws NotExitException {
        String jsonStr = "{\"uid\":\"1\",\"token\":\"aaa\",\"meshId\":\"45790182\",\"pid\":2545}";
        JSONObject jsonObject = JSON.parseObject(jsonStr);
        jsonObject.put("cid","20");
        System.out.println(jsonObject.toString());
//        placeService.delete(jsonObject);
    }

    @Test
    public void test2(){
        StrBuilder sb1 = new StrBuilder();
        StrBuilder sb2 = new StrBuilder();
        sb2.append("1");
        sb1.append("1").append(",").append("2");
        sb1 = sb2;
        System.out.println(sb1.toString());
    }

    @Test
    public void test3(){
        String str = "21761202";
        Integer versionCode = placeService.getVersionCode(str);
        System.out.println(versionCode);
    }

    @Test
    public void moveGroupTest(){
        Map<String, Object> placeMap = placeDao.getPlace(1, "11223344","100");
//        System.out.println(placeMap.isEmpty());
        System.out.println(placeMap==null);

    }

}
