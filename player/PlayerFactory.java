/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *                          Team 12                          *
 * Chen Fu(987369), Yizhou Zhu(1034676), Shengqi Zhou(893295)*
 *                   last update: 2020.6.6                   *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package player;

import java.util.ArrayList;

public class PlayerFactory {
	// Singleton Factory method. Only on Factory is allowed to generate Players.

	private static final PlayerFactory playerFactory = new PlayerFactory();

	private PlayerFactory() {
		
	}

	public static PlayerFactory getInstance() {
		return playerFactory;
	}

	public ArrayList<IPlayer> getPlayers(int nbInteractivePlayer,int nbRandomNPC,int nbLegalNPC,int nbSmartNPC){
		// Generate a list of players.
		int i = 0;
		ArrayList<IPlayer> players = new ArrayList<>();
		for (int j = 0; j < nbInteractivePlayer; j++) {
			players.add(getPlayer("InteractivePlayer",i));
			i++;
		}
		for (int j = 0; j < nbRandomNPC; j++) {
			players.add(getPlayer("RandomNPC",i));
			i++;
		}
		for (int j = 0; j < nbLegalNPC; j++) {
			players.add(getPlayer("LegalNPC",i));
			i++;
		}
		for (int j = 0; j < nbSmartNPC; j++) {
			players.add(getPlayer("SmartNPC",i));
			i++;
		}
		return players;
	}



	public IPlayer getPlayer(String name, int id){ // return new player instance via the name
		IPlayer player;
		if(name.equals("InteractivePlayer"))
			player = new InteractivePlayer(id);
		else if(name.equals("RandomNPC"))
			player = new RandomNPC(id);
		else if(name.equals("LegalNPC"))
			player = new LegalNPC(id);
		else
			player = new SmartNPC(id);
		return player;
	}

}
