package whatap.io;

import whatap.lang.pack.ActiveStackPack1;
import whatap.lang.pack.CounterPack1;
import whatap.lang.pack.ProfilePack;
import whatap.lang.pack.RealtimeUserPack1;
import whatap.lang.service.TxRecord;
import whatap.lang.step.SqlStepX;
import whatap.lang.step.Step;
import whatap.util.cardinality.HyperLogLog;

import java.lang.reflect.Field;

public class NetCalc {
	public static void main(String[] args) throws Exception {
		int sz = 0;
		System.out.println("ProfilePack "+size1());
		System.out.println("CounterPack1 "+size2());
		System.out.println("RealtimeUserPack1 "+size3());
		System.out.println("ActiveStack "+size4());

	}

	private static int size1() throws Exception {
		ProfilePack p = new ProfilePack();
		fill(p);
		TxRecord w = new TxRecord();
		p.transaction =w;
		Step[] s = new Step[10];
		for(int i = 0 ; i < s.length ; i++) {
			s[i] = new SqlStepX();
			fill(s[i]);
		}
		p.setProfile(s);
		fill(w);
		return new DataOutputX().writePack(p).toByteArray().length;
	}
	private static int size2() throws Exception {
		CounterPack1 p = new CounterPack1();
		fill(p);
		return new DataOutputX().writePack(p).toByteArray().length;
	}
	private static int size3() throws Exception {
		RealtimeUserPack1 p = new RealtimeUserPack1();
		p.logbits= new HyperLogLog().getBytes();
		fill(p);
		return new DataOutputX().writePack(p).toByteArray().length;
	}
	private static int size4() throws Exception {
		ActiveStackPack1 p = new ActiveStackPack1();
		fill(p);
		return new DataOutputX().writePack(p).toByteArray().length;
	}
	private static void fill(Object w) throws Exception {
		Field[] f= w.getClass().getFields();
		for(int i=0 ; i< f.length ;i++) {
			Class c = f[i].getType();
			//System.out.println(f[i].getName());
			if(c.isArray()) {
				if(c.getName().equals("[B")){
					f[i].set(w, new byte[10]);
				}else if(c.getName().equals("[I")){
					f[i].set(w, new int[50]);
				}
			}else
			if(c == Boolean.TYPE) {
				f[i].set(w, true);
			}else if(c == Byte.TYPE) {
				f[i].set(w, (byte)10);
			}else if(c == Short.TYPE) {
				f[i].set(w, (short)200);
			}else if(c.isPrimitive()) {
				f[i].set(w, 1000);
			}else {
				//System.out.println(c);
			}
			
		}
	}
}
