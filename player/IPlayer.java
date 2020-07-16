/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *                          Team 12                          *
 * Chen Fu(987369), Yizhou Zhu(1034676), Shengqi Zhou(893295)*
 *                   last update: 2020.6.6                   *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package player;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;

public interface IPlayer {

	public int getID();

	public int getScore();

	public void setInitHand(Hand handCards); // set the hand to the dealed card of the player

	public Hand getHand();

	public Card play();

	public void win(); // increase the score if winned

}
