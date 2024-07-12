package whatap.lang.pack;

import whatap.io.DataInputX;
import whatap.io.DataOutputX;
import whatap.util.IntEnumer;
import whatap.util.IntKeyMap;
import whatap.util.IntKeyMap.IntKeyEntry;

import java.util.Enumeration;

public class HitViewPack extends AbstractPack {
	public final static byte ID_ELAPSED = 1;
	public final static byte ID_ERROR = 2;
	public final static byte ID_SQL = 3;
	public final static byte ID_HTTPCALL = 4;

	public IntKeyMap<HitViewRec> map = new IntKeyMap<HitViewRec>(31, 1f);

	@Override
	public short getPackType() {
		return PackEnum.HITVIEW;
	}

	public void write(DataOutputX o) {
		super.write(o);

		o.writeByte(0);// version

		Enumeration<IntKeyEntry<HitViewRec>> en = map.entries();
		while (en.hasMoreElements()) {
			IntKeyEntry<HitViewRec> hr = en.nextElement();
			o.writeBoolean(true);
			o.writeByte(hr.getKey());
			hr.getValue().write(o);
		}
		o.writeBoolean(false);

	}

	public Pack read(DataInputX in) {
		super.read(in);

		byte version = in.readByte();

		while (in.readBoolean()) {
			byte id = in.readByte();
			HitViewRec hr = new HitViewRec().read(in);
			this.map.put(id, hr);
		}
		return this;
	}

	public HitViewRec get(int id) {
		return this.map.get(id);
	}

	public void put(int id, HitViewRec hr) {
		this.map.put(id, hr);
	}

	public void add(HitViewPack p) {
		IntEnumer en = p.map.keys();
		while (en.hasMoreElements()) {
			int id = en.nextInt();
			HitViewRec r = p.get(id);
			HitViewRec old = this.map.get(id);
			if (old == null) {
				old = new HitViewRec();
				this.map.put(id, old);
			}
			old.add(r);
		}
	}
	public HitMapPack1 toHitMapPack() {
		HitMapPack1 p = new HitMapPack1();
		p.pcode = this.pcode;
		p.time = this.time;
		p.oid = this.oid;
		p.okind = this.okind;
		p.onode = this.onode;

		merge(p.hit, this.get(ID_ELAPSED));
		merge(p.error,  this.get(ID_ERROR));
		
		return p;
	}

	protected void merge(int[] map, HitViewRec r) {
		if(r==null || map==null)
			return;
		for (int i = 0; i < 40; i++) { //0~5
			map[i] += r.hit[i];
		}
		for (int i = 40; i < 80; i++) {//5~10
			map[40 + (i - 40) / 2] += r.hit[i];
		}
		for (int i = 80; i < 120; i++) {//10~20
			map[60 + (i - 80) / 2] += r.hit[i];
		}
		for (int i = 120; i < 160; i++) {//20~40
			map[80 + (i - 120) / 2] += r.hit[i];
		}
		for (int i = 160; i < 200; i++) {//40~80
			map[100 + (i - 160) / 2] += r.hit[i];
		}
		for (int i = 200; i < 240; i++) {//80~160
			map[119] += r.hit[i];
		}
		if(r.hitOver!=null) {
			map[119] += r.hitOver.size();			
		}
	}
}
