package whatap.lang.pack.os;

import whatap.lang.H3;
import whatap.lang.pack.PackEnum;
import whatap.util.HashUtil;

public class XosCounterPack extends AbstractOSCounterPack {

	public final static int cpu = HashUtil.hash("xcpu");
	public final static int cpu_user = HashUtil.hash("xcpu_user");
	public final static int cpu_sys = HashUtil.hash("xcpu_sys");
	public final static int cpu_iowait = HashUtil.hash("xcpu_iowait");
	public final static int cpu_idle = HashUtil.hash("xcpu_idle");
	public final static int mem = HashUtil.hash("xmem");
	public final static int mem_total = HashUtil.hash("xmem_total");
	public final static int mem_free = HashUtil.hash("xmem_free");
	public final static int mem_used = HashUtil.hash("xmem_used");
	public final static int mem_buffcache = HashUtil.hash("xmem_buffcache");

	public final static int cpu_xos = HashUtil.hash("cpu(xos)");
	public final static int cpu_user_xos = HashUtil.hash("cpu_user(xos)");
	public final static int cpu_sys_xos = HashUtil.hash("cpu_sys(xos)");
	public final static int cpu_iowait_xos = HashUtil.hash("cpu_iowait(xos)");
	public final static int cpu_idle_xos = HashUtil.hash("cpu_idle(xos)");
	public final static int cpu_xos2 = HashUtil.hash("cpu(xos2)");
	public final static int cpu_user_xos2 = HashUtil.hash("cpu_user(xos2)");
	public final static int cpu_sys_xos2 = HashUtil.hash("cpu_sys(xos2)");
	public final static int cpu_iowait_xos2 = HashUtil.hash("cpu_iowait(xos2)");
	public final static int cpu_idle_xos2 = HashUtil.hash("cpu_idle(xos2)");
	public final static int mem_xos = HashUtil.hash("mem(xos)");
	public final static int mem_total_xos = HashUtil.hash("mem_total(xos)");
	public final static int mem_free_xos = HashUtil.hash("mem_free(xos)");
	public final static int mem_used_xos = HashUtil.hash("mem_used(xos)");
	public final static int mem_buffcache_xos = HashUtil.hash("mem_buffcache(xos)");
	public final static int mem_swaptotal_xos = HashUtil.hash("mem_swaptotal(xos)");
	public final static int mem_swapfree_xos = HashUtil.hash("mem_swapfree(xos)");
	public final static int mem_available_xos = HashUtil.hash("mem_available(xos)");

	public final static int disk_reads_xos = HashUtil.hash("disk_reads(xos)");
	public final static int disk_read_bytes_xos = HashUtil.hash("disk_read_bytes(xos)");
	//public final static int disk_reads_req_xos = HashUtil.hash("disk_reads_req(xos)");
	public final static int disk_writes_xos = HashUtil.hash("disk_writes(xos)");
	public final static int disk_write_bytes_xos = HashUtil.hash("disk_write_bytes(xos)");
	//public final static int disk_writes_req_xos = HashUtil.hash("disk_writes_req(xos)");
	public final static int net_recv_bytes_xos = HashUtil.hash("net_recv_bytes(xos)");
	public final static int net_recv_packets_xos = HashUtil.hash("net_recv_packets(xos)");
	public final static int net_send_bytes_xos = HashUtil.hash("net_send_bytes(xos)");
	public final static int net_send_packets_xos = HashUtil.hash("net_send_packets(xos)");

	public synchronized static void send(H3 <Integer, String, String> handler) {
		try {
			handler.process(cpu, "xcpu", "%");
			handler.process(cpu_user, "xcpu_user", "%");
			handler.process(cpu_sys, "xcpu_sys", "%");
			handler.process(cpu_iowait, "xcpu_iowait", "%");
			handler.process(cpu_idle, "xcpu_idle", "%");
			handler.process(mem, "xmem", "%");
			handler.process(mem_total, "xmem_total", "Mb");
			handler.process(mem_free, "xmem_free", "Mb");
			handler.process(mem_used, "xmem_used", "Mb");
			handler.process(mem_buffcache, "xmem_buffcache", "Mb");

			handler.process(cpu_xos, "cpu(xos)", "%");
			handler.process(cpu_user_xos, "cpu_user(xos)", "%");
			handler.process(cpu_sys_xos, "cpu_sys(xos)", "%");
			handler.process(cpu_iowait_xos, "cpu_iowait(xos)", "%");
			handler.process(cpu_idle_xos, "cpu_idle(xos)", "%");
			handler.process(cpu_xos2, "cpu(xos2)", "%");
			handler.process(cpu_user_xos2, "cpu_user(xos2)", "%");
			handler.process(cpu_sys_xos2, "cpu_sys(xos2)", "%");
			handler.process(cpu_iowait_xos2, "cpu_iowait(xos2)", "%");
			handler.process(cpu_idle_xos2, "cpu_idle(xos2)", "%");
			handler.process(mem_xos, "mem(xos)", "%");
			handler.process(mem_total_xos, "mem_total(xos)", "Mb");
			handler.process(mem_free_xos, "mem_free(xos)", "Mb");
			handler.process(mem_used_xos, "mem_used(xos)", "Mb");
			handler.process(mem_buffcache_xos, "mem_buffcache(xos)", "Mb");
			handler.process(mem_swaptotal_xos, "mem_swaptotal(xos)", "Mb");
			handler.process(mem_swapfree_xos, "mem_swapfree(xos)", "Mb");
			handler.process(mem_available_xos, "mem_available(xos)", "Mb");

			handler.process(disk_reads_xos, "disk_reads(xos)", null);
			handler.process(disk_reads_xos, "disk_read_bytes(xos)", null);
			//handler.process(disk_reads_req_xos, "disk_reads_req(xos)", null);
			handler.process(disk_writes_xos, "disk_writes(xos)", null);
			handler.process(disk_writes_xos, "disk_write_bytes(xos)", null);
			//handler.process(disk_writes_req_xos, "disk_writes_req(xos)", null);
			handler.process(net_recv_bytes_xos, "net_recv_bytes(xos)", "bytes");
			handler.process(net_recv_packets_xos, "net_recv_packets(xos)", null);
			handler.process(net_send_bytes_xos, "net_send_bytes(xos)", "bytes");
			handler.process(net_send_packets_xos, "net_send_packets(xos)", null);
		} catch (Exception e) {
		}
	}

	@Override
	public short getPackType() {
		// TODO Auto-generated method stub
		return PackEnum.XOS_COUNTER;
	}

}
