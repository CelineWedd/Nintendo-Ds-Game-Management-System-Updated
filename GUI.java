package ndsgms;
/**
 *Celine Wedderburn
 *CEN-3024C - Software Development 1
 *Mar 27, 2026
 *ndsgms.ndsgms.GUI.java
 *
 *Main ndsgms.ndsgms.GUI application class for the Nintendo DS Game Management System.The purpose of this class is to Launch the
 *JavaFX application. Will Launch and display the main application window
 */

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GUI extends Application {
    // Loads the fxml
    // Starts javaFX app
    @Override
    public void start(Stage stage) throws Exception {
        System.out.println(getClass().getResource("/scenebuilder.fxml"));


        FXMLLoader loader = new FXMLLoader(getClass().getResource("/scenebuilder.fxml"));
        Scene scene = new Scene(loader.load());

        // Scene creation
        // Displays window
        stage.setScene(scene);
        stage.setTitle("DS Game Manager ndsgms.ndsgms.GUI");
        stage.show();
    }
    // Main Method
    public static void main(String[] args) {
        launch(args);
    }
}