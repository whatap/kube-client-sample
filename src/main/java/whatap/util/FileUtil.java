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

import whatap.io.DataInputX;
import whatap.io.DataOutputX;
import whatap.lang.H1;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.io.Writer;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.util.Properties;
import java.util.jar.JarFile;

public class FileUtil {
	public static DatagramSocket close(DatagramSocket udp) {
		try {
			if (udp != null) {
				udp.close();
			}
		} catch (Throwable e) {
		}
		return null;
	}

	public static InputStream close(InputStream in) {
		try {
			if (in != null) {
				in.close();
			}
		} catch (Throwable e) {
		}
		return null;
	}

	public static OutputStream close(OutputStream out) {
		try {
			if (out != null) {
				out.close();
			}
		} catch (Throwable e) {
		}
		return null;
	}

	public static Reader close(Reader in) {
		try {
			if (in != null) {
				in.close();
			}
		} catch (Throwable e) {
		}
		return null;
	}

	public static Writer close(Writer out) {
		try {
			if (out != null) {
				out.close();
			}
		} catch (Throwable e) {
		}
		return null;
	}

	public static byte[] readAll(InputStream fin) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] buff = new byte[4096];
		int n = fin.read(buff);
		while (n >= 0) {
			out.write(buff, 0, n);
			n = fin.read(buff);
		}
		return out.toByteArray();
	}

	public static IClose close(IClose object) {
		try {
			if (object != null) {
				object.close();
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}

	public static RandomAccessFile close(RandomAccessFile raf) {
		try {
			if (raf != null) {
				raf.close();
			}
		} catch (Throwable e) {
		}
		return null;
	}

	public static Socket close(Socket socket) {
		try {
			if (socket != null) {
				socket.close();
			}
		} catch (Throwable e) {
		}
		return null;
	}

	public static ServerSocket close(ServerSocket socket) {
		try {
			if (socket != null) {
				socket.close();
			}
		} catch (Throwable e) {
		}
		return null;
	}

	public static int save(String file, byte[] b) {
		return save(new File(file), b);
	}

	public static int save(File file, byte[] byteArray) {
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(file);
			out.write(byteArray);
		} catch (Exception e) {
			System.out.println(e);
			return -1;
		}
		close(out);
		return 0;
	}

	public static byte[] readAll(File file) {
		FileInputStream in = null;
		try {
			in = new FileInputStream(file);
			return readAll(in);
		} catch (Exception e) {
		} finally {
			close(in);
		}
		return null;
	}

	public static void copy(File src, File dest) {
		try {
			copy(src, dest, true);
		} catch (Exception e) {
		}
	}

	public static boolean copy(File src, File dest, boolean overwrite) throws IOException {
		if (!src.isFile() || !src.exists())
			return false;
		if (dest.exists()) {
			if (dest.isDirectory()) // Directory이면 src파일명을 사용한다.
				dest = new File(dest, src.getName());
			else if (dest.isFile()) {
				if (!overwrite)
					throw new IOException(dest.getAbsolutePath() + "' already exists!");
			} else
				throw new IOException("Invalid  file '" + dest.getAbsolutePath() + "'");
		}
		File destDir = dest.getParentFile();
		if (destDir!=null && !destDir.exists())
			if (!destDir.mkdirs())
				throw new IOException("Failed to create " + destDir.getAbsolutePath());
		long fileSize = src.length();
		if (fileSize > 20 * 1024 * 1024) {
			FileInputStream in = null;
			FileOutputStream out = null;
			try {
				in = new FileInputStream(src);
				out = new FileOutputStream(dest);
				int done = 0;
				int buffLen = 32768;
				byte buf[] = new byte[buffLen];
				while ((done = in.read(buf, 0, buffLen)) >= 0) {
					if (done == 0)
						Thread.yield();
					else
						out.write(buf, 0, done);
				}
			} finally {
				close(in);
				close(out);
			}
		} else {
			FileInputStream in = null;
			FileOutputStream out = null;
			FileChannel fin = null;
			FileChannel fout = null;
			try {
				in = new FileInputStream(src);
				out = new FileOutputStream(dest);
				fin = in.getChannel();
				fout = out.getChannel();
				long position = 0;
				long done = 0;
				long count = Math.min(65536, fileSize);
				do {
					done = fin.transferTo(position, count, fout);
					position += done;
					fileSize -= done;
				} while (fileSize > 0);
			} finally {
				close(fin);
				close(fout);
				close(in);
				close(out);
			}
		}
		return true;
	}

	public static FileChannel close(FileChannel fc) {
		if (fc != null) {
			try {
				fc.close();
			} catch (IOException e) {
			}
		}
		return null;
	}

	// public static void chmod777(File file) {
	// try {
	// file.setReadable(true, false);
	// file.setWritable(true, false);
	// file.setExecutable(true, false);
	// } catch (Throwable th) {}
	// }
	public static void close(DataInputX in) {
		try {
			if (in != null)
				in.close();
		} catch (Exception e) {
		}
	}

	public static void close(DataOutputX out) {
		try {
			if (out != null)
				out.close();
		} catch (Exception e) {
		}
	}

	public static String load(File file, String enc) {
		if (file == null || file.canRead() == false)
			return null;
		BufferedInputStream in = null;
		try {
			in = new BufferedInputStream(new FileInputStream(file));
			return new String(readAll(in), enc);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			close(in);
		}
		return null;
	}

	public static void append(String file, String line) {
		PrintWriter out = null;
		try {
			out = new PrintWriter(new FileWriter(file, true));
			out.println(line);
		} catch (Exception e) {
		}
		close(out);
	}

	public static boolean mkdirs(String path) {
		File f = new File(path);
		if (f.exists() == false)
			return f.mkdirs();
		else
			return true;
	}

	public static Properties readProperties(File f) {
		BufferedInputStream reader = null;
		Properties p = new Properties();
		try {
			reader = new BufferedInputStream(new FileInputStream(f));
			p.load(reader);
		} catch (Exception e) {
		} finally {
			close(reader);
		}
		return p;
	}

	public static void writeProperties(File f, Properties p) {
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(f);
			p.list(pw);
		} catch (Exception e) {
		} finally {
			close(pw);
		}
	}


	public static void close(JarFile jarfile) {
		try {
			if (jarfile != null) {
				jarfile.close();
			}
		} catch (Throwable e) {
		}
	}

	public static void delete(String home, String prefix, String postfix) {
		File[] files = new File(home).listFiles();
		for (int i = 0; i < files.length; i++) {
			if (files[i].isFile()) {
				String nm = files[i].getName();
				if (nm.startsWith(prefix) && nm.endsWith(postfix)) {
					files[i].delete();
				}
			}
		}
	}

	public static void createNew(File file, long size) {
		BufferedOutputStream w = null;
		try {
			w = new BufferedOutputStream(new FileOutputStream(file));
			for (int i = 0; i < size; i++) {
				w.write(0);
			}
		} catch (Exception e) {

		} finally {
			close(w);
		}
	}

	public static int getCrc(File f) {
		BufferedInputStream in = null;
		try {
			in = new BufferedInputStream(new FileInputStream(f));
			return HashUtil.hash(in, (int) f.length());
		} catch (Exception e) {
		} finally {
			close(in);
		}
		return 0;
	}
	
	public static void readLines(Path f, H1<String> h1) throws Exception {
		BufferedReader bf = null;
		try {
			bf = new BufferedReader(new FileReader(f.toString()));
			String line = null;
			while ((line = bf.readLine()) != null) {
				h1.process(line);
			}
		} finally {
			close(bf);
		}
	}
	public static void scanLines(File f, H1<String> h1) {
		BufferedReader bf = null;
		try {
			bf = new BufferedReader(new FileReader(f));
			String line = null;
			while ((line = bf.readLine()) != null) {
				h1.process(line);
			}
		} catch (Throwable t) {
			//gnore t
		} finally {
			close(bf);
		}
	}
}
