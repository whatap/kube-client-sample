package whatap.lang.pack;

import whatap.io.DataInputX;
import whatap.io.DataOutputX;

import java.util.Arrays;
import java.util.Random;

/**
 * MAP_LENGTH = 100 80초이상은 99번째 인덱스(마지막셀)에 건수를 표현한다.
 */
public class HitMapPack1 extends AbstractPack {
	public final static int LENGTH = 120;

	public int[] hit = new int[LENGTH];
	public int[] error = new int[LENGTH];

	@Override
	public short getPackType() {
		return PackEnum.HITMAP_1;
	}

	@Override
	public String toString() {
		return "HitMap1 [" + super.toString() + ", hit=" + Arrays.toString(hit) + ", error=" + Arrays.toString(error)
				+ "]";
	}

	public void write(DataOutputX o) {
		super.write(o);

		o.writeByte(3);// version
		writeArr(o, this.hit);
		writeArr(o, this.error);

	}

	private static void writeArr(DataOutputX o, int[] arr) {
		if (arr == null) {
			o.writeByte(0);
		} else {
			int len = LENGTH - 1;
			for (; len >= 0; len--) {
				if (arr[len] != 0)
					break;
			}
			len = len + 1;
			o.writeByte((byte) len);
			for (int i = 0; i < len; i++) {
				o.writeShort(arr[i]);
			}
		}
	}

	private static void readArr(DataInputX in, int[] arr) {
		int len = in.readByte() & 0xff;
		for (int i = 0; i < len; i++) {
			arr[i] = in.readShort() & 0xffff;
		}
	}

	private static int[] dummy = new int[LENGTH];

	public Pack read(DataInputX in) {
		super.read(in);
		byte ver = in.readByte();
		switch (ver) {
		case 1:
			for (int i = 0; i < LENGTH; i++) {
				this.hit[i] = in.readShort() & 0xffff;
				this.error[i] = in.readShort() & 0xffff;
			}
			break;
		case 2:
			int len = in.readByte();
			for (int i = 0; i < len; i++) {
				this.hit[i] = in.readShort() & 0xffff;
				this.error[i] = in.readShort() & 0xffff;
			}
			break;
		case 3:
			readArr(in, this.hit);
			readArr(in, this.error);
			break;
		case 4:// 멀티뷰를 위해 기능 추가
			readArr(in, this.hit);
			readArr(in, this.error);

			// this.onExtraHit();
			readArr(in, dummy);
			readArr(in, dummy);
			break;
		default:
			for (int i = 0; i < LENGTH; i++) {
				this.hit[i] = in.readShort() & 0xffff;
				this.error[i] = in.readShort() & 0xffff;
			}
			break;
		}
		return this;
	}

	public void add(int time, boolean isError) {
		int idx = index(time);
		this.hit[idx]++;
		if (isError) {
			this.error[idx]++;
		}
	}

	public void addSync(int time, boolean isError) {
		int idx = index(time);
		if (isError) {
			synchronized (this.hit) {
				this.hit[idx]++;
				this.error[idx]++;
			}
		} else {
			synchronized (this.hit) {
				this.hit[idx]++;
			}
		}
	}

	public static int index(int time) {
		int x = time / 10000;
		switch (x) {
		case 0:
			if (time < 5000)
				return time / 125;
			else
				return 40 + (time - 5000) / 250;
		case 1:
			return 60 + (time - 10000) / 500;
		case 2:
		case 3:
			return 80 + (time - 20000) / 1000;
		case 4:
		case 5:
		case 6:
		case 7:
			return 100 + (time - 40000) / 2000;
		default:
			return 119;
		}
	}

	public static int time(int inx) {
		if (inx <= 40)
			return inx * 125;
		inx = inx - 40;
		if (inx <= 20)
			return 5000 + inx * 250;
		inx = inx - 20;
		if (inx <= 20)
			return 10000 + inx * 500;
		inx = inx - 20;
		if (inx <= 20)
			return 20000 + inx * 1000;
		inx = inx - 20;
		if (inx <= 20)
			return 40000 + inx * 2000;
		return 80000;
	}

	public int getPct(double pct) {
		int tot = this.hitSum();
		if (tot == 0)
			return 0;
		int pctCnt = (int) (tot * pct);
		int cur = 0;
		for (int i = 0; i < LENGTH; i++) {
			int h = this.hit[i];
			if (cur + h >= pctCnt) {
				return time(i);
			}
			cur+=h;
		}
		return time(LENGTH);
	}

	private int hitSum() {
		int s = 0;
		for (int h : this.hit)
			s += h;
		return s;
	}

	public static void main(String[] args) {
		HitMapPack1 p = new HitMapPack1();
		Random r = new Random();
		for(int i = 0 ; i < 1000 ; i++) {
			p.add(r.nextInt(20000), false);
		}
		int pct90 = p.getPct(0.90);
		int pct95 = p.getPct(0.95);
		int pct98 = p.getPct(0.98);
		System.out.println("pct90="+pct90);
		System.out.println("pct95="+pct95);
		System.out.println("pct98="+pct98);
	}
}
