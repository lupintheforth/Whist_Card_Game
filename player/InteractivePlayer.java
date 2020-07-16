/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *                          Team 12                          *
 * Chen Fu(987369), Yizhou Zhu(1034676), Shengqi Zhou(893295)*
 *                   last update: 2020.6.6                   *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package player;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.CardAdapter;
import ch.aplu.jcardgame.CardListener;
import ch.aplu.jcardgame.Hand;
import ch.aplu.jgamegrid.GameGrid;

public class InteractivePlayer implements IPlayer {

	private final int id;
	private int score;
	private Hand hand;
	private Card selected = null;

	public InteractivePlayer(int id) {
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
	public Card play() { // Interactive Player can pick the card by double click the card. Here is the function when he is called to play
		this.selected = null;
		this.hand.setTouchEnabled(true);
		while (null == this.selected) {
			GameGrid.delay(100);
		}
		return this.selected;
	}

	@Override
	public void win() {
		this.score++;
	}

	// use CardListener to make the play interactively
	public void addCardListener() {
		CardListener cardListener = new CardAdapter() // Human Player plays card
		{
			@Override
			public void leftDoubleClicked(Card card) {
				selected = card;
				hand.setTouchEnabled(false);
			}
		};
		this.hand.addCardListener(cardListener);
	}

}
