package whatap.org.json;
import java.util.Iterator;
public class Test {
	public static void main(String[] args) {
		JSONObject jo = new JSONObject();
		jo.put("Paul1", 10);
		jo.put("Paul2", 20);
		jo.put("Paul3", "\"30\"");
		
		String s = jo.toString();
		System.out.println(jo);
		jo = new JSONObject(s);
		Iterator<String> itr = jo.keys();
		while (itr.hasNext()) {
			String key = itr.next();
			String value = jo.optString(key);
			System.out.println(key + "=" + value);
		}
	}
}
