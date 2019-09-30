package com.after;

import com.tpadsz.after.config.SpringConfig;
import com.tpadsz.after.entity.PlaceExtend;
import com.tpadsz.after.service.PlaceService;
import net.rubyeye.xmemcached.XMemcachedClient;
import net.rubyeye.xmemcached.exception.MemcachedException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by hongjian.chen on 2018/11/26.
 */

@ContextConfiguration(classes = {SpringConfig.class})
@RunWith(SpringJUnit4ClassRunner.class)
public class ConfigurationTest {

    @Autowired
    private XMemcachedClient xMemcachedClient;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private PlaceService placeService;

    @Test
    public void testXmemcached() throws InterruptedException, MemcachedException, TimeoutException {
        // set: 第一个参数是key，第二个参数是超时时间，第三个参数是value
        xMemcachedClient.set("uic_cache_message_verify_123456",0, "12345");//添加或者更新
        xMemcachedClient.set("first", 3, "tianjin");//添加或者更新
        xMemcachedClient.set("second", 2, "chengdu");//添加,key不存在添加成功返回true,否则返回false
        xMemcachedClient.replace("first", 3, "Beijing");//替换,key已经存在替换成功返回true,不存在返回false
        System.out.println("code=======================" + xMemcachedClient.get("uic_cache_message_verify_123456"));
        System.out.println("first=======================" + xMemcachedClient.get("first"));
        System.out.println("second======================" + xMemcachedClient.get("second"));
        System.out.println("--------------------------------------------------------------");

        Thread.sleep(5000);
        System.out.println("first========================" + xMemcachedClient.get("first"));
        System.out.println("second=======================" + xMemcachedClient.get("second"));
        System.out.println("demo=======================" + xMemcachedClient.get("test"));
    }

    @Test
    public void set() {
//		  redisTemplate.opsForValue().set("mykey","test is ok");
//        redisTemplate.opsForValue().set("mykey", "LogTest is ok!", 10, TimeUnit.SECONDS);
        System.out.println(redisTemplate.opsForValue().get(formatKey("54298700349")));

//            new Thread().sleep(10000);
        System.out.println(redisTemplate.opsForValue().get(formatKey("54300050120")));

    }

    public String formatKey(String adzone_id) {
        return String.format("pid_%s", adzone_id);
    }

    @Test
    public void testSetKey() throws InterruptedException {
        String adzone_id = "54300950058";
        String uid = "3bc9f45ab42e453f93ee8a966b5a9725";

        String key = formatKey(adzone_id);
        ValueOperations<String, Object> operations = redisTemplate.opsForValue();
        operations.set(key, uid, 3, TimeUnit.SECONDS);
        System.out.println(redisTemplate.hasKey(key) + "\t" + operations.get(key));
        System.out.println("-----------------分隔线-----------------");
        Thread.sleep(3000);
    }

    @Test
    public void moveGroupTest(){
        List<PlaceExtend> placesAndGroups = placeService.getPlacesAndGroups(199);
    }
}
