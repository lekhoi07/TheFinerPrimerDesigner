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
 * This class is the error message that pops up when a user is trying to manually design a primer pair.
 */
public class ManualPrimerDesignError {
    private Stage errorStage;
    private Button abortButton;

    /**
     * This constructor initializes the instance variables and calls the helper methods to set up the error message
     * graphically.
     * @param message
     */
    public ManualPrimerDesignError(String message) {
        this.errorStage = new Stage();
        this.errorStage.setScene(new Scene(this.createErrorPane(this.errorStage, message)));
        this.errorStage.setTitle("ERROR");
        this.errorStage.show();
    }

    /**
     * This helper method creates the error message graphically by adding the error text and the two buttons.
     * @param errorStage
     * @param message
     * @return
     */
    private BorderPane createErrorPane(Stage errorStage, String message) {
        BorderPane errorPane = new BorderPane();
        Label errorMessage = new Label(message);
        HBox quitButtonPane = new HBox();
        quitButtonPane.setAlignment(Pos.CENTER);
        quitButtonPane.setSpacing(20);
        Button quitButton = new Button("OK");
        quitButton.setOnAction((ActionEvent e) -> errorStage.close());
        this.abortButton = new Button("ABORT CURRENT PRIMER PAIR");
        quitButtonPane.getChildren().addAll(quitButton, this.abortButton);
        errorPane.setTop(errorMessage);
        errorPane.setBottom(quitButtonPane);
        return errorPane;
    }

    public Button getAbortButton() {
        return this.abortButton;
    }

    public Stage getErrorStage() {
        return this.errorStage;
    }
}
