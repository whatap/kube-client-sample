package whatap.util;
public class SeqGen {
  private static long number =  System.currentTimeMillis();
  public static synchronized long next(){
	  return number++;
  }
}
