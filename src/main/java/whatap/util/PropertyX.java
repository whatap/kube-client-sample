package whatap.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

public class PropertyX {

	final private CipherAgent cipher;
	public PropertyX() {		
		this.cipher = new CipherAgent(null);
	}
	public PropertyX(String masterkey) {
		this.cipher = new CipherAgent(masterkey);
	}
	protected LinkedMap<String, String> property = new LinkedMap<String, String>();

	public String setProperty(String key, String value) {
		return property.put(key, value);
	}

	public void removeProperty(String key) {
		property.remove(key);
	}

	
	public PropertyX load(File file) {
		if (file.canRead() == false)
			return this;
		FileReader reader = null;
		try {
			reader = new FileReader(file);
			load(reader);
		} catch (Exception e) {
		} finally {
			FileUtil.close(reader);
		}
		return this;
	}

	public void save(File file) {
		
		PrintWriter pw = null;
		try {
			pw=new PrintWriter(file);
			save(pw);
		} catch (Exception e) {
		}finally {
			FileUtil.close(pw);
		}
	}

	public void save(PrintWriter pw) {
		try {
			Enumeration<String> en = this.property.keys();
			while (en.hasMoreElements()) {
				String key = en.nextElement();
				String value = this.property.get(key);
				if (value.indexOf("\n") > 0) {
					pw.println(key + "='''");
					pw.println(value);
					pw.println("'''");
				} else {
					pw.println(key + "=" + value);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public synchronized PropertyX load(Reader fileReader) {

		BufferedReader reader = null;
		try {
			reader = new BufferedReader(fileReader);
			StringBuffer buff = new StringBuffer();
			boolean backslash = false;
			while (true) {
				String line = StringUtil.trim(reader.readLine());
				if (line == null) {
					break;
				}

				if (backslash == false) {
					if (buff.length() > 0) {
						buff = putAndClear(buff);
					}
					if (isComment(line)) {
						continue;
					}
					if (line.endsWith("'''")) {
						buff.append(line.substring(0, line.length() - 3));
						line = StringUtil.trimRight(reader.readLine());
						while (line != null && line.endsWith("'''") == false) {
							buff.append(line).append("\n");
							line = StringUtil.trimRight(reader.readLine());
						}
						if (line != null) {
							buff.append(line.substring(0, line.length() - 3));
						}
						buff = putAndClear(buff);
						continue;
					}
				}
				if (isBackslash(line)) {
					backslash = true;
					if (buff.length() > 0) {
						buff.append(' ');
					}
					buff.append(line.substring(0, line.length() - 1));
				} else {
					backslash = false;
					if (buff.length() > 0) {
						buff.append(' ');
					}
					buff.append(line);
				}
			}
			buff = putAndClear(buff);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}

	private boolean isComment(String line) {
		return line.startsWith("#");
	}

	private boolean isBackslash(String line) {
		return line.endsWith("\\");
	}

	private StringBuffer putAndClear(StringBuffer buff) {
		if (buff.length() > 0) {
			int x = buff.indexOf("=");
			if (x > 0) {
				String key = buff.substring(0, x).trim();
				String value = buff.substring(x + 1).trim();
				this.property.put(key, value);
			}
			return new StringBuffer();
		} else {
			return buff;
		}
	}

	public String getProperty(String key) {
		return property.get(key);
	}

	public String getProperty(String key, String defaultValue) {
		String val = getProperty(key);
		return (val == null) ? defaultValue : val;
	}

	public boolean getBoolean(String key, boolean defValue) {
		String value = this.getProperty(key);
		return value != null ? CastUtil.cboolean(value) : defValue;
	}

	public int getInt(String key) {
		return CastUtil.cint(this.getProperty(key));
	}

	public int getInt(String key, int defValue) {
		String value = this.getProperty(key);
		return value != null ? CastUtil.cint(value) : defValue;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		Enumeration<String> en = this.property.keys();
		while (en.hasMoreElements()) {
			String key = en.nextElement();
			String value = this.getProperty(key);
			if (value.indexOf("\n") < 0) {
				sb.append(key).append("=").append(value).append("\n");
			} else {
				sb.append(key).append("=").append("'''").append("\n");
				sb.append(value).append("\n");
				sb.append("'''\n");
			}
		}
		return sb.toString();
	}

	public Enumeration<String> keys() {
		return this.property.keys();
	}

	public void setProperty(Map<String, String> p) {
		Iterator<String> itr = p.keySet().iterator();
		while (itr.hasNext()) {
			String key = itr.next();
			String value = p.get(key);
			this.setProperty(key, value);
		}
	}

	public void setProperty(PropertyX p) {
		this.property.putAll(p.property);
	}

	public String getCipherValue(String key) {
		String value = this.getProperty(key);
		return this.cipher.decryptIfCipher(value);
	}

	public void setCipherProperty(String key, String value) {
		if (value != null) {
			value = this.cipher.encryptIfPlain(value);
			this.property.put(key, value);
		}
	}

	public StringSet getStringSet(String key, String deli) {
		StringSet set = new StringSet();
		String v = getProperty(key);
		if (v != null) {
			String[] vv = StringUtil.tokenizer(v, deli);
			for (String x : vv) {
				x = StringUtil.trimToEmpty(x);
				if (x.length() > 0)
					set.put(x);
			}
		}
		return set;
	}

	public StringLinkedSet getStringLinedSet(String key, String deli) {
		StringLinkedSet set = new StringLinkedSet();
		String v = getProperty(key);
		if (v != null) {
			String[] vv = StringUtil.tokenizer(v, deli);
			for (String x : vv) {
				x = StringUtil.trimToEmpty(x);
				if (x.length() > 0)
					set.put(x);
			}
		}
		return set;
	}

	public IntLinkedSet getIntLinedSet(String key, String deli) {
		IntLinkedSet set = new IntLinkedSet();
		String v = getProperty(key);
		if (v != null) {
			String[] vv = StringUtil.tokenizer(v, deli);
			for (String x : vv) {
				x = StringUtil.trimToEmpty(x);
				if (x.equals("0")) {
					set.put(0);
				} else {
					int i = CastUtil.cint(x);
					if (i != 0)
						set.put(i);
				}
			}
		}
		return set;
	}

	public void putAll(Properties m) {
		Iterator<Entry<Object, Object>> itr = m.entrySet().iterator();
		while (itr.hasNext()) {
			Entry<Object, Object> e = itr.next();
			this.property.put((String) e.getKey(), (String) e.getValue());
		}
	}

}
