package indy;

import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class ManualPrimerDesignError {
    private Stage errorStage;
    private Button abortButton;

    public ManualPrimerDesignError(String message) {
        this.errorStage = new Stage();
        this.errorStage.setScene(new Scene(this.createErrorPane(this.errorStage, message)));
        this.errorStage.setTitle("ERROR");
        this.errorStage.show();
    }
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
