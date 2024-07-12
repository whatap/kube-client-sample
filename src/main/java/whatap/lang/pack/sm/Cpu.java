package whatap.lang.pack.sm;

import whatap.io.DataInputX;
import whatap.io.DataOutputX;
import whatap.lang.value.Value;
import whatap.org.json.JSONObject;

public interface Cpu {
	public void write(DataOutputX dout);
	public void read(DataInputX din);
	public Cpu create();
	public JSONObject toJson();
	public float pct();
	public Value toMapValue();
	public void readv2(DataInputX din);
	public void writev2(DataOutputX dout);
	public float stealpct();
}
