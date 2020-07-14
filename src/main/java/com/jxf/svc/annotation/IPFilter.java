package com.jxf.svc.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;

@Documented
@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface IPFilter {

    /**
     * 访问ip白名单
     * @return
     */
    String[] allow() default {};
    
    /**
     * 访问ip黑名单
     * @return
     */
    String[] deny() default {};
}
