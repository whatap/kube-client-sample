package whatap.util.cardinality;

import whatap.util.KeyGen;
import whatap.util.SysJMX;

import java.io.IOException;
import java.util.HashSet;
import java.util.Random;
public class TestHLL {
	public static void main(String[] args) throws IOException {
//		for (int i = 5; i < 20; i++) {
//			test2(i);
//		}
		test4();
	}
	private static void test4() throws IOException {
		HyperLogLog h1 = 	new HyperLogLog();
		HyperLogLog h2 = 	new HyperLogLog();
		long intersect = (h1.cardinality()+h2.cardinality()) - (h1.merge(h2).cardinality());
//		h1.offer(10);
//		h1.offer(20);
		h2.offer(10);
		h2.offer(30);
		System.out.println("h1="+h1.cardinality());
		System.out.println("h2="+h2.cardinality());
		System.out.println("h1 U h2="+h1.merge(h2).cardinality());
		System.out.println("합에서 "+intersect);
	}
	private static void test3(int rsd) throws IOException {
		HyperLogLog hll = 	new HyperLogLog(rsd);
		for(int i = 0 ; i <100000 ; i++){
			hll.offer(KeyGen.next());
		}
		
		byte[] b = hll.getBytes();
		hll  = HyperLogLog.build(b);
		System.out.println("cadinality = " + hll.cardinality() + " = " + b.length);
	}
	private static void test2(int rsd) throws IOException {
		// int rsd=9;
		long smem = SysJMX.getCurrentThreadAllocBytes();
		for (int i = 0; i < 10000; i++) {
			new HyperLogLog(rsd);
		}
		long emem = SysJMX.getCurrentThreadAllocBytes();
		System.out.println(rsd +"=>"+(emem - smem) / 10000 + " == " +new HyperLogLog(rsd).getBytes().length);
	}
	private static void test1() {
		int rsd = 10;
		HashSet<Long> realSet = new HashSet<Long>();
		HyperLogLog all = new HyperLogLog(rsd);
		HyperLogLog odd = new HyperLogLog(rsd);
		HyperLogLog even = new HyperLogLog(rsd);
		Random r = new Random();
		for (int i = 1; i <= 10000000; i++) {
			long value = r.nextLong();
			all.offer(value);
			realSet.add(value);
			if (i % 2 == 0)
				even.offer(value);
			else
				odd.offer(value);
			int u = unit(i);
			if (i % u == 0) {
				HyperLogLog sum = new HyperLogLog(rsd);
				sum.addAll(even);
				sum.addAll(odd);
				sum.addAll(all);
				System.out.println(realSet.size() + " => all=" + all.cardinality() + " even=" + even.cardinality()
						+ " odd=" + odd.cardinality() + "  sum=" + sum.cardinality());
			}
		}
	}
	private static int unit(int value) {
		if (value < 10)
			return 10;
		int decVal = 1;
		for (int x = value; x >= 10; x /= 10) {
			decVal *= 10;
		}
		return decVal;
	}
}
