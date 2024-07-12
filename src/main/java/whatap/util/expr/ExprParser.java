/*
 *  @(#) ExprParser.java
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

import whatap.util.Stack;
import whatap.util.StringUtil;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class ExprParser {

    public Stack<YyToken> parse(String rule) throws RuntimeException {
        List<YyToken> tmp = step1(rule);
        return step2(tmp);
    }

    public List<Stack<YyToken>> parseSelect(String rule) throws RuntimeException {
        List<YyToken> tmp = step1(rule);
        return stepArray(tmp);
    }

    private List<YyToken> step1(String rule) throws RuntimeException {
        Stack<YyToken> stack = new Stack<YyToken>();

        try {
        			
            YyLex yyLex = new YyLex(new StringReader(rule));

            YyToken lastToken = new YyToken(0, "");
            YyToken t;
            List<YyToken> list = new ArrayList<YyToken>();
            while ((t = yyLex.yylex()) != null) {
                if (t.index == 11) {
                    if (lastToken.index <= 51 || lastToken.index > 1000) {
                        t = yyLex.yylex();
                        t.mux = -1;
                        list.add(t);
                        lastToken=t;
                        continue;
                    }
                }
                list.add(t);
                if (t.index == YyToken.FUNCTION) {
                	//<중요>step1 토큰에서는 나타날 수없다. -20220329@sjkim
                    t.value = StringUtil.strip((String) t.value, " (");

                    t = new YyToken(YyToken.BRACKET_OPEN, "[");
                    list.add(t);
                    stack.push(t);
				} else if (t.index == YyToken.PHRENTHESIS_OPEN) {
					if (lastToken.index == YyToken.VARIABLE) {
						//jlex파일에서 YyToken.FUNCTIO토큰은 검사하지 않는다. 
						//그리고 변수+괄호가 나타나면 변수를 함수로 변경한다 -20220329@sjkim
						lastToken.index = YyToken.FUNCTION;
						t.index = YyToken.BRACKET_OPEN;
						t.value = "[";
					}
					stack.push(t);
                } else if (t.index == YyToken.PHRENTHESIS_CLOSE) {
                    YyToken tmp = (YyToken) stack.pop();
                    if (tmp.index == YyToken.BRACKET_OPEN) {
                        t.index = YyToken.BRACKET_CLOSE;
                        t.value = "]";
                    }
                }
                lastToken=t;
            }
            return list;
        } catch (RuntimeException e) {
            throw new RuntimeException("unknown rule error");
        } catch (Exception e) {
            throw new RuntimeException("unknown rule error");
        }

    }

    private Stack<YyToken> step2(List<YyToken> yyTokens) throws RuntimeException {
        Stack<YyToken> result = new Stack<YyToken>();
        Stack<YyToken> stack = new Stack<YyToken>();

        YyToken temp = null;

        int currentProcedence = 0;
        int stackProcedence = 0;

        for (int i = yyTokens.size() - 1; i >= 0; i--) {
            YyToken yyToken = (YyToken) yyTokens.get(i);

            if (yyToken.index == YyToken.BRACKET_OPEN) { // '['이면
                while ((temp = (YyToken) stack.pop()).index < 1000) {
                    result.push(temp);
                }
                result.push(yyToken);
                result.push(yyTokens.get(i - 1));
                i--;
            } else if (yyToken.index == YyToken.BRACKET_CLOSE) { // ']'이면
                result.push(yyToken);
                stack.push(yyToken);
            } else if (yyToken.index == YyToken.PHRENTHESIS_CLOSE) { // ')'이면
                stack.push(yyToken);
            } else if (yyToken.index == YyToken.PHRENTHESIS_OPEN) { // '('이면
                while ((temp = (YyToken) stack.pop()).index != 51) {
                    result.push(temp);
                }
            } else if (yyToken.index == YyToken.COMMA) { // ','이면
                while ((temp = (YyToken) stack.pop()).index < 1000) {
                    result.push(temp);
                }
                result.push(yyToken);
                stack.push(yyToken);
            } else if (yyToken.index > YyToken.BASE_VAL) { // Operation이 아니면
                result.push(yyToken);
            } else {
                // 피연산자가 있는 경우
                if (stack.empty()) {
                    stack.push(yyToken);
                } else {
                    currentProcedence = precedence(yyToken);
                    do {
                        temp = (YyToken) stack.pop();

                        stackProcedence = precedence(temp);
                        if (currentProcedence < stackProcedence) {
                            result.push(temp);
                            if (stack.empty()) {
                                stack.push(yyToken);
                                break;
                            }
                        } else {
                            stack.push(temp);
                            stack.push(yyToken);
                        }
                    } while (currentProcedence < stackProcedence);
                } // end of if
            } // end of if
        } // end of while

        while (!stack.empty()) {
            YyToken tok = (YyToken) stack.pop();
            if (tok.index == YyToken.PHRENTHESIS_OPEN || tok.index == YyToken.PHRENTHESIS_CLOSE)
                throw new RuntimeException("Unknown rule error");
            result.push(tok);
        }
        return result;
    }

    private List<Stack<YyToken>> stepArray(List<YyToken> yyTokens) throws RuntimeException {
        List<Stack<YyToken>> out = new ArrayList<Stack<YyToken>>();
        Stack<YyToken> result = createItem();
        Stack<YyToken> stack = new Stack<YyToken>();

        YyToken temp = null;

        int currentProcedence = 0;
        int stackProcedence = 0;

        int bracket_open = 0;
        for (int i = yyTokens.size() - 1; i >= 0; i--) {
            YyToken yyToken = (YyToken) yyTokens.get(i);


            if (yyToken.index == YyToken.BRACKET_OPEN) { // '['이면
                bracket_open++;
                while (stack.size() > 0 && (temp = (YyToken) stack.pop()).index < 1000) {
                    result.push(temp);
                }
                result.push(yyToken);
                result.push(yyTokens.get(i - 1));
                i--;
            } else if (yyToken.index == YyToken.BRACKET_CLOSE) { // ']'이면
                bracket_open--;
                result.push(yyToken);
                stack.push(yyToken);
            } else if (yyToken.index == YyToken.PHRENTHESIS_CLOSE) { // ')'이면
                stack.push(yyToken);
            } else if (yyToken.index == YyToken.PHRENTHESIS_OPEN) { // '('이면
                while ((temp = (YyToken) stack.pop()).index != YyToken.PHRENTHESIS_CLOSE) {
                    result.push(temp);
                }
            } else if (yyToken.index == YyToken.COMMA) { // ','이면
                if (stack.size() == 0) {
                    out.add(result);
                    result = createItem();
                } else {
                    while (stack.size() > 0 && (temp = (YyToken) stack.pop()).index < 1000) {
                        result.push(temp);
                    }
                    if (bracket_open == 0) {
                        out.add(result);
                        result = createItem();
                    } else {
                        result.push(yyToken);
                        stack.push(yyToken);
                    }
                }
            } else if (yyToken.index > 100) { // Operation이 아니면
                result.push(yyToken);
            } else { // 여기는 반드시 Operation만이 올수 있다.

                if (stack.empty()) {
                    stack.push(yyToken);
                } else {
                    currentProcedence = precedence(yyToken);
                    do {
                        temp = (YyToken) stack.pop();
                        stackProcedence = precedence(temp);
                        if (currentProcedence <= stackProcedence) {
                            result.push(temp);
                            if (stack.empty()) {
                                stack.push(yyToken);
                                break;
                            }
                        } else {
                            stack.push(temp);
                            stack.push(yyToken);
                        }
                    } while (currentProcedence <= stackProcedence);
                } // end of if
            } // end of if
        } // end of while

        while (!stack.empty()) {
            YyToken tok = (YyToken) stack.pop();
            if (tok.index == YyToken.PHRENTHESIS_OPEN || tok.index == YyToken.PHRENTHESIS_CLOSE)
                throw new RuntimeException("Unknown rule error");
            result.push(tok);
        }
        if (result.size() > 0) {
            out.add(result);
        }

        List<Stack<YyToken>> out2 = new ArrayList<Stack<YyToken>>();
        for (int i = out.size() - 1; i >= 0; i--) {
            Stack<YyToken> item = out.get(i);
            out2.add(item);
        }
        return out2;
    }

    protected Stack<YyToken> createItem() {
        return new Stack<YyToken>(1, 3);
    }


    private int precedence(YyToken yyToken) {
        if (yyToken.index < 100) {
            return 10 - yyToken.index / 10;
        } else if (yyToken.index > 1000) {
            return 2;
        } else
            return 0;
    }
}
