package indy;

import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class ErrorMessage {
    public ErrorMessage(String message) {
        Stage errorStage = new Stage();
        errorStage.setScene(new Scene(this.createErrorPane(errorStage, message)));
        errorStage.show();
    }
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
