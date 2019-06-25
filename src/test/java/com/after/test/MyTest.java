package com.after.test;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tpadsz.after.dao.AccountDao;
import com.tpadsz.after.service.ValidationService;
import com.tpadsz.after.util.HttpUtils;
import com.tpadsz.after.util.RandomNumberGenerator;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hongjian.chen on 2017/10/24.
 */
public class MyTest {
    private static ClassPathXmlApplicationContext ctx;

    public static SqlSession getSession() {
        ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        SqlSessionFactory factory = (SqlSessionFactory) ctx.getBean("sqlSessionFactory");
        return factory.openSession();
    }

    public static SqlSessionTemplate getSqlSessionTemplate() {
        ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        return (SqlSessionTemplate) ctx.getBean("sqlSessionTemplate");
    }


    @Test
    public void test() {
        SqlSessionTemplate sqlSessionTemplate = getSqlSessionTemplate();
        Map map=new HashMap();
        map.put("uid",10);
        map.put("sceneId",0);
        map.put("meshId","77661234");
//        System.out.println("template=" + sqlSessionTemplate);
//        List<Map> list=new ArrayList<>();
//        for (int i = 0; i < 5000; i++) {
//            Map map=new HashMap();
//            map.put("mesh_id", RandomNumberGenerator.generateNumber2());
//            list.add(map);
//        }
        sqlSessionTemplate.insert("com.tpadsz.after.dao.BltConsoleDao.restScene",map);

//        System.out.println("xMemcachedClient=" + ctx.getBean("xMemcachedClient"));
    }

    @Test
    public void testAccount() {
        String str = "{\"mobile\":\"18170756879\",\"uname\":\"admin\",\"uid\":\"231231sddfw23\"}";
        Map map = JSON.parseObject(str);
        SqlSession session = getSession();
        session.getMapper(AccountDao.class).updateAccount(map);
        System.out.println(map.size());
    }

    @Test
    public void testMsg() {
        HttpUtils.sendSms("13", "18550791817", "123456");
    }

    @Test
    public void testSend() throws Exception {
        ValidationService validationService = (ValidationService) ctx.getBean("validationService");
        String code = validationService.sendCode("13", "18170756879");
        validationService.checkCode(code, "18170756879");
    }

    @Test
    public void testArray() {
        String[] arys = {"0", "4", "5"};
        List list = new ArrayList();
        list.add("1");
        list.add("2");
        list.add("3");
        JSONObject object = new JSONObject();
        object.put("gids", arys);
        object.put("sids", list);
        System.out.println("result=" + object);
    }
}
