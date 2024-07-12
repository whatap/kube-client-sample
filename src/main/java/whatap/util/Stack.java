package whatap.util;

import java.util.Vector;

@SuppressWarnings("serial")
public class Stack<V> extends Vector<V> {
	public Stack() {
		super(10, 0);
	}

	public Stack(int initial, int increment) {
		super(initial, increment);
	}

	public V push(V item) {
		addElement(item);
		return item;
	}

	public synchronized V pop() {
		V obj;
		int len = size();
		obj = peek();
		removeElementAt(len - 1);
		return obj;
	}

	public synchronized V peek() {
		int len = size();
		if (len == 0)
			return null;
		return elementAt(len - 1);
	}

	public boolean empty() {
		return size() == 0;
	}

}