package com.tpadsz.after.util;

import com.tpadsz.after.entity.dd.UrlConstants;
import org.apache.log4j.Logger;
import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;

/**
 * Created by hongjian.chen on 2018/8/1.
 */
public class WSClientUtil {

    private static WebSocketClient client = null;


    private static Logger logger = Logger.getLogger(WSClientUtil.class);

    public static void initClient() {

        try {
            client = new WebSocketClient(new URI(UrlConstants.TEST_URL.getUrl()), new Draft_17()) {
                @Override
                public void onOpen(ServerHandshake arg0) {
                }

                @Override
                public void onMessage(String arg0) {
                    logger.warn("from server：" + arg0);
                }

                @Override
                public void onError(Exception arg0) {
                    logger.error(arg0.getMessage());
                }

                @Override
                public void onClose(int arg0, String arg1, boolean arg2) {
                }

                @Override
                public void onMessage(ByteBuffer bytes) {
                    try {
                        logger.warn(new String(bytes.array(), "utf-8"));
                    } catch (UnsupportedEncodingException e) {
                        logger.error(e.getCause());
                    }
                }
            };
            client.connect();
        } catch (URISyntaxException e) {
            logger.error(e.getMessage());
        }
    }

    public static void sendMsg(String content) {
        initClient();
        while (!client.getReadyState().equals(WebSocket.READYSTATE.OPEN)) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                logger.error(e.getMessage());
            }
        }
        client.send(content);
        closeCliet();
    }

    //主动关闭连接才调用，不然不需要调用这个方法
    public static void closeCliet() {
        if (client != null) {
            client.close();
            client = null;
        }
    }


//    public static void main(String[] args) {
//        JSONObject object = new JSONObject();
//        object.put("from", "admin");
//        object.put("to", "test");
//        object.put("message", "hello test,ok that is fine.");
//        sendMsg(object.toString());
//    }
}
