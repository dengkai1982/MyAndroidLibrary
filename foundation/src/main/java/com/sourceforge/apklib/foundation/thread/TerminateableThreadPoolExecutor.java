package com.sourceforge.apklib.foundation.thread;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 可以被终结的线程池
 * 调用stopUnRunning终结所有未执行的线程
 * 在最终退出程序是调用shutdownNow终结所有线程
 * 调用该类的线程在run方法中需要捕获InterruptedException异常,处理线程被终结的情况
 * 一切为了优雅 I fuck
 */
public class TerminateableThreadPoolExecutor extends ThreadPoolExecutor{

	public TerminateableThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
			BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
	}
	/**
	 * 终止未提交的线程,在activity的onStop或onDestory中调用
	 * @return 未执行的线程
	 */
	public ArrayList<Runnable> stopUnRunning(){
		BlockingQueue<Runnable> q = getQueue();
        ArrayList<Runnable> taskList = new ArrayList<Runnable>();
        q.drainTo(taskList);
        if (!q.isEmpty()) {
            for (Runnable r : q.toArray(new Runnable[0])) {
                if (q.remove(r))
                    taskList.add(r);
            }
        }
        return taskList;
	}
	
}
