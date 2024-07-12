package whatap.lang.pack;

import whatap.lang.value.Value;

public interface ITagPack {

	public long getPcode();

	public long getTime();

	public int getOid();

	public int getOKind();

	public int getONode();

	public short getPackType();

	public String getCategory();

	public Value getTag(String key);

	public Value getField(String key);

}
