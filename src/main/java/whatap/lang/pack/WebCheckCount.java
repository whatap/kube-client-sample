package whatap.lang.pack;

import whatap.io.DataInputX;
import whatap.io.DataOutputX;
import whatap.util.IntIntMap;

public class WebCheckCount {
	public int wc_pcode_count;
	public int wc_request_count;
	public int wc_count;
	public long wc_timesum;
	public long wc_rsizesum;
	public IntIntMap wc_status_count = new IntIntMap();

	public byte[] toBytes() {

		DataOutputX out = new DataOutputX();
		
		out.writeDecimal(this.wc_pcode_count);
		out.writeDecimal(this.wc_request_count);
		out.writeDecimal(this.wc_count);
		out.writeDecimal(this.wc_timesum);
		out.writeDecimal(this.wc_rsizesum);
		this.wc_status_count.toBytes(out);

		return out.toByteArray();
	}

	public WebCheckCount toObject(byte[] b) {

		DataInputX in = new DataInputX(b);
		
		this.wc_pcode_count = (int) in.readDecimal();
		this.wc_request_count = (int) in.readDecimal();
		this.wc_count = (int) in.readDecimal();
		this.wc_timesum = in.readDecimal();
		this.wc_rsizesum = in.readDecimal();
		this.wc_status_count.toObject(in);
		return this;
	}

}
