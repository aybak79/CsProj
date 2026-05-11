package game.engine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import game.engine.cards.Card;
import game.engine.cells.CardCell;
import game.engine.cells.DoorCell;
import game.engine.dataloader.DataLoader;
import game.engine.exceptions.InvalidMoveException;
import game.engine.exceptions.OutOfEnergyException;
import game.engine.monsters.*;
import game.engine.cells.*;;;

public class Game {
	private Board board;
	private ArrayList<Monster> allMonsters; 
	private Monster player;
	private Monster opponent;
	private Monster current;
	
	public Game(Role playerRole) throws IOException {
		this.board = new Board(DataLoader.readCards());
		
		this.allMonsters = DataLoader.readMonsters();
		
		this.player = selectRandomMonsterByRole(playerRole);
		this.opponent = selectRandomMonsterByRole(playerRole == Role.SCARER ? Role.LAUGHER : Role.SCARER);
		this.current = player;
		
		allMonsters.remove(player);
		allMonsters.remove(opponent);
		
		Board.setStationedMonsters(allMonsters);
		board.initializeBoard(DataLoader.readCells());
	}
	
	public Board getBoard() {
		return board;
	}
	
	public ArrayList<Monster> getAllMonsters() {
		return allMonsters; 
	}
	
	public Monster getPlayer() {
		return player;
	}
	
	public Monster getOpponent() {
		return opponent;
	}
	
	public Monster getCurrent() {
		return current;
	}
	
	public void setCurrent(Monster current) {
		this.current = current;
	}
	
	private Monster selectRandomMonsterByRole(Role role) {
		Collections.shuffle(allMonsters);
	    return allMonsters.stream()
	    		.filter(m -> m.getRole() == role)
	    		.findFirst()
	    		.orElse(null);
	}
	
	private Monster getCurrentOpponent() {
		return current == player ? opponent : player;
	}

	private int rollDice() {
		Random rand = new Random();
		return rand.nextInt(6) + 1;
	}
	
	public void usePowerup() throws OutOfEnergyException {
		if (current.getEnergy() < Constants.POWERUP_COST)
			throw new OutOfEnergyException("Not enough energy to use powerup");
		
		current.executePowerupEffect(getCurrentOpponent());
		current.setEnergy(current.getEnergy() - Constants.POWERUP_COST);
	}
	
	public TurnResult playTurn() throws InvalidMoveException {
		if (current.isFrozen()) {
			current.setFrozen(false);
			switchTurn();
			return new TurnResult(0, null, false, true);
		}

		int roll = rollDice();
		Cell landedCell = board.moveMonster(current, roll, getCurrentOpponent());
		Card cardDrawn = null;
		boolean landedOnDoor = false;

		if (landedCell instanceof CardCell) {
			cardDrawn = Board.getDrawnCards().getFirst();
		} else if (landedCell instanceof DoorCell) {
			landedOnDoor = true;
		}

		switchTurn();
		return new TurnResult(roll, cardDrawn, landedOnDoor, false);
	}
	
	private void switchTurn() {
		this.setCurrent(getCurrentOpponent());
	}
	
	private boolean checkWinCondition(Monster monster) {
		return monster.getPosition() == Constants.WINNING_POSITION && 
		       monster.getEnergy() >= Constants.WINNING_ENERGY;
	}
	
	public Monster getWinner() {
		if (checkWinCondition(player)) 
			return player;
		
		if (checkWinCondition(opponent)) 
			return opponent;
		
		return null;
	}
	
}