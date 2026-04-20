package game.engine.monsters;

import game.engine.Constants;
import game.engine.Role;
import game.engine.Board;

public class Schemer extends Monster {
	
	public Schemer(String name, String description, Role role, int energy) {
		super(name, description, role, energy);
	}

	@Override
	public void executePowerupEffect(Monster opponentMonster){
		int total = 0;
		total+= stealEnergyFrom(opponentMonster);
		for(Monster stationed : Board.getStationedMonsters()) {
			if(stationed != opponentMonster) {
				total+= stealEnergyFrom(stationed);
			}
		}
		this.alterEnergy(total);
	}

	private int stealEnergyFrom(Monster target) {
    	int steal = Math.min(Constants.SCHEMER_STEAL, target.getEnergy());
    	target.alterEnergy(-steal);
    	return steal;
	}

	@Override
	public void setEnergy(int energy){
		super.setEnergy(energy + 10);
	}
	
}
