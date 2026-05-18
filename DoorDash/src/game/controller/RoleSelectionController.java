package game.controller;

import java.io.IOException;

import game.engine.Game;
import game.engine.Role;
import game.engine.exceptions.InvalidCSVFormat;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class RoleSelectionController {

    @FXML private Pane root;
    @FXML private Button scarerButton;
    @FXML private Button laugherButton;
    @FXML private VBox buttons;
    private double width;
    private double height;

    @FXML
    public void initialize() {
        Rectangle2D screen = Screen.getPrimary().getBounds();
        width = screen.getWidth();
        height = screen.getHeight();
        root.setPrefWidth(width);
        root.setPrefHeight(height);
        layoutAll();
    }

    private void layoutAll() {
        buttons.setLayoutX(x(0.1688));
        buttons.setLayoutY(y(0.1907));
    }
    
    private double x(double percentX) { 
        return width * percentX; 
    }

    private double y(double percentY) { 
        return height * percentY; 
    }

    @FXML
    private void handleScarer() {
        startGame(Role.SCARER);
    }

    @FXML
    private void handleLaugher() {
        startGame(Role.LAUGHER);
    }

    private void startGame(Role role) {
        try {
            Game game = new Game(role);
            GameController.game = game;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/game/view/views/GameView.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) scarerButton.getScene().getWindow();
            stage.setTitle("Door Dash");
            stage.setScene(scene);
            stage.setFullScreen(true);
            stage.setFullScreenExitHint("");
        }catch(InvalidCSVFormat e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Data Loading Error");
            alert.setHeaderText("Invalid CSV Format");
            alert.setContentText("A CSV file could not be loaded:\n\n" + e.getMessage());
            alert.showAndWait();        
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("An error occurred");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }
}