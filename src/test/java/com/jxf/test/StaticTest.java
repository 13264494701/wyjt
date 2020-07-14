package com.jxf.test;

public class StaticTest {
	 public static int k = 0;  
	 public static int i = print("i");  
	    public static StaticTest t1 = new StaticTest("t1");  
	    public static StaticTest t2 = new StaticTest("t2");  
	    public static int n = 99;  
	    public int j = print("j");  
	       
	    {  
	        print("构造块");  
	    }  
	       
	    static{  
	        print("静态块");  
	    }  
	       
	    public StaticTest(String str) {  
	        System.out.println((++k) + ":" + str + " i=" + i + " n=" + n);  
	        ++n;  
	        ++i;  
	    }  
	       
	    public static int print(String str) {  
	        System.out.println((++k) + ":" + str + " i=" + i + " n=" + n);  
	        ++i;  
	        return ++n;  
	    }  
	    public static void main(String[] args) {  
	        StaticTest t = new StaticTest("init");  
	    }  
}
