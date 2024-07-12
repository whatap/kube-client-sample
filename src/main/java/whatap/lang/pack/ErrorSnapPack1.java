package whatap.lang.pack;
import whatap.io.DataInputX;
import whatap.io.DataOutputX;
import whatap.lang.step.Step;
import whatap.util.ArrayUtil;
public class ErrorSnapPack1 extends AbstractPack {
    public long errorSnapId;
    public byte[] profile;
    public byte[] stack;
	
    public byte append_type;
	public int append_hash;
	
    public short getPackType() {
        return PackEnum.ERROR_SNAP_1;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ErrorSnap ");
        sb.append(super.toString());
        sb.append(" profile_bytes=" + ArrayUtil.len(profile));
        sb.append(" stack_bytes=" + ArrayUtil.len(stack));
        sb.append(" append_type=" + append_type);
        sb.append(" append_hash=" + append_hash);
          return sb.toString();
    }

    public void write(DataOutputX dout)  {
        super.write(dout);
        dout.writeLong(errorSnapId);
        dout.writeBlob(profile);
        dout.writeBlob(stack);
        dout.writeByte(append_type);
        dout.writeDecimal(append_hash);
    }

    public Pack read(DataInputX din)  {
        super.read(din);

        this.errorSnapId = din.readLong();
        this.profile = din.readBlob();
        this.stack = din.readBlob();
        this.append_type = din.readByte();
        this.append_hash = (int)din.readDecimal();
        return this;
    }

    public ErrorSnapPack1 setProfile(Step[] steps) throws Exception {
        this.profile = Step.toBytes(steps);
        return this;
    }

    public ErrorSnapPack1 setStack(int[] callstacks) throws Exception {
        this.stack = new DataOutputX().writeArray(callstacks).toByteArray();
        return this;
    }

    public Step[] getProfile() throws Exception {
        if(this.profile == null || this.profile.length == 0) {
            return new Step[0];
        }
        return Step.toObjects(this.profile);
    }

    public int[] getStack() throws Exception {
        if (this.stack == null || this.stack.length == 0)
            return null;
        return new DataInputX(this.stack).readArray(new int[0]);
    }
}
