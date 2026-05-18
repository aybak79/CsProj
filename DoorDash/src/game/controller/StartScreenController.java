package game.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class StartScreenController {

    @FXML private Pane root;
    @FXML private Label title;
    @FXML private Button singleplayer;
    @FXML private Button multiplayer;
    @FXML private Button instructions;
    @FXML private Button options;
    @FXML private Button exit;
    private double width;
    private double height;

    
    @FXML
    public void initialize() {
        Font.loadFont(getClass().getResourceAsStream("/game/view/fonts/RussoOne.ttf"), 50);
        root.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.windowProperty().addListener((obs2, oldWin, newWin) -> {
                    if (newWin != null) {
                        Stage stage = (Stage) newWin;
                        stage.showingProperty().addListener((obs3, wasShowing, isShowing) -> {
                            if (isShowing) {
                                layoutAll();
                            }
                        });
                        if (stage.isShowing()) {
                            layoutAll();
                        }
                    }
                });
            }
        });
    }
    
    private void layoutAll() {
        width = root.getScene().getWidth();
        height = root.getScene().getHeight();

        title.setLayoutX(x(0.1943 - 0.045));
        title.setLayoutY(y(0.1176));
        singleplayer.setLayoutX(x(0.2922 - 0.028));
        singleplayer.setLayoutY(y(0.4213));
        multiplayer.setLayoutX(x(0.2922 - 0.028));
        multiplayer.setLayoutY(y(0.5213));    
        instructions.setLayoutX(x(0.2922 - 0.028));
        instructions.setLayoutY(y(0.6213));
        options.setLayoutX(x(0.2922 - 0.028));
        options.setLayoutY(y(0.7213));
        exit.setLayoutX(x(0.2922 - 0.028));
        exit.setLayoutY(y(0.8213));
    }
    
    private double x(double percentX) { 
        return width * percentX; 
    }

    private double y(double percentY) { 
        return height * percentY; 
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