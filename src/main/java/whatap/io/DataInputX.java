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
package whatap.io;

import whatap.lang.pack.Pack;
import whatap.lang.pack.PackEnum;
import whatap.lang.pack.ParamPack;
import whatap.lang.step.Step;
import whatap.lang.step.StepEnum;
import whatap.lang.value.Value;
import whatap.lang.value.ValueEnum;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class DataInputX {
	private int offset;
	private DataInput inner;
	private DataInputStream din;

	public DataInputX(byte[] buff) {
		this(new ByteArrayInputStream(buff));
	}

	public DataInputX(byte[] buff, int offset) {
		this(new ByteArrayInputStream(buff, offset, buff.length - offset));
	}

	public DataInputX(ByteArrayInputStream in) {
		this(new DataInputStream(in));
	}

	public DataInputX(BufferedInputStream in) {
		this.din = new DataInputStream(in);
		this.inner = this.din;
	}

	public DataInputX(DataInputStream in) {
		this.inner = in;
		this.din = in;
	}

	public DataInputX(RandomAccessFile in) {
		this.inner = in;
	}

	public byte[] readIntBytes() {
		int len = readInt();
		return read(len);
	}

	// 프로토콜이깨졌을 경우를 대비하여 ..
	public byte[] readIntBytes(int max) {
		int len = readInt();
		if (len < 0 || len > max)
			throw new RuntimeException("read byte is overflowed max:" + max + " len:" + len);
		return read(len);
	}

	public byte[] read(int len) {
		offset += len;
		byte[] buff = new byte[len];
		try {
			this.inner.readFully(buff);
		} catch (IOException e) {
			throw new DataIOException(e);
		}
		return buff;
	}

	public byte[] readShortBytes() {
		int len = readUnsignedShort();
		offset += len;
		byte[] buff = new byte[len];
		try {
			this.inner.readFully(buff);
		} catch (IOException e) {
			throw new DataIOException(e);
		}
		return buff;
	}

	public byte[] readBlob() {
		int baselen = readUnsignedByte();
		switch (baselen) {
		case 255: {
			int len = readUnsignedShort();
			byte[] buffer = read(len);
			return buffer;
		}
		case 254: {
			int len = this.readInt();
			byte[] buffer = read(len);
			return buffer;
		}
		case 0: {
			return new byte[0];
		}
		default:
			byte[] buffer = read(baselen);
			return buffer;
		}
	}

	public int readInt3() {
		byte[] readBuffer = read(3);
		return toInt3(readBuffer, 0);
	}

	public long readLong5() {
		byte[] readBuffer = read(5);
		return toLong5(readBuffer, 0);
	}
	public long readLong6() {
		byte[] readBuffer = read(6);
		return toLong5(readBuffer, 0);
	}

	public long readDecimal() {
		byte len = readByte();
		switch (len) {
		case 0:
			return 0;
		case 1:
			return readByte();
		case 2:
			return readShort();
		case 3:
			return readInt3();
		case 4:
			return readInt();
		case 5:
			return readLong5();
		case 8:
			return readLong();
		default:
			return readLong();
		}
	}
	public long readDecimal(byte len) {
		switch (len) {
		case 0:
			return 0;
		case 1:
			return readByte();
		case 2:
			return readShort();
		case 3:
			return readInt3();
		case 4:
			return readInt();
		case 5:
			return readLong5();
		case 8:
			return readLong();
		default:
			return readLong();
		}
	}
	final private static String empty = new String("");

	public String readText() {
		byte[] buffer = readBlob();
		try {
			if (buffer.length == 0)
				return empty;
			else
				return new String(buffer, "UTF8");
		} catch (IOException e) {
			throw new DataIOException(e);
		}
	}
	public String readObfusText() {
		byte[] buffer = readBlob();
		try {
			if(buffer.length==0)
				return "";
			return new String(DataOutputX.obfus(buffer), "UTF8");
		} catch (IOException e) {
			throw new DataIOException(e);
		}
	}
	public static boolean toBoolean(byte[] buf, int pos) {
		return buf[pos] != 0;
	}

	public static short toShort(byte[] buf, int pos) {
		int ch1 = buf[pos] & 0xff;
		int ch2 = buf[pos + 1] & 0xff;
		return (short) ((ch1 << 8) + (ch2 << 0));
	}

	public static int toInt3(byte[] buf, int pos) {
		int ch1 = buf[pos] & 0xff;
		int ch2 = buf[pos + 1] & 0xff;
		int ch3 = buf[pos + 2] & 0xff;

		return ((ch1 << 24) + (ch2 << 16) + (ch3 << 8)) >> 8;
	}

	public static int toInt(byte[] buf, int pos) {
		int ch1 = buf[pos] & 0xff;
		int ch2 = buf[pos + 1] & 0xff;
		int ch3 = buf[pos + 2] & 0xff;
		int ch4 = buf[pos + 3] & 0xff;
		return ((ch1 << 24) + (ch2 << 16) + (ch3 << 8) + (ch4 << 0));
	}

	public static long toLong(byte[] buf, int pos) {
		return (((long) buf[pos] << 56)//
				+ ((long) (buf[pos + 1] & 255) << 48) //
				+ ((long) (buf[pos + 2] & 255) << 40) //
				+ ((long) (buf[pos + 3] & 255) << 32) //
				+ ((long) (buf[pos + 4] & 255) << 24) + ((buf[pos + 5] & 255) << 16) //
				+ ((buf[pos + 6] & 255) << 8) //
				+ ((buf[pos + 7] & 255) << 0));
	}

	public static long toLong5(byte[] buf, int pos) {
		return (((long) buf[pos] << 32) + //
				((long) (buf[pos + 1] & 255) << 24) + //
				((buf[pos + 2] & 255) << 16) + //
				((buf[pos + 3] & 255) << 8) + //
				((buf[pos + 4] & 255) << 0));
	}
	public static long toLong6(byte[] buf, int pos) {
		return (
				((long) buf[pos] << 40) + //
				((long) (buf[pos + 1] & 255) << 32) + //
				((long) (buf[pos + 2] & 255) << 24) + //
				((long) (buf[pos + 3] & 255) << 16) + //
				((long) (buf[pos + 4] & 255) << 8) +//
				((buf[pos + 5] & 255) << 0)				
				);
	}

	public static float toFloat(byte[] buf, int pos) {
		return Float.intBitsToFloat(toInt(buf, pos));
	}

	public static double toDouble(byte[] buf, int pos) {
		return Double.longBitsToDouble(toLong(buf, pos));
	}

	public static byte[] get(byte[] buf, int pos, int length) {
		byte[] out = new byte[length];
		System.arraycopy(buf, pos, out, 0, length);
		return out;
	}

	public int[] readDecimalArray(int[] data) {
		int length = (int) readDecimal();
		data = new int[length];
		for (int i = 0; i < length; i++) {
			data[i] = (int) readDecimal();
		}
		return data;
	}

	public long[] readDecimalArray() {
		int length = (int) readDecimal();
		long[] data = new long[length];
		for (int i = 0; i < length; i++) {
			data[i] = readDecimal();
		}
		return data;
	}

	public long[] readArray() {
		return readArray(new long[0]);
	}

	public long[] readArray(long[] data) {
		int length = readShort();
		data = new long[length];
		for (int i = 0; i < length; i++) {
			data[i] = readLong();
		}
		return data;
	}

	public int[] readArray(int[] data) {
		int length = readShort();
		data = new int[length];
		for (int i = 0; i < length; i++) {
			data[i] = readInt();
		}
		return data;
	}

	public float[] readArray(float[] data) {
		int length = readShort();
		data = new float[length];
		for (int i = 0; i < length; i++) {
			data[i] = readFloat();
		}
		return data;
	}
	
	public double[] readArray(double[] data) {
		int length = readShort();
		data = new double[length];
		for (int i = 0; i < length; i++) {
			data[i] = readDouble();
		}
		return data;
	}

	public Value readValue() {
		this.offset++;
		try {
			byte type = this.inner.readByte();
			return ValueEnum.create(type).read(this);
		} catch (IOException e) {
			throw new DataIOException(e);
		}
	}

	public Step readStep() {
		this.offset++;
		try {
			byte type = this.inner.readByte();
			return StepEnum.create(type).read(this);
		} catch (IOException e) {
			throw new DataIOException(e);
		}
	}

	public Pack readPack() {
		this.offset++;
		try {
			short type = this.inner.readShort();
			return PackEnum.create(type).read(this);
		} catch (IOException e) {
			throw new DataIOException(e);
		}
	}

	public ParamPack readMapPack() {
		return (ParamPack) readPack();
	}

	public void readFully(byte[] b) {
		this.offset += b.length;
		try {
			this.inner.readFully(b);
		} catch (IOException e) {
			throw new DataIOException(e);
		}
	}

	public void readFully(byte[] b, int off, int len) {
		this.offset += len;
		try {
			this.inner.readFully(b, off, len);
		} catch (IOException e) {
			throw new DataIOException(e);
		}
	}

	public int skipBytes(int n) {
		this.offset += n;
		try {
			return this.inner.skipBytes(n);
		} catch (IOException e) {
			throw new DataIOException(e);
		}
	}

	public boolean readBoolean() {
		this.offset += 1;
		try {
			return this.inner.readBoolean();
		} catch (IOException e) {
			throw new DataIOException(e);
		}
	}

	public byte readByte() {
		this.offset += 1;
		try {
			return this.inner.readByte();
		} catch (IOException e) {
			throw new DataIOException(e);
		}
	}

	public int readUnsignedByte() {
		this.offset += 1;
		try {
			return this.inner.readUnsignedByte();
		} catch (IOException e) {
			throw new DataIOException(e);
		}
	}

	public short readShort() {
		this.offset += 2;
		try {
			return this.inner.readShort();
		} catch (IOException e) {
			throw new DataIOException(e);
		}
	}

	public int readUnsignedShort() {
		this.offset += 2;
		try {
			return this.inner.readUnsignedShort();
		} catch (IOException e) {
			throw new DataIOException(e);
		}
	}

	public char readChar() {
		this.offset += 2;
		try {
			return this.inner.readChar();
		} catch (IOException e) {
			throw new DataIOException(e);
		}
	}

	public int readInt() {
		this.offset += 4;
		try {
			return this.inner.readInt();
		} catch (IOException e) {
			throw new DataIOException(e);
		}
	}

	public long readLong() {
		this.offset += 8;
		try {
			return this.inner.readLong();
		} catch (IOException e) {
			throw new DataIOException(e);
		}
	}

	public float readFloat() {
		this.offset += 4;
		try {
			return this.inner.readFloat();
		} catch (IOException e) {
			throw new DataIOException(e);
		}
	}
	public float readFloatSafe() {
		this.offset += 4;
		try {
			float v= this.inner.readFloat();
			if(Float.isInfinite(v) || Float.isNaN(v))
				return 0f;
			return v;
		} catch (IOException e) {
			throw new DataIOException(e);
		}
	}
	public double readDouble() {
		this.offset += 8;
		try {
			return this.inner.readDouble();
		} catch (IOException e) {
			throw new DataIOException(e);
		}
	}
	public double readDoubleSafe() {
		this.offset += 8;
		try {
			double v= this.inner.readDouble();
			if(Double.isInfinite(v) || Double.isNaN(v))
				return 0.0;
			return v;
		} catch (IOException e) {
			throw new DataIOException(e);
		}
	}
	public int available() {
		try {
			return this.din == null ? 0 : this.din.available();
		} catch (IOException e) {
			throw new DataIOException(e);
		}
	}

	public void close() {
		try {
			if (this.inner instanceof RandomAccessFile) {
				((RandomAccessFile) this.inner).close();
			} else if (this.inner instanceof InputStream) {
				((InputStream) this.inner).close();
			}
		} catch (IOException e) {
			throw new DataIOException(e);
		}
	}

	public int getOffset() {
		return this.offset;
	}

	public static byte[] read(FileChannel channel, int len) {
		byte[] buf = new byte[len];
		ByteBuffer dst = ByteBuffer.wrap(buf);
		try {
			channel.read(dst);
		} catch (IOException e) {
			throw new DataIOException(e);
		}
		return buf;
	}

}
