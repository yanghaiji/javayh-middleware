package com.javayh.ip;

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName javayh-rabbitmq → com.javayh → IpUtil
 * @Description IP
 * @Author Dylan
 * @Date 2019/9/12 17:31
 * @Version
 */
public class IpUtil {

    /**
     * @Description 获取客户端真实ip地址
     * @author Dylan
     * @date 2019/9/12
     * @param request 
     * @return java.lang.String
     */
    public static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        return ip;
    }
    
}
