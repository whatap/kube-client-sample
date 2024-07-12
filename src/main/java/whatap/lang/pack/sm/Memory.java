package whatap.lang.pack.sm;

import whatap.io.DataInputX;
import whatap.io.DataOutputX;
import whatap.lang.value.Value;
import whatap.org.json.JSONObject;

public interface Memory {
	public void write(DataOutputX dout);
	public void read(DataInputX din);
	public JSONObject toJson();

	public float pct();
	public Value toMapValue();
	public float swappct();
	public void readv2(DataInputX din);
	public void writev2(DataOutputX dout);
}
