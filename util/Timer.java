package util;

/**
 * Klasse zur Bereitstellung von Timern.
 * 
 * @version 1.00	02.12.98
 * @version	1.10	12.08.99	getActValue, R�ckgabewert bei stop
 * @author Thomas Brinkhoff
 */
public class Timer {
	/**
	 * max. Zahl von Timern.
	 */
	public static final int maxNumOfTimers = 10;
	
	/**
	 * Summierte Zeit und Statzzeit.
	 */
	private static long[] time = new long[maxNumOfTimers];
	private static long[] start = new long[maxNumOfTimers];
	

/**
 * Gibt die bislang gestoppte Zeit zur�ck.
 * @return gestoppte Zeit in Millisekunden
 * @param timer Timer-Index
 */
public static long get (int timer) {
	return time[timer];
}
/**
 * Gibt die aktuelle verstrichene Zeit zur�ck.
 * @return verstrichene Zeit in Millisekunden
 * @param timer Timer-Index
 */
public static long getActValue (int timer) {
	return time[timer]+System.currentTimeMillis()-start[timer];
}
/**
 * Setzt den angegebenen Timer zur�ck.
 * @param timer Timer-Index
 */
public static void reset (int timer) {
	time[timer] = 0;
	start[timer] = System.currentTimeMillis();
}
/**
 * Startet den angegebenen Timer zur�ck.
 * @param timer Timer-Index
 */
public static void start (int timer) {
	start[timer] = System.currentTimeMillis();
}
/**
 * Stoppt den angegebenen Timer und gibt dessen Wert zur�ck.
 * @param timer Timer-Index
 */
public static long stop (int timer) {
	time[timer] += System.currentTimeMillis()-start[timer];
	start[timer] = System.currentTimeMillis();
	return time[timer];
}
}
