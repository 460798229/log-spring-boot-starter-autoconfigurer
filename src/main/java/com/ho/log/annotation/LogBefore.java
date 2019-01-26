package com.ho.log.annotation;

import com.ho.log.LogLevel;
import com.ho.log.LogRef;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogBefore {
    String value() default "";

    LogRef type() default LogRef.CONSOLE;

    LogLevel level() default LogLevel.INFO;

    long action() default 0;
}
