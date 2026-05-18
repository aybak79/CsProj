package game.controller;

import game.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class OptionsController {
    @FXML private Pane root;
    @FXML private Button back;
    @FXML private Slider volume;

    @FXML
    public void initialize() { 
        volume.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (Main.mediaPlayer != null) {
                Main.mediaPlayer.setVolume(newVal.doubleValue());
            }
        });
    }

    @FXML
    private void handleBack() {
        loadScene("/game/view/views/StartView.fxml");
    }

    @FXML
    private void handleTheme() {
        // navigate back to the start screen
        loadScene("/game/view/views/StartView.fxml");
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
