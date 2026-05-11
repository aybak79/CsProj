package game.controller;

import game.engine.Game;
import game.engine.Role;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class RoleSelectionController {

    @FXML private Button scarerButton;
    @FXML private Button laugherButton;

    @FXML
    private void handleScarer(ActionEvent event) {
        startGame(Role.SCARER, event);
    }

    @FXML
    private void handleLaugher(ActionEvent event) {
        startGame(Role.LAUGHER, event);
    }

    private void startGame(Role role, ActionEvent event) {
        try {
            Game game = new Game(role);
            GameController.game = game;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/game/view/views/GameView.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}