package indy;

import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 * This class is a stage that pops up whenever the user does something that would generate an error in the program.
 * The stage also has a button that says "OK" for the user to close the stage.
 */
public class ErrorMessage {
    /**
     * This constructor creates the error stage.
     * @param message
     */
    public ErrorMessage(String message) {
        Stage errorStage = new Stage();
        errorStage.setScene(new Scene(this.createErrorPane(errorStage, message)));
        errorStage.setTitle("ERROR");
        errorStage.show();
    }

    /**
     * This helper method sets the error stage graphically, such as by adding an "OK" button and displaying the
     * specific error message.
     * @param errorStage
     * @param message
     * @return
     */
    private BorderPane createErrorPane(Stage errorStage, String message) {
        BorderPane errorPane = new BorderPane();
        Label errorMessage = new Label(message);
        HBox quitButtonPane = new HBox();
        quitButtonPane.setAlignment(Pos.CENTER);
        Button quitButton = new Button("OK");
        quitButton.setOnAction((ActionEvent e) -> errorStage.close());
        quitButtonPane.getChildren().add(quitButton);
        errorPane.setTop(errorMessage);
        errorPane.setBottom(quitButtonPane);
        return errorPane;
    }
}
