package com.tpadsz.after.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * Created by hongjian.chen on 2019/5/30.
 */
public class PropertiesUtil {

    public static String getPath() {
        Properties pro = new Properties();
        try {
            InputStream in = PropertiesUtil.class.getResourceAsStream("/common.properties");
            BufferedReader bf = new BufferedReader(new InputStreamReader(in,"gbk"));
            pro.load(bf);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pro.getProperty("upload", "/mydata/alink/ota");
    }
}
