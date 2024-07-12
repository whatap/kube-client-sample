/*
 *  Copyright 2015 the original author or authors. 
 *  @https://github.com/scouter-project/scouter
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
 */
/**
 * bugfix :  parse error for '*' by 2016.08.12 Paul S.J.Kim
 */
package whatap.util;

import java.io.File;

public class EscapeLiteralSQL {
	final static int STAT_NORMAL=1;
	final static int STAT_COMMENT=2;
	final static int STAT_ALPABET=3;
	final static int STAT_NUMBER=4;
	final static int STAT_QUOTATION=5;
	final static int STAT_COLON=6;

	private String substitute = "#";
	private String substitute_num = "#";
	private boolean substitute_str_mode = false;
	private char[] chars;
	private int pos;
	private int length;
	final StringBuffer parsedSql;
	final StringBuffer param;
	private int status;
	public char sqlType;

	public EscapeLiteralSQL(String sql) {
		this.chars = sql.toCharArray();
		this.length = this.chars.length;
		this.parsedSql = new StringBuffer(this.length + 10);
		this.param = new StringBuffer();
	}

	public EscapeLiteralSQL(String sql, String subs) {
		this(sql);
		this.setSubstitute(subs);
	}
	
	public EscapeLiteralSQL setSubstitute(String chr) {
		this.substitute = chr;
		if (this.substitute_str_mode) {
			this.substitute_num = "'" + chr + "'";
		} else {
			this.substitute_num = this.substitute;
		}
		return this;
	}

	public EscapeLiteralSQL setSubstituteStringMode(boolean b) {
		if (this.substitute_str_mode == b)
			return this;
		this.substitute_str_mode = b;
		if (this.substitute_str_mode) {
			this.substitute_num = "'" + this.substitute + "'";
		} else {
			this.substitute_num = this.substitute;
		}
		return this;
	}

	public EscapeLiteralSQL process() {
			status = STAT_NORMAL;
			for (pos = 0; pos < chars.length; pos++) {
				switch (chars[pos]) {
				case '0':
				case '1':
				case '2':
				case '3':
				case '4':
				case '5':
				case '6':
				case '7':
				case '8':
				case '9':
					_number();
					break;
				case ':':
					_colon();
					break;
				case '.':
					_dot();
					break;
				case '-':
					_minus();
					break;
				case '/':
					_slash(); 
					break;
				case '*':
					_astar();
					break;
				case '\'':
					_quotation();
					break;
				default:
					_others();
				}
			}
		
		return this;
	}

	private void _others() {
		// System.out.println("other=>'"+chars[pos]+"' " +status);
		switch (status) {
		case STAT_COMMENT:
			parsedSql.append(chars[pos]);
			break;
		case STAT_ALPABET:
			parsedSql.append(chars[pos]);
			if (isProgLetter(chars[pos]) == false) {
				status = STAT_NORMAL;
			}
			break;
		case STAT_NUMBER:
			parsedSql.append(chars[pos]);
			status = STAT_NORMAL;
			break;
		case STAT_QUOTATION:
			param.append(chars[pos]);
			break;
		default:
			if (isProgLetter(chars[pos])) {
				status = STAT_ALPABET;
				if (this.sqlType == 0) {
					define_crud();
				}
			} else {
				status = STAT_NORMAL;
			}
			parsedSql.append(chars[pos]);
			break;
		}
	}

	public boolean isProgLetter(char ch) {
		return Character.isLetter(ch) || ch == '_' || ch=='$';
	}

	private void define_crud() {
		this.sqlType = Character.toUpperCase(chars[pos]);
		switch (sqlType) {
		case 'S':
		case 'U':
		case 'D':
		case 'I':
			break;
		default:
			this.sqlType = '*';
		}
	}

	private void _colon() {
		switch (status) {
		case STAT_COMMENT:
			parsedSql.append(chars[pos]);
			break;
		case STAT_QUOTATION:
			param.append(chars[pos]);
			break;
		default:
			parsedSql.append(chars[pos]);
			status = STAT_COLON;
			break;
		}
	}

	private void _quotation() {
		switch (status) {
        case STAT_ALPABET:
        case STAT_NUMBER:
		case STAT_NORMAL:
			if (param.length() > 0) {
				param.append(",");
			}
			param.append(chars[pos]);
			status = STAT_QUOTATION;
			break;
		case STAT_COMMENT:
			parsedSql.append(chars[pos]);
			break;
//		case STAT_ALPABET:
//			parsedSql.append(chars[pos]);
//			status = STAT_QUOTATION;
//			break;
//		case STAT_NUMBER:
//			parsedSql.append(chars[pos]);
//			status = STAT_QUOTATION;
//			break;
		case STAT_QUOTATION:
			if (getNext(pos) == '\'') {
				param.append(chars[pos++]);
				param.append(chars[pos]);
			} else {
				param.append("'");
				parsedSql.append('\'').append(substitute).append('\'');
				status = STAT_NORMAL;
			}
			break;
		}
	}

	private void _astar() {
		switch (status) {
		case STAT_COMMENT:
			parsedSql.append(chars[pos]);
			if (getNext(pos) == '/') {
				parsedSql.append('/');
				pos++;
				status = STAT_NORMAL;
			}
			break;
		case STAT_QUOTATION:
			param.append(chars[pos]);
			break;
		default:
			parsedSql.append(chars[pos]);
			status = STAT_NORMAL;
		}
	}

	private void _slash() {
		switch (status) {
		case STAT_COMMENT:
			parsedSql.append(chars[pos]);
			break;
		case STAT_QUOTATION:
			param.append(chars[pos]);
			break;
		default:
			if (getNext(pos) == '*') {
				pos++;
				parsedSql.append("/*");
				status = STAT_COMMENT;
			}else { // "/" 연산자가 출력안되는 버그있음-201809 
				parsedSql.append("/");
				status = STAT_NORMAL;
			}
		}
	}

	private void _minus() {
		switch (status) {
		case STAT_COMMENT:
			parsedSql.append(chars[pos]);
			break;
		case STAT_QUOTATION:
			param.append(chars[pos]);
			break;
		default:
			if (getNext(pos) == '-') {
				parsedSql.append(chars[pos]);
				while (chars[pos] != '\n') {
					pos++;
					if (pos < length) {
						parsedSql.append(chars[pos]);
					} else {
						break;
					}
				}
			} else {
				parsedSql.append(chars[pos]);
			}
			status = STAT_NORMAL;
		}
	}

	private void _dot() {
		switch (status) {
		case STAT_NORMAL:
			parsedSql.append(chars[pos]);
			break;
		case STAT_COMMENT:
			parsedSql.append(chars[pos]);
			break;
		case STAT_ALPABET:
			parsedSql.append(chars[pos]);
			status = STAT_NORMAL;
			break;
		case STAT_NUMBER:
			param.append(chars[pos]);
			break;
		case STAT_QUOTATION:
			param.append(chars[pos]);
			break;
		}
	}

	private void _number() {
		switch (status) {
		case STAT_NORMAL:
			if (param.length() > 0) {
				param.append(",");
			}
			param.append(chars[pos]);
			parsedSql.append(substitute_num);
			status = STAT_NUMBER;
			break;
		case STAT_COMMENT:
		case STAT_COLON:
		case STAT_ALPABET:
			parsedSql.append(chars[pos]);
			break;
		case STAT_NUMBER:
		case STAT_QUOTATION:
			param.append(chars[pos]);
			break;
		}
	}

	private char getNext(int x) {
		return x < length - 1 ? chars[x + 1] : 0;
	}

	public static void main(String[] args) throws Exception {
		// String s = new String(FileUtil.readAll(new
		// File("d:/tmp/sample-query2.sql")), "EUC_KR");
		// String s = "CALL USER()";
//		String s = "select declare aa number ; begin aa := 1 ; execute immediate 'update kwlee set b=''111'' where a=:x' using aa ; dbms_lock.sleep (20) ; commit ; end ;'33dddd33'\n\n";// new
		String s =new String(FileUtil.readAll(new File("/Users/paul/tmp1/1.sql")),"UTF8");
		long time =  System.currentTimeMillis();
		EscapeLiteralSQL ec = new EscapeLiteralSQL(s,"#").process();
		long etime =  System.currentTimeMillis();
		// FileUtil.save("d:/tmp/sample-query2.out",
		// ec.parsedSql.toString().getBytes());
		System.out.println("SQL: " + ec.parsedSql + " " + (etime - time) + " ms");
		System.out.println("PARAM: " + ec.param);
		System.out.println("type: " + ec.sqlType);

		File o = new File("/Users/paul/tmp1/1-1.sql");
		FileUtil.save(o, ec.parsedSql.toString().getBytes());
		
	}

	public String getParsedSql() {
		return this.parsedSql.toString();
	}

	public String getParameter() {
		return this.param.toString();
	}
}
