package com.jxf.test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;


public class DynamicSubject implements InvocationHandler {
	
	 private Object obj;//这是动态代理的好处，被封装的对象是Object类型，接受任意类型的对象 
	 
	 public DynamicSubject() {  
		 
	 }  
	  
	 public DynamicSubject(Object obj) { 
		 
	    this.obj = obj;  
	 }  
	 
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		
		System.out.println("before calling " + method); 
		
	    method.invoke(obj, args);  
	    
		System.out.println("after calling " + method);  
		
		return null;
	}

}
