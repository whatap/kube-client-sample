package whatap.lang.pack;

import whatap.io.DataInputX;
import whatap.io.DataOutputX;
import whatap.util.IntList;

public class HitViewRec {

	public final static int LENGTH = 240;
	public int[] hit = new int[LENGTH];
	public IntList hitOver;

	public HitViewRec write(DataOutputX o) {
		o.writeByte(0); // version
		if (this.hit == null) {
			o.writeShort(0);
		} else {
			int len = LENGTH - 1;
			for (; len >= 0; len--) {
				if (this.hit[len] != 0)
					break;
			}
			len = len + 1;
			o.writeShort(len);
			for (int i = 0; i < len; i++) {
				o.writeShort(this.hit[i]);
			}
		}
		if (this.hitOver == null || this.hitOver.size() == 0) {
			o.writeShort(0);
		} else {
			int sz = hitOver.size();
			o.writeShort(sz);
			final int hit_over_unit = 4000;
			o.writeByte(hit_over_unit / 1000); // 4초
			for (int i = 0; i < sz; i++) {
				o.writeShort(this.hitOver.get(i) / hit_over_unit);
			}
		}
		return this;
	}

	public HitViewRec read(DataInputX in) {
		this.hit = new int[LENGTH];

		byte ver = in.readByte();

		int len = in.readShort();
		for (int i = 0; i < len; i++) {
			this.hit[i] = in.readShort() & 0xffff;
		}

		int overCnt = in.readShort() & 0xffff;
		if (overCnt > 0) {
			this.hitOver = new IntList(overCnt + 1);
			int hit_over_unit = in.readByte() * 1000;
			for (int i = 0; i < overCnt; i++) {
				int tm = in.readShort() & 0xffff;
				this.hitOver.add(tm * hit_over_unit);
			}
		}
		return this;
	}

	public void add(int time) {
		int x = time / 10000;
		switch (x) {
		case 0:
			synchronized (hit) {
				this.hit[time / 125]++;				
			}
			break;
		case 1:
			synchronized (hit) {
				this.hit[80 + ((time - 10000) / 250)]++;
			}
			break;
		case 2:
		case 3:
			synchronized (hit) {
				this.hit[120 + ((time - 20000) / 500)]++;
			}
			break;
		case 4:
		case 5:
		case 6:
		case 7:
			synchronized (hit) {
				this.hit[160 + ((time - 40000) / 1000)]++;
			}
			break;
		case 8:
		case 9:
		case 10:
		case 11:
		case 12:
		case 13:
		case 14:
		case 15:
			synchronized (hit) {
				this.hit[200 + ((time - 80000) / 2000)]++;
			}
			break;
		default:
			if (this.hitOver == null) {
				this.hitOver = new IntList(31);
			}
			if (this.hitOver.size() < 256) {// 256개만 수집
				synchronized (hit) {
					this.hitOver.add(time);
				}
			}
			break;
		}
	}

	public void add(HitViewRec r) {
		for (int i = 0; i < LENGTH; i++) {
			this.hit[i] += r.hit[i];
		}
		if (r.hitOver != null && r.hitOver.size() > 0) {
			if (this.hitOver == null) {
				this.hitOver = new IntList();
			}
			this.hitOver.addAll(r.hitOver);
		}
	}

	public static int index(int time) {
		int x = time / 10000;
		switch (x) {
		case 0:
			return time / 125;
		case 1:
			return 80 + (time - 10000) / 250;
		case 2:
		case 3:
			return 120 + (time - 20000) / 500;
		case 4:
		case 5:
		case 6:
		case 7:
			return 160 + (time - 40000) / 1000;
		case 8:
		case 9:
		case 10:
		case 11:
		case 12:
		case 13:
		case 14:
		case 15:
			return 200 + (time - 80000) / 2000;
		default:
			return 240;
		}
	}

	public static int time(int inx) {
		if (inx <= 0)
			return 0;
		int p = inx / 40;
		switch (p) {
		case 0:
		case 1:
			return inx * 125;
		case 2:
			return 10000 + (inx - 80) * 250;
		case 3:
			return 20000 + (inx - 120) * 500;
		case 4:
			return 40000 + (inx - 160) * 1000;
		case 5:
			return 80000 + (inx - 200) * 2000;
		default:
			return 160000;
		}

	}

	public int[] getConvMap(int maxTime) {
		maxTime = maxTime / 10000 * 10000;
		int[] out = new int[80];
		switch (maxTime) {
		case 10000:
			return b10(out);
		case 20000:
			return b20(out);
		case 40000:
			return b40(out);
		case 80000:
			return b80(out);
		case 160000:
			return b160(out);
		case 320000:
			return b320(out);
		}
		return b320(out);
	}

	protected int[] b320(int[] out) {
		for (int i = 0; i < 80; i++) {
			out[i / 32] += this.hit[i]; // 0~2.5
		}
		for (int i = 80; i < 120; i++) {
			out[(int) (i / 16f - 2.5)] += this.hit[i]; // 2.5~5
		}
		for (int i = 120; i < 160; i++) {
			out[i / 8 - 10] += this.hit[i]; // 5~10
		}
		for (int i = 160; i < 200; i++) {
			out[i / 4 - 30] += this.hit[i]; // 10~20
		}
		for (int i = 200; i < 240; i++) {
			out[i / 2 - 80] += this.hit[i]; // 20~40
		}
		if (this.hitOver != null) {
			for (int i = 0; i < this.hitOver.size(); i++) {
				int t = this.hitOver.get(i);
				if (t >= 320000) {
					out[79]++;
				} else {
					out[t / 4000]++;
				}
			}
		}
		return out;
	}

	protected int[] b160(int[] out) {
		for (int i = 0; i < 80; i++) {
			out[i / 16] += this.hit[i];
		}
		for (int i = 80; i < 120; i++) {
			out[i / 8 - 5] += this.hit[i];
		}
		for (int i = 120; i < 160; i++) {
			out[i / 4 - 20] += this.hit[i];
		}
		for (int i = 160; i < 200; i++) {
			out[i / 2 - 60] += this.hit[i];
		}
		for (int i = 200, j = 40; i < 240; i++, j++) {
			out[j] = this.hit[i];
		}
		if (this.hitOver != null) {
			out[79] += this.hitOver.size();
		}
		return out;
	}

	protected int[] b80(int[] out) {
		for (int i = 0; i < 80; i++) {
			out[i / 8] += this.hit[i];
		}
		for (int i = 80; i < 120; i++) {
			out[i / 4 - 10] += this.hit[i];
		}
		for (int i = 120; i < 160; i++) {
			out[i / 2 - 40] += this.hit[i];
		}
		for (int i = 160, j = 40; i < 200; i++, j++) {
			out[j] = this.hit[i];
		}

		for (int i = 200; i < LENGTH; i++) {
			out[79] += this.hit[i];
		}
		if (this.hitOver != null) {
			out[79] += this.hitOver.size();
		}
		return out;
	}

	protected int[] b40(int[] out) {
		for (int i = 0; i < 80; i++) {
			out[i / 4] += this.hit[i];
		}
		for (int i = 80; i < 120; i++) {
			out[i / 2 - 20] += this.hit[i];
		}
		for (int i = 120, j = 40; i < 160; i++, j++) {
			out[j] = this.hit[i];
		}
		for (int i = 160; i < LENGTH; i++) {
			out[79] += this.hit[i];
		}
		if (this.hitOver != null) {
			out[79] += this.hitOver.size();
		}
		return out;
	}

	protected int[] b20(int[] out) {
		for (int i = 0; i < 80; i++) {
			out[i / 2] += this.hit[i];
		}
		for (int i = 80, j = 40; i < 120; i++, j++) {
			out[j] = this.hit[i];
		}

		for (int i = 120; i < LENGTH; i++) {
			out[79] += this.hit[i];
		}
		if (this.hitOver != null) {
			out[79] += this.hitOver.size();
		}
		return out;
	}

	protected int[] b10(int[] out) {
		int sx = 0, dx = 80;
		for (int i = sx; i < dx; i++) {
			out[i] = this.hit[i];
		}
		for (int i = dx; i < LENGTH; i++) {
			out[79] += this.hit[i];
		}
		if (this.hitOver != null) {
			out[79] += this.hitOver.size();
		}
		return out;
	}

}
