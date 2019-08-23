package com.tpadsz.after.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * @author hongjian.chen
 * @date 2019/8/13 17:15
 */
public class FileReadUtils {

    private static Logger logger = LoggerFactory.getLogger(FileReadUtils.class);

//    public static String parseTxtFile(String filePath) throws IOException {
//        StringBuilder txtValue = new StringBuilder();
//        InputStreamReader read = null;
//        try {
//            read = new InputStreamReader(new FileInputStream(filePath), "gbk");// 考虑到编码格式
//            BufferedReader bufferedReader = new BufferedReader(read);
//            String lineTxt;
//            while ((lineTxt = bufferedReader.readLine()) != null) {
//                txtValue.append(lineTxt);
//            }
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            read.close();
//        }
//        return txtValue.toString();
//    }

    public static String parseTxtFile(File file){
        StringBuilder txtValue = new StringBuilder();
        InputStreamReader read = null;
        try {
            read = new InputStreamReader(new FileInputStream(file), "gbk");// 考虑到编码格式
            BufferedReader bufferedReader = new BufferedReader(read);
            String lineTxt;
            while ((lineTxt = bufferedReader.readLine()) != null) {
                txtValue.append(lineTxt);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                read.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return txtValue.toString();
    }

    public static String pareUrlTxt(String uri) throws IOException {
        StringBuilder txtValue = new StringBuilder();
        BufferedReader br = null;
        try {
            URL url = new URL(uri);
            HttpURLConnection urlCon = (HttpURLConnection) url.openConnection();
            urlCon.setConnectTimeout(5000);
            urlCon.setReadTimeout(5000);
            br = new BufferedReader(new InputStreamReader(urlCon.getInputStream(), "gbk"));
            String lineTxt;
            while ((lineTxt = br.readLine()) != null) {
                txtValue.append(lineTxt);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            br.close();
        }
        return txtValue.toString();
    }

    public static void main(String[] args) throws IOException {
        String text = parseTxtFile(new File("C:/file/37853561.txt"));
        JSONObject jsonObject=JSONObject.parseObject(text);

        JSONObject mesh=jsonObject.getJSONObject("Project_Mesh");
        Map head=jsonObject.getJSONObject("Head");
        head.put("Mesh_ID",mesh.getString("Mesh_ID"));
        head.put("Mesh_Name",mesh.getString("Mesh_Name"));
//        String urlTxtInfo = pareUrlTxt("http://uichange.com/file/ota/37853561.txt");
        logger.warn("text:" + JSON.toJSONString(head));
    }
}
