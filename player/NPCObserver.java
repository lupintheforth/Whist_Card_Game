/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *                          Team 12                          *
 * Chen Fu(987369), Yizhou Zhu(1034676), Shengqi Zhou(893295)*
 *                   last update: 2020.6.6                   *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package player;

import ch.aplu.jcardgame.Card;
import whist.Whist;

public interface NPCObserver {
	// interface to facilitates the implemented one to become the observer in Subject class.

	public void update(Card card); // pass through a card to the listener

	public void updateTrump(Whist.Suit trump);

	public void clear(); // told the player it is end of a trick and do something

}
