package indy;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * This class sets the primary stage of the program and instantiates the top-level graphical class TabOrganizer.
 */

public class App extends Application {

    @Override
    public void start(Stage stage) {
        stage.setTitle("TheFinerPrimerDesigner");
        stage.setScene(new Scene(new TabOrganizer().getRoot(), Constants.APP_SIZE, Constants.APP_SIZE));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args); // launch is a method inherited from Application
    }
}
