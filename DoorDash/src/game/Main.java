package game;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

public class Main extends Application {
    public static MediaPlayer mediaPlayer;
    public static String preferedTheme = "midnight-theme";

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/game/view/views/StartView.fxml"));
        Scene scene = new Scene(loader.load());
        String musicFile = getClass().getResource("/game/view/music/menu.mp3").toExternalForm();
        Media sound = new Media(musicFile);
        Main.mediaPlayer = new MediaPlayer(sound);
        Main.mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        Main.mediaPlayer.setVolume(0);
        Main.mediaPlayer.play();
        primaryStage.setTitle("Main Menu");
        primaryStage.setFullScreen(true);
        primaryStage.setFullScreenExitHint("");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
