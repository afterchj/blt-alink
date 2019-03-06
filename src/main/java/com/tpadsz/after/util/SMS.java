package com.tpadsz.after.util;

/**
 * Created by hongjian.chen on 2018/12/26.
 */
public enum SMS {
    URL("https://api.rtc.huaweicloud.com:10443/sms/batchSendSms/v1"), APPKEY("knh5JajPUZ519f3QYzvrAfUaU033"), APPSECRET("GY23G6q44KFvS3UHNm4M1Buf1ws5");
    String value;

    SMS(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
