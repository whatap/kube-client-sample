package whatap.lang.slog;

import whatap.lang.value.MapValue;
import whatap.lang.value.Value;
import whatap.lang.value.ValueEnum;
import whatap.util.AnsiPrint;
import whatap.util.CastUtil;
import whatap.util.StringKeyLinkedMap.StringKeyLinkedEntry;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class SLog {
	private static final String INDICATOR = "[+]";

	private StringBuilder buffer;

	public SLog() {
		this(80);
	}

	public SLog(int len) {
		buffer = new StringBuilder(len);
		buffer.append(INDICATOR);
	}

	public SLog append(String key, String value) {
		buffer.append(' ');
		buffer.append(key).append('=');
		if (value == null) {
			addNull();
		} else {
			addText(value);
		}
		return this;
	}

	private void addNull() {
		buffer.append('"').append('"');
	}

	private void addText(String value) {
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
			buffer.append('"');
			buffer.append(str);
			buffer.append('"');
		} else {
			buffer.append(str);
		}
	}

	public SLog append(String key, Object value) {
		buffer.append(' ');
		buffer.append(key).append('=');
		if (value == null) {
			addNull();
		} else {
			if (value instanceof String) {
				addText((String) value);
			} else if (value instanceof Number) {
				if (value instanceof Float || value instanceof Double) {
					buffer.append(new DecimalFormat("#0.0#######").format(value));
				} else {
					buffer.append(value);
				}
			} else {
				addText(value.toString());
			}
		}
		return this;
	}

	public SLog append(String key, long value) {
		buffer.append(' ');
		buffer.append(key).append('=');
		buffer.append(value);
		return this;
	}
	public SLog yellow(String key, long value) {
		buffer.append(' ');
		buffer.append(key).append('=');
		buffer.append(AnsiPrint.yellow(value));
		return this;
	}

	public SLog append(String key, float value) {
		buffer.append(' ');
		buffer.append(key).append('=');
		buffer.append(value);
		return this;
	}
	public SLog yellow(String key, float value) {
		buffer.append(' ');
		buffer.append(key).append('=');
		buffer.append(AnsiPrint.yellow(value));
		return this;
	}

	public SLog append(String key, double value) {
		buffer.append(' ');
		buffer.append(key).append('=');
		buffer.append(new DecimalFormat("#0.0#######").format(value));
		return this;
	}
	public SLog yellow(String key, double value) {
		buffer.append(' ');
		buffer.append(key).append('=');
		buffer.append(AnsiPrint.yellow(new DecimalFormat("#0.0#######").format(value)));
		return this;
	}

	public SLog append(String key, float value, String format) {
		buffer.append(' ');
		buffer.append(key).append('=');
		buffer.append(new DecimalFormat(format).format(value));
		return this;
	}

	public SLog append(String key, double value, String format) {
		buffer.append(' ');
		buffer.append(key).append('=');
		buffer.append(new DecimalFormat(format).format(value));
		return this;
	}

	@Override
	public String toString() {
		return buffer.toString();
	}

	private static SimpleDateFormat timeformat = new SimpleDateFormat("HH:mm:ss.SSS");

	public SLog time(String key, Date time) {
		buffer.append(' ');
		buffer.append(key).append('=');
		buffer.append(timeformat.format(time));
		return this;
	}

	public SLog t(String key, Date date) {
		return time(key, date);
	}

	public SLog t(String key, long date) {
		return time(key, new Date(date));
	}

	private static SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");

	public SLog date(String key, Date date) {
		buffer.append(' ');
		buffer.append(key).append('=');
		buffer.append(dateformat.format(date));
		return this;
	}

	public SLog d(String key, Date date) {
		return date(key, date);
	}

	public SLog d(String key, long date) {
		return date(key, new Date(date));
	}

	private static SimpleDateFormat timestampFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

	public SLog timestamp(String key, Date date) {
		buffer.append(' ');
		buffer.append(key).append('=');
		buffer.append('"').append(timestampFormat.format(date)).append('"');
		return this;
	}

	public SLog ts(String key, Date date) {
		return timestamp(key, date);
	}

	public SLog ts(String key, long date) {
		return timestamp(key, new Date(date));
	}

	public SLog append(MapValue m) {
		SLog slog = this;
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

	public SLog yellow(MapValue m) {
		SLog slog = this;
		Enumeration<StringKeyLinkedEntry<Value>> en = m.entries();
		while (en.hasMoreElements()) {
			StringKeyLinkedEntry<Value> ent = en.nextElement();
			switch (ent.getValue().getValueType()) {
			case ValueEnum.DECIMAL:
			case ValueEnum.DECIMAL_INT:
			case ValueEnum.DECIMAL_LONG:
			case ValueEnum.LONG_SUMMARY:
				slog.yellow(ent.getKey(), AnsiPrint.yellow(((Number) ent.getValue()).longValue()));
				break;
			case ValueEnum.FLOAT:
				slog.yellow(ent.getKey(), ((Number) ent.getValue()).floatValue());
				break;
			case ValueEnum.DOUBLE:
			case ValueEnum.METRIC:
				slog.yellow(ent.getKey(), ((Number) ent.getValue()).doubleValue());
				break;
			default:
				slog.yellow(ent.getKey(), ent.getValue());
			}
		}
		return slog;
	}

	public SLog append(MapValue m, Fmt keyfmt, Fmt valfmt) {
		SLog slog = this;
		Enumeration<StringKeyLinkedEntry<Value>> en = m.entries();
		while (en.hasMoreElements()) {
			StringKeyLinkedEntry<Value> ent = en.nextElement();
			slog.append(keyfmt.format(ent.getKey()), valfmt.format(ent.getValue()));
		}
		return slog;
	}

	public SLog append(MapValue m, String prefix) {
		if (m == null)
			return this;
		SLog slog = this;
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

	public SLog append(MapValue m, Map<String, Type> types) {
		SLog slog = this;
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

	public SLog append(Map m) {
		SLog slog = this;
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

	public SLog append(Map m, String prefix) {
		SLog slog = this;
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

	public String str() {
		return this.toString();
	}
	public SLog a(Object... kv) {
		return append(kv);
	} 
	public SLog a(String key, String value) {
		return this.append(key, value);
	}

	public SLog a(String key, long value) {
		return this.append(key, value);
	}

	public SLog a(String key, double value) {
		return this.append(key, value);
	}

	public SLog a(String key, Object value) {
		return this.append(key, value);
	}
	public SLog red(String key, Object value) {
		return this.append(key, AnsiPrint.red(value));
	}
	public SLog yellow(String key, Object value) {
		return this.append(key, AnsiPrint.yellow(value));
	}
	public SLog green(String key, Object value) {
		return this.append(key, AnsiPrint.green(value));
	}
	public SLog a(Map value) {
		return this.append(value);
	}

	public SLog a(MapValue value) {
		return this.append(value);
	}

	public SLog a(Map m, String prefix) {
		return this.append(m, prefix);
	}

	public SLog a(MapValue m, String prefix) {
		return this.append(m, prefix);
	}

	public static SLog n(String key, String value) {
		return New(key, value);
	}
	public static SLog n(String key, Object value) {
		return New(key, value);
	}

	public static SLog n(String key, long value) {
		return New(key, value);
	}

	public static SLog n(String key, double value) {
		return New(key, value);
	}

	public static SLog New(String key, String value) {
		return new SLog().append(key, value);
	}

	public static SLog New(String key, Object value) {
		return new SLog().append(key, value);
	}
	public static SLog New(String key, long value) {
		return new SLog().append(key, value);
	}

	public static SLog New(String key, double value) {
		return new SLog().append(key, value);
	}

	public SLog append(SMap m) {
		Enumeration<StringKeyLinkedEntry<String>> sn = m.buffer.entries();
		while (sn.hasMoreElements()) {
			StringKeyLinkedEntry<String> et = sn.nextElement();
			this.buffer.append(' ');
			this.buffer.append(et.getKey()).append('=');
			this.buffer.append(et.getValue());
		}
		return this;
	}

	public SLog append(SMap m, String prefix) {
		Enumeration<StringKeyLinkedEntry<String>> sn = m.buffer.entries();
		while (sn.hasMoreElements()) {
			StringKeyLinkedEntry<String> et = sn.nextElement();
			this.buffer.append(' ');
			this.buffer.append(prefix).append(et.getKey()).append('=');
			this.buffer.append(et.getValue());
		}
		return this;
	}
	public SLog append(Object... kv) {
		if (kv == null || kv.length < 2)
			return this;
		try {
			int max = kv.length / 2 * 2;
			for (int i = 0; i < max; i += 2) {
				append((String) kv[i], kv[i + 1]);
			}
		} catch (Throwable t) {
			// ignore
		}
		return this;
	} 
	public SLog yellow(Object... kv) {
		if (kv == null || kv.length < 2)
			return this;
		try {
			int max = kv.length / 2 * 2;
			for (int i = 0; i < max; i += 2) {
				yellow((String) kv[i], kv[i + 1]);
			}
		} catch (Throwable t) {
			// ignore
		}
		return this;
	} 
	public SLog a(SMap m) {
		return append(m);
	}

	public SLog a(SMap m, String prefix) {
		return append(m, prefix);
	}
	
	public String red() {
		return AnsiPrint.red(this.str());
	}
	public String yellow() {
		return AnsiPrint.yellow(this.str());
	}
	public String green() {
		return AnsiPrint.green(this.str());
	}
	public String cyan() {
		return AnsiPrint.cyan(this.str());
	}
}
