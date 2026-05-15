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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import game.engine.cards.Card;
import game.engine.exceptions.InvalidMoveException;
import game.engine.exceptions.InvalidTurnException;
import game.engine.exceptions.OutOfEnergyException;
import javafx.scene.image.Image;
import static javafx.geometry.Pos.*;
import game.engine.cells.*;
import javafx.animation.*;
import javafx.util.Duration;

public class GameController {
    static Game game;
    @FXML private Button playerRollButton;
    @FXML private Button playerActivateButton;
    @FXML private Button opponentRollButton;
    @FXML private Button opponentActivateButton;
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
    @FXML private VBox cardPanel;
    @FXML private Label cardType;
    @FXML private Pane main;

    private static final Image IMG_SULLIVAN = new Image(GameController.class.getResourceAsStream("/game/view/assets/Sullivan.png"));
    private static final Image IMG_WAZOWSKI = new Image(GameController.class.getResourceAsStream("/game/view/assets/Wazowski.png"));
    private static final Image IMG_RANDALL  = new Image(GameController.class.getResourceAsStream("/game/view/assets/Randall.png"));
    private static final Image IMG_CELIA    = new Image(GameController.class.getResourceAsStream("/game/view/assets/Celia.png"));
    private static final Image IMG_ROZ      = new Image(GameController.class.getResourceAsStream("/game/view/assets/Roz.png"));
    private static final Image IMG_FUNGUS   = new Image(GameController.class.getResourceAsStream("/game/view/assets/Fungus.png"));
    private static final Image IMG_WATERNOOSE = new Image(GameController.class.getResourceAsStream("/game/view/assets/Waternoose.png"));
    private static final Image IMG_YETI     = new Image(GameController.class.getResourceAsStream("/game/view/assets/Yeti.png"));
    private int prevPlayerEnergy;
    private int prevOpponentEnergy;

    @FXML
    public void initialize() {
        main.requestFocus();
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
            dice.setImage(new Image(getClass().getResourceAsStream("/game/view/assets/" + turnResult.roll + ".png")));
        }
        playerSelector.setVisible(game.getPlayer() == game.getCurrent());
        opponentSelector.setVisible(game.getOpponent() == game.getCurrent());
        playerShield.setVisible(game.getPlayer().isShielded());
        playerFreeze.setVisible(game.getPlayer().isFrozen());
        playerConfusion.setVisible(game.getPlayer().isConfused());
        opponentShield.setVisible(game.getOpponent().isShielded());
        opponentFreeze.setVisible(game.getOpponent().isFrozen());
        opponentConfusion.setVisible(game.getOpponent().isConfused());

        if (game.getWinner() != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/game/view/views/GameOverView.fxml"));
                Scene scene = new Scene(loader.load());
                Stage stage = (Stage) main.getScene().getWindow();
                stage.setScene(scene);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        updateBoardImages(board);

        if(turnResult.landedOnDoor) {
            ImageView person = (game.getCurrent() == game.getPlayer()) ? opponent : player;
            Monster playableCharacter = (game.getCurrent() == game.getPlayer()) ? game.getOpponent() : game.getPlayer();
            int[] pos = intToCoords(playableCharacter.getPosition());
            StackPane cell = (StackPane) getCell(pos[0], pos[1]);
            for(Node n :  cell.getChildren()) {
                if(n instanceof ImageView && n != person && playableCharacter.getPosition() != 99) {
                    ((ImageView) n).setImage(new Image(getClass().getResourceAsStream("/game/view/assets/door-closed.png")));
                }
            }
        }

        cardsCount.setText("Cards: " + Board.getCards().size());
        if (!Board.getDrawnCards().isEmpty()) {
            Card last = Board.getDrawnCards().getFirst();
            cardTitle.setText(last.getName());
            cardType.setText(last.getClass().getSimpleName().replace("Card", ""));
            cardEffect.setText(last.getDescription());
        }

        if (playerRollButton.getScene() != null) {
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
        int[] opponentPos = intToCoords(game.getOpponent().getPosition() % 100);
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

    private int coordsToInt(int gridRow, int gridCol) {
        int row = 9 - gridRow;
        int col = (row % 2 == 0) ? gridCol : (9 - gridCol);
        return row * 10 + col;
    }
    private void showEnergyChange(Monster monster, int oldEnergy) {
        int delta = monster.getEnergy() - oldEnergy;
        if (delta == 0) return;

        int[] coords = intToCoords(monster.getPosition());
        StackPane cell = (StackPane) getCell(coords[0], coords[1]);
        if (cell == null) return;

        // Get the cell's position in screen coordinates
        javafx.geometry.Bounds bounds = cell.localToScene(cell.getBoundsInLocal());

        String sign = delta > 0 ? "+" : "";
        Label indicator = new Label(sign + delta);
        indicator.setStyle(
            "-fx-font-size: 16px; -fx-font-weight: bold; " +
            "-fx-text-fill: " + (delta > 0 ? "#00ff88" : "#ff4444") + "; " +
            "-fx-background-color: rgba(0,0,0,0.75); " +
            "-fx-padding: 4 8 4 8; -fx-background-radius: 8;"
        );

        indicator.setLayoutX(bounds.getMinX() + bounds.getWidth() / 2 - 20);
        indicator.setLayoutY(bounds.getMinY() - 20);

        // Add to the root pane so it floats above everything
        Pane root = (Pane) main.getScene().getRoot();
        root.getChildren().add(indicator);

       FadeTransition ft = new FadeTransition(
            Duration.seconds(3.0), indicator
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
                    int row = GridPane.getRowIndex(node) == null ? 0 : GridPane.getRowIndex(node);
                    int col = GridPane.getColumnIndex(node) == null ? 0 : GridPane.getColumnIndex(node);
                    int pos = coordsToInt(row, col);

                    for (Monster m : Board.getStationedMonsters()) {
                        if (m.getPosition() == pos) {
                            Image img = switch (m.getName()) {
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
                                StackPane.setAlignment(iv, CENTER);
                                cell.getChildren().add(iv);
                            }

                            Label nameLabel = new Label(m.getName());
                            nameLabel.setStyle(
                                "-fx-font-size: 9px; -fx-text-fill: white; " +
                                "-fx-background-color: rgba(0,0,0,0.6); " +
                                "-fx-padding: 2 4 2 4; -fx-background-radius: 4;"
                            );
                            StackPane.setAlignment(nameLabel, BOTTOM_CENTER);
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
                Cell boardCell = game.getBoard().getBoardCells()[boardRow][boardCol];

                if (boardCell instanceof DoorCell door) {
                    Label energyLabel = new Label( "" + door.getEnergy());
                    energyLabel.setStyle(
                        "-fx-font-size: 10px; -fx-font-weight: bold; -fx-text-fill: " +
                        (door.getEnergy() > 0 ? "white" : "red") + "; " +
                        "-fx-background-color: rgba(0,0,0,0.6); " +
                        "-fx-padding: 2 4 2 4; -fx-background-radius: 4;"
                    );
                    StackPane.setAlignment(energyLabel, BOTTOM_RIGHT);
                    cell.getChildren().add(energyLabel);
                }
            }
        }
    }
    
   @FXML
    private void handlePlayerPowerup(ActionEvent event) throws Exception {
        try {
            if(game.getCurrent() != game.getPlayer()) {
                throw new InvalidTurnException();
            }
            game.usePowerup();
            updateUI(new TurnResult(0, null, false, false));
        } catch (OutOfEnergyException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Not Enough Energy");
            alert.setHeaderText("You cannot use this powerup");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        } catch (InvalidTurnException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Turn");
            alert.setHeaderText("It's not your turn!");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    private void handleOpponentPowerup(ActionEvent event) throws Exception {
        try {
            if(game.getCurrent() != game.getOpponent()) {
                throw new InvalidTurnException();
            }
            game.usePowerup();
            updateUI(new TurnResult(0, null, false, false));
        } catch (OutOfEnergyException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Not Enough Energy");
            alert.setHeaderText("You cannot use this powerup");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        } catch (InvalidTurnException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Turn");
            alert.setHeaderText("It's not your turn!");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    private void handlePlayerRoll(ActionEvent event) throws Exception {
        try {
            if(game.getCurrent() != game.getPlayer()) {
                throw new InvalidTurnException();
            }
            TurnResult turnResult = game.playTurn();
            updateUI(turnResult);
        } catch (InvalidMoveException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Move");
            alert.setHeaderText("You cannot perform this action");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        } catch (InvalidTurnException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Turn");
            alert.setHeaderText("It's not your turn!");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }
    @FXML
    private void handleOpponentRoll(ActionEvent event) throws Exception {
        try {
            if(game.getCurrent() != game.getOpponent()) {
                throw new InvalidTurnException();
            }
            TurnResult turnResult = game.playTurn();
            updateUI(turnResult);
        } catch (InvalidMoveException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Move");
            alert.setHeaderText("You cannot perform this action");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        } catch (InvalidTurnException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Turn");
            alert.setHeaderText("It's not your turn!");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    private void handleKeyPress(KeyEvent event) {
        KeyCode key = event.getCode();
        switch (key) {
            case E:
                game.getCurrent().setEnergy(game.getCurrent().getEnergy() + 100);
                updateUI(new TurnResult(0, null, false, false));
                break;
            case W:
                game.getCurrent().setPosition(99);
                updateUI(new TurnResult(0, null, false, false));
                break;
        }
    }
}