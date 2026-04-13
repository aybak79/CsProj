package game.engine.cards;

import game.engine.interfaces.CanisterModifier;
import game.engine.monsters.Monster;

public class EnergyStealCard extends Card implements CanisterModifier {
	private int energy;

	public EnergyStealCard(String name, String description, int rarity, int energy) {
		super(name, description, rarity, true);
		this.energy = energy;
	}

	public void modifyCanisterEnergy(Monster monster, int CanisterValue) {
		monster.setEnergy(monster.getEnergy() + CanisterValue);
	}

	public void performAction(Monster monster, Monster opponent) {
		if(opponent.isShielded()) {
			opponent.setShielded(false);
		} else {
			int energyToSteal = Math.min(energy, opponent.getEnergy());
			this.modifyCanisterEnergy(monster, energyToSteal);
			this.modifyCanisterEnergy(opponent, -energyToSteal);
		}
	}
	
	public int getEnergy() {
		return energy;
	}
	
}
