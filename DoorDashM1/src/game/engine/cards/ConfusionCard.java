package game.engine.cards;

import game.engine.monsters.Monster;
import game.engine.Role;

public class ConfusionCard extends Card {
	private int duration;
	
	public ConfusionCard(String name, String description, int rarity, int duration) {
		super(name, description, rarity, false);
		this.duration = duration;
	}

	@Override
	public void performAction(Monster monster, Monster opponent) {
		Role monsterRole = monster.getRole();
		Role opponentRole = opponent.getRole();
		monster.setConfusionTurns(duration);
		monster.setRole(opponentRole);
		opponent.setConfusionTurns(duration);
		opponent.setRole(monsterRole);
	
	}
	
	public int getDuration() {
		return duration;
	}

}
