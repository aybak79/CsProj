package game.controller;

import game.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class OptionsController {
    @FXML private Pane root;
    @FXML private Button back;
    @FXML private Slider volume;
    @FXML private VBox buttons;
    private double width;
    private double height;

    @FXML
    public void initialize() {
        root.getStyleClass().clear();
        root.getStyleClass().add(Main.preferedTheme);
        volume.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (Main.mediaPlayer != null) {
                Main.mediaPlayer.setVolume(newVal.doubleValue());
            }
        });
        Rectangle2D screen = Screen.getPrimary().getBounds();
        width = screen.getWidth();
        height = screen.getHeight();
        root.setPrefWidth(width);
        root.setPrefHeight(height);
        layoutAll();
    }

    private void layoutAll() {
        buttons.setLayoutX(x(0.4));
        buttons.setLayoutY(y(0.21));
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

    @FXML
    private void handleMidnight() {
        Main.preferedTheme = "midnight-theme";
        root.getStyleClass().clear();
        root.getStyleClass().add(Main.preferedTheme);
    }

    @FXML
    private void handleForest() {
        Main.preferedTheme = "forest-theme";
        root.getStyleClass().clear();
        root.getStyleClass().add(Main.preferedTheme);
    }

    @FXML
    private void handleInc() {
        Main.preferedTheme = "monsterinc-theme";
        root.getStyleClass().clear();
        root.getStyleClass().add(Main.preferedTheme);
    }

    @FXML
    private void handleSunset() {
        Main.preferedTheme = "sunset-theme";
        root.getStyleClass().clear();
        root.getStyleClass().add(Main.preferedTheme);
    }

    private void loadScene(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) root.getScene().getWindow();
            stage.setTitle("Main Menu");
            stage.setScene(scene);
            stage.setFullScreen(true);
            stage.setFullScreenExitHint("");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
