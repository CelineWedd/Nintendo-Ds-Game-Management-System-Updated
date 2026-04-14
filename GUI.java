package ndsgms;
/**
 * @author Celine Wedderburn
 *CEN-3024C - Software Development 1
 * @since Mar 27, 2026
 *GUI.java

 *Main application class for the Nintendo DS Game Management System.The purpose of this class is to Launch the
 *JavaFX application. Will Launch and display the main application window
 */

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GUI extends Application {
    /**
    *Starts the javaFX application
    *Loads FMXL and displays the main window
    *
    *@param stage the main window where the application is displayed
    *@throws Exception if FMXL can not load
    */
    @Override
    public void start(Stage stage) throws Exception {
        System.out.println(getClass().getResource("/scenebuilder.fxml"));

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/scenebuilder.fxml"));
        Scene scene = new Scene(loader.load());

        stage.setScene(scene);
        stage.setTitle("DS Game Manager ndsgms.ndsgms.GUI");
        stage.show();
    }
    /**
     * @param args arguments needed when the system starts
     */
    public static void main(String[] args) {
        launch(args);
    }
}