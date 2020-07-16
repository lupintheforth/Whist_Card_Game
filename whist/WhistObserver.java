/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *                          Team 12                          *
 * Chen Fu(987369), Yizhou Zhu(1034676), Shengqi Zhou(893295)*
 *                   last update: 2020.6.6                   *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package whist;

import ch.aplu.jcardgame.Hand;

import java.util.ArrayList;

public interface WhistObserver {
	// interface to facilitates the implemented one to be the observer in subject class

	public void updateTrick(Hand trick); // notify the whist to update trick graphic

	public void updateScore(ArrayList<Integer> scoreList); // notify the whist to update Score graphic

	public void updateTrickClear(Hand trick);// notify the whist to clear the trick area

	public void updateStatus(String text); // notify the whist to update status text

	public void updateClear(); // notify the whist to remove all the actors to display
}
