package whatap.util.expr;

import java.util.HashMap;
import java.util.Map;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Expr r = new Expr("1 - 44 - 3 - 4");
		Expr r1 = new Expr("'cnt='+min(cpu)");
//		Expr r2 = new Expr("avg(cpu)   ");
//		Expr r3 = new Expr("replace('abcaa','aa','xx') ");

		Map m = new HashMap();
		m.put("a", new Long(10));
		m.put("b", 20);
		//m.put("cpu", 30);
		System.out.println(r.execute(m));
		System.out.println(r1.execute(m));
//		System.out.println(r3.execute(m));

	}

}
