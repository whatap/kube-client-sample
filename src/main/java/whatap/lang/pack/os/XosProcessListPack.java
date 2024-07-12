package whatap.lang.pack.os;

import whatap.lang.H3;
import whatap.lang.pack.PackEnum;
import whatap.util.HashUtil;

public class XosProcessListPack extends AbstractOSListPack {
	public final static int pid = HashUtil.hash("pid");
	public final static int uid = HashUtil.hash("uid");
	public final static int cputime = HashUtil.hash("cputime");
	public final static int cpuusage = HashUtil.hash("cpuusage");
	public final static int elapse = HashUtil.hash("elapse");
	public final static int vsize = HashUtil.hash("vsize");
	public final static int rss = HashUtil.hash("rss");
	public final static int pss = HashUtil.hash("pss");
	public final static int ioread = HashUtil.hash("ioread");
	public final static int iowrite = HashUtil.hash("iowrite");
	public final static int state = HashUtil.hash("state");
	public final static int cmd = HashUtil.hash("cmd");
	public final static int longcmd = HashUtil.hash("longcmd");

	public synchronized static void send(H3 <Integer, String, String> handler) {
		try {
			handler.process(pid, "pid", null);
			handler.process(uid, "uid", null);
			handler.process(cputime, "cputime", null);
			handler.process(cpuusage, "cpuusage", null);
			handler.process(elapse, "elapse", null);
			handler.process(vsize, "vsize", null);
			handler.process(rss, "rss", null);
			handler.process(pss, "pss", null);
			handler.process(ioread, "ioread", null);
			handler.process(iowrite, "iowrite", null);
			handler.process(state, "state", null);
			handler.process(cmd, "cmd", null);
			handler.process(longcmd, "longcmd", null);
		} catch (Exception e) {
		}
	}

	@Override
	public short getPackType() {
		return PackEnum.XOS_PROCESS_LIST;
	}
}
