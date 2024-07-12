package whatap.lang.pack.os;

import whatap.lang.H3;
import whatap.lang.pack.PackEnum;
import whatap.util.HashUtil;

public class XosDiskUsagePack extends AbstractOSListPack {
	public final static int mountid = HashUtil.hash("mountid");
	public final static int parentid = HashUtil.hash("parentid");
	public final static int majorminor = HashUtil.hash("majorminor");
	public final static int root = HashUtil.hash("root");
	public final static int mount_point = HashUtil.hash("mount_point");
	public final static int mount_options = HashUtil.hash("mount_options");
	public final static int optional_fields = HashUtil.hash("optional_fields");
	public final static int fstype = HashUtil.hash("fstype");
	public final static int mount_source = HashUtil.hash("mount_source");
	public final static int super_options = HashUtil.hash("super_options");
	public final static int size = HashUtil.hash("size");
	public final static int free = HashUtil.hash("free");


	public synchronized static void send(H3 <Integer, String, String> handler) {
		try {
			handler.process(mountid,"mountid", null);
			handler.process(parentid, "parentid", null);
			handler.process(majorminor, "majorminor", null);
			handler.process(root, "root", null);
			handler.process(mount_point, "mount_point", null);
			handler.process(mount_options, "mount_options", null);
			handler.process(optional_fields, "optional_fields", null);
			handler.process(fstype, "fstype", null);
			handler.process(mount_source, "mount_source", null);
			handler.process(super_options, "super_options", null);
			handler.process(size, "size", "bytes");
			handler.process(free, "free", "bytes");
		} catch (Exception e) {
		}
	}

	@Override
	public short getPackType() {
		return PackEnum.XOS_DISK_USAGE;
	}
}
