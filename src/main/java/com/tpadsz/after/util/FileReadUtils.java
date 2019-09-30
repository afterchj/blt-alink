package com.tpadsz.after.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

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

    public static String parseTxtFile(File file) {
        StringBuilder txtValue = new StringBuilder();
        Reader reader = null;
        try {
            reader = new InputStreamReader(new FileInputStream(file), getCharset(file));// 考虑到编码格式
            BufferedReader bufferedReader = new BufferedReader(reader);
            String lineTxt;
            while ((lineTxt = bufferedReader.readLine()) != null) {
                txtValue.append(lineTxt);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        }
        return txtValue.toString();
    }

    public static String pareUrlTxt(String uri) {
        StringBuilder txtValue = new StringBuilder();
        BufferedReader br = null;
        try {
            URL url = new URL(uri);
            HttpURLConnection urlCon = (HttpURLConnection) url.openConnection();
            urlCon.setConnectTimeout(5000);
            urlCon.setReadTimeout(5000);
            br = new BufferedReader(new InputStreamReader(urlCon.getInputStream(), "utf-8"));
            String lineTxt;
            while ((lineTxt = br.readLine()) != null) {
                txtValue.append(lineTxt);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            logger.error(e.getMessage());
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        }
        return txtValue.toString();
    }

    private static String getCharset(File file) throws IOException {
        InputStream in = new FileInputStream(file);
        byte[] b = new byte[3];
        in.read(b);
        in.close();
        String code;
        if (b[0] == -17 && b[1] == -69 && b[2] == -65)
            code = "UTF-8";
        else
            code = "GBK";
        return code;
    }

//    public static void main(String[] args) throws IOException {

//        String text = pareUrlTxt("http://iotsztp.com/file/ota/37853561.txt");
//        String text1 = parseTxtFile(new File("c:/file/37853561.txt"));
//        String text2 = parseTxtFile(new File("c:/file/test.txt"));
//        System.out.println("result=" + text1);
//        System.out.println(getCharset(new File("c:/file/test.txt")));
//        System.out.println("result=" + text2);
//        System.out.println(getCharset(new File("c:/file/37853561.txt")));

//        JSONObject jsonObject=JSONObject.parseObject(text);
//
//        JSONObject mesh=jsonObject.getJSONObject("Project_Mesh");
//        Map head=jsonObject.getJSONObject("Head");
//        head.put("Mesh_ID",mesh.getString("Mesh_ID"));
//        head.put("Mesh_Name",mesh.getString("Mesh_Name"));
////        String urlTxtInfo = pareUrlTxt("http://uichange.com/file/ota/37853561.txt");
//        logger.warn("text:" + JSON.toJSONString(head));
//    }
}
