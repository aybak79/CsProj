package game.engine.cells;

import game.engine.Role;
import game.engine.interfaces.CanisterModifier;
import game.engine.monsters.*;
import game.engine.cards.*;
import game.engine.Board;

public class DoorCell extends Cell implements CanisterModifier {
	private Role role;
	private int energy;
	private boolean activated;
	
	public DoorCell(String name, Role role, int energy) {
		super(name);
		this.role = role;
		this.energy = energy;
		this.activated = false;
	}
	
	public Role getRole() {
		return role;
	}
	
	public int getEnergy() {
		return energy;
	}
	
	public boolean isActivated() {
		return activated;
	}

	public void setActivated(boolean isActivated) {
		this.activated = isActivated;
	}

	@Override
	public void onLand(Monster landingMonster, Monster opponentMonster){
		super.onLand(landingMonster, opponentMonster);
		if(!this.isActivated()){
			modifyCanisterEnergy(landingMonster, this.getEnergy());
			for (Monster stationed : Board.getStationedMonsters()) {
            	if (stationed.getRole() == landingMonster.getRole()) {
                	modifyCanisterEnergy(stationed, this.getEnergy());
            }
			if(this.getEnergy() != 0) {
				this.setActivated(true);
			}
		}
		}
	}
	@Override
	public void modifyCanisterEnergy(Monster monster, int canisterValue) {
    	monster.alterEnergy(canisterValue);
	}



}
