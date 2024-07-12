package whatap.lang.slog;

import whatap.lang.value.MapValue;
import whatap.lang.value.Value;
import whatap.lang.value.ValueEnum;
import whatap.util.CastUtil;
import whatap.util.StringEnumer;
import whatap.util.StringKeyLinkedMap;
import whatap.util.StringKeyLinkedMap.StringKeyLinkedEntry;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class SMap {

	StringKeyLinkedMap<String> buffer;

	public SMap() {
		buffer = new StringKeyLinkedMap<String>();
	}

	public StringEnumer keys() {
		return this.buffer.keys();
	}

	public String getValue(String key) {
		return this.buffer.get(key);
	}

	public SMap put(String key, String value) {

		this.buffer.put(key, addText(value));

		return this;
	}

	private static String none = "\"\"";

	private String addText(String value) {
		if (value == null)
			return none;
		boolean hasSpace = false;
		char[] str = value.toCharArray();
		for (int i = 0; i < str.length; i++) {
			switch (str[i]) {
			case '"':
				str[i] = '\'';
				break;
			case ' ':
				hasSpace = true;
				break;
			}
		}
		if (hasSpace) {
			StringBuilder b = new StringBuilder();
			b.append('"');
			b.append(str);
			b.append('"');
			return b.toString();
		} else {
			return new String(str);
		}
	}

	public SMap append(String key, Object value) {

		if (value == null) {
			this.buffer.put(key, none);
		} else {
			if (value instanceof String) {
				this.buffer.put(key, addText((String) value));
			} else if (value instanceof Number) {
				if (value instanceof Float || value instanceof Double) {
					this.buffer.put(key, new DecimalFormat("#0.0#######").format(value));
				} else {
					this.buffer.put(key, String.valueOf(value));
				}
			} else {
				this.buffer.put(key, addText(value.toString()));
			}
		}
		return this;
	}

	public SMap append(String key, long value) {
		this.buffer.put(key, String.valueOf(value));
		return this;
	}

	public SMap append(String key, float value) {
		this.buffer.put(key, new DecimalFormat("#0.0#######").format(value));
		return this;
	}

	public SMap append(String key, double value) {
		this.buffer.put(key, new DecimalFormat("#0.0#######").format(value));
		return this;
	}

	public SMap append(String key, float value, String format) {
		this.buffer.put(key, new DecimalFormat(format).format(value));
		return this;
	}

	public SMap append(String key, double value, String format) {
		this.buffer.put(key, new DecimalFormat(format).format(value));
		return this;
	}

	@Override
	public String toString() {
		return str();
	}

	public String str() {
		return new SLog().a(this).toString();
	}

	private static SimpleDateFormat timeformat = new SimpleDateFormat("HH:mm:ss.SSS");

	public SMap time(String key, Date time) {
		this.buffer.put(key, timeformat.format(time));
		return this;
	}

	public SMap t(String key, Date date) {
		return time(key, date);
	}

	public SMap t(String key, long date) {
		return time(key, new Date(date));
	}

	private static SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");

	public SMap date(String key, Date date) {
		this.buffer.put(key, dateformat.format(date));
		return this;
	}

	public SMap d(String key, Date date) {
		return date(key, date);
	}

	public SMap d(String key, long date) {
		return date(key, new Date(date));
	}

	private static SimpleDateFormat timestampFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

	public SMap timestamp(String key, Date date) {
		this.buffer.put(key, '"' + timestampFormat.format(date) + '"');
		return this;
	}

	public SMap ts(String key, Date date) {
		return timestamp(key, date);
	}

	public SMap ts(String key, long date) {
		return timestamp(key, new Date(date));
	}

	public SMap append(MapValue m) {
		SMap slog = this;
		Enumeration<StringKeyLinkedEntry<Value>> en = m.entries();
		while (en.hasMoreElements()) {
			StringKeyLinkedEntry<Value> ent = en.nextElement();
			switch (ent.getValue().getValueType()) {
			case ValueEnum.DECIMAL:
			case ValueEnum.DECIMAL_INT:
			case ValueEnum.DECIMAL_LONG:
			case ValueEnum.LONG_SUMMARY:
				slog.append(ent.getKey(), ((Number) ent.getValue()).longValue());
				break;
			case ValueEnum.FLOAT:
			case ValueEnum.DOUBLE:
			case ValueEnum.METRIC:
				slog.append(ent.getKey(), ((Number) ent.getValue()).doubleValue());
				break;
			default:
				slog.append(ent.getKey(), ent.getValue().toString());
			}
		}
		return slog;
	}

	public SMap append(MapValue m, Fmt keyfmt, Fmt valfmt) {
		SMap slog = this;
		Enumeration<StringKeyLinkedEntry<Value>> en = m.entries();
		while (en.hasMoreElements()) {
			StringKeyLinkedEntry<Value> ent = en.nextElement();
			slog.append(keyfmt.format(ent.getKey()), valfmt.format(ent.getValue()));
		}
		return slog;
	}

	public SMap append(MapValue m, String prefix) {
		if (m == null)
			return this;
		SMap slog = this;
		Enumeration<StringKeyLinkedEntry<Value>> en = m.entries();
		while (en.hasMoreElements()) {
			StringKeyLinkedEntry<Value> ent = en.nextElement();
			switch (ent.getValue().getValueType()) {
			case ValueEnum.DECIMAL:
			case ValueEnum.DECIMAL_INT:
			case ValueEnum.DECIMAL_LONG:
			case ValueEnum.LONG_SUMMARY:
				slog.append(ent.getKey() + prefix, ((Number) ent.getValue()).longValue());
				break;
			case ValueEnum.FLOAT:
			case ValueEnum.DOUBLE:
			case ValueEnum.METRIC:
				slog.append(ent.getKey() + prefix, ((Number) ent.getValue()).doubleValue());
				break;
			default:
				slog.append(ent.getKey() + prefix, ent.getValue().toString());
			}
		}
		return slog;
	}

	public SMap append(MapValue m, Map<String, Type> types) {
		SMap slog = this;
		Enumeration<StringKeyLinkedEntry<Value>> en = m.entries();
		while (en.hasMoreElements()) {
			StringKeyLinkedEntry<Value> ent = en.nextElement();

			Type t = types.get(ent.getKey());
			if (t != null) {
				switch (t) {
				case date:
					slog.date(ent.getKey(), new Date(CastUtil.clong(ent.getValue())));
					break;
				case time:
					slog.time(ent.getKey(), new Date(CastUtil.clong(ent.getValue())));
					break;
				case timestamp:
					slog.timestamp(ent.getKey(), new Date(CastUtil.clong(ent.getValue())));
					break;
				}

			} else {
				switch (ent.getValue().getValueType()) {
				case ValueEnum.DECIMAL:
				case ValueEnum.DECIMAL_INT:
				case ValueEnum.DECIMAL_LONG:
				case ValueEnum.LONG_SUMMARY:
					slog.append(ent.getKey(), ((Number) ent.getValue()).longValue());
					break;
				case ValueEnum.FLOAT:
				case ValueEnum.DOUBLE:
				case ValueEnum.METRIC:
					slog.append(ent.getKey(), ((Number) ent.getValue()).doubleValue());
					break;
				default:
					slog.append(ent.getKey(), ent.getValue().toString());
				}
			}
		}
		return slog;
	}

	public SMap append(Map m) {
		SMap slog = this;
		Iterator<Entry> en = m.entrySet().iterator();
		while (en.hasNext()) {
			Entry ent = en.next();
			if (ent.getValue() instanceof Number) {
				if (ent.getValue() instanceof Long || ent.getValue() instanceof Integer) {
					slog.append(noSpace(ent.getKey().toString()), ((Number) ent.getValue()).longValue());
				} else {
					slog.append(noSpace(ent.getKey().toString()), ((Number) ent.getValue()).doubleValue());
				}
			} else {
				slog.append(noSpace(ent.getKey().toString()), ent.getValue().toString());
			}
		}
		return slog;
	}

	private String noSpace(String v) {
		boolean notMod = true;
		char[] k = v.toCharArray();
		for (int i = 0; i < k.length; i++) {
			if (k[i] == ' ') {
				notMod = false;
				k[i] = '_';
			}

		}
		return notMod ? v : new String(k);
	}

	public SMap append(Map m, String prefix) {
		SMap slog = this;
		Iterator<Entry> en = m.entrySet().iterator();
		while (en.hasNext()) {
			Entry ent = en.next();
			if (ent.getValue() instanceof Number) {
				if (ent.getValue() instanceof Long || ent.getValue() instanceof Integer) {
					slog.append(noSpace(prefix + ent.getKey()), ((Number) ent.getValue()).longValue());
				} else {
					slog.append(noSpace(prefix + ent.getKey()), ((Number) ent.getValue()).doubleValue());
				}
			} else {
				slog.append(noSpace(prefix + ent.getKey()), ent.getValue().toString());
			}
		}
		return slog;
	}

	public SMap a(String key, String value) {
		return this.append(key, value);
	}

	public SMap a(String key, long value) {
		return this.append(key, value);
	}

	public SMap a(String key, double value) {
		return this.append(key, value);
	}

	public SMap a(String key, Object value) {
		return this.append(key, value);
	}

	public SMap a(Map value) {
		return this.append(value);
	}

	public SMap a(MapValue value) {
		return this.append(value);
	}

	public SMap a(Map m, String prefix) {
		return this.append(m, prefix);
	}

	public SMap a(MapValue m, String prefix) {
		return this.append(m, prefix);
	}

	public static SMap n(String key, String value) {
		return New(key, value);
	}

	public static SMap n(String key, long value) {
		return New(key, value);
	}

	public static SMap n(String key, double value) {
		return New(key, value);
	}

	public static SMap New(String key, String value) {
		return new SMap().append(key, value);
	}

	public static SMap New(String key, long value) {
		return new SMap().append(key, value);
	}

	public static SMap New(String key, double value) {
		return new SMap().append(key, value);
	}

}