package com.tpadsz.after.entity.dd;

/**
 * Created by hongjian.chen on 2018/8/1.
 */
public enum UrlConstants {
    SESSION_USERNAME("USERNAME"),
    WEB_SSM("ws://122.112.229.195/web-ssm/websocket"),
    BLT_LIGHT("ws://ctc-hq.tpadsz.com/blt_light/websocket"),
    UICHANGE_BLT("ws://uichange.com/blt_light/websocket"),
    TEST_URL("ws://127.0.0.1:8080/blt_alink/websocket/pushCode"),
//    TEST_URL("ws://127.0.0.1:8080/web-ssm/websocket"),
    BLT_URL("ws://127.0.0.1:8080//ws/webSocket");

    private String url;

    UrlConstants(String url) {
        this.url = url;
    }

    public String getUrl() {
        return this.url;
    }

//    public static void main(String[] args) {
//        System.out.println(UrlConstants.TEST_URL.value());
//    }

}
