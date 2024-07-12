/*
 *  @(#) FunctionLoader.java
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

import whatap.util.StringUtil;
import whatap.util.scan.Scanner;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * @author PaulKim
 */
public class FunctionLoader {
	private static HashMap<String, Function> funcTable = new HashMap<String, Function>();

	static {
		String packageName = Scanner.cutOutLast(FunctionLoader.class.getName(), ".");
		Set<String> classes = new Scanner(packageName).process();

		Iterator<String> itr = classes.iterator();
		while (itr.hasNext()) {
			try {
				Class c = Class.forName(itr.next());
				if (Function.class != c && Function.class.isAssignableFrom(c)) {
					funcTable.put(getFunctionName(c), (Function) c.newInstance());
				}
			} catch (Exception e) {
			}
		}
	}

	private static String getFunctionName(Class c) {
		String className = c.getName();
		className = className.substring(className.lastIndexOf(".") + 1);
		if(className.charAt(0)=='_'){
			return className = className.substring(1);
		}
		return StringUtil.lowerFirst(className);
	}

	public static Function getFunction(String name) throws RuntimeException {
		return (Function) funcTable.get(name);
	}

}
