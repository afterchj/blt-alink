package com.tpadsz.after.util;

import org.springframework.ui.ModelMap;

import javax.servlet.http.HttpServletRequest;

/**
 * @program: blt-alink
 * @description:
 * @author: Mr.Ma
 * @create: 2019-10-23 15:57
 **/
public class UrlUtils {

    private final static String HEAD = "http://";
    private final static String DEFAUL_LOCAL_HOST = "localhost";
    private final static String DEFAUL_127_HOST = "127.0.0.1";

    private static String getUrl(HttpServletRequest request){
        String url;
        String hostName = request.getServerName();
        int port = request.getServerPort();
        String contextPath  = request.getContextPath();
        String servletPath = request.getServletPath();
        if (DEFAUL_LOCAL_HOST.equals(hostName) || DEFAUL_127_HOST.equals(hostName)){
            url = new StringBuffer().append(HEAD).append(hostName).append(":").append(port).append(contextPath).append(servletPath).toString();
        }else {
            url = new StringBuffer().append(HEAD).append(hostName).append(contextPath).append(servletPath).toString();
        }
        return url;
    }

    public static ModelMap getModelUrl(HttpServletRequest request){
        ModelMap modelMap = new ModelMap();
        String url = getUrl(request);
        modelMap.put("url",url);
        return modelMap;
    }
}
