package game.engine;

import java.util.ArrayList;
import java.util.Collections;

import game.engine.cards.Card;
import game.engine.cells.*;
import game.engine.exceptions.InvalidMoveException;
import game.engine.monsters.Monster;

public class Board {
	private Cell[][] boardCells;
	private static ArrayList<Monster> stationedMonsters; 
	private static ArrayList<Card> originalCards;
	public static ArrayList<Card> cards;
	
	public Board(ArrayList<Card> readCards) {
		this.boardCells = new Cell[Constants.BOARD_ROWS][Constants.BOARD_COLS];
		stationedMonsters = new ArrayList<Monster>();
		originalCards = readCards;
		this.setCardsByRarity();
		Board.reloadCards();
	}

	private int[] indexToRowCol(int index) {
		index = index % Constants.BOARD_SIZE;
		int row = (index / Constants.BOARD_ROWS);
		int col = index % Constants.BOARD_COLS;
		if(row % 2 == 1) {
			col = Constants.BOARD_COLS - 1 - col;
		}
		return new int[] {row, col};
	}

	public void initializeBoard(ArrayList<Cell> specialCells) {
		for(int i = 0; i < Constants.BOARD_SIZE; i++) {
			if(i % 2 == 0) {
				setCell(i, new Cell("Rest Cell"));
			} else {
				setCell(i, specialCells.remove(0));
			}
		}

		for(int i = 0; i < Constants.MONSTER_CELL_INDICES.length; i++) {
			int monsterIndex = Constants.MONSTER_CELL_INDICES[i];
			if (!stationedMonsters.isEmpty()){
				Monster monster = stationedMonsters.get(i);
				monster.setPosition(monsterIndex);
				this.setCell(monsterIndex, new MonsterCell(monster.getName(),monster));
			}
		}

		for(int i = 0; i < Constants.CARD_CELL_INDICES.length; i++) {
			int cardIndex = Constants.CARD_CELL_INDICES[i];
			this.setCell(cardIndex, new CardCell("Card Cell"));
		}

		for(int i = 0; i < Constants.CONVEYOR_CELL_INDICES.length; i++) {
			int conveyIndex = Constants.CONVEYOR_CELL_INDICES[i];
			this.setCell(conveyIndex, specialCells.get(2 * i));
		}

		for(int i = 0; i < Constants.SOCK_CELL_INDICES.length; i++) {
			int sockIndex = Constants.SOCK_CELL_INDICES[i];
			this.setCell(sockIndex, specialCells.get(2 * i + 1));
		}
	}

	private void setCardsByRarity() {
		ArrayList<Card> cardsByRarity = new ArrayList<Card>();
		for(Card card : Board.originalCards) {
			for(int i = 0; i < card.getRarity(); i++) {
				cardsByRarity.add(card);
			}
		}
		Board.originalCards = cardsByRarity;
	}

	public static void reloadCards() {
		Collections.shuffle(Board.originalCards);
		Board.cards = Board.originalCards;
	}

	public static Card drawCard() {
		if(Board.cards.isEmpty()) {
			Board.reloadCards();
		}
		return Board.cards.remove(0);
	}

	public void moveMonster(Monster currentMonster, int roll, Monster opponentMonster) throws InvalidMoveException {
		if(currentMonster.getPosition() + roll == opponentMonster.getPosition()) {
			throw new InvalidMoveException("Cannot move to a cell occupied by the opponent monster.");
		}
		currentMonster.move(roll);
		if(currentMonster.getConfusionTurns() > 0) {
			currentMonster.decrementConfusion();
		}
		this.getCell(currentMonster.getPosition()).onLand(currentMonster, opponentMonster);
		this.updateMonsterPositions(currentMonster, opponentMonster);
	}

	private void updateMonsterPositions(Monster player, Monster opponent) {
		for(int i = 0; i < Constants.BOARD_SIZE; i++) {
			Cell cell = this.getCell(i);
			cell.setMonster(null);
		}
		this.getCell(player.getPosition()).setMonster(player);
		this.getCell(opponent.getPosition()).setMonster(opponent);
	}

	private Cell getCell(int index) {
		int[] rowCol = this.indexToRowCol(index);
		return this.boardCells[rowCol[0]][rowCol[1]];
	}

	private void setCell(int index, Cell cell) {
		int[] rowCol = this.indexToRowCol(index);
		this.boardCells[rowCol[0]][rowCol[1]] = cell;
	}
	
	public Cell[][] getBoardCells() {
		return boardCells;
	}
	
	public static ArrayList<Monster> getStationedMonsters() {
		return stationedMonsters;
	}
	
	public static void setStationedMonsters(ArrayList<Monster> stationedMonsters) {
		Board.stationedMonsters = stationedMonsters;
	}

	public static ArrayList<Card> getOriginalCards() {
		return originalCards;
	}
	
	public static ArrayList<Card> getCards() {
		return cards;
	}
	
	public static void setCards(ArrayList<Card> cards) {
		Board.cards = cards;
	}
}
