package com.javayh.interceptor;

import com.javayh.constants.ResponseCode;
import com.javayh.ip.IpUtil;
import com.javayh.annotation.AccessLimit;
import com.javayh.exception.ServiceException;
import com.javayh.redis.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.handler.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

import static com.javayh.constants.ResponseCode.ACCESS_LIMIT;

/**
 * @ClassName javayh-rabbitmq → com.javayh.interceptor → AccessLimitInterceptor
 * @Description
 * @Author Dylan
 * @Date 2019/9/12 17:16
 * @Version
 */
@Slf4j
@Configuration
public class AccessLimitInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();

        AccessLimit annotation = method.getAnnotation(AccessLimit.class);
        if (annotation != null) {
            check(annotation, request);
        }
        return true;
    }

    private void check(AccessLimit annotation, HttpServletRequest request) {
        int maxCount = annotation.maxCount();
        int seconds = annotation.seconds();
        log.info("请求次数:{},请求时间:{}",maxCount,seconds);
        StringBuilder sb = new StringBuilder();
        sb.append("ACCESS_LIMIT_PREFIX").append(IpUtil.getIpAddress(request)).append(request.getRequestURI());
        String key = sb.toString();
        Boolean exists = redisUtil.hasKey(key);
        if (!exists) {
            redisUtil.set(key, String.valueOf(1), seconds);
        } else {
            int count = Integer.valueOf((String)redisUtil.get(key));
            if (count < maxCount) {
                Long ttl = redisUtil.getExpire(key);
                if (ttl <= 0) {
                    redisUtil.set(key, String.valueOf(1), seconds);
                } else {
                    redisUtil.set(key, String.valueOf(++count), ttl.intValue());
                }
            } else {
                throw new ServiceException(ACCESS_LIMIT.getMsg());
            }
        }
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
    }
}
