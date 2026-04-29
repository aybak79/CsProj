package game.engine.cards;

import game.engine.interfaces.CanisterModifier;
import game.engine.monsters.Monster;

public class EnergyStealCard extends Card implements CanisterModifier {
	private int energy;

	public EnergyStealCard(String name, String description, int rarity, int energy) {
		super(name, description, rarity, true);
		this.energy = energy;
	}

	@Override
	public void modifyCanisterEnergy(Monster monster, int CanisterValue) {
		monster.alterEnergy(CanisterValue);
	}

	@Override
	public void performAction(Monster monster, Monster opponent) {
		int energyToSteal = Math.min(this.energy, opponent.getEnergy());
		if(!opponent.isShielded()) {
			this.modifyCanisterEnergy(monster, energyToSteal);
		}
		this.modifyCanisterEnergy(opponent, -energyToSteal);
		
	}
	
	public int getEnergy() {
		return energy;
	}
	
}
