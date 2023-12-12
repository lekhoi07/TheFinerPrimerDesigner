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
 * This class represents a stage that pops up when a warning message needs to be displayed. The stage will let the user
 * continue with the intended action if the user presses the continue button.
 */
public class WarningMessage {
    private Stage warningStage;
    private Button continueButton;

    /**
     * This constructor initializes the instance variables and displays the warning stage.
     * @param message
     */
    public WarningMessage(String message) {
        this.warningStage = new Stage();
        this.warningStage.setScene(new Scene(this.createWarningPane(message)));
        this.warningStage.setTitle("WARNING");
        this.warningStage.show();
    }

    /**
     * This helper method sets up the warning stage graphically with its warning text and two buttons.
     * @param message
     * @return
     */
    private BorderPane createWarningPane(String message) {
        BorderPane warningPane = new BorderPane();
        Label warningMessage = new Label(message);
        HBox buttonPane = new HBox();
        buttonPane.setSpacing(20);
        buttonPane.setAlignment(Pos.CENTER);
        this.continueButton = new Button("CONTINUE");
        Button cancelButton = new Button("CANCEL");
        cancelButton.setOnAction((ActionEvent e) -> this.warningStage.close());
        buttonPane.getChildren().addAll(this.continueButton, cancelButton);
        warningPane.setTop(warningMessage);
        warningPane.setBottom(buttonPane);
        return warningPane;
    }

    public Stage getWarningStage() {
        return this.warningStage;
    }

    public Button getContinueButton() {
        return this.continueButton;
    }
}
