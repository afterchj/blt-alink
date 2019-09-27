package com.tpadsz.after.config;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @program: central-console
 * @description:
 * @author: Mr.Ma
 * @create: 2019-05-21 10:44
 **/
@Component
@ServerEndpoint(value = "/ws/webSocket")
public class WebSocket {
    private static Logger logger = LoggerFactory.getLogger(WebSocket.class);
    //J.U.C包下线程安全的类，主要用来存放每个客户端对应的webSocket连接
    private static CopyOnWriteArraySet<Session> copyOnWriteArraySet = new CopyOnWriteArraySet();

    /**
     * @Name：onOpen
     * @Description：打开连接。进入页面后会自动发请求到此进行连接
     * @Author：mYunYu
     * @Create Date：14:46 2018/11/15
     * @Parameters：
     * @Return：
     */
    @OnOpen
    public void onOpen(Session session) {
        copyOnWriteArraySet.add(session);
    }

    /**
     * @Name：onClose
     * @Description：用户关闭页面，即关闭连接
     * @Author：mYunYu
     * @Create Date：14:46 2018/11/15
     * @Parameters：
     * @Return：
     */
    @OnClose
    public void onClose(Session session) {
        copyOnWriteArraySet.remove(session);
    }

    /**
     * @Name：onMessage
     * @Description：测试客户端发送消息，测试是否联通
     * @Author：mYunYu
     * @Create Date：14:46 2018/11/15
     * @Parameters：
     * @Return：
     */
    @OnMessage
    public void onMessage(String message) {
        logger.warn("发来的消息:" + message);
    }

    /**
     * @Name：onError
     * @Description：出现错误
     * @Author：mYunYu
     * @Create Date：14:46 2018/11/15
     * @Parameters：
     * @Return：
     */
    @OnError
    public void onError(Session session, Throwable error) {
        logger.warn("session ERROR [{}] session ID [{}]", error.getMessage(), session.getId());
    }

    /**
     * @Name：sendMessage
     * @Description：用于发送给客户端消息（群发）
     * @Author：mYunYu
     * @Create Date：14:46 2018/11/15
     * @Parameters：
     * @Return：
     */
    public static void sendMessage(Map map) {
        String message = JSON.toJSONString(map);
        //遍历客户端
        for (Session session : copyOnWriteArraySet) {
//             logger.warn("websocket广播消息：" + message);
            try {
                //服务器主动推送
                synchronized (session) {
                    session.getBasicRemote().sendText(message);
                }
            } catch (Exception e) {
                logger.error("params [{}] error [{}]", message, e.getMessage());
            }
        }
    }

    /**
     * 如果使用springboot内置tomcat，需要配置，否则不需要
     *
     * @return
     */
//    @Bean
//    public ServerEndpointExporter serverEndpointExporter() {
//        return new ServerEndpointExporter();
//    }

}