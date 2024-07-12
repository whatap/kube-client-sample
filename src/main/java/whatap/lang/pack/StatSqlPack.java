package whatap.lang.pack;

import whatap.io.DataInputX;
import whatap.io.DataOutputX;
import whatap.util.ArrayUtil;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class StatSqlPack extends AbstractPack {
   

    public byte[] records;
    public int record_count;

	private short packType;
	public long dataStartTime;
	
	public StatSqlPack() {
		this(PackEnum.STAT_SQL);
	}
	public StatSqlPack(short t) {
		this.packType=t;
	}	
    public short getPackType() {
        return this.packType;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("StatSql ");
        sb.append(super.toString());
        sb.append(",records=" + this.record_count);
        sb.append(",bytes=" + ArrayUtil.len(records));
        return sb.toString();
    }

    public void write(DataOutputX dout) {
        super.write(dout);
        dout.writeBlob(records);
        dout.writeDecimal(record_count);
        if (this.packType == PackEnum.STAT_SQL)
        	return;
        
        DataOutputX o =new DataOutputX();
		o.writeDecimal(dataStartTime);
		
		dout.writeBlob(o.toByteArray());
    }

    public Pack read(DataInputX din) {
        super.read(din);
        this.records = din.readBlob();
        this.record_count = (int) din.readDecimal();
        if (this.packType == PackEnum.STAT_SQL)
			return this;

		DataInputX in = new DataInputX(din.readBlob());
		this.dataStartTime = in.readDecimal();
		return this;
    }

    public StatSqlPack setRecords(int size, Enumeration<SqlRec> items) {
        DataOutputX o = new DataOutputX();
        o.writeShort(size);
        for (int i = 0; i < size; i++) {
            items.nextElement().write(o);
        }
        records = o.toByteArray();
        record_count = size;
        return this;
    }
    public StatSqlPack setRecords(List<SqlRec> items) {
        DataOutputX o = new DataOutputX();
        o.writeShort(items.size());
        for (int i = 0; i < items.size(); i++) {
             items.get(i).write(o);
        }
        records = o.toByteArray();
        record_count = items.size();
        return this;
    }

	public StatSqlPack setRecords(List<SqlRec> items, boolean v2) {
		DataOutputX o = new DataOutputX();
		o.writeShort(items.size());
		for (int i = 0; i < items.size(); i++) {
			items.get(i).write(o, v2);
		}
		records = o.toByteArray();
		record_count = items.size();
		return this;
	}

   
    public List<SqlRec> getRecords() {
        List<SqlRec> items = new ArrayList<SqlRec>();
        if (records == null)
            return items;
        DataInputX in = new DataInputX(records);
        int size = in.readShort() & 0xffff;
        for (int i = 0; i < size; i++) {
            items.add(new SqlRec().read(in));
        }
        return items;
    }

  
}
