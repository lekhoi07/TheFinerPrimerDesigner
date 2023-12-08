package indy;

import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class SequenceSelector {
    private GridPane root;
    private SequenceDisplayer displayer;

    public SequenceSelector(GridPane root, SequenceDisplayer displayer) {
        this.root = root;
        this.displayer = displayer;
        this.setUpGUI();
    }

    private void setUpGUI() {
        Label selectRegion = new Label("SELECT REGION");
        selectRegion.setTranslateX(55);
        selectRegion.setTranslateY(50);

        Label startIndex = new Label("Start Index:");
        startIndex.setTranslateX(10);
        startIndex.setTranslateY(100);

        Label endIndex = new Label("End Index:");
        endIndex.setTranslateX(10);
        endIndex.setTranslateY(150);

        TextField startIndexTextbox = new TextField();
        startIndexTextbox.setTranslateX(80);
        startIndexTextbox.setTranslateY(100);
        startIndexTextbox.setMaxWidth(100);

        TextField endIndexTextbox = new TextField();
        endIndexTextbox.setTranslateX(80);
        endIndexTextbox.setTranslateY(150);
        endIndexTextbox.setMaxWidth(100);

        Button selectButton = new Button("SELECT");
        selectButton.setTranslateX(70);
        selectButton.setTranslateY(200);
        selectButton.setOnAction((ActionEvent e) -> this.selectRegion(startIndexTextbox, endIndexTextbox));

        this.root.getChildren().addAll(selectRegion, startIndex, endIndex, startIndexTextbox, endIndexTextbox, selectButton);
    }

    private void selectRegion(TextField start, TextField end) {
        String startInput = start.getText().replaceAll(" ", "");
        startInput = startInput.replaceAll("\n", "");
        String endInput = end.getText().replaceAll(" ", "");
        endInput = endInput.replaceAll("\n", "");

        try {
            int startIndex = Integer.parseInt(startInput);
            int endIndex = Integer.parseInt(endInput);
            if (startIndex > 0 && endIndex < this.displayer.getInputSequenceLength() - 1 && startIndex <= endIndex) {
                for (GraphicalNucleotide nucleotide : this.displayer.getGraphicalSequence()) {
                    if (nucleotide.getPosition() < startIndex || nucleotide.getPosition() > endIndex) {
                        nucleotide.setFill(Color.WHITE);
                    } else {
                        nucleotide.setFill(Color.VIOLET);
                    }
                }
            } else {
                new ErrorMessage("Inputs out of bounds");
            }
        } catch (NumberFormatException e) {
            new ErrorMessage("Inputs must be integers");
        }
    }
}
