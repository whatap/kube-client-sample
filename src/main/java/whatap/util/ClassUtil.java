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
package whatap.util;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

public class ClassUtil {
	public static void copy(Object src, Object dest) {
		Field[] fields = src.getClass().getFields();
		for (int i = 0; i < fields.length; i++) {
			int mod = fields[i].getModifiers();
			if (Modifier.isFinal(mod) == false) {
				fields[i].setAccessible(true);
				try {
					Object value = fields[i].get(src);
					fields[i].set(dest, value);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static <V> Map<String, V> getPublicFinalNameMap(Class<?> cls, Class v) {

		Map<String, V> map = new HashMap<String, V>();
		Field[] fields = cls.getFields();
		for (int i = 0; i < fields.length; i++) {
			int mod = fields[i].getModifiers();

			if (fields[i].getType().equals(v) == false) {
				continue;
			}
			if (Modifier.isFinal(mod) && Modifier.isStatic(mod) && Modifier.isPublic(mod)) {
				try {
					String name = fields[i].getName();
					Object value = fields[i].get(null);
					map.put(name, (V) value);
				} catch (Exception e) {
				}
			}
		}
		return map;
	}

	public static <V> Map<V, String> getPublicFinalValueMap(Class<?> cls, Class type) {

		Map<V, String> map = new HashMap<V, String>();
		Field[] fields = cls.getFields();
		for (int i = 0; i < fields.length; i++) {
			int mod = fields[i].getModifiers();

			if (fields[i].getType().equals(type) == false) {
				continue;
			}
			if (Modifier.isFinal(mod) && Modifier.isStatic(mod) && Modifier.isPublic(mod)) {
				try {
					String name = fields[i].getName();
					Object value = fields[i].get(null);
					map.put((V) value, name);
				} catch (Exception e) {
				}
			}
		}
		return map;
	}

	public static IntKeyMap<String> getConstantValueMap(Class<?> cls, Class type) {

		IntKeyMap<String> map = new IntKeyMap<String>();
		Field[] fields = cls.getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			int mod = fields[i].getModifiers();

			if (fields[i].getType().equals(type) == false) {
				continue;
			}
			if (Modifier.isFinal(mod) && Modifier.isStatic(mod) && Modifier.isPublic(mod)) {
				try {
					String name = fields[i].getName();
					Object value = fields[i].get(null);
					map.put(CastUtil.cint(value), name);
				} catch (Exception e) {
				}
			}
		}
		return map;
	}

	public static StringIntMap getConstantKeyMap(Class<?> cls, Class type) {

		StringIntMap map = new StringIntMap();
		Field[] fields = cls.getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			int mod = fields[i].getModifiers();

			if (fields[i].getType().equals(type) == false) {
				continue;
			}
			if (Modifier.isFinal(mod) && Modifier.isStatic(mod) && Modifier.isPublic(mod)) {
				try {
					String name = fields[i].getName();
					Object value = fields[i].get(null);
					map.put(name, CastUtil.cint(value));
				} catch (Exception e) {
				}
			}
		}
		return map;
	}

	public static String getClassDescription(Class c1, boolean isAll) {
		int x = c1.getName().lastIndexOf(".");

		StringBuffer sb = new StringBuffer();
		if (x > 0) {
			sb.append("package ").append(c1.getName().substring(0, x)).append(";\n\n");
		}
		int acc = c1.getModifiers();
		mod(sb, acc, c1.isInterface());
		if (c1.isInterface()) {
			sb.append("interface ");
		} else {
			sb.append("class ");
		}

		if (x > 0) {
			sb.append(c1.getName().substring(x + 1));
		} else {
			sb.append(c1.getName());
		}
		if (c1.getSuperclass() != null && c1.getSuperclass() != Object.class) {
			sb.append(" extends ").append(c1.getSuperclass().getName());
		}
		Class[] inf = c1.getInterfaces();
		for (int i = 0; i < inf.length; i++) {
			if (i == 0) {
				sb.append(" implements ");
			}
			if (i > 0)
				sb.append(",");
			sb.append(inf[i].getName());
		}
		sb.append("{\n");
		Field[] f = isAll ? c1.getFields() : c1.getDeclaredFields();
		for (int i = 0; i < f.length; i++) {
			sb.append("\t");
			mod(sb, f[i].getModifiers(), c1.isInterface());
			sb.append(toClassString(f[i].getType())).append(" ");
			sb.append(f[i].getName()).append(";\n");
		}
		Method[] m = isAll ? c1.getMethods() : c1.getDeclaredMethods();
		if (f.length > 0 && m.length > 0) {
			sb.append("\n");
		}
		for (int i = 0; i < m.length; i++) {
			sb.append("\t");
			mod(sb, m[i].getModifiers(), c1.isInterface());
			sb.append(toClassString(m[i].getReturnType())).append(" ");
			sb.append(m[i].getName());
			sb.append("(");
			Class[] pc = m[i].getParameterTypes();
			for (int p = 0; p < pc.length; p++) {
				if (p > 0)
					sb.append(",");
				sb.append(toClassString(pc[p])).append(" a" + p);
			}
			sb.append(")");
			if (Modifier.isAbstract(m[i].getModifiers()) == false) {
				sb.append("{...}\n");
			} else {
				sb.append(";\n");
			}
		}
		sb.append("}");
		return sb.toString();
	}
//
//	private static String toClassString(String name) {
//		if (name.startsWith("java.lang")) {
//			return name.substring("java.lang".length() + 1);
//		}
//		
//		return name;
//	}

	private static void mod(StringBuffer sb, int acc, boolean isInterface) {
		if (Modifier.isAbstract(acc) && isInterface == false) {
			sb.append("abstract ");
		}
		if (Modifier.isProtected(acc)) {
			sb.append("protected ");
		}
		if (Modifier.isPrivate(acc)) {
			sb.append("private ");
		}
		if (Modifier.isPublic(acc)) {
			sb.append("public ");
		}
		if (Modifier.isFinal(acc)) {
			sb.append("final ");
		}
		if (Modifier.isSynchronized(acc)) {
			sb.append("synchronized ");
		}
	}

	public static byte[] getByteCode(Class c) {
		if (c == null)
			return null;
		String clsAsResource = "/" + c.getName().replace('.', '/').concat(".class");
		InputStream in = null;
		try {
			in = c.getResourceAsStream(clsAsResource);
			if (in == null) {
				return null;
			}
			ByteArrayOutputStream out = new ByteArrayOutputStream();

			byte[] buff = new byte[1024];
			int n = 0;
			while ((n = in.read(buff, 0, 1024)) >= 0) {
				out.write(buff, 0, n);
			}
			return out.toByteArray();
		} catch (Exception e) {
		} finally {
			FileUtil.close(in);
		}
		return null;
	}

	private static IntKeyLinkedMap<String> compactNames = new IntKeyLinkedMap<String>().setMax(2000);

	public static String getCompactName(Object o) {
		return getCompactName(o.getClass());
	}

	public static String getCompactName(Class<? extends Object> class1) {
		String compact = compactNames.get(class1.hashCode());
		if (compact != null)
			return compact;

		String[] nameArr = StringUtil.split(class1.getName(), '.');
		switch (nameArr.length) {
		case 0:
			compact = "";
			break;
		case 1:
			compact = nameArr[0];
			break;
		case 2:
			compact = nameArr[0].substring(0, 1) + "." + nameArr[1];
			break;
		default:
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < nameArr.length - 1; i++) {
				sb.append(nameArr[i].substring(0, 1));
				sb.append('.');
			}
			sb.append(nameArr[nameArr.length - 1]);
			compact = sb.toString();
		}
		compactNames.put(class1.hashCode(), compact);
		return compact;
	}

	public static String wrapper(Class c1, String packageName, String postfix) {
		String cr = "\n";
		String tab = "   ";
		String className=c1.getSimpleName()+postfix;
		StringBuffer sb = new StringBuffer();
		sb.append("package ").append(packageName).append(";\n\n");

		sb.append("public class ").append(className);

		if (c1.isInterface() == false) {
			sb.append(" extends ").append(c1.getName());
		} else {
			sb.append(" implements ").append(c1.getName());
		}

		sb.append("{" + cr);
		sb.append(cr + tab);
		sb.append("protected ").append(c1.getName()).append(" inner; ");
		sb.append(cr);
		Method[] m = c1.getMethods();
		sb.append(cr);

		sb.append(tab);
		sb.append("public " + className).append("(").append(c1.getName()).append(" o){" + cr);
		sb.append(tab + tab + "this.inner=o;" + cr);
		sb.append(tab + "}" + cr);

		for (int i = 0; i < m.length; i++) {
			if (c1.isInterface() == false && Modifier.isPublic(m[i].getModifiers()) == false)
				continue;

			sb.append(tab);
			mod(sb, m[i].getModifiers(), c1.isInterface());
			sb.append(toClassString(m[i].getReturnType())).append(" ");
			sb.append(m[i].getName());
			sb.append("(");
			Class[] pc = m[i].getParameterTypes();
			for (int p = 0; p < pc.length; p++) {
				if (p > 0)
					sb.append(",");
				sb.append(toClassString(pc[p])).append(" a" + p);
			}
			sb.append(")");
			Class[] ex = m[i].getExceptionTypes();
			if (ex != null && ex.length > 0) {
				sb.append(" throws ");
				for (int p = 0; p < ex.length; p++) {
					if (p > 0)
						sb.append(",");
					sb.append(toClassString(ex[p]));
				}
			}

			sb.append("{" + cr);
			sb.append(tab + tab);
			if (toClassString(m[i].getReturnType()).equals("void") == false) {
				sb.append("return ");
			}
			sb.append(" this.inner." + m[i].getName() + "(");
			for (int p = 0; p < pc.length; p++) {
				if (p > 0)
					sb.append(",");
				sb.append("a" + p);
			}
			sb.append(");" + cr);
			sb.append(tab + "}" + cr);
		}
		sb.append("}");
		return sb.toString();
	}

	public static String toClassString(Class clazz) {
		if (clazz == null) {
			return null;
		}
		if (clazz.isArray()) {
			return toClassString(clazz.getComponentType()) + "[]";
		}
		if (clazz == boolean.class) {
			return "boolean";
		} else if (clazz == byte.class) {
			return "byte";
		} else if (clazz == short.class) {
			return "short";
		} else if (clazz == int.class) {
			return "int";
		} else if (clazz == long.class) {
			return "long";
		} else if (clazz == float.class) {
			return "float";
		} else if (clazz == double.class) {
			return "double";
		} else if (clazz == char.class) {
			return "char";
		} else {
			String name = clazz.getName();
			if (name.startsWith("java.lang")) {
				return name.substring("java.lang".length() + 1);
			}
			return name;
		}
	}
}
