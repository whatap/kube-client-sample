package whatap.lang.slog;

import java.util.ArrayList;
import java.util.List;

public class SLogParser {

	private static final String INDICATOR = "+";
	private String[] head;
	private char startCh = '[';
	private char endCh = ']';

	public SLogParser(String... head) {
		this('[', ']', head);
	}

	public SLogParser(char startCh, char endCh, String... head) {
		this.head = head;
		this.startCh = startCh;
		this.endCh = endCh;
	}

	public List<Field> parse(String log) {
		List<Field> out = new ArrayList<Field>();
		char[] str = log.toCharArray();
		int headLen = this.head.length;

		List<String> headValue = new ArrayList<String>();
		
		StringBuilder word = new StringBuilder();
		boolean beginValue = false;
		for (int i = 0; i < str.length; i++) {
			char ch = str[i];
			if (beginValue == false) {
				if (ch == startCh) {
					beginValue = true;
				}
			} else {
				if (ch == endCh) {
					beginValue = false;
					String value = word.toString().trim();
					if (value.equals(INDICATOR)) {
						setHead(out, headValue);
						parse(out, str, i);
						return out;
					}
					if (headValue.size() < headLen) {
						headValue.add(value);
					}
					word = new StringBuilder();
				} else {
					word.append(ch);
				}
			}
		}
		setHead(out, headValue);
		return out;
	}

	private void setHead(List<Field> out, List<String> headValue) {
		for (int i = 0; i < headValue.size(); i++) {
			out.add(new Field(this.head[i], headValue.get(i)));
		}
	}

	private void parse(List<Field> out, char[] str, int p) {

		boolean startQuart = false;
		boolean startValue = false;

		String key = null;
		String value = null;
		StringBuilder buf = new StringBuilder();
		for (int i = p; i < str.length; i++) {
			switch (str[i]) {
			case ' ':
				if (startQuart == true) {
					buf.append(' ');
				} else {
					if (buf.length() > 0) {
						value = buf.toString();
						buf = new StringBuilder();
					}
					if (key != null && value != null) {
						out.add(new Field(key, value));
					}
					key = null;
					value = null;
					startQuart = false;
					startValue = false;
				}
				break;
			case '=':
				if (startValue || startQuart) {
					buf.append('=');
				} else if (buf.length() > 0) {
					key = buf.toString();
					buf = new StringBuilder();
					startValue = true;
				}
				break;
			case '"':
				if (startQuart == true) {
					if (buf.length() > 0) {
						value = buf.toString();
						buf = new StringBuilder();
					}
					if (key != null && value != null) {
						out.add(new Field(key, value));
					}
					key = null;
					value = null;
					startQuart = false;
				} else {
					startQuart = true;
				}
				break;
			default:
				buf.append(str[i]);
			}
		}
		if (buf.length() > 0) {
			value = buf.toString();
			buf = new StringBuilder();
		}
		if (key != null && value != null) {
			out.add(new Field(key, value));
		}
	}
}
