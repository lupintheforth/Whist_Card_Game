/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *                          Team 12                          *
 * Chen Fu(987369), Yizhou Zhu(1034676), Shengqi Zhou(893295)*
 *                   last update: 2020.6.6                   *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package whist;

import ch.aplu.jcardgame.*;
import ch.aplu.jgamegrid.*;
import exception.NumberPlayersException;

import java.awt.Color;
import java.awt.Font;
import java.io.IOException;
import java.util.*;

@SuppressWarnings("serial")
public class Whist extends CardGame implements WhistObserver {
	// this class is the main system and GUI of the Whist game

	// .................... system and GUI instances ....................//
	public enum Suit {
		SPADES, HEARTS, DIAMONDS, CLUBS
	}

	public enum Rank {
		// Reverse order of rank importance (see rankGreater() below)
		// Order of cards is tied to card images
		ACE, KING, QUEEN, JACK, TEN, NINE, EIGHT, SEVEN, SIX, FIVE, FOUR, THREE, TWO
	}

	private final Deck deck = new GameDeck(Suit.values(), Rank.values(), "cover");

	// UI settings for the Card game

	final String trumpImage[] = { "bigspade.gif", "bigheart.gif", "bigdiamond.gif", "bigclub.gif" };
	private final String version = "1.0";
	private final int handWidth = 400;
	private final int trickWidth = 40;
	private final Location[] handLocations = { new Location(350, 625), new Location(75, 350), new Location(350, 75),
			new Location(625, 350) };
	private final Location[] scoreLocations = { new Location(575, 675), new Location(25, 575), new Location(575, 25),
			new Location(650, 575) };
	private Actor[] scoreActors = { null, null, null, null };
	private final Location trickLocation = new Location(350, 350);
	private final Location textLocation = new Location(350, 450);
	private Location hideLocation = new Location(-500, -500);
	private Location trumpsActorLocation = new Location(50, 50);
	Font bigFont = new Font("Serif", Font.BOLD, 36);

	// The gamee logic is insanities in the round variable
	private Game round;


	// .................... system and GUI instances ....................//

	// .................... Random ....................//
	private static Random random;

	public static Random getRandom() {
		return random;
	}
	// .................... Random ....................//

	// .................... Observer: update GUI ....................//

	@Override
	public void updateTrick(Hand trick) { // Using observer pattern to notify the Whist instance to update the graphics
		trick.setView(this, new RowLayout(trickLocation, (trick.getNumberOfCards() + 2) * trickWidth));
		trick.draw();
		trick.setVerso(false);
	}

	@Override
	public void updateScore(ArrayList<Integer> scoreList) { // Using observer pattern to notify the Whist instance to update the score graphics
		for (int i = 0; i < scoreList.size(); i++) {
			removeActor(scoreActors[i]);
			scoreActors[i] = new TextActor(String.valueOf(scoreList.get(i)), Color.WHITE, bgColor,
					bigFont);
			addActor(scoreActors[i], scoreLocations[i]);
		}
	}

	@Override
	public void updateTrickClear(Hand trick) { // Using observer pattern to notify the Whist instance to clear the trick cards graphics
		trick.setView(this, new RowLayout(hideLocation, 0));
		trick.draw();
	}

	@Override
	public void updateStatus(String text) {
		setStatusText(text);
	}

	@Override
	public void updateClear() {
		removeAllActors();
	} // remove all the graphic actors when called
	// .................... Observer: update GUI ....................//

	// .................... System logic ....................//
	private void initScoreHandTrump(int nbPlayers) { // initGUI
		removeAllActors();
		// initialize the score graphic
		for (int i = 0; i < nbPlayers; i++) {
			String score = Integer.toString(round.getPlayer(i).getScore());
			scoreActors[i] = new TextActor(score, Color.WHITE, bgColor, bigFont);
			addActor(scoreActors[i], scoreLocations[i]);
		}

		// initialize the hand graphic
		RowLayout[] layouts = new RowLayout[nbPlayers];
		for (int i = 0; i < nbPlayers; i++) {
			layouts[i] = new RowLayout(handLocations[i], handWidth);
			layouts[i].setRotationAngle(90 * i);
			// layouts[i].setStepDelay(10);
			round.getPlayer(i).getHand().setView(this, layouts[i]);
			round.getPlayer(i).getHand().setTargetArea(new TargetArea(trickLocation));
			round.getPlayer(i).getHand().draw();
		}

		// Select and display the trump suit
		final Actor trumpsActor = new Actor("sprites/" + trumpImage[round.getTrump().ordinal()]);
		addActor(trumpsActor, trumpsActorLocation);
		// End trump suit
//		for (int i = 1; i < nbPlayers; i++) // This code can be used to visually hide
//			// the cards in a hand (make them face down)
//			round.getPlayer(i).getHand().setVerso(true);
	}

	public Whist() throws IOException {
		super(700, 700, 30);
		setTitle("Whist (V" + version + ") Constructed for UofM SWEN30006 with JGameGrid (www.aplu.ch)");
		setStatusText("Initializing...");

		// .................... properties ....................//
		PropertyManagement propertyManagement = PropertyManagement.getInstance();
		//  * * * * * * property file name is "whist.properties"! * * * * * *  //
		propertyManagement.setWhistProperties("whist.properties");
		int seed = Integer.parseInt(propertyManagement.getWhistProperties("seed"));
		random = new Random(seed);
		int nbInteractivePlayer = Integer.parseInt(propertyManagement.getWhistProperties("nbInteractivePlayer"));
		int nbRandomNPC = Integer.parseInt(propertyManagement.getWhistProperties("nbRandomNPC"));
		int nbLegalNPC = Integer.parseInt(propertyManagement.getWhistProperties("nbLegalNPC"));
		int nbSmartNPC = Integer.parseInt(propertyManagement.getWhistProperties("nbSmartNPC"));
		int nbStartCards = Integer.parseInt(propertyManagement.getWhistProperties("nbStartCards"));
		int winningScore = Integer.parseInt(propertyManagement.getWhistProperties("winningScore"));
		boolean enforceRules = Boolean.parseBoolean(propertyManagement.getWhistProperties("enforceRules"));

		Map<String, Integer> playerMap = new HashMap<>();
		playerMap.put("nbInteractivePlayer", nbInteractivePlayer);
		playerMap.put("nbRandomNPC", nbRandomNPC);
		playerMap.put("nbLegalNPC", nbLegalNPC);
		playerMap.put("nbSmartNPC", nbSmartNPC);

		int nbPlayers = nbInteractivePlayer + nbRandomNPC + nbLegalNPC + nbSmartNPC;
		try {
			if (nbPlayers != 4) {
				String violation = "The number of players should be 4!";
				System.out.println(violation);
				throw new NumberPlayersException(violation);
			} else if (nbInteractivePlayer > 1) {
				String violation = "There should be only 1 interactive player!";
				System.out.println(violation);
				throw new NumberPlayersException(violation);
			}
		} catch (Exception e) {
			System.exit(0);
		}
		// .................... properties ....................//

		round = new Game(this.deck, playerMap, nbStartCards, winningScore, enforceRules);
		round.addWhistObserver((WhistObserver) this); // Use Observer mode to add the Whist instance to listen to the Game logic.
		Optional<Integer> winner;
		// Play till the winner is present, otherwise, deal the card and reset all the graphics.
		do {
			initScoreHandTrump(nbPlayers);
			winner = round.playGame();
		} while (!winner.isPresent());
		addActor(new Actor("sprites/gameover.gif"), textLocation);
		setStatusText("Game over. Winner is player: " + winner.get());
		refresh();
	}

	public static void main(String[] args) throws IOException {
		new Whist();
	}
	// .................... System logic ....................//

}
