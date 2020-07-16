/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *                          Team 12                          *
 * Chen Fu(987369), Yizhou Zhu(1034676), Shengqi Zhou(893295)*
 *                   last update: 2020.6.6                   *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package player;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;
import whist.Whist;

public abstract class NPCPlayer implements IPlayer {
	// The base class to create all the NPC players.
	// Different nature to the InteractivePlayer

	protected final int id;
	protected int score;
	protected Hand hand; // hand of dealt cards
	protected Card selected = null;
	protected final int thinkingTime = 2000;

	public NPCPlayer(int id) {
		this.id = id;
		this.score = 0;
		this.hand = null;
	}

	@Override
	public int getID() {
		return this.id;
	}

	@Override
	public int getScore() {
		return this.score;
	}

	@Override
	public void setInitHand(Hand dealedCards) {
		this.hand = dealedCards;
	}

	@Override
	public Hand getHand() {
		return this.hand;
	}

	@Override
	public abstract Card play();

	@Override
	public void win() {
		this.score++;
	}

	public Card randomCard(Hand hand) {
		int x = Whist.getRandom().nextInt(hand.getNumberOfCards());
		return hand.get(x);
	}

}
