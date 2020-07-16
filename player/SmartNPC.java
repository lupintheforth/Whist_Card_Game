/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *                          Team 12                          *
 * Chen Fu(987369), Yizhou Zhu(1034676), Shengqi Zhou(893295)*
 *                   last update: 2020.6.6                   *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package player;

import java.util.ArrayList;

import ch.aplu.jcardgame.Card;
import ch.aplu.jgamegrid.GameGrid;
import whist.Whist;

public class SmartNPC extends NPCPlayer implements NPCObserver {

	// SmartNPC inherits the NPCPlayer and create two fields
	// The trickCards track the cards played by others in this trick
	// trump stores the trump of this game

	private ArrayList<Card> trickCards;
	private Whist.Suit trump;

	public SmartNPC(int id) {
		super(id);
		this.trickCards = new ArrayList<>();
		this.trump = null;
	}

	private boolean rankGreater(Card card1, Card card2) {
		return card1.getRankId() < card2.getRankId(); // Warning: Reverse rank order of cards (see comment on enum)
	}

	private Card getGreatestCard(ArrayList<Card> cardList) { // get largest ranked card in a card list
		Card greatestCard = cardList.get(0);
		for (Card card : cardList) {
			if (!rankGreater(greatestCard, card)) {
				greatestCard = card;
			}
		}
		return greatestCard;
	}

	private Card getSmallestCard(ArrayList<Card> cardList) { // get smallest ranked card in a card list
		Card smallestCard = cardList.get(0);
		for (Card card : cardList) {
			if (rankGreater(smallestCard, card)) {
				smallestCard = card;
			}
		}
		return smallestCard;
	}

	private boolean allSmaller(ArrayList<Card> cards1, ArrayList<Card> cards2) { // whether cards in list cards1 are all junior to ones in cards2
		for (Card cardinList1 : cards1) {
			for (Card cardinList2 : cards2) {
				if (rankGreater(cardinList1, cardinList2))
					return false;
			}
		}
		return true;
	}

	@Override
	public Card play() {
		GameGrid.delay(thinkingTime);
		ArrayList<Card> cardList = hand.getCardList();
		if (trickCards.isEmpty()) { // if takes the lead, choose the ranked highest card in hand
			return getGreatestCard(cardList);
		} else {
			// classify the hand cards to three types
			// Cards with the same suit to the lead
			// Cards with the same suit to the trump
			// other Cards
			Whist.Suit lead = (Whist.Suit) this.trickCards.get(0).getSuit();
			ArrayList<Card> sameAsLeadCards = new ArrayList<>();
			ArrayList<Card> sameAsTrumpCards = new ArrayList<>();
			ArrayList<Card> otherCards = new ArrayList<>();
			for (Card card : cardList) {
				if (card.getSuit() == lead) {
					sameAsLeadCards.add(card);
				} else if (card.getSuit() == trump) {
					sameAsTrumpCards.add(card);
				} else {
					otherCards.add(card);
				}
			}
			if (!sameAsLeadCards.isEmpty()) {
				Card card;
				if (allSmaller(sameAsLeadCards, trickCards)) {
					// the player need to play legally but no chance to win. Minimize the loss by taking the smallest card
					card = getSmallestCard(sameAsLeadCards);
				} else {
					// get chance to win. Take the Greatest card!
					card = getGreatestCard(sameAsLeadCards);
				}
				return card;
			} else if (trump != lead && !sameAsTrumpCards.isEmpty()) {
				// Has no card same as lead suit, try the trump card if possible
				return getSmallestCard(sameAsTrumpCards);
			} else {
				// minimize the loss.
				return getSmallestCard(otherCards);
			}
		}
	}

	@Override
	public void update(Card card) {
		// add the card to the trickCards as record of this trick
		this.trickCards.add(card);

	}

	@Override
	public void updateTrump(Whist.Suit trump) {
		this.trump = trump;
	}

	@Override
	public void clear() {
		// end of this trick. Clear the record
		this.trickCards.clear();
	}

}
