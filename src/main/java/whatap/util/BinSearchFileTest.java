package whatap.util;

import whatap.io.DataInputX;
import whatap.io.DataOutputX;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
/**
 * Created by sjokim on 2015. 12. 29..
 */
public class BinSearchFileTest {
	public static void main(String[] args) throws IOException {
		prepare();
		BinSearchFile<int[], Integer> bsf = new BinSearchFile<int[], Integer>(new File("/tmp/a"), 8) {
			@Override
			protected int[] record(long pos) throws Exception {
				byte[] b = this.read(pos, 8);
				DataInputX in = new DataInputX(b);
				return new int[] { in.readInt(), in.readInt() };
			}
			@Override
			protected Integer key(long pos) throws Exception {
				byte[] b = this.read(pos, 4);
				return DataInputX.toInt(b, 0);
			}
			@Override
			protected int compare(Integer key1, Integer key2) {
				return key1.intValue() - key2.intValue();
			}
		};
		int[] b = bsf.findLessEqual(1000);
		//int[] b = bsf.findLessEqual(-1);
		System.out.println(b.length);
		for (int i = 0; i < 10 && b != null; i++) {
			System.out.println(b[0] + "=>" + b[1]);
			b = bsf.readNext();
		}
	}
	private static void prepare() throws IOException {
		File f = new File("/tmp/a");
		f.delete();
		RandomAccessFile raf = new RandomAccessFile("/tmp/a", "rw");
		for (int i = 0; i < 100; i++) {
			DataOutputX out = new DataOutputX();
			out.writeInt(i * 10);
			out.writeInt(i);
			raf.write(out.toByteArray());
		}
		raf.close();
		System.out.println("create file = " + f.length());
	}
}
