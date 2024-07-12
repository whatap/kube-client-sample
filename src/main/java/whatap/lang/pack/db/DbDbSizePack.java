package whatap.lang.pack.db;

import whatap.lang.pack.PackEnum;

public class DbDbSizePack extends AbstractDbTablePack {
//	public final static short dbname = 1;
//	public final static short size = 2;
//
//	public static IntKeyMap<String> values = ClassUtil.getConstantValueMap(DbDbSizePack.class, Short.TYPE);
//	public static StringIntMap names = ClassUtil.getConstantKeyMap(DbDbSizePack.class, Short.TYPE);

	public short getPackType() {
		return PackEnum.DB_DBSIZE;
	}

}
