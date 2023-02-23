package com.logger.sunil.springboot.loggerdemo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.slf4j.MDC;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ServiceInterceptor extends HandlerInterceptorAdapter {

    private static final Logger logger = LogManager.getLogger(ServiceInterceptor.class);

    @Override

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) throws Exception {
        MDC.clear();
        MDC.put("userId", request.getHeader("UserId"));

        MDC.put("sessionId ", request.getHeader("SessionId"));

        MDC.put("requestId", request.getHeader("RequestId"));

return true;
}
}