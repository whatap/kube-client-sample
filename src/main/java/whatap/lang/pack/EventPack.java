package whatap.lang.pack;

import whatap.io.DataInputX;
import whatap.io.DataOutputX;
import whatap.lang.EventLevel;
import whatap.util.CastUtil;
import whatap.util.DateUtil;
import whatap.util.StringKeyLinkedMap;
import whatap.util.StringKeyLinkedMap.StringKeyLinkedEntry;
import whatap.util.StringUtil;
import whatap.util.UUIDUtil;

import java.util.Enumeration;

public class EventPack extends AbstractPack {

    private static final String ESCALATION_KEY = "_esca_";
    private static final String UUID_KEY = "_uuid_";
    private static final String STATUS_KEY = "_status_";
    private static final String OTYPE_KEY = "_otype_";


    public String uuid;
    public boolean escalation;

    public byte level;
    public String title;
    public String message;
    public int status;  // on, off, ack, ok
    public int otype;  // @See io.whatap.agents.OType

    public StringKeyLinkedMap<String> attr = new StringKeyLinkedMap<String>();

    //Log only
    public long eid;


    public EventPack() {
    }

    public int size() {
        int size = 1;
        size += title == null ? 0 : title.length();
        size += message == null ? 0 : message.length();
        size += attr.size() * 20;
        return size;
    }

    public short getPackType() {
        return PackEnum.EVENT;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ALERT ");
        sb.append(super.toString());
        sb.append(" " + EventLevel.values.get(level));
        sb.append(" " + title);
        sb.append(" " + message);
        sb.append(" " + attr);
        return sb.toString();
    }

    public void write(DataOutputX dout) {
        super.write(dout);
        dout.writeByte(level);
        dout.writeText(title);
        dout.writeText(message);

        if (StringUtil.isNotEmpty(this.uuid)) {
            this.attr.put(UUID_KEY, this.uuid);
        }
        this.attr.put(ESCALATION_KEY, this.escalation ? "true" : "false");
        this.attr.put(STATUS_KEY, String.valueOf(this.status));
        this.attr.put(OTYPE_KEY, String.valueOf(this.otype));
        dout.writeByte(attr.size());
        Enumeration<StringKeyLinkedEntry<String>> en = attr.entries();
        for (int i = 0; i < attr.size(); i++) {
            StringKeyLinkedEntry<String> e = en.nextElement();
            dout.writeText(e.getKey());
            dout.writeText(e.getValue());
        }
    }

    public Pack read(DataInputX din) {
        super.read(din);

        this.level = din.readByte();
        this.title = din.readText();
        this.message = din.readText();
        int tagsz = din.readUnsignedByte();//255
        for (int i = 0; i < tagsz; i++) {
            String key = din.readText();
            String value = din.readText();
            this.attr.put(key, value);
        }
        //attr에서는 삭제한다. 데이터를 시리얼라이즈할때만 사용한다.
        this.escalation = "true".equals(this.attr.remove(ESCALATION_KEY));
        this.uuid = this.attr.remove(UUID_KEY);
        this.otype = CastUtil.cint( this.attr.remove(OTYPE_KEY));
        this.status = CastUtil.cint(this.attr.remove(STATUS_KEY));

        return this;
    }

    public void setUuid() {
        if (StringUtil.isNotEmpty(this.uuid)) {
            return;
        }
        this.uuid = UUIDUtil.generate();
    }

    public byte getLevel() {
        return level;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public StringKeyLinkedMap<String> getAttr() {
        return attr;
    }

    public long getEventId() {
        if (eid == 0) {
            eid = StringUtil.isNotEmpty(this.uuid) ? UUIDUtil.toLong(this.uuid) : DateUtil.currentTime();
        }
        return eid;
    }

}
