package game.engine;

import game.engine.cards.Card;

public class TurnResult {
    public int roll;
    public Card cardDrawn;
    public boolean landedOnDoor;
    public boolean turnSkipped;
    
    public TurnResult(int roll, Card cardDrawn, boolean landedOnDoor, boolean turnSkipped) {
        this.roll = roll;
        this.cardDrawn = cardDrawn;
        this.landedOnDoor = landedOnDoor;
        this.turnSkipped = turnSkipped;
    }
}
