package whatap.lang.value;

public interface ArrayValue {

	public int length();

	public ArrayValue subArray(int start, int end);
	public Value get(int i);

	public boolean isNumber();
}
