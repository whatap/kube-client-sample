package whatap.lang;

import whatap.io.DataInputX;
import whatap.io.DataOutputX;
import whatap.util.HashUtil;
import whatap.util.Hexa32;
import whatap.util.KeyGen;
import whatap.util.StringUtil;

public class License {
	public static class Key {

		public long pcode;
		public byte[] security_key;

		@Override
		public String toString() {
			return "[project=" + pcode + ", security_key_hash=" + HashUtil.hash(security_key) + "]";
		}
	}

	public static String toString(long pcode, byte[] secure_key) {
		try {
			DataOutputX out = new DataOutputX();
			out.writeDecimal(pcode);
			out.writeBlob(secure_key);
			int len = out.getWriteSize();
			int n = len / 8;
			if (len - n * 8 > 0) {
				out.writeLong(KeyGen.next());
				n++;
			}
			DataInputX in = new DataInputX(out.toByteArray());
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < n; i++) {
				if (i > 0)
					sb.append("-");
				sb.append(Hexa32.toString32(in.readLong()));
			}
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Key getKey(String lic) {
		try {
			String[] tokens = StringUtil.tokenizer(lic.trim(), "-_");
			DataOutputX out = new DataOutputX();
			for (int i = 0; i < tokens.length; i++) {
				out.writeLong(Hexa32.toLong32(tokens[i]));
			}
			Key key = new Key();
			DataInputX in = new DataInputX(out.toByteArray());
			key.pcode = in.readDecimal();
			key.security_key = in.readBlob();
			return key;
		} catch (Exception e) {
			throw new RuntimeException("invalid license format error");
		}
	}
	public static long getProjectCode(String lic) {
		Key k = getKey(lic);
		return k.pcode;
	}

}
