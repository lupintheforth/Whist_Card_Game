/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *                          Team 12                          *
 * Chen Fu(987369), Yizhou Zhu(1034676), Shengqi Zhou(893295)*
 *                   last update: 2020.6.6                   *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package exception;

/**
 * An exception thrown when the number of players doesn't meet the game logic
 */
@SuppressWarnings("serial")
public class NumberPlayersException extends Exception {
	public NumberPlayersException(String violation) {
		super(violation);
	}
}
