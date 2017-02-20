package concurrent.demo;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ProducerConsumerDemo2 {

	public static void main(String[] args) {
		StackBasket s = new StackBasket(100);
		new Thread(new Producer(s), "===> pro-1").start();
		new Thread(new Producer(s), "===> pro-2").start();
		new Thread(new Consumer(s), "+++> con-1").start();
		new Thread(new Consumer(s), "+++> con-2").start();
		System.out.println("main Thread over!!!");
	}
}

class Product { // 产品
	private int id;// 产品编号

	public Product() {
		super();
	}

	public Product(int id) {
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

class StackBasket {

	Lock lock = new ReentrantLock();
	Condition proCondition = lock.newCondition();
	Condition conCondition = lock.newCondition();

	Product[] products = null;
	int len;

	public StackBasket(int size) {
		if (size < 1) {
			size = 10;
		}
		System.out.println("StackBasket init size= " + size);
		products = new Product[size];
	}

	public StackBasket() {
		this(0);
	}

	public Product pop() throws InterruptedException { // 消费
		lock.lock();
		// 消息的打印 和 len值的改变
		try {
			// 当篮子里没有 产品时，所有进来的消费者线程都要等待，且释放cpu的执行权
			while (0 == len) {
				System.out.println("======仓库是空的====，无法消费: " + Thread.currentThread().getName() + " 等待");
				proCondition.await();
			}
			System.out.println(Thread.currentThread().getName() + " 消费了一个产品(id= " + products[len - 1].getId()
					+ "), 仓库里面还有 " + (len - 1) + " 个产品");
			// 此时len递减，但在len递减之后，products数组仍然持有len位置上Product实例的引用，
			// 且在程序运行结束之前可能不会被释放，则可能导致内存泄漏，为了防止内存泄漏，出栈后将对应栈的位置元素置null
			Product p = products[--len];
			products[len] = null;
			// 只唤醒 通过conCongdition.await()冻结的线程，不会唤醒生产线程
			conCondition.signalAll();
			return p;
		} finally {
			lock.unlock();
		}
	}

	public void push(Product product) throws InterruptedException {// 生产
		lock.lock();
		try {
			while (len == products.length) {
				System.out.println("++++++++仓库已经满了++++++，无法继续生产: " + Thread.currentThread().getName() + " 等待");
				conCondition.await();
			}
			System.out.println(Thread.currentThread().getName() + "生产了一个产品（id= " + product.getId() + ",  仓库里面还有 "
					+ (len + 1) + " 个产品");
			products[len++] = product;
			// 一旦生产一个产品，就唤醒所有的消费者
			proCondition.signalAll();
		} finally {
			lock.unlock();
		}
	}
}

class Producer implements Runnable {

	StackBasket stackBasket = null;

	public Producer() {
		super();
	}

	public Producer(StackBasket stackBasket) {
		super();
		this.stackBasket = stackBasket;
	}

	@Override
	public void run() {
		// 生产100个产品
		Product product = null;
		for (int index = 1; index < 50; index++) {
			product = new Product(index);
			try {
				stackBasket.push(product);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}

class Consumer implements Runnable {

	StackBasket stackBasket = null;

	public Consumer() {
		super();
	}

	public Consumer(StackBasket stackBasket) {
		super();
		this.stackBasket = stackBasket;
	}

	@Override
	public void run() {

		for (int index = 1; index < 100; index++) {
			try {
				stackBasket.pop();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
