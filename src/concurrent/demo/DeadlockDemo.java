package concurrent.demo;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 
 * 死锁 概念：两个或多个线程持有对方正在等待的锁
 * 
 * 如何避免死锁？
 * 
 * 避免一个线程同时获取多个锁
 * 
 * 1.避免一个线程在锁内同时占用多个资源，尽量保证每个锁只占用一个资源
 * 
 * 2.尝试使用定时锁
 * 
 * 3.对于数据库锁，加锁和解锁必须在一个数据库连接里
 * 
 * 例子: 线程A 必须同时持有锁1 和 锁2 ，才能完成任务一 。
 * 
 * 线程B 必须同时持有锁1 和 锁2，才能完成任务二
 * 
 * 线程A 持有锁1，等待锁2
 * 
 * 线程B 持有锁2，等待1
 * 
 * @author chenyi
 *
 * @date 2019年3月16日
 */
public class DeadlockDemo {

	public static void main(String[] args) {
		DeadlockDemo deadlockDemo = new DeadlockDemo();
		new Thread(deadlockDemo.new Task1()).start();
		new Thread(deadlockDemo.new Task2()).start();
	}

	Lock lock1 = new ReentrantLock();
	Lock lock2 = new ReentrantLock();

	class Task1 implements Runnable {

		@Override
		public void run() {
			try {
				lock1.lock();
				// 线程A 获得锁1后，睡10秒
				TimeUnit.SECONDS.sleep(10);
				while (true) {
					if (lock2.tryLock()) {
						try {
							System.out.println("manipulate protected state");
						} finally {
							lock2.unlock();
						}
					}else{
						TimeUnit.SECONDS.sleep(1);
						System.out.println("线程A,持有鎖1， 等待 1 秒, 继续尝试获得锁2");
					}
				}

			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
				lock1.unlock();
			}

		}
	}

	class Task2 implements Runnable {

		@Override
		public void run() {
			try {
				lock2.lock();
				// 线程A 获得锁1后，睡10秒
				TimeUnit.SECONDS.sleep(10);
				while (true) {
					if (lock1.tryLock()) {
						try {
							System.out.println("manipulate protected state");
						} finally {
							lock1.unlock();
						}
					} else {
						TimeUnit.SECONDS.sleep(1);
						System.out.println("线程B, 持有鎖2，等待 1 秒, 继续尝试获得锁1");
					}
				}

			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
				lock2.unlock();
			}

		}
	}

}
