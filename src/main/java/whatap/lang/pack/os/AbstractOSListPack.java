package whatap.lang.pack.os;

import whatap.io.DataIOException;
import whatap.io.DataInputX;
import whatap.io.DataOutputX;
import whatap.lang.pack.AbstractPack;
import whatap.lang.pack.Pack;
import whatap.lang.value.IntMapValue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

abstract public class AbstractOSListPack extends AbstractPack {
	protected List<IntMapValue> oslist = new ArrayList<IntMapValue>();

	public int size() {
		return oslist.size();
	}

	public void add(IntMapValue row) {
		this.oslist.add(row);
	}

	public void add(List<IntMapValue> rows) {
		this.oslist.addAll(rows);
	}

	public IntMapValue get(int inx) {
		return oslist.get(inx);
	}

	@Override
	abstract public short getPackType();

	@Override
	public void write(DataOutputX dout) {
		super.write(dout);
		dout.writeShort(this.oslist.size());

		for (IntMapValue r : oslist) {
			try {
				r.write(dout);
			}catch(IOException e){
				throw new DataIOException(e);
			}
		}
	}

	@Override
	public Pack read(DataInputX din) {
		super.read(din);
		int listSz = din.readShort();
		List<IntMapValue> list = new ArrayList<IntMapValue>();
		for (int j = 0; j < listSz; j++) {
			try {
				list.add((IntMapValue) new IntMapValue().read(din));
			}catch(IOException e){
				throw new DataIOException(e);
			}
		}

		this.oslist.addAll(list);

		return this;
	}
}
