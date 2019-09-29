package com.tpadsz.after.config;


import org.apache.log4j.Logger;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


/**
 * Created by after on 2018/7/31.
 */

public class SpringWebSocketHandler extends AbstractWebSocketHandler {

    private Logger logger = Logger.getLogger(SpringWebSocketHandler.class);
    //ArrayList会出现性能问题，最好用Map来存储，key用userid
    public static final Map<Object, WebSocketSession> users = new HashMap<>();

    public FileOutputStream output;

    /**
     * 连接成功时候，会触发页面上onopen方法
     */
    public void afterConnectionEstablished(WebSocketSession session) {
        // TODO Auto-generated method stub
        Object o = session.getAttributes().get("USERNAME");
        String user = UUID.randomUUID().toString().replace("-", "");
        if (null != o) {
            user = o.toString();
        }
        users.put(user, session);
        logger.info("current user:" + user + ",online num:" + users.size());
        try {
            session.sendMessage(new TextMessage(user + "：I'm " + user));
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    /**
     * 关闭连接时触发
     */
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) {
//        super.afterConnectionClosed(session, closeStatus);
        Object username = session.getAttributes().get("USERNAME");
        users.remove(username);
        //这块会实现自己业务，比如，当用户登录后，会把离线消息推送给用户
//        TextMessage returnMessage = new TextMessage(username.toString() + "退出聊天室！");
//        session.sendMessage(returnMessage);
    }

    /**
     * js调用websocket.send时候，会调用该方法
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        logger.info("收到消息：" + message.getPayload());
        Object o = session.getAttributes().get("USERNAME");
        String user = UUID.randomUUID().toString().replace("_", "");
        if (null != o) {
            user = o.toString();
        }

        sendMessageToAll(user, message);

//        sendMessageToAll(session.getAttributes().get("USERNAME").toString(), message);
    }

    @Override
    public void handleBinaryMessage(WebSocketSession session, BinaryMessage message) {
//        logger.info("处理BinaryMessage..." + message.getPayload().toString());
        ByteBuffer buffer = message.getPayload();
        try {
            output.write(buffer.array());
        } catch (IOException e) {
        }
    }

    public void handleTransportError(WebSocketSession session, Throwable exception) {
        try {
            super.handleTransportError(session, exception);
        } catch (Exception e) {
            logger.error("webSocket connection closed......ERROR" + e.getMessage());
        }
        users.remove(session);
    }

    public boolean supportsPartialMessages() {
        return true;
    }


    /**
     * 给某个用户发送消息
     *
     * @param user
     * @param message
     */
    public void sendMessageToUser(String user, TextMessage message) {
        for (Map.Entry<Object, WebSocketSession> entry : users.entrySet()) {
            if (entry.getKey().equals(user)) {
                if (entry.getValue().isOpen()) {
                    try {
                        entry.getValue().sendMessage(message);
                    } catch (IOException e) {
                        logger.error("给用户发送消息失败！" + user);
                    }
                }
                break;
            }
        }
    }

    /**
     * 给所有在线用户发送消息
     *
     * @param message
     */
    public void sendMessageToAll(String user, TextMessage message) {
        try {
            for (Map.Entry<Object, WebSocketSession> entry : users.entrySet()) {
                if (entry.getValue().isOpen()) {
                    entry.getValue().sendMessage(message);
//                    if (!entry.getKey().equals(user)) {
//                        entry.getValue().sendMessage(message);
//                    }
                }
            }
        } catch (IOException e) {
            logger.error("给所有在线用户发送消息失败！" + e.getMessage());
        }
    }

}