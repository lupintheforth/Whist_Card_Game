/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *                          Team 12                          *
 * Chen Fu(987369), Yizhou Zhu(1034676), Shengqi Zhou(893295)*
 *                   last update: 2020.6.6                   *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package player;

import ch.aplu.jcardgame.Card;
import ch.aplu.jgamegrid.GameGrid;

public class RandomNPC extends NPCPlayer {
	// Inherits the NPCPlayer. Play method is to randomly choose any card in hand.

	public RandomNPC(int id) {
		super(id);
	}

	@Override
	public Card play() {
		selected = null;
		selected = randomCard(hand);
		GameGrid.delay(thinkingTime);
		return selected;
	}

}
