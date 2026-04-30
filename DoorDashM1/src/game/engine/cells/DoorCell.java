package game.engine.cells;

import game.engine.Role;
import game.engine.interfaces.CanisterModifier;
import game.engine.monsters.*;
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
	public void onLand(Monster landingMonster, Monster opponentMonster) {
		super.onLand(landingMonster, opponentMonster);
		if (!isActivated()) {
			int energyChange = this.getEnergy();
			if (!landingMonster.isShielded() || landingMonster.getRole() == this.getRole()) {
				for (Monster stationed : Board.getStationedMonsters()) {
					if (stationed.getRole() == landingMonster.getRole()) {
						this.modifyCanisterEnergy(stationed, energyChange);
					}
				}
				this.setActivated(true);
			}
			this.modifyCanisterEnergy(landingMonster, energyChange);
		}
	}
	@Override
	public void modifyCanisterEnergy(Monster monster, int canisterValue) {
		if (monster.getRole() == this.getRole()) {
			monster.alterEnergy(canisterValue);
		} else {
			monster.alterEnergy(-canisterValue);
		}
	}

}
