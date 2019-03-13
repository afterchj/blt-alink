package com.tpadsz.after.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class HttpUtils {

    //无需修改,用于格式化鉴权头域,给"X-WSSE"参数赋值
    private static final String WSSE_HEADER_FORMAT = "UsernameToken Username=\"%s\",PasswordDigest=\"%s\",Nonce=\"%s\",Created=\"%s\"";
    //无需修改,用于格式化鉴权头域,给"Authorization"参数赋值
    private static final String AUTH_HEADER_VALUE = "WSSE realm=\"SDP\",profile=\"UsernameToken\",type=\"Appkey\"";

    private static HttpClient httpclient = HttpClientBuilder.create().build();
    private static Logger logger = LoggerUtils.SYSTEM;

    public static JSONObject sendSms(String appid, String receiver, String code) {

        /**
         * 选填,使用无变量模板时请赋空值 String templateParas = "";
         * 单变量模板示例:模板内容为"您的验证码是${NUM_6}"时,templateParas可填写为"[\"369751\"]"
         * 双变量模板示例:模板内容为"您有${NUM_2}件快递请到${TXT_32}领取"时,templateParas可填写为"[\"3\",\"人民公园正门\"]"
         * 查看更多模板变量规则:常见问题>业务规则>短信模板内容审核标准
         */
        String templateParas = String.format("[\"%s\"]", code); //模板变量
        //请求Body,通用模板,使用如下代码
        String body;
        String result = "";
        switch (appid) {
            case "1":
                body = buildRequestBody(SMSContains.TT_LOCK.getSender(), receiver, SMSContains.TT_LOCK.getTemplate(), templateParas);
                break;
            case "11":
                body = buildRequestBody(SMSContains.XM_LIGHT.getSender(), receiver, SMSContains.XM_LIGHT.getTemplate(), templateParas);
                break;
            case "9":
                body = buildRequestBody(SMSContains.XM_GIFT.getSender(), receiver, SMSContains.XM_GIFT.getTemplate(), templateParas);
                break;
            case "12":
                body = buildRequestBody(SMSContains.XM_LIFE.getSender(), receiver, SMSContains.XM_LIFE.getTemplate(), templateParas);
                break;
            case "13":
                body = buildRequestBody(SMSContains.ALINK_LAIGHT.getSender(), receiver, SMSContains.ALINK_LAIGHT.getTemplate(), templateParas);
                break;
            default:
                body = buildRequestBody(SMSContains.ALINK_LAIGHT.getSender(), receiver, SMSContains.ALINK_LAIGHT.getTemplate(), templateParas);
                break;
        }
//        System.out.println("body=" + body);
        //请求Headers中的X-WSSE参数值
        String wsseHeader = buildHeader(SMS.APPKEY.getValue(), SMS.APPSECRET.getValue());
        //为防止因HTTPS证书认证失败造成API调用失败,需要先忽略证书信任问题
        try {
            CloseableHttpClient client = HttpClients.custom()
                    .setSSLContext(new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
                        @Override
                        public boolean isTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                            return true;
                        }
                    }).build()).setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE).build();
            HttpResponse response = client.execute(RequestBuilder.create("POST")//请求方法POST
                    .setUri(SMS.URL.getValue())
                    .addHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded")
                    .addHeader(HttpHeaders.AUTHORIZATION, AUTH_HEADER_VALUE)
                    .addHeader("X-WSSE", wsseHeader)
                    .setEntity(new StringEntity(body)).build());
            result = EntityUtils.toString(response.getEntity());
//            System.out.println("response=" + result);
        } catch (Exception e) {
            return null;
        }
        return JSONObject.parseObject(result);
    }

    /**
     * 构造请求Body体 for 通用模板
     *
     * @param sender
     * @param receiver
     * @param templateId
     * @param templateParas
     * @return
     */
    public static String buildRequestBody(String sender, String receiver, String templateId, String templateParas) {

        List<NameValuePair> keyValues = new ArrayList<NameValuePair>();

        keyValues.add(new BasicNameValuePair("from", sender));
        keyValues.add(new BasicNameValuePair("to", receiver));
        keyValues.add(new BasicNameValuePair("templateId", templateId));
        keyValues.add(new BasicNameValuePair("templateParas", templateParas));
        //如果JDK版本是1.6,可使用:URLEncodedUtils.format(keyValues, Charset.forName("UTF-8"));
        return URLEncodedUtils.format(keyValues, StandardCharsets.UTF_8);
    }

    /**
     * 构造X-WSSE参数值
     *
     * @param appKey
     * @param appSecret
     * @return
     */
    public static String buildHeader(String appKey, String appSecret) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        String time = sdf.format(new Date());
        String nonce = UUID.randomUUID().toString().replace("-", "");

        byte[] passwordDigest = DigestUtils.sha256(nonce + time + appSecret);
        String hexDigest = Hex.encodeHexString(passwordDigest);
        String passwordDigestBase64Str = Base64.encodeBase64String(hexDigest.getBytes(Charset.forName("utf-8")));
        return String.format(WSSE_HEADER_FORMAT, appKey, passwordDigestBase64Str, nonce, time);
    }
    /**
     * 发送 post请求访问本地应用并根据传递参数不同返回不同结果
     */
//    public String post(String url,String respEncoding) {
//        return post(url,"UTF-8",respEncoding,new ArrayList<NameValuePair>());
//    }
//    public static HttpResponse post(String url) {
//        return post(url, "UTF-8", new ArrayList<NameValuePair>());
//    }

    /**
     * 发送 post请求访问本地应用并根据传递参数不同返回不同结果
     */
    public static HttpResponse post(String url, String reqEncoding, List<NameValuePair> param) {
        HttpResponse response = null;
        // 创建httppost
        HttpPost httppost = new HttpPost(url);
        // 创建参数队列
//        List<NameValuePair> formparams = param;
        UrlEncodedFormEntity uefEntity;
        try {
            uefEntity = new UrlEncodedFormEntity(param, reqEncoding);
            httppost.setEntity(uefEntity);
            response = httpclient.execute(httppost);
//            HttpEntity entity = response.getEntity();
//            if (entity != null) {
//                resStr = EntityUtils.toString(entity,respEncoding);
//            }
        } catch (Exception e) {
            logger.error(e);
        } finally {
            // 关闭连接,释放资源
            // httpclient.getConnectionManager().shutdown();
        }
        return response;
    }

    public static boolean checkResult(HttpEntity entity) throws ParseException, IOException {
        String data = EntityUtils.toString(entity, "gbk");
        System.out.println("result=" + data);
        boolean isSended = false;
        if (StringUtils.isNotBlank(data)) {
            String[] pairs = StringUtils.split(data, "&");
            if (pairs != null && pairs.length != 0) {
                String result = null;
                String descr = null;
                for (String pair : pairs) {
                    if (StringUtils.isNotBlank(pair)) {
                        String[] nameValue = StringUtils.split(pair, "=");
                        if (nameValue != null && nameValue.length == 2) {
                            String name = nameValue[0];
                            String value = nameValue[1];
                            if (StringUtils.equals(name, "result")) {
                                result = value;
                            } else if (StringUtils.equals(name, "description")) {
                                descr = value;
                            }
                        }
                    }
                }
                if (StringUtils.equals(result, "0")) {
                    isSended = true;
                } else {
                    logger.error("Sending Message Error. Code: " + result + ", Because : " + descr);
                }
            }
        }
        return isSended;
    }

    /**
     * 发送 post请求访问本地应用并根据传递参数不同返回不同结果
     */
    public static HttpResponse get(String url, List<NameValuePair> param) {
        HttpResponse response = null;
        try {
            URIBuilder builder = new URIBuilder(url);
            builder.setParameters(param);
            URI uri = builder.build();
            System.out.println(uri);
            HttpGet httpget = new HttpGet(uri);
            response = httpclient.execute(httpget);
        } catch (Exception e) {
            logger.error(e);
        }
        return response;
    }


    /**
     * 发送 get请求
     */
    public String get(String url) {
        //httpclient = new DefaultHttpClient();
        String resStr = "";
        try {
            // 创建httpget.
            HttpGet httpget = new HttpGet(url);
            // 执行get请求.
            HttpResponse response = httpclient.execute(httpget);
            // 获取响应实体
            HttpEntity entity = response.getEntity();
            // 打印响应状态
            System.out.println(response.getStatusLine());
            if (entity != null) {
                // 打印响应内容长度
//                System.out.println("Response content length: "
//                        + entity.getContentLength());
                // 打印响应内容
//                System.out.println("Response content: "
//                        + EntityUtils.toString(entity));
                resStr = EntityUtils.toString(entity);
            }
        } catch (Exception e) {
            logger.error(e);
        } finally {
            // 关闭连接,释放资源
            //httpclient.getConnectionManager().shutdown();
        }
        return resStr;
    }
}