package com.zenquery.interceptor;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by willy on 21.04.14.
 */
public class ForceHerokuSSL extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest servletRequest, HttpServletResponse servletResponse, Object handler) throws Exception {
        String proto = servletRequest.getHeader("x-forwarded-proto");
        return proto == null || "https".equalsIgnoreCase(proto);
    }
}
