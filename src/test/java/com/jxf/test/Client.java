package com.jxf.test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;


public class Client {
	public static void main(String[] args) throws Throwable{  
		
		Subject rs=new RealSubject();//这里指定被代理类  
		DynamicSubject ds=new DynamicSubject(rs);  
        Class<?> cls=rs.getClass();
		
        Subject subject=(Subject) Proxy.newProxyInstance(  
        cls.getClassLoader(),cls.getInterfaces(), ds);
		
//        System.out.println(subject instanceof Proxy);
//		
//        System.out.println("subject的Class类是："+subject.getClass().toString());
//        
//        System.out.print("subject中的属性有：");
//        
//        Field[] field = subject.getClass().getDeclaredFields();
//        
//        for(Field f: field){
//        	System.out.print(f.getName()+", ");
//        }
//		
//        System.out.print("\n"+"subject中的方法有：");
//		
//        Method[] method = subject.getClass().getDeclaredMethods();
//		
//        for(Method m:method){ 
//        	System.out.print(m.getName()+", ");
//        }
//        System.out.println("\n"+"subject的父类是："+subject.getClass().getSuperclass());
//        System.out.print("\n"+"subject实现的接口是：");
//        Class<?>[] interfaces=subject.getClass().getInterfaces();
//        for(Class<?> i:interfaces){
//        	System.out.print(i.getName()+", ");
//        }
//        System.out.println("\n\n"+"运行结果为：");
        subject.app2();
		
	}
}
