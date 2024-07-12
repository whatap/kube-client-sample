package whatap.lang.conf;

import whatap.lang.value.MapValue;
import whatap.util.FileUtil;
import whatap.util.SortUtil;
import whatap.util.StringUtil;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Properties;

public class ConfigMake {
	public static Properties reset(File file, List<String> keys, List<String> values, List<String> rmKeys) {
		Properties prop = read(file);
		boolean dirty = false;
		if (keys != null && values != null && keys.size() == values.size()) {
			for (int i = 0; i < keys.size(); i++) {
				String key = keys.get(i);
				String value = values.get(i);
				String oldValue = StringUtil.trim(prop.getProperty(key));
				if (value.equals(oldValue) == false) {
					prop.setProperty(key, value);
					dirty = true;
				}
			}
		}
		if (rmKeys != null) {
			for (String key : rmKeys) {
				if (prop.containsKey(key)) {
					dirty = true;
					prop.remove(key);
				}
			}
		}
		if (dirty) {
			save(file, prop);
		}
		return prop;
	}

	public static void add(File file, String key, String value) {
		Properties prop = read(file);
		String oldValue = StringUtil.trim(prop.getProperty(key));
		if (value.equals(oldValue) == false) {
			prop.setProperty(key, value);
			save(file, prop);
		}
	}
	public static boolean addProfile(File file, String key, String value) {
		value= StringUtil.trimEmpty(value);
		if(value.length()==0)
			return false;
		
		Properties prop = read(file);
		String oldValue = StringUtil.trimEmpty(prop.getProperty(key));
		
		if (oldValue.length()==0) {
			prop.setProperty(key, value);
			save(file, prop);
			return true;
		}
		if (oldValue.indexOf(value) < 0) {
			prop.setProperty(key, oldValue + "," + value);
			save(file, prop);
			return true;
		}
		return false;
	}

	public static void remove(File file, String key) {
		Properties prop = read(file);
		if (prop.containsKey(key)) {
			prop.remove(key);
			save(file, prop);
		}
	}

	private static void save(File file, Properties s) {
		String[] keys = SortUtil.sort_string(s.keys(), s.size());
		BufferedOutputStream out = null;
		try {
			out = new BufferedOutputStream(new FileOutputStream(file));
			for (int i = 0; i < keys.length; i++) {
				out.write((keys[i] + "=" + s.getProperty(keys[i]) + "\n").getBytes("UTF-8"));
			}
		} catch (Exception e) {
		} finally {
			FileUtil.close(out);
		}
	}

	private static Properties read(File file) {
		Properties p = new Properties();
		FileInputStream in = null;
		try {
			in = new FileInputStream(file);
			if (file.exists()) {
				p.load(in);
			}
		} catch (Exception e) {
		} finally {
			FileUtil.close(in);
		}
		return p;
	}

	public static void add(File file, String key, int value) {
		add(file, key, Integer.toString(value));
	}

	public static MapValue load(File file) {
		Properties p = ConfigMake.read(file);
		MapValue m = new MapValue();
		String[] keys = SortUtil.sort_string(p.propertyNames(), p.size());
		for (String key : keys) {
			String value = p.getProperty(key);
			m.put(key, value);
		}
		return m;
	}
}
