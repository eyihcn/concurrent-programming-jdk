package concurrent.demo;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ProducerConsumerDemo2 {

	public static void main(String[] args) {
		BlockingQueue<Product2> arrayBlockingQueue = new ArrayBlockingQueue<Product2>(100);
		int tobeProduceNum = 100;
		new Thread(new Producer2(arrayBlockingQueue, tobeProduceNum), "===> pro-1").start();
		new Thread(new Producer2(arrayBlockingQueue, tobeProduceNum), "===> pro-2").start();
		new Thread(new Consumer2(arrayBlockingQueue), "+++> con-1").start();
		new Thread(new Consumer2(arrayBlockingQueue), "+++> con-2").start();
		System.out.println("main Thread over!!!");
	}
}

class Product2 { // 产品
	private int id;// 产品编号

	public Product2() {
		super();
	}

	public Product2(int id) {
		super();
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}

class Producer2 implements Runnable {

	BlockingQueue<Product2> blockingQueue = null;
	int tobeProduceNum;// 每个生产者可以生成的数量

	public Producer2() {
		super();
	}

	public Producer2(BlockingQueue<Product2> blockingQueue, int tobeProduceNum) {
		super();
		this.blockingQueue = blockingQueue;
		this.tobeProduceNum = tobeProduceNum;
	}

	@Override
	public void run() {
		Product2 p = null;
		// 生产者不停的生产
		for (int index = 0; index < tobeProduceNum; index++) {
			p = new Product2(index);
			try {
				// 将指定的元素插入到该队列中，如果需要的话，等待空间可用
				blockingQueue.put(p);
				System.out.println(Thread.currentThread().getName() + " 生产了一个产品(id= " + p.getId() + "), 仓库里面还有 "
						+ blockingQueue.size() + " 个产品");
				// 日志打印有问题的，将商品放入队列和打印日志 非原子操作

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("生产者线程一共生产了" + tobeProduceNum + "个商品，生产结束");
	}

}

class Consumer2 implements Runnable {

	BlockingQueue<Product2> blockingQueue = null;

	public Consumer2() {
		super();
	}

	public Consumer2(BlockingQueue<Product2> blockingQueue) {
		super();
		this.blockingQueue = blockingQueue;
	}

	@Override
	public void run() {

		for (;;) {
			try {
				// 检索并移除该队列的头，在必要时等待，直到元素变为可用。
				Product2 p = blockingQueue.take();
				System.out.println(Thread.currentThread().getName() + " 消费了一个产品(id= " + p.getId() + "), 仓库里面还有 "
						+ blockingQueue.size() + " 个产品");
				// 日志打印有问题的，将商品从队列中取出和打印日志 非原子操作
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
