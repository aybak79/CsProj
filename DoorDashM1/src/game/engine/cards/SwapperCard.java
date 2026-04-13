package game.engine.cards;

import game.engine.monsters.Monster;

public class SwapperCard extends Card {

	public SwapperCard(String name, String description, int rarity) {
		super(name, description, rarity, true);
	}
	
	@Override
	public void performAction(Monster monster, Monster opponent) {
		int monsterPosition = monster.getPosition();
		int opponentPosition = opponent.getPosition();
		if(monsterPosition < opponentPosition) {
			monster.setPosition(opponentPosition);
			opponent.setPosition(monsterPosition);
		}
	}
}
