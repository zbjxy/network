package drawables;

/**
 * Abstract class for filled drawable primitives.
 *
 * @version	1.00	17.03.99	first version
 * @author Thomas Brinkhoff
 */

public abstract class FilledDrawable extends Drawable {

	/**
	 * Is the primitive filled?
	 */
	protected boolean filled = false;

/**
 * Gibt zur�ck, ob das Grafik-Primitiv gef�llt ist.
 * @return gef�llt?
 */
public boolean isFilled () {
	return filled;
}
/**
 * Schaltet die F�llung des Grafik-Primitivs an bzw. aus.
 * @param on gef�llt?
 */
public void setFilling (boolean on) {
	filled = on;
}
}
