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

	private Monster getCurrentOpponent(){
		return this.current == this.player ? this.opponent : this.player;
	}

	private int rollDice(){
		return (int)(Math.random() * 6) +1;// Returns a random number between 1 and 6
	}

	public void usePowerup() throws OutOfEnergyException{
		if(this.current.getEnergy() < Constants.POWERUP_COST) {
			throw new OutOfEnergyException("Not enough energy to use power-up.");
		}
		this.current.executePowerupEffect(getCurrentOpponent());
		this.current.setEnergy(this.current.getEnergy() - Constants.POWERUP_COST);
	}

	private void switchTurn(){
		this.current = getCurrentOpponent();
	}

	private boolean checkWinCondition(Monster monster){
		return monster.getPosition() == Constants.WINNING_POSITION && monster.getEnergy() >= Constants.WINNING_ENERGY;
	}

	public void playTurn() throws InvalidMoveException{
		if(this.current.isFrozen()){
			this.current.setFrozen(false);
		} else{
			int roll = rollDice();
			this.board.moveMonster(this.current, roll, this.getCurrentOpponent());
		}
		this.switchTurn();
	}
	
	public Monster getWinner(){
		if(checkWinCondition(this.player)) return this.player;
		if(checkWinCondition(this.opponent)) return this.opponent;
		return null;
	}
}