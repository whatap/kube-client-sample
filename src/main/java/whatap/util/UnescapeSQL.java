/*
 * by kwlee 180404
 * EscapeLiteralSQL 로 분리된 sql 과 param 을 합체.
 */

package whatap.util;

import java.util.StringTokenizer;

public class UnescapeSQL {

	public static String combineSql(String sql, String param) {
		String comSql = "";
		int idxBegin = 0, idxEnd, idx;
		int paramCnt;

		// 우선 param 을 String [] 으로 분리.
		
		if (param == null) return sql;
		
		StringTokenizer st = new StringTokenizer(param, ",");

		int ncnt = st.countTokens();
		
		String [] params = new String[ncnt];
		int i = 0;
		
		while (st.hasMoreTokens()) {
			String token = st.nextToken();
			// quoted string 이 token 으로 나뉘어 있으면 합침.
			if (token.charAt(0) == '\'') {
				while (token.charAt(token.length() - 1) != '\'' && st.hasMoreTokens())
					token += ',' + st.nextToken();
			}
			
			params[i] = token;
			System.out.println("params [ " + i + " ] : " + params[i]);
			i ++;
		}
		
		paramCnt = i;

		// sql 을 읽어 가면서 # 이 나오면 String [] 에서 가져다 붙임.
		i = 0;
		idx = idxBegin;
		while (true) {
			if (paramCnt > i)
				idxEnd = sql.indexOf('#', idx);
			else
				idxEnd = -1;
			if (idxEnd == -1) {
				comSql += sql.substring(idxBegin);
				break;
			}

			// 단어의 뒤에 붙은 # 은 치환하지 않는다. (예. serial#)
			int ch = sql.charAt(idxEnd - 1);
			if (Character.isLetterOrDigit(ch)) {
				idx = idxEnd + 1;
				continue;
			}

			comSql += sql.substring(idxBegin, idxEnd);
			comSql += params[i];
			i ++;
			
			idxBegin = idxEnd + 1;
			idx = idxBegin;
		}
		
		return comSql;
	}
}
