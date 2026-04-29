package game.engine.cards;

import game.engine.monsters.Monster;

public class StartOverCard extends Card {

	public StartOverCard(String name, String description, int rarity, boolean lucky) {
		super(name, description, rarity, lucky);
	}

	@Override
	public void performAction(Monster monster, Monster opponent) {
		if(this.isLucky()) opponent.setPosition(0);
		else monster.setPosition(0);
	}
}
