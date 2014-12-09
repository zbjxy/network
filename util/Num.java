package util;

/**
 * Klasse zur Unterst�tzung der Handhabung von Zahlen.
 *
 * @version	1.00	22.06.99	erste  Fassung
 * @author Thomas Brinkhoff
 */
public class Num {
/**
 * Beschr�nkt den Wert des ersten Parameters auf das angegebene Intervall.
 * @return beschr�nkter Wert
 * @param x Eingabewert
 * @param min Unterschranke
 * @param max Oberschranke
 */
public static byte putIntoInterval (byte x, byte min, byte max) {
	if (x < min)
		x = min;
	if (x > max)
		x = max;
	return x;
}
/**
 * Beschr�nkt den Wert des ersten Parameters auf das angegebene Intervall.
 * @return beschr�nkter Wert
 * @param x Eingabewert
 * @param min Unterschranke
 * @param max Oberschranke
 */
public static int putIntoInterval (int x, int min, int max) {
	if (x < min)
		x = min;
	if (x > max)
		x = max;
	return x;
}
/**
 * Beschr�nkt den Wert des ersten Parameters auf das angegebene Intervall.
 * @return beschr�nkter Wert
 * @param x Eingabewert
 * @param min Unterschranke
 * @param max Oberschranke
 */
public static long putIntoInterval (long x, long min, long max) {
	if (x < min)
		x = min;
	if (x > max)
		x = max;
	return x;
}
/**
 * Beschr�nkt den Wert des ersten Parameters auf das angegebene Intervall.
 * @return beschr�nkter Wert
 * @param x Eingabewert
 * @param min Unterschranke
 * @param max Oberschranke
 */
public static short putIntoInterval (short x, short min, short max) {
	if (x < min)
		x = min;
	if (x > max)
		x = max;
	return x;
}
}
