/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *                          Team 12                          *
 * Chen Fu(987369), Yizhou Zhu(1034676), Shengqi Zhou(893295)*
 *                   last update: 2020.6.6                   *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package exception;

/**
 * An exception thrown when a player breaks a rule
 */
@SuppressWarnings("serial")
public class BrokeRuleException extends Exception {
	public BrokeRuleException(String violation) {
		super(violation);
	}
}
