package whatap.util;

public class LongStack extends LongList {

	public long push(long item) {
		add(item);
		return item;
	}

	public synchronized long pop() {
		long obj;
		int len = size();

		obj = peek();
		remove(len - 1);
		return obj;
	}

	public synchronized long peek() {
		int len = size();
		if (len == 0)
			return 0;
		return get(len - 1);
	}

}
