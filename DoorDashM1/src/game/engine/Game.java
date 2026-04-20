package game.engine;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import game.engine.dataloader.DataLoader;
import game.engine.monsters.*;
import game.engine.exceptions.*;


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

		ArrayList<Monster> stationedMonsters = new ArrayList<>(allMonsters);
		stationedMonsters.remove(player);
		stationedMonsters.remove(opponent);
		allMonsters.remove(player);
		allMonsters.remove(opponent);
		Board.setStationedMonsters(stationedMonsters);
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

	private Monster getCurrentOpponent(){
		return current == player ? opponent : player;
	}

	private int rollDice(){
		return (int)(Math.random() * 6) +1;// Returns a random number between 1 and 6
	}

	public void usePowerup() throws OutOfEnergyException{
		if(current.getEnergy() < Constants.POWERUP_COST) {
			throw new OutOfEnergyException("Not enough energy to use power-up.");
		}
		current.executePowerupEffect(getCurrentOpponent());
		current.alterEnergy(- Constants.POWERUP_COST);
	}

	private void switchTurn(){
		current = getCurrentOpponent();
	}

	private boolean checkWinCondition(Monster monster){
		if ((monster.getPosition()==99)&&monster.getEnergy()>=1000){
			return true;
		}
		return false;
	}

	public void playTurn() throws InvalidMoveException{
		if(current.isFrozen()){
			current.setFrozen(false);
		} else{
			int roll = rollDice();
			board.moveMonster(current, roll, getCurrentOpponent());
		}
		switchTurn();
	}
	
	public Monster getWinner(){
		if(checkWinCondition(player)) {
			return player;
		} else if(checkWinCondition(opponent)) {
			return opponent;
		}
		return null; // No winner yet
	}
}