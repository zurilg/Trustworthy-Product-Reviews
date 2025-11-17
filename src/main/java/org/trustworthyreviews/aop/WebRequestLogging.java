package org.trustworthyreviews.aop;

import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Logs all incoming requests to any controllers inside the .web directory in the format below
 * Incoming Request: GET /api/user/login from 0:0:0:0:0:0:0:1
 */
public class WebRequestLogging {

    @Aspect
    @Component
    public static class RequestLoggingAspect {

        private static final Logger logger = LoggerFactory.getLogger(RequestLoggingAspect.class);

        @Before("execution(* org.trustworthyreviews.web.*.*(..))")
        public void logRequest() {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

            logger.info("Incoming Request: {} {} from {}", request.getMethod(), request.getRequestURI(), request.getRemoteAddr());
        }
    }
}
