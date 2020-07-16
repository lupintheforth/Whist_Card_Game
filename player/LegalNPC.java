/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *                          Team 12                          *
 * Chen Fu(987369), Yizhou Zhu(1034676), Shengqi Zhou(893295)*
 *                   last update: 2020.6.6                   *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package player;

import whist.Whist;

import ch.aplu.jcardgame.Card;
import ch.aplu.jgamegrid.GameGrid;

public class LegalNPC extends NPCPlayer implements NPCObserver {
	// inherits all the variables from NPCPlayer, except for creating the new currentLead field
	// An instance of NPCObserve facilitates it to become a listener in Game class.

	private Whist.Suit currentLead; // note down the Suit of the lead card this trick

	public LegalNPC(int id) {
		super(id);
		this.currentLead = null;
	}

	@Override
	public Card play() {
		selected = null;
		selected = randomCard(hand);
		// if currentLeand is empty. It implies the Player is taking the lead. pick the card randomly
		// if not, the player should randomly choose the card until it is a legal one.
		if (this.currentLead != null) {
			while (selected.getSuit() != currentLead && hand.getNumberOfCardsWithSuit(currentLead) > 0) {
				selected = randomCard(hand);
			}
		}
		GameGrid.delay(thinkingTime);
		return selected;
	}

	@Override
	public void update(Card card) {
		// when the player is told to update the card, the Suit will get extracted to assign
		// to  the currentLead field.
		this.currentLead = (Whist.Suit) card.getSuit();
	}

	@Override
	public void updateTrump(Whist.Suit trump) {
		// PlaceHolder
	}

	@Override
	// End of the trick, the player is told to abandon the lead of last trick.
	public void clear() {
		this.currentLead = null;
	}

}
