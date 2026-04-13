package game.engine.cards;

import game.engine.monsters.Monster;
public class ShieldCard extends Card {
	
	public ShieldCard(String name, String description, int rarity) {
		super(name, description, rarity, true); 
	}

	@Override
	public void performAction(Monster monster, Monster opponent) {
		monster.setShielded(true);
		opponent.setShielded(false);
	}
}
