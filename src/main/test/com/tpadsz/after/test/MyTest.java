package com.tpadsz.after.test;


import com.tpadsz.after.service.ValidationService;
import com.tpadsz.after.util.HttpUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by hongjian.chen on 2017/10/24.
 */
public class MyTest {
    private static ClassPathXmlApplicationContext ctx;

    static {
        ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
    }


    public static SqlSession getSession() {
        SqlSessionFactory factory = (SqlSessionFactory) ctx.getBean("sqlSessionFactory");
        return factory.openSession();
    }

    public static SqlSessionTemplate getSqlSessionTemplate() {
        return (SqlSessionTemplate) ctx.getBean("sqlSessionTemplate");
    }


    public static void main(String[] args) {
        HttpUtils.sendSms("1", "18170756879", "123456");
//        System.out.println("template=" + getSqlSessionTemplate());
//        System.out.println("xMemcachedClient=" + ctx.getBean("xMemcachedClient"));
    }

    @Test
    public void testSend() throws Exception {
        ValidationService validationService = (ValidationService) ctx.getBean("validationService");
        String code = validationService.sendCode("12", "18170756879");
        validationService.checkCode(code, "18170756879");
    }
}
