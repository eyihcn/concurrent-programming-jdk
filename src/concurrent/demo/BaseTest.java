package concurrent.demo;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

public class BaseTest {

	public static void main(String[] args) {
	}

	@Override
	public int hashCode() {
		System.out.println("hashCode .....");
		return super.hashCode();
	}

	@Test
	public void testEntity() throws InterruptedException {
		long start = System.currentTimeMillis();
		for (int i = 0; i < 10000000; i++) {
			new Entity("name" + i, "name" + i, "name" + i, "name" + i);
		}
		System.out.println(System.currentTimeMillis() - start);
		TimeUnit.SECONDS.sleep(20);
	}

	@Test
	public void testMap() {
		String name = "name";
		String name2 = "name2";
		String name3 = "name3";
		String name4 = "name4";
		long start = System.currentTimeMillis();
		for (int i = 0; i < 10000000; i++) {
			Map<String, Object> map = new HashMap<>();
			map.put(name, "name" + i);
			map.put(name2, "name" + i);
			map.put(name3, "name" + i);
			map.put(name4, "name" + i);
		}
		System.out.println(System.currentTimeMillis() - start);
		try {
			TimeUnit.SECONDS.sleep(20);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
