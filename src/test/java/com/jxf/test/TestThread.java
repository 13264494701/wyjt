package com.jxf.test;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class TestThread {

	
	public ReentrantLock lock = new ReentrantLock();
	public Condition condition = lock.newCondition();
	
	public void run() {
		System.err.println(Thread.currentThread().getName()+"线程进来了");
		try {
			lock.lockInterruptibly();
			for (int i = 0; i < 5; i++) {
				System.err.println(i);
			}	
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			lock.unlock();
		}
		
	}
	
	
}
