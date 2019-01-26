package com.ho.log;

import com.ho.log.annotation.LogAfter;
import com.ho.log.annotation.LogAround;
import com.ho.log.annotation.LogBefore;
import com.ho.log.annotation.LogParam;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Aspect
public class LogAspect implements ApplicationContextAware {
    private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);

    private ApplicationContext applicationContext;

    @Before("@annotation(com.ho.log.annotation.LogBefore)")
    public void logBefore(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        LogBefore annotation = method.getAnnotation(LogBefore.class);
        String value = annotation.value();
        LogRef type = annotation.type();
        LogLevel level = annotation.level();
        long action = annotation.action();
        value = this.replaceParameter(value, joinPoint.getArgs(), method);
        this.logOutPut(value, type, level, action);
    }

    @After("@annotation(com.ho.log.annotation.LogAfter)")
    public void logAfter(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        LogAfter annotation = method.getAnnotation(LogAfter.class);
        String value = annotation.value();
        LogRef type = annotation.type();
        LogLevel level = annotation.level();
        long action = annotation.action();
        value = this.replaceParameter(value, joinPoint.getArgs(), method);
        this.logOutPut(value, type, level, action);
    }

    @Around("@annotation(com.ho.log.annotation.LogAround)")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        LogAround annotation = method.getAnnotation(LogAround.class);
        String value = annotation.value();
        LogRef type = annotation.type();
        LogLevel level = annotation.level();
        long action = annotation.action();

        long startTime = System.currentTimeMillis();
        Object object = joinPoint.proceed();
        long time = (System.currentTimeMillis() - startTime)/1000;

        value = this.replaceParameter(value, joinPoint.getArgs(), method);
        this.logOutPut(value, type, level, action, time, true);

        return object;
    }

    private void logOutPut(String value, LogRef type, LogLevel level, long action) {
        this.logOutPut(value, type, level, action, -1, false);
    }

    private void logOutPut(String value, LogRef type, LogLevel level, long action, long time, boolean flag) {
        if(type == LogRef.CONSOLE || type == LogRef.FILE) {
            if(flag) {
                value = value + ">>执行时间: " + time + "s";
            }

            if(logger.isTraceEnabled() && level.isLevel() >= LogLevel.TRACE.isLevel()) {
                logger.trace(value);
            } else if(logger.isDebugEnabled() && level.isLevel() >= LogLevel.DEBUG.isLevel()) {
                logger.debug(value);
            } else if(logger.isInfoEnabled() && level.isLevel() >= LogLevel.INFO.isLevel()) {
                logger.info(value);
            } else if(logger.isWarnEnabled() && level.isLevel() >= LogLevel.WARN.isLevel()) {
                logger.warn(value);
            } else if(logger.isErrorEnabled() && level.isLevel() >= LogLevel.ERROR.isLevel()) {
                logger.error(value);
            }
        } else if(type == LogRef.DB) {
            Log log = new Log();
            log.setLogBody(value);
            log.setAction(action);
            log.setLogLevel(level.isLevel());
            log.setExecuteTime(time);

            if(this.applicationContext.containsBean("logBean")) {
                Object bean = this.applicationContext.getBean("logBean");
                if(bean instanceof DBLogger) {
                    ((DBLogger)bean).save(log);
                }
            } else {
                logger.info("不存在[logBean]...");
            }
        }
    }


    private String replaceParameter(String value, Object[] args, Method method) {
        Map<String, Object> map = new HashMap<String, Object>();

        if(args != null && args.length > 0) {
            for(Object obj : args) {
                if(obj instanceof Map) {
                    map.putAll((Map)obj);
                } else {
                    Annotation[][] parameterAnnotations = method.getParameterAnnotations();
                    for(int i=0; i<parameterAnnotations.length; i++) {
                        Annotation[] annotations = parameterAnnotations[i];
                        for(Annotation ann : annotations) {
                            if(ann instanceof LogParam) {
                                LogParam annotation2 = (LogParam)ann;
                                map.put(annotation2.value(), args[i]);
                            }
                        }
                    }
                }
            }
        }

        return this.parse(map, value);
    }

    public String parse(Map<String, Object> map, String value) {
        Matcher matcher = Pattern.compile("\\[(\\w+)\\]", 2).matcher(value);

        while(matcher.find()) {
            String group = matcher.group(1);
            if (map.containsKey(group)) {
                value = value.replace("[" + group + "]", map.get(group).toString());
            }
        }

        return value;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}



















