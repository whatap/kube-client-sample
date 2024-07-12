/*
 *  @(#) Expr.java
 *  Copyright 2003 the original author or authors. 
 *  
 *  Licensed under the Apache License, Version 2.0 (the "License"); 
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License. 
 *  
 *   @author Paul Kim(sjokim@gmail.com)
 */

package whatap.util.expr;

import whatap.lang.value.MapValue;
import whatap.lang.value.Value;
import whatap.lang.value.ValueEnum;
import whatap.util.Stack;
import whatap.util.StrMatch;
import whatap.util.StringUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * <pre>
 * 구문분석을 수행하는 클래스이다.
 * 어휘분석을 통해서 수식은 토큰화되면 구문 분석을 통해서 
 * 수식이 해독되고 처리된다.
 * </pre>
 * 
 * @since 1.1
 */
public class Expr {
	private String ruleString = null;
	private Stack<YyToken> yyTokens;

	public Expr(String rule) throws RuntimeException {
		ruleString = rule;
		this.yyTokens = new ExprParser().parse(rule);
	}

	public Object execute() throws RuntimeException {
		return this.execute(null);
	}

	public boolean checkVars(Set<String> param) {
		for (int i = 0; i < yyTokens.size(); i++) {
			if (yyTokens.get(i).index == YyToken.VARIABLE) {
				if (param.contains(yyTokens.get(i).value) == false) {
					return false;
				}
			}
		}
		return true;
	}

	public Set<String> getVarSet() {
		Set<String> vars = new HashSet<String>();
		for (int i = 0; i < yyTokens.size(); i++) {
			if (yyTokens.get(i).index == YyToken.VARIABLE) {
				vars.add((String) yyTokens.get(i).value);
			}
		}
		return vars;
	}

	public static boolean check(String formula) {
		try {
			if(StringUtil.isEmpty(formula))
				return false;
			new ExprParser().parse(formula);
			return true;
		} catch (Exception e) {
			return false;
		}
	}


	public Object execute(Object param) throws RuntimeException {
		Stack<YyToken> tokList = (Stack<YyToken>) this.yyTokens.clone();
		Stack<YyToken> stack = new Stack<YyToken>();
		if (tokList.size() == 1) {
			YyToken yyToken = (YyToken) tokList.pop();
			switch (yyToken.index) {
			case YyToken.VARIABLE:
				yyToken = this.getParamValue((String) yyToken.value,  param);
				break;
			}
			tokList.push(yyToken);
		} else {
			MAIN_LOOP: while (tokList.size() > 1) {
				YyToken yyToken = (YyToken) tokList.pop();
				YyToken temp = null;
				YyToken operand1 = null;
				YyToken operand2 = null;
				if (yyToken.index < YyToken.BASE_VAL) { // token이 op이면 or
														// function
					if (tokList.size() < 2)
						throw new RuntimeException("Unknown formula error");

					// operation이 "function or user function" 이면
					if (yyToken.index == YyToken.FUNCTION) {
						List<Object> opList = new ArrayList<Object>();
						List<YyToken> tmpList = new ArrayList<YyToken>();
						operand2 = (YyToken) tokList.pop();
						while (operand2.index != YyToken.BRACKET_CLOSE) {
							// 두번째 Operand를 가져와서 이것이 연산자인지를 검사한다.
							if (operand2.index < YyToken.BASE_VAL) {
								tokList.push(operand2);
								stack.push(yyToken);
								for (Iterator<YyToken> i = tmpList.iterator(); i.hasNext();) {
									stack.push(i.next());
								}
								continue MAIN_LOOP;
							} else if (operand2.index < YyToken.BRACKET_OPEN) {
								switch (operand2.index) {
								case YyToken.VARIABLE:
									operand2 = this.getParamValue((String) operand2.value, param);
									break;
								}
								// 유일하게 집합OP인 경우에만 parameter에 Set이나 List를 사용할 수
								if (operand2.index == YyToken.OBJECT && operand2.value instanceof Collection) {
									opList.addAll((Collection<?>) operand2.value);
								} else {
									if (operand2.index == YyToken.NULL) {
										opList.add(null);
									} else {
										opList.add(operand2.value);
									}
								}
							}
							tmpList.add(operand2);
							if (tokList.isEmpty())
								throw new RuntimeException("Unknown formula error");

							operand2 = (YyToken) tokList.pop();
						}
						temp = operate_func(yyToken, opList);
					} else {
						operand1 = (YyToken) tokList.pop();
						switch (operand1.index) {
						case YyToken.VARIABLE:
							operand1 = this.getParamValue((String) operand1.value, param);
							break;
						}
						if (operand1.index < YyToken.BASE_VAL) {
							tokList.push(operand1);
							stack.push(yyToken);
							continue;
						}

						// Short Circuit Operation
						if ((yyToken.index == 41 && "true".equals(operand1.value.toString()))
								|| (yyToken.index == 40 && "false".equals(operand1.value.toString()))) {
							if (stack.isEmpty()) {
								return (Boolean) operand1.value;
							}
							// 나머지 오퍼레이션은 수행하지 않는다.
							this.dropNextOperand(tokList);
							tokList.push(new YyToken(YyToken.BOOLEAN, operand1.value));
						}
						if (yyToken.index == 42) { // 삼항 연산인 경우
							YyToken token2 = (YyToken) tokList.pop();
							if (token2.index != 43)
								throw new RuntimeException("Unknown formula error");
							operand2 = (YyToken) tokList.pop();
							if (operand2.index < YyToken.BASE_VAL) {
								tokList.push(operand2);
								stack.push(yyToken);
								stack.push(operand1);
								stack.push(token2);
								continue;
							}
							if ("true".equals(operand1.value.toString())) {
								temp = operand2;
								this.dropNextOperand(tokList);
							} else {
								YyToken operand3 = (YyToken) tokList.pop();
								if (operand3.index < YyToken.BASE_VAL) {
									tokList.push(operand3);
									stack.push(yyToken);
									stack.push(operand1);
									stack.push(token2);
									stack.push(operand2);
									continue;
								}
								temp = operate3(operand1, operand2, operand3);
							}
							// 삼항 연산의 경우에는 변수 대치를 연산후에 수행한다.
							switch (temp.index) {
							case YyToken.VARIABLE:
								temp = this.getParamValue((String) temp.value, param);
								break;
							}
						} else { // 이항연산인 경우
							operand2 = (YyToken) tokList.pop();
							switch (operand2.index) {
							case YyToken.VARIABLE:
								operand2 = this.getParamValue((String) operand2.value, param);
								break;
							}
							if (operand2.index < YyToken.BASE_VAL) {
								tokList.push(operand2);
								stack.push(yyToken);
								stack.push(operand1);
								continue;
							}
							temp = operate(operand1, operand2, yyToken);
						}
					}
					tokList.push(temp);
					while (stack.isEmpty() == false) {
						tokList.push(stack.pop());
					}
				} else {
					throw new RuntimeException("Unknown formula error");
				}
			} // end of while
		}
		if (tokList.size() == 1) {
			YyToken result = (YyToken) tokList.pop();
			if (result.index == YyToken.NULL)
				return null;
			else
				return result.value;
		} else {
			throw new RuntimeException("Unknown formula error");
		}
	}

	private void dropNextOperand(Stack<YyToken> tokenList) throws RuntimeException {
		int dropCnt = 1;
		do {
			if (tokenList.isEmpty())
				throw new RuntimeException("Unknown formula error");
			YyToken yyToken = (YyToken) tokenList.pop();
			if (yyToken.index == YyToken.FUNCTION) {
				// 아무짓도 않함
			} else if (yyToken.index < YyToken.BASE_VAL) {
				dropCnt++;
			} else {
				if (yyToken.index == YyToken.BRACKET_OPEN) {
					int count = 1;
					while (count > 0) {
						if (tokenList.isEmpty())
							throw new RuntimeException("Unknown formula error");
						yyToken = (YyToken) tokenList.pop();
						if (yyToken.index == YyToken.BRACKET_CLOSE)
							count--;
						else if (yyToken.index == YyToken.BRACKET_OPEN)
							count++;
					}
				}
				dropCnt--;
			}
		} while (dropCnt > 0);
	}

	private YyToken getParamValue(String key, Object param) throws RuntimeException {
         if( param instanceof Map){
        	 return getParamValue(key, (Map<String,?>)param);
         }else if(param instanceof MapValue){
        	 return getParamValue(key, (MapValue)param);
         }
         return value2token(param);
	}
	private YyToken getParamValue(String key, Map<String, ?> param) throws RuntimeException {
		if (param == null) {
			throw new RuntimeException("parameter is null");
		}
		Object o = param.get(key);
		if (o instanceof Value)
			return value2token((Value) o);
		else
			return value2token(o);
	}

	private YyToken getParamValue(String key, MapValue param) throws RuntimeException {
		if (param == null) {
			throw new RuntimeException("parameter is null");
		}
		Value o = param.get(key);
		return value2token(o);
	}

	private YyToken operate(YyToken operand1, YyToken operand2, YyToken operator) throws RuntimeException {
		try {
			switch (operator.index) {
			case 0: /* * */ {
				if (operand1.index == YyToken.NULL || operand2.index == YyToken.NULL) {
					throw new RuntimeException("Unknown formula error");
				}
				if (operand1.index == YyToken.REAL || operand2.index == YyToken.REAL) {
					double opr1 = ((Number) operand1.value).doubleValue();
					double opr2 = ((Number) operand2.value).doubleValue();
					return new YyToken(YyToken.REAL, new Double(opr1 * opr2));
				} else {
					long opr1 = ((Number) operand1.value).longValue();
					long opr2 = ((Number) operand2.value).longValue();
					return new YyToken(YyToken.DECIMAL, new Long(opr1 * opr2));
				}
			}
			case 1: /* / */ {
				if (operand1.index == YyToken.NULL || operand2.index == YyToken.NULL) {
					throw new RuntimeException("Unknown formula error");
				}
				if (operand1.index == YyToken.REAL || operand2.index == YyToken.REAL) {
					double opr1 = ((Number) operand1.value).doubleValue();
					double opr2 = ((Number) operand2.value).doubleValue();
					if (opr2 == 0) {
						throw new RuntimeException("Unknown formula error");
					}
					return new YyToken(YyToken.REAL, new Double(opr1 / opr2));
				} else {
					long opr1 = ((Number) operand1.value).longValue();
					long opr2 = ((Number) operand2.value).longValue();
					if (opr2 == 0) {
						throw new RuntimeException("Unknown formula error");
					}
					return new YyToken(YyToken.DECIMAL, new Long(opr1 / opr2));
				}
			}
			case 2: /* % */ {
				if (operand1.index == YyToken.NULL || operand2.index == YyToken.NULL) {
					throw new RuntimeException("Unknown formula error");
				}
				long opr1 = ((Number) operand1.value).longValue();
				long opr2 = ((Number) operand2.value).longValue();
				if (opr2 == 0) {
					throw new RuntimeException("Unknown formula error");
				}
				return new YyToken(YyToken.DECIMAL, new Long(opr1 % opr2));
			}
			case 10: /* + */ {
				if (operand1.index == YyToken.STRING || operand2.index == YyToken.STRING) {
					return new YyToken(YyToken.STRING, new StringBuilder().append(operand1.value).append(operand2.value).toString());
				} else if (operand1.index == YyToken.REAL || operand2.index == YyToken.REAL) {
					double opr1 = ((Number) operand1.value).doubleValue();
					double opr2 = ((Number) operand2.value).doubleValue();
					return new YyToken(YyToken.REAL, new Double(opr1 + opr2));
				} else {
					long opr1 = ((Number) operand1.value).longValue();
					long opr2 = ((Number) operand2.value).longValue();
					return new YyToken(YyToken.DECIMAL, new Long(opr1 + opr2));
				}
			}
			case 11: /* - */ {
				if (operand1.index == YyToken.NULL || operand2.index == YyToken.NULL) {
					throw new RuntimeException("Unknown formula error");
				}
				if (operand1.index == YyToken.REAL || operand2.index == YyToken.REAL) {
					double opr1 = ((Number) operand1.value).doubleValue();
					double opr2 = ((Number) operand2.value).doubleValue();
					return new YyToken(YyToken.REAL, new Double(opr1 - opr2));
				} else if (operand1.index == YyToken.STRING || operand2.index == YyToken.STRING) {
					String s = operand1.value.toString();
					String delims = operand2.value.toString();
					StringBuffer sb = new StringBuffer();
					StringTokenizer st = new StringTokenizer(s, delims);
					while (st.hasMoreTokens()) {
						sb.append(st.nextToken());
					}
					return new YyToken(YyToken.STRING, sb.toString());
				} else {
					long opr1 = ((Number) operand1.value).longValue();
					long opr2 = ((Number) operand2.value).longValue();
					return new YyToken(YyToken.DECIMAL, new Long(opr1 - opr2));
				}
			}
			case 20: /* < */ {
				if (operand1.index == YyToken.NULL || operand2.index == YyToken.NULL) {
					throw new RuntimeException("Unknown formula error");
				}
				if ((operand1.index == YyToken.DECIMAL || operand1.index == YyToken.REAL)
						&& (operand2.index == YyToken.DECIMAL || operand2.index == YyToken.REAL)) {
					double opr1 = ((Number) operand1.value).doubleValue();
					double opr2 = ((Number) operand2.value).doubleValue();
					return new YyToken(YyToken.BOOLEAN, new Boolean(opr1 < opr2));
				} else {
					int comp = operand1.value.toString().compareTo(operand2.value.toString());
					return new YyToken(YyToken.BOOLEAN, new Boolean(comp < 0));
				}
			}
			case 21: /* > */ {
				if (operand1.index == YyToken.NULL || operand2.index == YyToken.NULL) {
					throw new RuntimeException("Unknown formula error");
				}
				if ((operand1.index == YyToken.DECIMAL || operand1.index == YyToken.REAL)
						&& (operand2.index == YyToken.DECIMAL || operand2.index == YyToken.REAL)) {
					double opr1 = ((Number) operand1.value).doubleValue();
					double opr2 = ((Number) operand2.value).doubleValue();
					return new YyToken(YyToken.BOOLEAN, new Boolean(opr1 > opr2));
				} else {
					int comp = operand1.value.toString().compareTo(operand2.value.toString());
					return new YyToken(YyToken.BOOLEAN, new Boolean(comp > 0));
				}
			}
			case 22: /* <= */ {
				if (operand1.index == YyToken.NULL || operand2.index == YyToken.NULL) {
					throw new RuntimeException("Unknown formula error");
				}
				if ((operand1.index == YyToken.DECIMAL || operand1.index == YyToken.REAL)
						&& (operand2.index == YyToken.DECIMAL || operand2.index == YyToken.REAL)) {
					double opr1 = ((Number) operand1.value).doubleValue();
					double opr2 = ((Number) operand2.value).doubleValue();
					return new YyToken(YyToken.BOOLEAN, new Boolean(opr1 <= opr2));
				} else {
					int comp = operand1.value.toString().compareTo(operand2.value.toString());
					return new YyToken(YyToken.BOOLEAN, new Boolean(comp <= 0));
				}
			}
			case 23: /* >= */ {
				if (operand1.index == YyToken.NULL || operand2.index == YyToken.NULL) {
					throw new RuntimeException("Unknown formula error");
				}
				if ((operand1.index == YyToken.DECIMAL || operand1.index == YyToken.REAL)
						&& (operand2.index == YyToken.DECIMAL || operand2.index == YyToken.REAL)) {
					double opr1 = ((Number) operand1.value).doubleValue();
					double opr2 = ((Number) operand2.value).doubleValue();
					return new YyToken(YyToken.BOOLEAN, new Boolean(opr1 >= opr2));
				} else {
					int comp = operand1.value.toString().compareTo(operand2.value.toString());
					return new YyToken(YyToken.BOOLEAN, new Boolean(comp >= 0));
				}
			}
			case 30: /* == */ {
				if (operand1.index == YyToken.NULL && operand2.index == YyToken.NULL) {
					return new YyToken(YyToken.BOOLEAN, new Boolean(true));
				}
				if (operand1.index == YyToken.NULL || operand2.index == YyToken.NULL) {
					return new YyToken(YyToken.BOOLEAN, new Boolean(false));
				}
				if ((operand1.index == YyToken.DECIMAL || operand1.index == YyToken.REAL)
						&& (operand2.index == YyToken.DECIMAL || operand2.index == YyToken.REAL)) {
					double opr1 = ((Number) operand1.value).doubleValue();
					double opr2 = ((Number) operand2.value).doubleValue();
					return new YyToken(YyToken.BOOLEAN, new Boolean(opr1 == opr2));
				} else {
					String opr1 = operand1.value.toString();
					String opr2 = operand2.value.toString();
					int comp = opr1.compareTo(opr2);
					return new YyToken(YyToken.BOOLEAN, new Boolean(comp == 0));
				}
			}
			case 31: /* != */ {
				if (operand1.index == YyToken.NULL && operand2.index == YyToken.NULL) {
					return new YyToken(YyToken.BOOLEAN, new Boolean(false));
				}
				if (operand1.index == YyToken.NULL || operand2.index == YyToken.NULL) {
					return new YyToken(YyToken.BOOLEAN, new Boolean(true));
				}
				if ((operand1.index == YyToken.DECIMAL || operand1.index == YyToken.REAL)
						&& (operand2.index == YyToken.DECIMAL || operand2.index == YyToken.REAL)) {
					double opr1 = ((Number) operand1.value).doubleValue();
					double opr2 = ((Number) operand2.value).doubleValue();
					return new YyToken(YyToken.BOOLEAN, new Boolean(opr1 != opr2));
				} else {
					String opr1 = operand1.value.toString();
					String opr2 = operand2.value.toString();
					int comp = opr1.compareTo(opr2);
					return new YyToken(YyToken.BOOLEAN, new Boolean(comp != 0));
				}
			}
			case YyToken.OPERATOR_LIKE: {
				if (operand1.index == YyToken.NULL && operand2.index == YyToken.NULL) {
					return new YyToken(YyToken.BOOLEAN, Boolean.TRUE);
				}
				if (operand1.index == YyToken.NULL || operand2.index == YyToken.NULL) {
					return new YyToken(YyToken.BOOLEAN, Boolean.FALSE);
				}

				String opr1 = operand1.value.toString();
				String opr2 = operand2.value.toString();
				StrMatch m =  StrMatch.createForLikeOperator(opr2);
				return new YyToken(YyToken.BOOLEAN, m.include(opr1));
			}			
			case 40: /* && */ {
				if (operand1.index == YyToken.NULL || operand2.index == YyToken.NULL) {
					throw new RuntimeException("Unknown formula error");
				}
				if (operand1.index == YyToken.BOOLEAN && operand2.index == YyToken.BOOLEAN) {
					boolean opr1 = ((Boolean) operand1.value).booleanValue();
					boolean opr2 = ((Boolean) operand2.value).booleanValue();
					return new YyToken(YyToken.BOOLEAN, new Boolean(opr1 && opr2));
				} else {
					throw new RuntimeException("Unknown formula error");
				}
			}
			case 41: /* || */ {
				if (operand1.index == YyToken.NULL || operand2.index == YyToken.NULL) {
					throw new RuntimeException("Unknown formula error");
				}
				if (operand1.index == YyToken.BOOLEAN && operand2.index == YyToken.BOOLEAN) {
					boolean opr1 = ((Boolean) operand1.value).booleanValue();
					boolean opr2 = ((Boolean) operand2.value).booleanValue();
					return new YyToken(YyToken.BOOLEAN, new Boolean(opr1 || opr2));
				} else {
					throw new RuntimeException("Unknown formula error");
				}
			}
			default:
				throw new RuntimeException("Unknown formula error");
			}
		} catch (RuntimeException re) {
			throw re;
		} catch (Exception e) {
			throw new RuntimeException("Unknown formula error");
		}
	}

	private YyToken operate_func(YyToken yyToken, List<?> params) throws RuntimeException {
		Function fobj = FunctionLoader.getFunction((String) yyToken.value);
		if (fobj == null)
			throw new RuntimeException("unknown function name " + yyToken.value);
		Object o = fobj.process(params);
		return value2token(o);
	}

	private YyToken operate3(YyToken operand1, YyToken operand2, YyToken operand3) throws RuntimeException {
		if (operand1.value == null) {
			throw new RuntimeException("Unknown formula error");
		}
		try {
			boolean opr1 = ((Boolean) operand1.value).booleanValue();
			return opr1 ? operand2 : operand3;
		} catch (Exception e) {
			throw new RuntimeException("Unknown formula error");
		}
	}

	private YyToken value2token(Object value) {
		if (value == null)
			return new YyToken(YyToken.NULL, "null");
		else if (value instanceof Double || value instanceof Float)
			return new YyToken(YyToken.REAL, value);
		else if (value instanceof Integer || value instanceof Long)
			return new YyToken(YyToken.DECIMAL, value);
		else if (value instanceof Boolean)
			return new YyToken(YyToken.BOOLEAN, value);
		else if (value instanceof String)
			return new YyToken(YyToken.STRING, value);
		else
			return new YyToken(YyToken.OBJECT, value);
	}

	private YyToken value2token(Value value) {
		if (value == null)
			return new YyToken(YyToken.NULL, "null");
		switch (value.getValueType()) {
		case ValueEnum.NULL:
			return new YyToken(YyToken.NULL, "null");
		case ValueEnum.BOOLEAN:
			return new YyToken(YyToken.BOOLEAN, value.toJavaObject());
		case ValueEnum.DECIMAL:
			return new YyToken(YyToken.DECIMAL, value.toJavaObject());
		case ValueEnum.FLOAT:
		case ValueEnum.DOUBLE:
			return new YyToken(YyToken.REAL, value.toJavaObject());
		case ValueEnum.TEXT:
			return new YyToken(YyToken.STRING, value.toJavaObject());
		case ValueEnum.BLOB:
		case ValueEnum.LIST:
		case ValueEnum.MAP:
			return new YyToken(YyToken.OBJECT, value.toJavaObject());
		default:
			throw new RuntimeException("unknown value type=" + value.getValueType());
		}
	}

	public String toString() {
		return ruleString;
	}
}
