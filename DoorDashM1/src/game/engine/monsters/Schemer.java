package game.engine.monsters;

import game.engine.Constants;
import game.engine.Role;

public class Schemer extends Monster {
	
	public Schemer(String name, String description, Role role, int energy) {
		super(name, description, role, energy);
	}

	public void executePowerupEffect(Monster opponentMonster) {
		int total = 0;
		total+= stealEnergyFrom(opponentMonster);
	}

	private int stealEnergyFrom(Monster target) {
    int steal = Math.min(Constants.SCHEMER_STEAL, target.getEnergy());
    target.alterEnergy(-steal);
    return steal;
	}
	
}
