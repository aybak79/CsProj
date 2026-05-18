package game.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class StartScreenController {

    @FXML private Pane root;
    @FXML private Button singleplayer;
    @FXML private Button multiplayer;
    @FXML private Button instructions;
    @FXML private Button options;
    @FXML private Button exit;

    @FXML
    public void initialize() {
        Font.loadFont(getClass().getResourceAsStream("/game/view/fonts/RussoOne.ttf"), 50);
    }

   @FXML
    private void handleSingleStart() throws Exception {
        loadScene("/game/view/views/ChooseView.fxml", "Choose Your Role");
    }

    @FXML
    private void handleMultiStart() throws Exception {
        loadScene("/game/view/views/ChooseView.fxml", "Choose");
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