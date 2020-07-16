/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *                          Team 12                          *
 * Chen Fu(987369), Yizhou Zhu(1034676), Shengqi Zhou(893295)*
 *                   last update: 2020.6.6                   *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package whist;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;
import player.NPCObserver;

import java.util.ArrayList;

public abstract class Subject {
//	Three obeserver lists. One for the whist to update graphic,
//	one for legalNPC to update the lead Suit. One for the smartNPC to
//	update the cards of this trick.

	protected ArrayList<WhistObserver> whistObservers;
	protected ArrayList<NPCObserver> legalNPCObservers;
	protected ArrayList<NPCObserver> smartNPCObservers;

	public Subject() {
		this.whistObservers = new ArrayList<>();
		this.legalNPCObservers = new ArrayList<>();
		this.smartNPCObservers = new ArrayList<>();
	}

	public void addWhistObserver(WhistObserver observer) {
		whistObservers.add(observer);
	};

	public void removeWhistObserver(WhistObserver observer) {
		whistObservers.remove(observer);
	}

	public void addLegalNPCObserver(NPCObserver player) {
		legalNPCObservers.add(player);
	}

	public void removeLegalNPCObserver(NPCObserver player) {
		legalNPCObservers.remove(player);
	}

	public void addSmartNPCObserver(NPCObserver player) {
		smartNPCObservers.add(player);
	}

	public void removeSmartNPCObserver(NPCObserver player) {
		smartNPCObservers.remove(player);
	}

	@SuppressWarnings("unchecked")
	public void notifyWhist(String name, Object object) {
		if (name.equals("trick")) {
			for (WhistObserver observer : this.whistObservers) {
				// tell whist to update the trick graphic
				observer.updateTrick((Hand) object);
			}
		} else if (name.equals("score")) {
			for (WhistObserver observer : this.whistObservers) {
				// tell whist to update the score graphic
				observer.updateScore((ArrayList<Integer>) object);
			}
		} else if (name.equals("trickclear")) {
			for (WhistObserver observer : this.whistObservers) {
				// tell the whist to clear the trick area graphic
				observer.updateTrickClear((Hand) object);
			}
		} else if (name.equals("text")) {
			for (WhistObserver observer : this.whistObservers) {
				// tell the whist to update the status text
				observer.updateStatus((String) object);
			}
		} else {
			for (WhistObserver observer : this.whistObservers) {
				// tell the whist to clear out all the actors
				observer.updateClear();
			}
		}
	}

	public void notifyLegalNPC(String name, Card card, Whist.Suit trump) {
		if (name.equals("card") && !legalNPCObservers.isEmpty())
			// transfer the latest card to the listener
			for (NPCObserver player : this.legalNPCObservers) {
				player.update(card);
			}
		else if (name.equals("clear") && !legalNPCObservers.isEmpty()) {
			// instruct the listener to clear one of its instance
			for (NPCObserver player : this.legalNPCObservers) {
				player.clear();
			}
		}
	}

	public void notifySmartNPC(String name, Card card, Whist.Suit trump) {
		if (name.equals("card") && !smartNPCObservers.isEmpty())
			// transfer the latest card to the listener
			for (NPCObserver player : this.smartNPCObservers) {
				player.update(card);
			}
		else if (name.equals("trump") && !smartNPCObservers.isEmpty()) {
			// transfer the latest trump Suit to the listener
			for (NPCObserver player : this.smartNPCObservers) {
				player.updateTrump(trump);
			}
		} else if (name.equals("clear") && !smartNPCObservers.isEmpty()) {
			// instruct the listener to clear one of its instance
			for (NPCObserver player : this.smartNPCObservers) {
				player.clear();
			}
		}
	}

}
