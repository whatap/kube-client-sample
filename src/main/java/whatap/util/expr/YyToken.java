package whatap.util.expr;
public class YyToken {
	/**
	 * BASE_VAL(100) 이후의 번호는 값들을 의미한다.
	 * BASE_VAL(100) 이하의 토큰들은 오퍼랜드들을 갖는 토큰들이다.
	 */
	public final static int OPERATOR_MUL = 0; // *
	public final static int OPERATOR_DIV = 1; // /
	public final static int OPERATOR_MOD = 2; // %
	public final static int OPERATOR_ADD = 10; // +
	public final static int OPERATOR_SUB = 11; // -
	public final static int OPERATOR_GT = 20; // < greater than
	public final static int OPERATOR_LT = 21; // > lower than
	public final static int OPERATOR_GTE = 22; // <= greater than or equal
	public final static int OPERATOR_LTE = 23; // >= lower than or equal
	public final static int OPERATOR_EQUAL = 30; // ==
	public final static int OPERATOR_NOT_EQUAL = 31; // !=
	public final static int OPERATOR_LIKE = 32; // like
	public final static int OPERATOR_AND = 40; // && and
	public final static int OPERATOR_OR = 41; // || or , Short Circuit Operation
	public final static int OPERATOR_TERNARY_QUESTION = 42; // ?, 3항 연산자
	public final static int OPERATOR_TERNARY_COLON = 43; // :, 3항 연산자

	public final static int FUNCTION = 91;
	public final static int BASE_VAL = 100;
	public final static int NULL = 101;
	public final static int BOOLEAN = 102;
	public final static int DECIMAL = 103;
	public final static int REAL = 104;
	public final static int OBJECT = 105;
	public final static int STRING = 106;

	public final static int VARIABLE = 120;

	public final static int PHRENTHESIS_OPEN = 50; // '('
	public final static int PHRENTHESIS_CLOSE = 51; // ')'
	public final static int BRACKET_OPEN = 1001;  // '['
	public final static int BRACKET_CLOSE = 1002;  // ']'
	public final static int COMMA = 1003;

	public YyToken(int index, Object value) {
		this.index = index;
		this.value = value;
	}

	public int index;
	public Object value;
	public int mux = 1;

	public String toString() {
		if(mux==1) {
			return "YyToken (" + index + ":" + value + ")";
		}else {
			return "YyToken (" + index + ":" + value + " x=" + mux+")";
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + index;
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		YyToken other = (YyToken) obj;
		if (index != other.index)
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

	public boolean isNumber() {
		return index == YyToken.DECIMAL || index == YyToken.REAL || value instanceof Number;
	}

}
