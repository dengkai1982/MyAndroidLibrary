package com.sourceforge.apklib.foundation.thread;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
/**
 * 线程执行器,在Activity的onCreate中进行初始化(必须,否则无法持有主线程的Handler)
 * 在Activity的onDestory终止所有线程的执行
 */
public abstract class ThreadExecutor<Params, Progress, Result> {
	/**
	 * 任务结束消息
	 */
	private static final int MESSAGE_POST_RESULT = 0x1;
	/**
	 * 任务执行消息
	 */
	private static final int MESSAGE_POST_PROGRESS = 0x2;
	/**
	 * CPU核心数量
	 */
	private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
	/**
	 * 线程池最小持有数量
	 */
	private static final int CORE_POOL_SIZE = CPU_COUNT + 1;
	/**
	 * 线程池最多持有数量
	 */
	private static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;
	/**
	 * 线程终结等待时长
	 */
	private static final int KEEP_ALIVE = 1;
	/**
	 * 线程池工厂,标准写法不纠结
	 */
	private static final ThreadFactory threadFactory = new ThreadFactory() {
		private final AtomicLong mCount = new AtomicLong(1);

		public Thread newThread(Runnable r) {
			return new Thread(r, "ThreadExecutor#" + mCount.getAndIncrement());
		}
	};
	public static final TerminateableThreadPoolExecutor executor = new TerminateableThreadPoolExecutor(CORE_POOL_SIZE,
			MAXIMUM_POOL_SIZE, KEEP_ALIVE, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(), threadFactory);
	/**
	 * android主线程
	 */
	private static Handler handler = new Handler(Looper.getMainLooper()) {
		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {
			AsyncTaskResult<?> result = (AsyncTaskResult<?>) msg.obj;
			// 主线程中处理
			switch (msg.what) {
			case MESSAGE_POST_RESULT:
				result.mTask.onPostExecute(result.mData);
				break;
			case MESSAGE_POST_PROGRESS:
				result.mTask.onProgressUpdate(result.mData);
                break;
			}
		};
	};

	/**
	 * 此处为主线程调用方法,在doInBackground中调用setProcess，会在此方法中进行处理
	 * @param result
     */
	protected  void onProgressUpdate(Progress result){
		
	};
	public void executThread(final Params params) {
		executor.execute(new Runnable() {
			@Override
			public void run() {
				try{
					Result result = doInBackground(params);
					Message message = handler.obtainMessage(MESSAGE_POST_RESULT,
							new AsyncTaskResult<Result>(ThreadExecutor.this, result));
					message.sendToTarget();
				}catch(InterruptedException e){
					interrupted(params);
				}
			}
		});
	}
	/**
	 * 线程被中断后可执行的任务
	 * @param params
	 */
	protected void interrupted(Params params){
		
	}
	/**
	 * 后台任务执行完毕,更新主线程
	 * @param result
	 */
	protected abstract void onPostExecute(final Result result);
	/**
	 * 执行时更新主线程,在doInBackground中调用此方法更新主页面
	 * @param progress
	 */
	public void setProcess(Progress progress){
		handler.obtainMessage(MESSAGE_POST_PROGRESS,
                new AsyncTaskResult<Progress>(this, progress)).sendToTarget();
	}

	/**
	 * 后台执行耗时的代码
	 * 
	 * @param params
	 * @return
	 */
	protected abstract Result doInBackground(final Params params)throws InterruptedException;

	/**
	 * 停止尚未执行的线程
	 */
	public void stopRun() {
		executor.stopUnRunning();
	}

	/**
	 * 终结线程的方法,貌似都应该选择一个合适的地方优雅的调用
	 */
	public void destory() {
		executor.shutdownNow();
	}


	private static class AsyncTaskResult<Result> {
		final ThreadExecutor mTask;
		final Result mData;
		AsyncTaskResult(ThreadExecutor task, Result data) {
			mTask = task;
			mData = data;
		}
	}
	public boolean isTerminated(){
		return executor.isTerminated();
	}
	public boolean isShutdown(){
		return executor.isShutdown();
	}
}
