package concurrent.demo;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

public class SemasphoreDemo {

	public static void main(String[] args) {

		// 资源被同时访问的任务数为5
		Semaphore semaphore = new Semaphore(5);
		ExecutorService exec = Executors.newCachedThreadPool();
		// 模拟10个客户端访问
		for (int index = 1; index < 11; index++) {
			final int num = index;
			exec.execute(new Runnable() {

				@Override
				public void run() {
					try {
						if (semaphore.availablePermits() == 0) {
							System.out.println("许可证一用完 o o ,线程" + num + "暂时进入阻塞状态");
						}
						// 先获取许可
						semaphore.acquire();
						System.out.println("线程-" + num + "获取许可");

						// 模拟执行任务耗时
						for (int i = 0; i < 10000; i++)
							;

						// 任务完成归还许可
						semaphore.release();
						System.out.println("线程-" + num + "完成任务，释放许可");
						System.out.println("可用的许可数量为：" + semaphore.availablePermits());
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
		}
	}
}
