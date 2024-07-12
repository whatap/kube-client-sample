package whatap.util;
import java.io.File;
public class Comments {
	public static void main(String[] args) throws Exception {
		File root = new File("/Users/paul/GitHub/apm/io.whatap/whatap.agent/src/main/java");
		process(root);
	}
	private static void process(File root) throws Exception {
		File[] files = root.listFiles();
		for (int i = 0; i < files.length; i++) {
			if(files[i].getName().startsWith("."))
				continue;
			if (files[i].isDirectory()) {
				process(files[i]);
			} else if (files[i].getName().endsWith(".java")) {
				processText(files[i]);
			}
		}
	}
	private static void processText(File file) throws Exception {
		System.out.println(file);
		byte[] b = FileUtil.readAll(file);
		String txt = new String(b, "UTF-8");
		String[] lines = StringUtil.split(txt, '\n');
		StringBuffer out = new StringBuffer();
		for (int i = 0; i < lines.length; i++) {
			if (lines[i].trim().equals("*")) {
				if (lines[i + 1].trim().equals("*") == false) {
					out.append(lines[i]).append("\n");
				}
			} else {
				out.append(lines[i]).append("\n");
			}
			if (lines[i].indexOf("Copyright 2015 the original author or authors") >= 0) {
				if (lines[i + 1].indexOf("scouter-project") < 0) {
					out.append(" *  @https://github.com/scouter-project/scouter\n");
				}
			}
		}
		FileUtil.save(file, out.toString().getBytes("UTF-8"));
	}
}
