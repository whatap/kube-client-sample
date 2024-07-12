package whatap.lang.pack;

import whatap.io.DataInputX;
import whatap.io.DataOutputX;
import whatap.util.ArrayUtil;
import whatap.util.CompressUtil;

import java.util.ArrayList;
import java.util.List;

public class LogSinkZipPack extends AbstractPack {
    public static final int ZIPPED = 1;
    public static final int UN_ZIPPED = 0;


    public byte[] records;
    public int recordCount;
    public byte status = UN_ZIPPED;

    public String license;

    public LogSinkZipPack(){

    }

    @Override
    public short getPackType() {
        return PackEnum.LOGSINK_ZIP;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("LogSinkZipPack ");
        sb.append(super.toString());
        sb.append("records=" + ArrayUtil.len(records)+"bytes");
        return sb.toString();
    }

    public void write(DataOutputX dout) {
        super.write(dout);
        dout.writeByte(status);
        dout.writeDecimal(this.recordCount);
        dout.writeBlob(records);
    }


    public Pack read(DataInputX din){
        super.read(din);
        this.status = din.readByte();
        this.recordCount = (int) din.readDecimal();
        this.records = din.readBlob();
        return this;
    }


    public void records(byte[] records, int zipMinSize) {
        this.records = doZip(records, zipMinSize);
    }

    public byte[] doZip(byte[] records, int zipMinSize) {
        if (this.status != LogSinkZipPack.UN_ZIPPED) {
            return records;
        }
        if (records.length < zipMinSize){
            return records;
        }
        status = LogSinkZipPack.ZIPPED;
        return CompressUtil.doZip(records);
    }

    private byte[] doUnZip(){
        if (this.status != LogSinkZipPack.ZIPPED) {
            return records;
        }
        return CompressUtil.unZip(records);
    }


    public List<LogSinkPack> records(){
        List<LogSinkPack> items = new ArrayList();
        if (records == null) {
            return items;
        }
        DataInputX in = new DataInputX(doUnZip());
        for (int i = 0; i < this.recordCount; i++) {
            LogSinkPack p = (LogSinkPack) in.readPack();
            p.pcode = this.pcode;
            p.oid = this.oid;
            p.okind = this.okind;
            p.onode = this.onode;
            // time은 자기 시간을 사용한다
            items.add(p);
        }
        return items;
    }



}

