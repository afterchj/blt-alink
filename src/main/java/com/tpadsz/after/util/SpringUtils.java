package com.tpadsz.after.util;

import com.tpadsz.after.config.SpringConfig;
import net.rubyeye.xmemcached.XMemcachedClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Created by hongjian.chen on 2019/3/6.
 */
public class SpringUtils {

    private static ApplicationContext ctx;

    static {
        ctx = new AnnotationConfigApplicationContext(SpringConfig.class);
    }

    public static XMemcachedClient getMemCachedClient() {
        return (XMemcachedClient) ctx.getBean("xMemcachedClient");
    }

}
