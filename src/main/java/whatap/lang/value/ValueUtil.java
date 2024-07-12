package whatap.lang.value;

import whatap.org.json.JSONArray;
import whatap.org.json.JSONObject;
import whatap.util.StringEnumer;

public class ValueUtil {
	public static JSONArray toJSON(ListValue list) {
		JSONArray arr = new JSONArray();
		for (int i = 0; i < list.size(); i++) {
			Value value = list.get(i);
			arr.put(valueToJSON(value));
		}
		return arr;
	}

	public static JSONObject toJSON(MapValue map) {
		JSONObject out = new JSONObject();
		StringEnumer en = map.keys();
		while (en.hasMoreElements()) {
			String key = en.nextString();
			Value value = map.get(key);
			out.put(key, valueToJSON(value));
		}
		return out;
	}
	
	public static JSONArray toArrayJSON(MapValue map) {
		JSONArray out = new JSONArray();
		StringEnumer en = map.keys();
		while (en.hasMoreElements()) {
			String key = en.nextString();
			Value value = map.get(key);
			if (value instanceof ListValue) {
				ListValue lv = (ListValue) value;
				for (int i = 0; i < lv.size(); i++) {
					Value v = lv.get(i);
					if(i >= out.length()){
						out.put(i, new JSONObject().put(key, valueToJSON(v)));
					}else{
						JSONObject jo = out.getJSONObject(i);
						jo.put(key, valueToJSON(v));
						out.put(i, jo);
					}
				}
			} else {
				out.put(new JSONObject().put(key, valueToJSON(value)));
			}
		}
		return out;
	}

	private static Object valueToJSON(Value value) {
		if(value instanceof ListValue){
			return toJSON((ListValue) value);
		}else if(value instanceof MapValue){
			return toJSON((MapValue) value);
		}else{
			return JSONObject.wrap(value.toJavaObject());
		}
	}

}
