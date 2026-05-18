package game.controller;

import game.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.Screen;

public class StartScreenController {

    @FXML private Pane root;
    @FXML private Label title;
    @FXML private Button start;
    @FXML private Button instructions;
    @FXML private Button options;
    @FXML private Button exit;
    @FXML private VBox buttons;
    private double width;
    private double height;

    
    @FXML
    public void initialize() {
        root.getStyleClass().clear();
        root.getStyleClass().add(Main.preferedTheme);
        Font.loadFont(getClass().getResourceAsStream("/game/view/fonts/RussoOne.ttf"), 50);
        Rectangle2D screen = Screen.getPrimary().getBounds();
        width = screen.getWidth();
        height = screen.getHeight();
        root.setPrefWidth(width);
        root.setPrefHeight(height);
        layoutAll();
    }
    
    private void layoutAll() {
        title.setLayoutX(x(0.1943 - 0.043));
        title.setLayoutY(y(0.1176));
        buttons.setLayoutX(x(0.2655));
        buttons.setLayoutY(y(0.4481));
    }
    
    private double x(double percentX) { 
        return width * percentX; 
    }

    private double y(double percentY) { 
        return height * percentY; 
    }
   @FXML
    private void handleStart() throws Exception {
        loadScene("/game/view/views/ChooseView.fxml", "Choose Your Role");
    }

    @FXML
    private void handleInstructions() throws Exception {
        loadScene("/game/view/views/InstructionsView.fxml", "Instructions");
    }

    @FXML
    private void handleOptions() throws Exception {
        loadScene("/game/view/views/OptionsView.fxml", "Options");
    }

    @FXML
    private void handleExit() {
        System.exit(0);
    }

    private void loadScene(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) root.getScene().getWindow();
            stage.setTitle(title);
            stage.setScene(scene);
            stage.setFullScreen(true);
            stage.setFullScreenExitHint("");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}