package game.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class InstructionsController {
    @FXML private Button backButton;
    @FXML private Pane root; 
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
        buttons.setLayoutX(x(0.2217));
        buttons.setLayoutY(y(0.1111));
    }
    
    private double x(double percentX) { 
        return width * percentX; 
    }

    private double y(double percentY) { 
        return height * percentY; 
    }

    @FXML
    private void handleBack() {
        loadScene("/game/view/views/StartView.fxml");
    }

    private void loadScene(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setTitle("Main Menu");
            stage.setScene(scene);
            stage.setFullScreen(true);
            stage.setFullScreenExitHint("");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
