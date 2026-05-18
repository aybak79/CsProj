package game.controller;

import game.engine.Game;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class GameOverController {
    @FXML private Label winner;
    @FXML private Label playerFinalEnergy;
    @FXML private Label opponentFinalEnergy;
    @FXML private Pane root; 
    @FXML private VBox buttons;
    private double width;
    private double height;
    
    @FXML public void initialize() {
        Game game = GameController.game;
        winner.setText("(" + game.getWinner().getOriginalRole().toString() + ") " + game.getWinner().getName());
        playerFinalEnergy.setText("" + game.getPlayer().getEnergy());
        opponentFinalEnergy.setText("" + game.getOpponent().getEnergy());
        Rectangle2D screen = Screen.getPrimary().getBounds();
        width = screen.getWidth();
        height = screen.getHeight();
        root.setPrefWidth(width);
        root.setPrefHeight(height);
        layoutAll();
    }

    private void layoutAll() {
        buttons.setLayoutX(x(0.2436));
        buttons.setLayoutY(y(0.2048));
    }
    
    private double x(double percentX) { 
        return width * percentX; 
    }

    private double y(double percentY) { 
        return height * percentY; 
    }

    @FXML public void handleBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/game/view/views/StartView.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) winner.getScene().getWindow();
            stage.setTitle("Main Menu");
            stage.setScene(scene);
            stage.setFullScreen(true);
            stage.setFullScreenExitHint("");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
