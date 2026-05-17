package game.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class StartScreenController {

    @FXML private AnchorPane root;
    @FXML private Button singleplayer;
    @FXML private Button multiplayer;
    @FXML private Button options;
    @FXML private Button exit;

    @FXML
    public void initialize() {
        root.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.windowProperty().addListener((obs2, oldWindow, newWindow) -> {
                    if (newWindow != null) {
                        Stage stage = (Stage) newWindow;
                        stage.maximizedProperty().addListener((obs3, wasMax, isMax) -> {
                            if (isMax) stage.setMaximized(true);
                        });
                    }
                });
            }
        });
        Font.loadFont(getClass().getResourceAsStream("/game/view/fonts/RussoOne.ttf"), 50);
    }

   @FXML
    private void handleSingleStart() throws Exception {
        loadScene("/game/view/views/InstructionsView.fxml");
    }

    @FXML
    private void handleMultiStart() throws Exception {
        loadScene("/game/view/views/InstructionsView.fxml");
    }

    @FXML
    private void handleOptions() throws Exception {
        loadScene("/game/view/views/OptionsView.fxml");
    }

    @FXML
    private void handleExit() {
        System.exit(0);
    }

    private void loadScene(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) root.getScene().getWindow();
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}