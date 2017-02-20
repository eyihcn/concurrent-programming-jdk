package concurrent.demo;


import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/*
 * 例子:一个为基于CountDownLatch 的模拟项目，一个项目可以分为多个模块，只有但这些模块都完成后才可以继续下一步的工作。
 */
public class CountDownLatchDemo {
 
	public static void main(String[] args) {
		int size = 20; // 模块个数
		CountDownLatch latch = new CountDownLatch(size);
		ExecutorService executors = Executors.newCachedThreadPool();
		Random r = new Random();
		Controller exec = new Controller(latch);
		executors.execute(exec); // 控制者先起来，将等待所有的模块完成
		for (int i=0; i<size; i++) {
			executors.execute(new Module(latch, i+"", r.nextInt(2000)));// 开启模块
		}
		executors.shutdown();
	}
}

class Controller implements Runnable {
	
	private CountDownLatch latch;
	
	public Controller(CountDownLatch latch) {
		this.latch = latch;
	}

	@Override
	public void run() {
		try {
			latch.await(); // 控制者等待所以的模块完成以后结束项目
			System.out.println("所有模块已经完成，项目结束。。。。。");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

/**
 * 模块
 * @author lenovo
 *
 */
class Module implements Runnable {
	
	private CountDownLatch latch;
	private String moduleName;
	private int time ;
	
	
	public Module(CountDownLatch latch, String moduleName, int time) {
		super();
		this.latch = latch;
		this.moduleName = moduleName;
		this.time = time;
	}

	@Override
	public void run() {
		try {
			doWork();
			latch.countDown();// 每完成一个模块计数器就减去 1
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void doWork() throws InterruptedException {
		TimeUnit.MILLISECONDS.sleep(time);
		System.out.println(moduleName+" 模块完成 ，耗时： "+time);
	}
}