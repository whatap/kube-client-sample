package whatap.lang.pack;

import whatap.io.DataInputX;
import whatap.io.DataOutputX;
import whatap.lang.var.I2;
import whatap.lang.var.I3;

public  class HttpcRec {
	public int url;
	public int host;
	public int port;
	public int count_total;
	public int count_error;
	//public int count_actived;
	public long time_sum;
	public long time_std;
	public int time_min;
	public int time_max;
	public int service;

	public void merge(HttpcRec o) {
		this.count_total += o.count_total;
		this.count_error += o.count_error;
		//this.count_actived += o.count_actived;
		this.time_sum += o.time_sum;
		this.time_std += o.time_std;
		this.time_max = Math.max(this.time_max, o.time_max);
		this.time_min = Math.min(this.time_min, o.time_min);
		if(this.service==0) {
	        this.service=o.service;
	    }
	}

	public I2 keyHostPort() {
		return new I2(host, port);
	}

	public I3 keyFull() {
		return new I3(url, host, port);
	}

	public HttpcRec setUrlHostPort(int url, int host, int port) {
		this.url = url;
		this.host = host;
		this.port = port;
		return this;
	}
	  public double getDeviation(){
	        if(this.count_total == 0) {
	            return 0;
	        }
	        long avg = this.time_sum / this.count_total;
	        long variation = (this.time_std - (2 * avg * this.time_sum) + (this.count_total * avg * avg))/this.count_total;
	        double ret = Math.sqrt(variation);
	        return ret == Double.NaN ? 0 : ret;
	    }
	public long time_avg() {
		if (this.count_total == 0) {
			return 0;
		}
		return this.time_sum / this.count_total;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("HttpRec ");
		sb.append("url=" + this.url);
		sb.append(",host=" + this.host);
		sb.append(",port=" + this.port);
		sb.append(",count_total=" + this.count_total);
		sb.append(",count_error=" + this.count_error);
		//sb.append(",count_actived=" + this.count_actived);
		sb.append(",time_sum=" + this.time_sum);
		sb.append(",time_std=" + this.time_std);
		sb.append(",time_min=" + this.time_min);
		sb.append(",time_max=" + this.time_max);
		return sb.toString();
	}

	public  void write(DataOutputX o) {
		o.writeInt(this.url);
		o.writeInt(this.host);
		o.writeInt(this.port);
		o.writeDecimal(this.count_total);
		o.writeDecimal(this.count_error);
		o.writeDecimal(-1); //version은 음수(-)로 증가시켜야 한다. 
		o.writeDecimal(this.time_sum);
		o.writeDecimal(this.time_std);
		o.writeDecimal(this.time_min);
		o.writeDecimal(this.time_max);
		
		o.writeDecimal(this.service);
	}
	public  HttpcRec read(DataInputX in) {
		this.url = in.readInt();
		this.host = in.readInt();
		this.port = in.readInt();
		this.count_total = (int) in.readDecimal();
		this.count_error = (int) in.readDecimal();
		int ver = (int) in.readDecimal();
		this.time_sum = in.readDecimal();
		this.time_std= in.readDecimal();
		this.time_min = (int) in.readDecimal();
		this.time_max = (int) in.readDecimal();
		if(ver>=0) {
			return this;
		}
		this.service = (int)in.readDecimal();
		return this;
	}
}
