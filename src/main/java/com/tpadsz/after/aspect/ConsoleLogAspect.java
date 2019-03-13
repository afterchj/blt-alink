package com.tpadsz.after.aspect;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tpadsz.after.service.ConsoleLogService;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by hongjian.chen on 2019/3/7.
 * 日志切面
 */
@Aspect
@Component
public class ConsoleLogAspect {

    private static Logger logger = Logger.getLogger(ConsoleLogAspect.class);

    @Autowired
    private ConsoleLogService logService;

    public ConsoleLogAspect() {
        logger.info("aspect start work...");
    }

    //记录操作日志节点
    @Pointcut("execution(* com.tpadsz.after.controller.AlinkConsoleController.console*(..))")
    public void consolePointcut() {
    }

    //操作 后置通知
    @After("consolePointcut()")
    public void saveLog(JoinPoint joinPoint) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        JSONObject map = (JSONObject) request.getSession().getAttribute("param");
        map.put("method", joinPoint.getSignature().getName());
        JSONArray gids = map.getJSONArray("gids");
        if (gids != null) {
            for (Object gid : gids) {
                map.put("gid", gid);
                logService.saveLog(map);
            }
        }else {
            logService.saveLog(map);
        }
    }
}
