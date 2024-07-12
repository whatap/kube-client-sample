package whatap.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class StringProperty extends StringKeyLinkedMap<String> {

	private static final String EMPTY = "__empty__";

	public StringProperty(int initCapacity, float loadFactor) {
		super(initCapacity, loadFactor);
	}

	public StringProperty() {
	}

	public StringList getNames() {
		StringList _keys = new StringList();
		StringEnumer en = this.keys();
		while (en.hasMoreElements()) {
			String key = en.nextString();
			if (key.startsWith(EMPTY) == false) {
				_keys.add(key);
			}
		}
		return _keys;
	}

	public synchronized StringProperty load(File file) {
		this.clear();
		load0(loadText(file));
		return this;
	}

	private List<String> loadText(File file) {
		List<String> out = new ArrayList<String>();
		BufferedReader reader = null;

		try {
			reader = new BufferedReader(new FileReader(file));
			StringBuffer buffer = new StringBuffer();
			String line;
			while ((line = reader.readLine()) != null) {
				line = StringUtil.trim(line);
				if (line.startsWith("#")) {
					out.add(line);
				} else if (line.endsWith("\\")) {
					buffer.append(line.substring(0, line.length() - 1));
				} else {
					if (buffer.length() == 0) {
						out.add(line);
					} else {
						buffer.append(line);
						out.add(buffer.toString());
						buffer = new StringBuffer();
					}
				}

			}

			if (buffer.length() > 0) {
				out.add(buffer.toString());
			}
		} catch (Throwable t) {
		} finally {
			FileUtil.close(reader);
		}

		return out;
	}

	private void load0(List<String> arr) {
		for (String line : arr) {
			if (line == null)
				continue;
			if (line.startsWith("#") || line.length() == 0) {
				this.put(EMPTY + this.size(), line);
			} else {
				String[] keyVal = StringUtil.divKeyValue(line, "=");
				if (keyVal.length == 1) {
					this.put(EMPTY + this.size(), line);
				} else if (keyVal.length == 2) {
					this.put(keyVal[0].trim(), keyVal[1].trim());
				}
			}
		}
	}

	public String toString() {
		String separator = System.getProperty("line.separator");
		StringBuffer sb = new StringBuffer();
		StringEnumer en = this.keys();
		while (en.hasMoreElements()) {
			String key = en.nextString();
			String value = this.get(key);
			if (sb.length() > 0) {
				sb.append(separator);
			}
			if (key.startsWith(EMPTY)) {
				sb.append(value);
			} else {
				sb.append(key).append(" = ").append(value);
			}
		}
		return sb.toString();
	}

	public StringProperty putAll(StringProperty p) {
		if (p == null)
			return null;
		String[] keys = p.keyArray();
		for (String key : keys) {
			String value = p.get(key);

			if (key.startsWith(EMPTY)) {
				this.put(EMPTY + this.size(), value);
			} else {
				this.put(key, value);
			}
		}
		return this;
	}
}
