package com.tpadsz.after.config;


import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.Random;

/**
 * Created by after on 2018/7/31.
 */

/**
 * WebSocket拦截器
 *
 * @author WANG
 */

public class SpringWebSocketHandlerInterceptor extends HttpSessionHandshakeInterceptor {

    private static Logger logger = Logger.getLogger(SpringWebSocketHandlerInterceptor.class);

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        // TODO Auto-generated method stub
        if (request instanceof ServletServerHttpRequest) {
            ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
            HttpSession session = servletRequest.getServletRequest().getSession(false);
            if (session != null) {
                //使用userName区分WebSocketHandler，以便定向发送消息
                String userName = (String) session.getAttribute("USERNAME");
                if (StringUtils.isEmpty(userName)) {
                    userName = "default-system" + new Random().nextInt(10);
                }
                attributes.put("USERNAME", userName);
            }
        }
        return super.beforeHandshake(request, response, wsHandler, attributes);
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception ex) {
        // TODO Auto-generated method stub
        super.afterHandshake(request, response, wsHandler, ex);
    }
}
