/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *                          Team 12                          *
 * Chen Fu(987369), Yizhou Zhu(1034676), Shengqi Zhou(893295)*
 *                   last update: 2020.6.6                   *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package whist;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

import ch.aplu.jcardgame.*;
import ch.aplu.jgamegrid.*;
import exception.BrokeRuleException;
import player.*;

public class Game extends Subject {

	private Deck deck;
	private int nbPlayers;
	private int nStartCards;
	private int winningScore;
	private boolean enforceRules;

	private Hand trick;
	private Whist.Suit trump;
	private IPlayer trickWinner;
	private ArrayList<IPlayer> players = new ArrayList<>();

	// get the trump of the game logic generated
	public Whist.Suit getTrump() {
		return trump;
	}


	// get player by the player id
	public IPlayer getPlayer(int id) {
		for (IPlayer player : players) {
			if (player.getID() == id) {
				return player;
			}
		}
		return null;
	}

	// determines whether the player still has cards to play.
	// If all of the three running out the card.We need to deal it again
	public boolean haveCards() {
		for (IPlayer player : players) {
			if (player.getHand().getNumberOfCards() <= 0)
				return false;
		}
		return true;
	}

	// return random Enum value
	public static <T extends Enum<?>> T randomEnum(Class<T> clazz) {
		int x = Whist.getRandom().nextInt(clazz.getEnumConstants().length);
		return clazz.getEnumConstants()[x];
	}

	public Game(Deck deck, Map<String, Integer> playerMap, int nStartCards, int winningScore, boolean enforceRules) {
		super();
		this.deck = deck;
		int nbInteractivePlayer = playerMap.get("nbInteractivePlayer");
		int nbRandomNPC = playerMap.get("nbRandomNPC");
		int nbLegalNPC = playerMap.get("nbLegalNPC");
		int nbSmartNPC = playerMap.get("nbSmartNPC");
		this.nbPlayers = nbInteractivePlayer + nbRandomNPC + nbLegalNPC + nbSmartNPC;
		this.nStartCards = nStartCards;
		this.winningScore = winningScore;
		this.enforceRules = enforceRules;
		this.trump = randomEnum(Whist.Suit.class);
		// initPlayers and deal cards

		// call the PlayerFactory to produce a list of players corresponding to the parameters.
		this.players = PlayerFactory.getInstance().getPlayers(nbInteractivePlayer,nbRandomNPC,nbLegalNPC,nbSmartNPC);

		// If there are any players need to be add to the listener list, add them.
		for (IPlayer player : this.players) {
			if (player instanceof LegalNPC) {
				addLegalNPCObserver((NPCObserver) player);
			}
		}
		for (IPlayer player : this.players) {
			if (player instanceof SmartNPC) {
				addSmartNPCObserver((NPCObserver) player);
			}
		}


		dealCards();

		// The smartNPC player need to know the trump suit to play.
		notifySmartNPC("trump", null, trump);
	}

	private void dealCards() {
		// sort and deal the cards to all the players
		Hand[] hands = deck.dealingOut(nbPlayers, nStartCards);
		for (int i = 0; i < nbPlayers; i++) {
			hands[i].sort(Hand.SortType.SUITPRIORITY, true);
		}

		// If the player is interactivePlayer, you need to make it interactivable.
		// add CardListener to it.
		for (int i = 0; i < nbPlayers; i++) {
			if (getPlayer(i) instanceof InteractivePlayer) {
				InteractivePlayer humanPlayer = (InteractivePlayer) getPlayer(i);
				humanPlayer.setInitHand(hands[i]);
				humanPlayer.addCardListener();
			} else
				getPlayer(i).setInitHand(hands[i]);
		}
	}

	private Card playTheleadCard(int nextPlayer) {
		Card played = null;
		String text = "Player " + nextPlayer + " taking lead...";

		// notify the Whist class to update status text
		notifyWhist("text", text);

		played = players.get(nextPlayer).play(); // call the player's own play() method to pick out a card
		played.transfer(trick, true); // transfer to trick (includes graphic effect)

		// notify the the listeners to update following changes
		notifyWhist("trick", trick);
		notifyLegalNPC("card", played, null);
		notifySmartNPC("card", played, null);

		return played;
	}

	private void checkLegality(Whist.Suit lead, int nextPlayer, Card played) { // check the whether it is legal to play the card
		if (played.getSuit() != lead && players.get(nextPlayer).getHand().getNumberOfCardsWithSuit(lead) > 0) {
			// Rule violation
			String violation = "Follow rule broken by player " + nextPlayer + " attempting to play " + played;
			System.out.println(violation);
			if (enforceRules) // force to quit if rule is enforced
				try {
					throw (new BrokeRuleException(violation));
				} catch (BrokeRuleException e) {
					e.printStackTrace();
					System.out.println("A cheating player spoiled the game!");
					System.exit(0);
				}
		}
		// End Check
	}

	private Card followTheLead(int nextPlayer, Whist.Suit lead) {
		Card played = null;

		// notify the Whist class to update status text
		String text = "Player " + nextPlayer + " thinking to follow...";
		notifyWhist("text", text);

		played = players.get(nextPlayer).play();// call the player's own play() method to pick out a card
		checkLegality(lead, nextPlayer, played); // check whether the card is played legally
		played.transfer(trick, true);// transfer to trick (includes graphic effect)

		// notify the the listeners to update following changes
		notifyWhist("trick", trick);
		notifySmartNPC("card", played, null);

		return played;
	}

	public boolean rankGreater(Card card1, Card card2) {
		return card1.getRankId() < card2.getRankId(); // Warning: Reverse rank order of cards (see comment on enum)
	}

	private Card winningCard(Card played, Card winningCard, int nextPlayer, Whist.Suit trump) {
		Card newWinningCard = winningCard;
		if ( // beat current winner with higher card
		(played.getSuit() == winningCard.getSuit() && rankGreater(played, winningCard)) ||
		// trumped when non-trump was winning
				(played.getSuit() == trump && winningCard.getSuit() != trump)) {
			System.out.println("NEW WINNER");
			trickWinner = players.get(nextPlayer);
			newWinningCard = played;
		}
		return newWinningCard;
	}

	public Optional<Integer> playGame() {
		// randomly choose the id of a player to play the lead
		int nextPlayer = Whist.getRandom().nextInt(nbPlayers);
		this.trickWinner = null;
		Whist.Suit lead;
		Card winningCard;
		Card played;

		while (haveCards()) {
			// figure the trick lead
			this.trick = new Hand(deck);

			// if there is a winner for last trick, this player should take this lead
			if (trickWinner != null)
				nextPlayer = trickWinner.getID();

			// Start the lead playing
			played = playTheleadCard(nextPlayer);


			lead = (Whist.Suit) played.getSuit();
			trickWinner = players.get(nextPlayer);
			winningCard = played;
			System.out.println("Player " + nextPlayer + " takes the lead: suit = " + played.getSuit() + ", rank = "
					+ played.getRankId());
			// End Lead

			// The other players should follow the lead to play
			for (int j = 1; j < nbPlayers; j++) {
				if (++nextPlayer >= nbPlayers)
					nextPlayer = 0; // From last back to first

				played = followTheLead(nextPlayer, lead);
				System.out.println("winning: suit = " + winningCard.getSuit() + ", rank = " + winningCard.getRankId());
				System.out.println("Player " + nextPlayer + " played: suit = " + played.getSuit() + ", rank = "
						+ played.getRankId());

				// iteratively figure out the winningCard of this trick, this step also
				// update the trickWinner of this trick
				winningCard = winningCard(played, winningCard, nextPlayer, trump);
			}

			GameGrid.delay(600);
			notifyWhist("trickclear", trick); // notify the whist to clear the table
			System.out.println("Player " + trickWinner.getID() + " wins trick." + "\n");
			trickWinner.win();

			// extract all the scores of all players for updating
			ArrayList<Integer> scoreList = new ArrayList<>();
			for(int i = 0; i < nbPlayers; i++){
				scoreList.add(players.get(i).getScore());
			}

			// notify the related partners to update the change
			// the players should be told to end this trick
			notifyWhist("score", scoreList);
			notifyLegalNPC("clear", null, null);
			notifySmartNPC("clear", null, null);

			// if winningScore is reached. The round ends.
			if (winningScore == trickWinner.getScore()) {
				int winningId = trickWinner.getID();
				System.out.println("----------------Winner is Player " + winningId + "------------");
				return Optional.of(winningId);
			}
		}

		// All the cards in the players' hand are played. Reset the Game for a new round
		dealCards();
		this.trump = randomEnum(Whist.Suit.class);
		notifySmartNPC("trump", null, trump);
		return Optional.empty();
	}

}
