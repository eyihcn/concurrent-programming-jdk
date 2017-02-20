package concurrent.demo;

import java.util.concurrent.Callable;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

public class CylicBarrierDemo {

	public static void main(String[] args) {
		FutureTask<String> futureTask = new FutureTask<String>(new Callable<String>() {
			@Override
			public String call() throws Exception {
				System.out.println("5个小并发任务已经完成，大任务完成！");
				return null;
			}
		});
		// 一个大任务分为5个并行的小任务
		CyclicBarrier cylic = new CyclicBarrier(5, futureTask);
		ExecutorService exec = Executors.newCachedThreadPool();
		// exec.execute(futureTask);

		// 开启五个线程执行 5个并发任务
		for (int i = 1; i <= 5; i++) {
			final int index = i;
			exec.execute(new FutureTask<String>(new Callable<String>() {
				@Override
				public String call() throws Exception {
					int tindex = index;
					System.out.println("开始任务 " + tindex);
					for (int ii = 0; ii < 10000; ii++)
						;
					System.out.println(tindex + " 任务结束");
					cylic.await();
					return null;
				}
			}));
		}
		exec.shutdown();
	}
}
