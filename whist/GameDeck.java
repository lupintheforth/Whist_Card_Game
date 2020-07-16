/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *                          Team 12                          *
 * Chen Fu(987369), Yizhou Zhu(1034676), Shengqi Zhou(893295)*
 *                   last update: 2020.6.6                   *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package whist;

import java.util.ArrayList;
import java.util.Collections;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Deck;
import ch.aplu.jcardgame.Hand;
import whist.Whist.Rank;
import whist.Whist.Suit;

public class GameDeck extends Deck {
	// this class extends Deck in order to support repeatable runs
	// (same dealingOut return) based on the random seed

	public GameDeck(Suit[] suits, Rank[] ranks, String cover) {
		super(suits, ranks, cover);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Hand[] dealingOut(int nbPlayers, int nbCardsPerPlayer) {
		int nbCards = 52;
		if (nbPlayers * nbCardsPerPlayer > nbCards) {
			fail("Error in Deck.dealing out.\n" + nbCards + " cards in deck. Not enough for" + "\n" + nbPlayers
					+ (nbPlayers > 1 ? " players with " : "player with ") + nbCardsPerPlayer
					+ (nbCardsPerPlayer > 1 ? " cards per player." : "card per player.")
					+ "\nApplication will terminate.");
		}

		ArrayList<Card> cards = new ArrayList<>();
		Enum[] arr$ = getSuits();
		int p = arr$.length;
		int k;
		for (k = 0; k < p; ++k) {
			Enum suit = arr$[k];
			Enum[] arr$1 = getRanks();
			int len$ = arr$1.length;
			for (int i$ = 0; i$ < len$; ++i$) {
				Enum rank = arr$1[i$];
				Card card = new Card(this, suit, rank);
				cards.add(card);
			}
		}
		Collections.shuffle(cards, Whist.getRandom());

		Hand[] hands = new Hand[nbPlayers + 1];

		for (p = 0; p < nbPlayers; ++p) {
			hands[p] = new Hand(this);
			for (k = 0; k < nbCardsPerPlayer; ++k) {
				hands[p].insert((Card) cards.get(p * nbCardsPerPlayer + k), false);
			}
		}

		hands[nbPlayers] = new Hand(this);

		for (p = nbPlayers * nbCardsPerPlayer; p < nbCards; ++p) {
			hands[nbPlayers].insert((Card) cards.get(p), false);
		}

		return hands;
	}

}
