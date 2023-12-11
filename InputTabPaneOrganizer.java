package indy;

import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class InputTabPaneOrganizer {
    private StackPane root;
    private TheFinerPrimerDesigner designer;

    public InputTabPaneOrganizer(TheFinerPrimerDesigner designer) {
        this.designer = designer;
        this.root = new StackPane();
        this.displayBackgroundImage();
        this.createButtons();
    }

    public StackPane getRoot() {
        return this.root;
    }

    private void displayBackgroundImage() {
        Image image = new Image("./indy/home_background.png");
        ImageView selectedImage = new ImageView();
        selectedImage.setImage(image);
        selectedImage.setFitWidth(700);
        selectedImage.setPreserveRatio(true);
        selectedImage.setSmooth(true);
        selectedImage.setCache(true);
        HBox imagePane = new HBox();
        imagePane.getChildren().add((selectedImage));
        this.root.getChildren().add(imagePane);
    }

    private void createButtons() {
        Rectangle inputButtonRect = new Rectangle(200, 50, Color.WHITE);
        inputButtonRect.setStroke(Color.BLACK);
        inputButtonRect.setTranslateY(200);

        Button inputButton = new Button("INPUT DNA SEQUENCE");
        inputButton.setTranslateY(200);
        inputButton.setOnAction((ActionEvent e) -> this.handleInputButtonPressed());

        Button quitButton = new Button("QUIT");
        quitButton.setTranslateY(275);
        quitButton.setOnAction((ActionEvent e) -> {
            WarningMessage message = new WarningMessage("Quitting the program will lose all your work. Do you wish to continue?");
            message.getContinueButton().setOnAction((ActionEvent f) -> System.exit(0));
        });
        this.root.getChildren().addAll(inputButtonRect, inputButton, quitButton);
    }

    private void handleInputButtonPressed() {
        if (this.designer.getInputSequence() != null) {
            WarningMessage warning = new WarningMessage("Inputting a new template DNA sequence will override the current one and its corresponding primers. Do you wish to continue?");
            warning.getContinueButton().setOnAction((ActionEvent e) -> {
                Stage inputStage = new Stage();
                inputStage.setTitle("INPUT YOUR DNA SEQUENCE 5' TO 3'");
                InputProcessor processor = new InputProcessor(inputStage, this.designer);
                inputStage.setScene(new Scene(processor.getRoot()));
                inputStage.show();
                warning.getWarningStage().close();
            });
        } else {
            Stage inputStage = new Stage();
            inputStage.setTitle("INPUT YOUR DNA SEQUENCE 5' TO 3'");
            InputProcessor processor = new InputProcessor(inputStage, this.designer);
            inputStage.setScene(new Scene(processor.getRoot()));
            inputStage.show();
        }
    }
}
