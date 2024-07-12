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

import whatap.util.StringKeyLinkedMap.StringKeyLinkedEntry;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

public class JarUtil {
	public static void print() throws IOException {
		InputStream manifestStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("META-INF/MANIFEST.MF");
		try {
			Manifest manifest = new Manifest(manifestStream);
			Attributes attributes = manifest.getMainAttributes();
			String impVersion = attributes.getValue("Implementation-Version");
			System.out.println(attributes);
		} catch (Throwable ex) {
		}
	}

	public static File getToolsFile() throws Exception {
		String java_home = System.getProperty("java.home");
		File tools = new File(java_home, "../lib/tools.jar");
		if (tools.canRead() == false) {
			java_home = System.getenv("JAVA_HOME");
			if (java_home == null) {
				throw new Exception("The JAVA_HOME environment variable is not defined correctly");
			}
			tools = new File(java_home, "lib/tools.jar");
			if (tools.canRead() == false) {
				throw new Exception("The JAVA_HOME environment variable is not defined correctly");
			}
		}
		return tools;
	}

	public static String getThisJarName(Class this0) {
		// URL url;
		if (this0 == null) {
			this0 = JarUtil.class;
		}
		return getJarFileName(this0);
	}

	public static String getJarLocation(Class class1) {
		try {
			String path = getJarFileName(class1);
			if (System.getProperty("os.name").contains("Windows"))
				path = path.substring(0, path.lastIndexOf('\\'));
			else
				path = path.substring(0, path.lastIndexOf('/'));
			return path;
		} catch (Throwable e) {
		}
		return null;
	}

	public static String getJarFileName(Class class1) {
		//아래 방법은 jar가 boot클래스 패스에 걸린 경우에는 찾지 못하는 현상이 있음..
		//return new File(MyClass.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
		boolean isWindows = false;

		if (System.getProperty("os.name").contains("Windows"))
			isWindows = true;

		try {
			String path = "" + class1.getResource("/" + class1.getName().replace('.', '/') + ".class");
			if (path.indexOf("!") < 0)
				return null;
			path = path.substring(isWindows ? "jar:file:/".length() : "jar:file:".length(), path.indexOf("!"));
			path = URLDecoder.decode(path, "UTF-8");
			return isWindows ? path.replace('/', '\\') : path.replace('\\', '/');
		} catch (Throwable e) {
		}
		return null;
	}

	public static String getSpringJarName(Class class1) {
		//아래 방법은 jar가 boot클래스 패스에 걸린 경우에는 찾지 못하는 현상이 있음..
		//return new File(MyClass.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());

		try {
			String path = "" + class1.getResource("/" + class1.getName().replace('.', '/') + ".class");
			int x = path.lastIndexOf("!");
			if(x <0)
				return null;
			path = path.substring("jar:file:".length(), x);
			x = path.indexOf("!");
			if (x > 0) {
				path = path.substring(x + 1);
			}
			path = URLDecoder.decode(path, "UTF-8");
			return path.replace('\\', '/');
		} catch (Throwable e) {
		}
		return null;
	}
	public static String getJarVersion(File file) {

		Attributes attr = null;
		JarFile jarfile = null;
		try {
			jarfile = new JarFile(file);
			attr = jarfile.getManifest().getMainAttributes();
		} catch (Throwable e) {
		} finally {
			FileUtil.close(jarfile);
		}

		if (attr != null) {
			String ver = attr.getValue("Implementation-Version");
			if (ver != null)
				return ver;
			ver = attr.getValue("Bundle-Version");
			if (ver != null)
				return ver;
		}

		String fileName = file.getName().substring(0, file.getName().lastIndexOf("."));
		if (fileName.contains(".")) {
			String mjrVer = fileName.substring(0, fileName.indexOf("."));
			int delimiter = mjrVer.lastIndexOf("-");
			if (mjrVer.indexOf("_") > delimiter)
				delimiter = mjrVer.indexOf("_");
			mjrVer = mjrVer.substring(delimiter + 1, fileName.indexOf("."));
			String minorVersion = fileName.substring(fileName.indexOf("."));
			return mjrVer + minorVersion;
		}
		return null;
	}

	public static String getJarFileNameVersion(String fileName) {
		int lastDot = fileName.lastIndexOf('.');
		if (lastDot >= 0) {
			int snx = fileName.lastIndexOf("-");
			if (snx < lastDot && snx > 0) {
				return fileName.substring(snx + 1, lastDot);
			}
		}
		return null;
	}

	public static byte[] makeJarFile(StringKeyLinkedMap<byte[]> classes) throws IOException {
		return makeJarFile(classes, null);
	}

	public static byte[] makeJarFile(StringKeyLinkedMap<byte[]> classes, Manifest manifest) throws IOException {
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		if (manifest == null) {
			manifest = new Manifest();
		}
		JarOutputStream jarOut = new JarOutputStream(bout, manifest);
		writeJarStream(classes, jarOut);

		return bout.toByteArray();
	}

	private static void writeJarStream(StringKeyLinkedMap<byte[]> classes, JarOutputStream jarOut) throws IOException {
		StringKeyLinkedMap<byte[]> resources = new StringKeyLinkedMap<byte[]>();
		Enumeration<StringKeyLinkedEntry<byte[]>> en = classes.entries();
		while (en.hasMoreElements()) {
			StringKeyLinkedEntry<byte[]> entry = en.nextElement();
			resources.put(entry.getKey().replace('.', '/') + ".class", entry.getValue());
		}
		try {
			addEntries(jarOut, resources);
		} finally {
			jarOut.close();
		}
	}

	public static void addEntries(JarOutputStream jarOut, StringKeyLinkedMap<byte[]> files) throws IOException {
		Enumeration<StringKeyLinkedEntry<byte[]>> en = files.entries();
		while (en.hasMoreElements()) {
			StringKeyLinkedEntry<byte[]> entry = en.nextElement();
			JarEntry jarEntry = new JarEntry(entry.getKey());
			jarOut.putNextEntry(jarEntry);
			jarOut.write(entry.getValue());
			jarOut.closeEntry();
		}
	}

	public static List<File> getLastJarFiles(String path, String prefix, String version, String ext) {
		StringKeyLinkedMap<Pair<String, File>> jarfiles = new StringKeyLinkedMap<Pair<String, File>>();
		File[] files = new File(path).listFiles();
		for (int i = 0; i < files.length; i++) {
			String lowercaseFileName = files[i].getName().toLowerCase();
			if (StringUtil.isNotEmpty(prefix) && lowercaseFileName.startsWith(prefix) == false)
				continue;
			if (StringUtil.isNotEmpty(version) && lowercaseFileName.indexOf(version) < 0)
				continue;
			if (StringUtil.isNotEmpty(ext) && lowercaseFileName.endsWith(ext) == false)
				continue;

			String name = files[i].getName();
			Pair<String, String> parsed = parse(name);
			Pair<String, File> vnf = jarfiles.get(parsed.getLeft());
			if (vnf == null) {
				vnf = new Pair<String, File>(parsed.getRight(), files[i]);
			} else {
				if (vnf.getLeft().compareTo(parsed.getRight()) < 0) {
					vnf = new Pair<String, File>(parsed.getRight(), files[i]);
				}
			}
			jarfiles.put(parsed.getLeft(), vnf);
		}
		ArrayList<File> out = new ArrayList<File>();
		Enumeration<Pair<String, File>> en = jarfiles.values();
		while (en.hasMoreElements()) {
			Pair<String, File> p = en.nextElement();
			out.add(p.getRight());
		}
		return out;
	}

	public static List<File> getAllFiles(String path, String prefix, StringSet extSet) {
		StringKeyLinkedMap<Pair<String, File>> jarfiles = new StringKeyLinkedMap<Pair<String, File>>();
		ArrayList<File> out = new ArrayList<File>();
		File[] files = new File(path).listFiles();
		if (files == null)
			return out;
		for (int i = 0; i < files.length; i++) {
			if (files[i].isDirectory())
				continue;

			String lowercaseFileName = files[i].getName().toLowerCase();
			if (StringUtil.isNotEmpty(prefix) && lowercaseFileName.startsWith(prefix) == false)
				continue;
			String ext = StringUtil.cutLastString(lowercaseFileName, '.');
			if (extSet != null && extSet.size() > 0 && extSet.hasKey(ext) == false)
				continue;

			out.add(files[i]);
		}
		return out;
	}

	private static Pair<String, String> parse(String name) {
		int x = name.lastIndexOf("-");
		if (x < 0) {
			x = name.lastIndexOf("_");
		}
		int y = name.lastIndexOf(".");
		if (x < 0 || y < 0 || x > y)
			return new Pair<String, String>(name, "version");
		String key = name.substring(0, x);
		String version = name.substring(x + 1, y);
		return new Pair<String, String>(key, version);
	}


	public static File createJarFile(String prefix, Map<String, byte[]> classes) throws IOException {
		return createJarFile(prefix, classes, null);
	}

	public static File createJarFile(String prefix, Map<String, byte[]> classes, Manifest manifest) throws IOException {
		File file = File.createTempFile(prefix, ".jar");
		file.deleteOnExit();
		if (manifest == null) {
			manifest = new Manifest();
		}
		JarOutputStream outStream = new JarOutputStream(new FileOutputStream(file), manifest);
		writeFilesToJarStream(classes, file, outStream);
		return file;
	}

	private static void writeFilesToJarStream(Map<String, byte[]> classes, File file, JarOutputStream outStream)
			throws IOException {
		Map<String, byte[]> resources = new HashMap();
		for (Map.Entry<String, byte[]> entry : classes.entrySet()) {
			resources.put(entry.getKey().replace('.', '/') + ".class", entry.getValue());
		}
		try {
			addJarEntries(outStream, resources);
		} finally {
			outStream.close();
		}
	}

	public static void addJarEntries(JarOutputStream jarStream, Map<String, byte[]> files) throws IOException {
		for (Map.Entry<String, byte[]> entry : files.entrySet()) {
			JarEntry jarEntry = new JarEntry(entry.getKey());

			jarStream.putNextEntry(jarEntry);
			jarStream.write(entry.getValue());
			jarStream.closeEntry();
		}
	}

}
