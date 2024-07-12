package whatap.lang.pack.os;

import whatap.lang.H3;
import whatap.lang.pack.PackEnum;
import whatap.util.HashUtil;

public class XosFilePack extends AbstractOSListPack {
	public final static int filename = HashUtil.hash("filename");
	public final static int log = HashUtil.hash("log");

	public synchronized static void send(H3 <Integer, String, String> handler) {
		try {
			handler.process(filename, "filename", null);
			handler.process(log, "log", null);
		} catch (Exception e) {
		}
	}

	@Override
	public short getPackType() {
		// TODO Auto-generated method stub
		return PackEnum.XOS_FILE;
	}
}
