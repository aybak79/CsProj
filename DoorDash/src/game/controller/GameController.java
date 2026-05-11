package game.controller;

import game.engine.Game;
import game.engine.TurnResult;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class GameController {
    static Game game;
    @FXML private Button rollButton;
    @FXML private Button activateButton;
    @FXML private TextField playerORole;
    @FXML private TextField playerCRole;
    @FXML private TextField playerType;
    @FXML private TextField playerName;
    @FXML private TextField playerEnergy;
    @FXML private TextField opponentORole;
    @FXML private TextField opponentCRole;
    @FXML private TextField opponentType;
    @FXML private TextField opponentName;
    @FXML private TextField opponentEnergy;
    @FXML private ImageView player;
    @FXML private ImageView opponent;
    @FXML private ImageView dice;
    @FXML private GridPane board;
    @FXML private ImageView playerShield;
    @FXML private ImageView playerFreeze;
    @FXML private ImageView playerConfusion;
    @FXML private ImageView opponentShield;
    @FXML private ImageView opponentFreeze;
    @FXML private ImageView opponentConfusion;
    @FXML private ImageView playerSelector;
    @FXML private ImageView opponentSelector;
    @FXML
    public void initialize() {
        playerORole.setText(game.getPlayer().getOriginalRole().toString());
        playerType.setText(game.getPlayer().getClass().getSimpleName());
        playerName.setText(game.getPlayer().getName());
        opponentORole.setText(game.getOpponent().getOriginalRole().toString());
        opponentType.setText(game.getOpponent().getClass().getSimpleName());
        opponentName.setText(game.getOpponent().getName());
        updateUI(new TurnResult(0, null, false, false));
    }

    private void updateUI(TurnResult turnResult) {
        playerCRole.setText(game.getPlayer().getRole().toString());
        playerEnergy.setText(String.valueOf(game.getPlayer().getEnergy()));
        opponentCRole.setText(game.getOpponent().getRole().toString());
        opponentEnergy.setText(String.valueOf(game.getOpponent().getEnergy()));
        if (turnResult.roll > 0) {
            dice.setImage(new javafx.scene.image.Image(getClass().getResourceAsStream("/game/view/assets/" + turnResult.roll + ".png")));
        }
        if (game.getPlayer() == game.getCurrent()) {
            playerSelector.setVisible(true);
            opponentSelector.setVisible(false);
        } else if (game.getOpponent() == game.getCurrent()) {
            playerSelector.setVisible(false);
            opponentSelector.setVisible(true);
        } else {
            playerSelector.setVisible(false);
            opponentSelector.setVisible(false);
        }
        if(game.getPlayer().isShielded()) {
            playerShield.setVisible(true);
        } else {
            playerShield.setVisible(false);
        }
        if(game.getPlayer().isFrozen()) {
            playerFreeze.setVisible(true);
        } else {
            playerFreeze.setVisible(false);
        }
        if(game.getPlayer().isConfused()) {
            playerConfusion.setVisible(true);
        } else {
            playerConfusion.setVisible(false);
        }
        if(game.getOpponent().isShielded()) {
            opponentShield.setVisible(true);
        } else {
            opponentShield.setVisible(false);
        }
        if(game.getOpponent().isFrozen()) {
            opponentFreeze.setVisible(true);
        } else {
            opponentFreeze.setVisible(false);
        }
        if(game.getOpponent().isConfused()) {
            opponentConfusion.setVisible(true);
        } else {
            opponentConfusion.setVisible(false);
        }
        if (game.getWinner() != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/game/view/views/GameOverView.fxml"));
                Scene scene = new Scene(loader.load());
                Stage stage = (Stage) rollButton.getScene().getWindow();
                stage.setScene(scene);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        updateBoardImages(board);
        if(turnResult.landedOnDoor) {
            if(game.getCurrent() == game.getPlayer()) {
                for(Node n :  ((StackPane)opponent.getParent()).getChildren()) {
                    if(n instanceof ImageView && n != opponent && game.getOpponent().getPosition() != 99) {
                        ((ImageView) n).setImage(new javafx.scene.image.Image(getClass().getResourceAsStream("/game/view/assets/door-closed.png")));
                    }
                }
            } else {
                for(Node n :  ((StackPane)player.getParent()).getChildren()) {
                    if(n instanceof ImageView && n != player && game.getPlayer().getPosition() != 99) {
                        ((ImageView) n).setImage(new javafx.scene.image.Image(getClass().getResourceAsStream("/game/view/assets/door-closed.png")));
                    }
                }
            }
        }
    }

    private void updateBoardImages(GridPane board) {
        if (player.getParent() != null) ((Pane) player.getParent()).getChildren().remove(player);
        int[] playerPos = intToCoords(game.getPlayer().getPosition() % 100);
        StackPane playerCell = (StackPane) getCell(playerPos[0], playerPos[1]);
        playerCell.getChildren().add(player);
        if (opponent.getParent() != null) ((Pane) opponent.getParent()).getChildren().remove(opponent);
        int[] opponentPos = intToCoords(game.getOpponent().getPosition());
        StackPane opponentCell = (StackPane) getCell(opponentPos[0], opponentPos[1]);
        opponentCell.getChildren().add(opponent);
}

    private Node getCell(int row, int col) {
        for (Node node : board.getChildren()) {
            int r = GridPane.getRowIndex(node) == null ? 0 : GridPane.getRowIndex(node);
            int c = GridPane.getColumnIndex(node) == null ? 0 : GridPane.getColumnIndex(node);
            if (r == row && c == col)
                return node;
        }
        return null;
    }

    private int[] intToCoords(int pos) {
        int row = pos / 10;
        int gridRow = 9 - row;
        int gridCol;
        if (row % 2 == 0) {
            gridCol = pos % 10;
        } else {
            gridCol = 9 - (pos % 10);
        }
        return new int[]{gridRow, gridCol};
    }

   @FXML
    private void handlePowerup(ActionEvent event) throws Exception {
        try {
                game.usePowerup();
                updateUI(new TurnResult(0, null, false, false));
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    @FXML
    private void handleRoll(ActionEvent event) throws Exception {
        try {
                TurnResult turnResult = game.playTurn();
                updateUI(turnResult);
            } catch (Exception e) {
                e.printStackTrace();
            }
    }
}
