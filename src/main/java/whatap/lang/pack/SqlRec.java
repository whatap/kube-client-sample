package whatap.lang.pack;

import whatap.io.DataInputX;
import whatap.io.DataOutputX;
import whatap.util.BitUtil;
import whatap.util.MathUtil;

public  class SqlRec {
    public int dbc;
    public int sql;
    public char sql_crud;
    public int count_total;
    public int count_error;
//    public int count_actived;
    public long time_sum;
	public long time_sqr_sum;
    public int time_min;
    public int time_max;
    public long fetch_count;
    public long fetch_time;
    public long update_count;
    
    public int service;
	public long txid_slowest;
	public long txid_error;

    public void merge(SqlRec o) {
        this.count_total += o.count_total;
        this.count_error += o.count_error;
//        this.count_actived += o.count_actived;
        this.time_sum += o.time_sum;
		this.time_sqr_sum += o.time_sqr_sum;
        this.time_min = Math.min(this.time_min, o.time_min);
        this.fetch_count += o.fetch_count;
        this.fetch_time += o.fetch_time;
        this.update_count += o.update_count;
        
        if (this.time_max <= o.time_max) {
			this.service = o.service;
			this.time_max = o.time_max;
			this.txid_slowest = o.txid_slowest;
		}
        if(o.txid_error!=0) {
        	this.txid_error = o.txid_error;
        }
    }

    public long key() {
        return BitUtil.composite(dbc, sql);
    }

    public SqlRec setDbcSql(int dbc, int sql) {
        this.dbc = dbc;
        this.sql = sql;
        return this;
    }

	public double getDeviation() {
		return MathUtil.getStandardDeviation(count_total, time_sum, time_sqr_sum);
	}

	public void write(DataOutputX o) {
		write(o, false);
	}

	public void write(DataOutputX o, boolean v2) {
		o.writeInt(this.dbc);
		o.writeInt(this.sql);
		o.writeByte(this.sql_crud);
		o.writeDecimal(this.count_total);
		o.writeDecimal(this.count_error);
		if (v2 == false) {
			o.writeDecimal(-1);
			o.writeDecimal(this.time_sum);
			o.writeDecimal(this.time_sqr_sum);
			o.writeDecimal(this.time_min);
			o.writeDecimal(this.time_max);
			o.writeDecimal(this.fetch_count);
			o.writeDecimal(this.fetch_time);
			o.writeDecimal(this.update_count);
			o.writeDecimal(this.service);
			return;
		}
		o.writeDecimal(-2);
		o.writeDecimal(this.time_sum);
		o.writeDecimal(this.time_sqr_sum);
		o.writeDecimal(this.time_min);
		o.writeDecimal(this.time_max);
		o.writeDecimal(this.fetch_count);
		o.writeDecimal(this.fetch_time);
		o.writeDecimal(this.update_count);
		o.writeDecimal(this.service);

		DataOutputX dout = new DataOutputX();
		dout.writeDecimal(this.txid_slowest);
		dout.writeDecimal(this.txid_error);
		o.writeBlob(dout.toByteArray());
	}

	public SqlRec read(DataInputX in) {
		this.dbc = in.readInt();
		this.sql = in.readInt();
		this.sql_crud = (char) in.readByte();
		this.count_total = (int) in.readDecimal();
		this.count_error = (int) in.readDecimal();

		int ver = (int) in.readDecimal();
		this.time_sum = in.readDecimal();
		this.time_sqr_sum = (int) in.readDecimal();
		this.time_min = (int) in.readDecimal();
		this.time_max = (int) in.readDecimal();
		this.fetch_count = in.readDecimal();
		this.fetch_time = in.readDecimal();
		this.update_count = in.readDecimal();
		if (ver >= 0) {
			return this;
		}
		this.service = (int) in.readDecimal();

		if (ver >= -1) {
			return this;
		}
		DataInputX din = new DataInputX(in.readBlob());
		this.txid_slowest = din.readDecimal();
		if (din.available() > 0) {
			this.txid_error = din.readDecimal();
		}

		return this;
	}
    
    public static void main(String[] args) {
		DataOutputX x = new DataOutputX();
		x.writeByte(' ');
		System.out.println(new DataInputX(x.toByteArray()).readByte());
	}
}