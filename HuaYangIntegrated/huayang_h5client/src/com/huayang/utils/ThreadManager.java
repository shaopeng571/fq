package com.huayang.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;


/**
 * 启动工作线程
 */
public class ThreadManager {

	private static volatile ThreadManager instance;
	private ExecutorService mThreadPool;
	private LinkedBlockingDeque<Object> linkedBlockingDeque;

	private  ThreadManager() {
		mThreadPool = Executors.newCachedThreadPool();
		linkedBlockingDeque = new LinkedBlockingDeque<Object>();
		start();
	}

	private void start() {
		
	}

	public static ThreadManager getInstance() {
		if (instance == null) {
			synchronized (ThreadManager.class) {
				if (instance == null) {
					instance = new ThreadManager();
				}
			}
		}
		return instance;
	}

	public void execute(Runnable task) {
		if (task == null) {
			return;
		}
		mThreadPool.execute(task);
	}
	public void submit(Runnable task) {
		if(task == null) {
			return;
		}
		mThreadPool.submit(task);
	}
	
	
	
	
	
	

}
