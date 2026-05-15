package game.controller;

import game.engine.Game;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class GameOverController {
    @FXML private Label winner;
    @FXML private Label playerFinalEnergy;
    @FXML private Label opponentFinalEnergy;
    
    @FXML public void initialize() {
        Game game = GameController.game;
        winner.setText("(" + game.getWinner().getOriginalRole().toString() + ") " + game.getWinner().getName());
        playerFinalEnergy.setText("" + game.getPlayer().getEnergy());
        opponentFinalEnergy.setText("" + game.getOpponent().getEnergy());
    }

    @FXML public void handleBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/game/view/views/StartView.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) winner.getScene().getWindow();
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
