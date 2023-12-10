package indy;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

public class SequenceSelector {
    private GridPane root;
    private SequenceDisplayer displayer;
    private TextField startIndexTextbox;
    private TextField endIndexTextbox;
    private Label gcContent, meltingTemp, length;

    public SequenceSelector(GridPane root, SequenceDisplayer displayer) {
        this.root = root;
        this.displayer = displayer;
        this.displayer.setSelector(this);
        this.setUpGUI();
    }

    private void setUpGUI() {
        Label selectRegion = new Label("1. SELECT REGION");
        selectRegion.setTranslateX(50);
        selectRegion.setTranslateY(50);

        Label startIndex = new Label("Start Index:");
        startIndex.setTranslateX(10);
        startIndex.setTranslateY(100);

        Label endIndex = new Label("End Index:");
        endIndex.setTranslateX(10);
        endIndex.setTranslateY(150);

        this.startIndexTextbox = new TextField();
        this.startIndexTextbox.setTranslateX(80);
        this.startIndexTextbox.setTranslateY(100);
        this.startIndexTextbox.setMaxWidth(100);

        this.endIndexTextbox = new TextField();
        this.endIndexTextbox.setTranslateX(80);
        this.endIndexTextbox.setTranslateY(150);
        this.endIndexTextbox.setMaxWidth(100);

        Button selectButton = new Button("SELECT");
        selectButton.setTranslateX(70);
        selectButton.setTranslateY(200);
        selectButton.setOnAction((ActionEvent e) -> this.selectRegion(this.startIndexTextbox, this.endIndexTextbox));

        this.gcContent = new Label("GC%: N/A");
        this.gcContent.setTranslateX(10);
        this.gcContent.setTranslateY(250);

        this.meltingTemp = new Label("Melting Temperature: N/A");
        this.meltingTemp.setTranslateX(10);
        this.meltingTemp.setTranslateY(267);

        this.length = new Label("Length: N/A");
        this.length.setTranslateX(10);
        this.length.setTranslateY(284);

        Label designPrimers = new Label("2. DESIGN PRIMERS");
        designPrimers.setTranslateX(50);
        designPrimers.setTranslateY(325);

        this.root.getChildren().addAll(selectRegion, startIndex, endIndex, this.startIndexTextbox, this.endIndexTextbox, selectButton, this.gcContent, this.meltingTemp, this.length, designPrimers);
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
                        nucleotide.setFill(Color.WHITE, 1);
                    } else {
                        nucleotide.setFill(Color.VIOLET, 1);
                    }
                }
                this.setText(this.displayer.getSelectedRegion());
            } else {
                new ErrorMessage("Inputs out of bounds");
            }
        } catch (NumberFormatException e) {
            new ErrorMessage("Inputs must be integers");
        }
    }

    public void setText(int[] selectedRegion) {
        if (selectedRegion == null) {
            this.startIndexTextbox.clear();
            this.endIndexTextbox.clear();
            this.gcContent.setText("GC%: N/A");
            this.meltingTemp.setText("Melting Temp.: N/A");
            this.length.setText("Length: N/A");
        } else {
            this.startIndexTextbox.setText("" + selectedRegion[0]);
            this.endIndexTextbox.setText("" + (selectedRegion[1] - 1));
            this.gcContent.setText("GC%: " + new Sequence(this.displayer.getInputSequence().getSequence().substring(selectedRegion[0], selectedRegion[1])).getGC_Content() * 100 + "%");
            this.meltingTemp.setText("Melting Temp.: " + new Sequence(this.displayer.getInputSequence().getSequence().substring(selectedRegion[0], selectedRegion[1])).getMeltingTemperature() + " degrees C");
            this.length.setText("Length: " + this.displayer.getInputSequence().getSequence().substring(selectedRegion[0], selectedRegion[1]).length() + " bp");
        }
    }
}
