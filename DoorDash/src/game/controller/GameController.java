package game.controller;

import game.engine.Board;
import game.engine.Game;
import game.engine.TurnResult;
import game.engine.monsters.Monster;
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
import game.engine.Board;
import game.engine.Role;
import game.engine.monsters.Monster;
import javafx.scene.control.Label;
import game.engine.Role;
import game.engine.cards.Card;

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
    @FXML private Label cardsCount;
    @FXML private Label cardTitle;
    @FXML private Label cardEffect;
    @FXML private StackPane cardPanel;

    private static final javafx.scene.image.Image IMG_SULLIVAN = new javafx.scene.image.Image(GameController.class.getResourceAsStream("/game/view/assets/Sullivan.png"));
    private static final javafx.scene.image.Image IMG_WAZOWSKI = new javafx.scene.image.Image(GameController.class.getResourceAsStream("/game/view/assets/Wazowski.png"));
    private static final javafx.scene.image.Image IMG_RANDALL  = new javafx.scene.image.Image(GameController.class.getResourceAsStream("/game/view/assets/Randall.png"));
    private static final javafx.scene.image.Image IMG_CELIA    = new javafx.scene.image.Image(GameController.class.getResourceAsStream("/game/view/assets/Celia.png"));
    private static final javafx.scene.image.Image IMG_ROZ      = new javafx.scene.image.Image(GameController.class.getResourceAsStream("/game/view/assets/Roz.png"));
    private static final javafx.scene.image.Image IMG_FUNGUS   = new javafx.scene.image.Image(GameController.class.getResourceAsStream("/game/view/assets/Fungus.png"));
    private static final javafx.scene.image.Image IMG_WATERNOOSE = new javafx.scene.image.Image(GameController.class.getResourceAsStream("/game/view/assets/Waternoose.png"));
    private static final javafx.scene.image.Image IMG_YETI     = new javafx.scene.image.Image(GameController.class.getResourceAsStream("/game/view/assets/Yeti.png"));
    private int prevPlayerEnergy;
    private int prevOpponentEnergy;

    @FXML
    public void initialize() {
        playerORole.setText(game.getPlayer().getOriginalRole().toString());
        playerType.setText(game.getPlayer().getClass().getSimpleName());
        playerName.setText(game.getPlayer().getName());
        opponentORole.setText(game.getOpponent().getOriginalRole().toString());
        opponentType.setText(game.getOpponent().getClass().getSimpleName());
        opponentName.setText(game.getOpponent().getName());
        initMonsterCells();
        initDoorCells();
        updateUI(new TurnResult(0, null, false, false));
        prevPlayerEnergy = game.getPlayer().getEnergy();
        prevOpponentEnergy = game.getOpponent().getEnergy();
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

        cardsCount.setText("Cards: " + Board.getCards().size());
        if (!Board.getDrawnCards().isEmpty()) {
            Card last = Board.getDrawnCards().getFirst();
            cardTitle.setText(last.getName());
            cardEffect.setText(last.getDescription());
}
       
        if (rollButton.getScene() != null) {
            showEnergyChange(game.getPlayer(), prevPlayerEnergy);
            showEnergyChange(game.getOpponent(), prevOpponentEnergy);
        }
        prevPlayerEnergy = game.getPlayer().getEnergy();
        prevOpponentEnergy = game.getOpponent().getEnergy();
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

    private void showEnergyChange(Monster monster, int oldEnergy) {
        int delta = monster.getEnergy() - oldEnergy;
        if (delta == 0) return;

        // Pick the monster ImageView (player or opponent)
        ImageView monsterView = (monster == game.getPlayer()) ? player : opponent;

        String sign = delta > 0 ? "+" : "";
        Label indicator = new Label(sign + delta);
        indicator.setStyle(
            "-fx-font-size: 16px; -fx-font-weight: bold; " +
            "-fx-text-fill: " + (delta > 0 ? "#00ff88" : "#ff4444") + "; " +
            "-fx-background-color: rgba(0,0,0,0.75); " +
            "-fx-padding: 4 8 4 8; -fx-background-radius: 8;"
        );

        Pane root = (Pane) rollButton.getScene().getRoot();

        // Position it next to the monster image in scene coordinates
        javafx.geometry.Bounds bounds = monsterView.localToScene(monsterView.getBoundsInLocal());
        boolean isPlayer = monster == game.getPlayer();
        indicator.setLayoutX(isPlayer ? bounds.getMaxX() + 8 : bounds.getMinX() - 60);
        indicator.setLayoutY(bounds.getMinY() + bounds.getHeight() / 2 - 12);

        root.getChildren().add(indicator);

        javafx.animation.FadeTransition ft = new javafx.animation.FadeTransition(
            javafx.util.Duration.seconds(3.0), indicator
        );
        ft.setFromValue(1.0);
        ft.setToValue(0.0);
        ft.setOnFinished(e -> root.getChildren().remove(indicator));
        ft.play();
    }

    private void initMonsterCells() {
    for (Node node : board.getChildren()) {
        if (node instanceof StackPane cell) {
            if (cell.getStyleClass().contains("monster")) {
                // Find which board index this cell maps to
                int row = GridPane.getRowIndex(node) == null ? 0 : GridPane.getRowIndex(node);
                int col = GridPane.getColumnIndex(node) == null ? 0 : GridPane.getColumnIndex(node);
                int pos = coordsToInt(row, col);

                // Find which stationed monster lives here
        for (Monster m : Board.getStationedMonsters()) {
            if (m.getPosition() == pos) {
                // Map monster name to image file
                javafx.scene.image.Image img = switch (m.getName()) {
                case "James P. Sullivan"    -> IMG_SULLIVAN;
                case "Mike Wazowski"        -> IMG_WAZOWSKI;
                case "Randall Boggs"        -> IMG_RANDALL;
                case "Celia Mae"            -> IMG_CELIA;
                case "Roz"                  -> IMG_ROZ;
                case "Fungus"               -> IMG_FUNGUS;
                case "Henry J. Waternoose"  -> IMG_WATERNOOSE;
                case "Yeti"                 -> IMG_YETI;
                default                     -> null;
            };

            if (img != null) {
                ImageView iv = new ImageView(img);
                iv.setFitWidth(40);
                iv.setFitHeight(55);
                iv.setPreserveRatio(true);
                StackPane.setAlignment(iv, javafx.geometry.Pos.CENTER);
                cell.getChildren().add(iv);
            }

                Label nameLabel = new Label(m.getName());
                nameLabel.setStyle(
                    "-fx-font-size: 9px; -fx-text-fill: white; " +
                    "-fx-background-color: rgba(0,0,0,0.6); " +
                    "-fx-padding: 2 4 2 4; -fx-background-radius: 4;"
                );
                StackPane.setAlignment(nameLabel, javafx.geometry.Pos.BOTTOM_CENTER);
                cell.getChildren().add(nameLabel);
                break;
            }
        }
            }
        }
    }
    }

    

private void initDoorCells() {
    for (Node node : board.getChildren()) {
        if (node instanceof StackPane cell) {
            int row = GridPane.getRowIndex(node) == null ? 0 : GridPane.getRowIndex(node);
            int col = GridPane.getColumnIndex(node) == null ? 0 : GridPane.getColumnIndex(node);
            int pos = coordsToInt(row, col);

            // Get the actual cell from the board
            int boardRow = pos / 10;
            int boardCol = (boardRow % 2 == 0) ? pos % 10 : 9 - (pos % 10);
            game.engine.cells.Cell boardCell = game.getBoard().getBoardCells()[boardRow][boardCol];

            if (boardCell instanceof game.engine.cells.DoorCell door) {
                Label energyLabel = new Label((door.getEnergy() > 0 ? "+" : "") + door.getEnergy());
                energyLabel.setStyle(
                    "-fx-font-size: 10px; -fx-font-weight: bold; -fx-text-fill: " +
                    (door.getEnergy() > 0 ? "lightgreen" : "red") + "; " +
                    "-fx-background-color: rgba(0,0,0,0.6); " +
                    "-fx-padding: 2 4 2 4; -fx-background-radius: 4;"
                );
                StackPane.setAlignment(energyLabel, javafx.geometry.Pos.BOTTOM_RIGHT);
                cell.getChildren().add(energyLabel);
            }
        }
    }
}

// Reverse of intToCoords — converts grid (row,col) back to board index
private int coordsToInt(int gridRow, int gridCol) {
    int row = 9 - gridRow;
    int col = (row % 2 == 0) ? gridCol : (9 - gridCol);
    return row * 10 + col;
}

}