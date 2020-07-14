package com.jxf.task.utils;

import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * 自定义SpringBeanJobFactory，用于对Job注入ApplicationContext等。
 * 
 * @author arganzheng
 */
public class SpringBeanJobFactory extends org.springframework.scheduling.quartz.SpringBeanJobFactory implements ApplicationContextAware {
    
	private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * 这里我们覆盖了super的createJobInstance方法，对其创建出来的类再进行autowire。
     */
    @Override
    protected Object createJobInstance(TriggerFiredBundle bundle) throws Exception {
        Object jobInstance = super.createJobInstance(bundle);
        applicationContext.getAutowireCapableBeanFactory().autowireBean(jobInstance);
        return jobInstance;
    }
}