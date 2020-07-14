package com.jxf.svc.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static java.lang.annotation.ElementType.METHOD;


/***
 * 描述 自定义限制注解,加载controller url 上 
 * @seconds  X秒时间内
 * @maxCount 最大访问次数
 * @author wo
 *
 */
@Retention(RUNTIME)
@Target(METHOD)
public @interface AccessLimit {
	
	/**X秒时间内*/
	int seconds();

	/**最大访问次数*/
	int maxCount();
}
