package indy;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import java.util.ArrayList;

public class SequenceDisplayer {
    private BorderPane root;
    private Sequence inputSequence;
    private HBox nothingDisplayedPane;
    private ArrayList<GraphicalNucleotide> graphicalSequence;
    private ScrollPane displayPane;
    private Pane displayPaneScrollable;

    public SequenceDisplayer(BorderPane root) {
        this.root = root;
        this.displayPaneScrollable = new Pane();
        this.displaySequence();
    }

    public void setInputSequence(Sequence inputSequence) {
        this.inputSequence = inputSequence;
        this.graphicalSequence = new ArrayList<>();
        this.displaySequence();
    }

    public int getInputSequenceLength() {
        return this.inputSequence.getSequence().length();
    }

    public void setDisplayPane(ScrollPane displayPane) {
        this.displayPane = displayPane;
        this.displayPaneScrollable = new Pane();
        this.displayPane.setContent(this.displayPaneScrollable);
    }

    private void displaySequence() {
        if (this.inputSequence == null) {
            this.nothingDisplayedPane = new HBox();
            Label nothingDisplayedLabel = new Label("No sequence to display. Upload your DNA template on the previous tab!");
            this.nothingDisplayedPane.setAlignment(Pos.CENTER);
            this.nothingDisplayedPane.getChildren().add(nothingDisplayedLabel);
            this.root.setCenter(this.nothingDisplayedPane);
        } else {
            this.root.getChildren().remove(this.nothingDisplayedPane);
            for (int i = 0; i < this.inputSequence.getLength(); i++) {
                this.graphicalSequence.add(new GraphicalNucleotide(new Sequence("" + this.inputSequence.getSequence().toCharArray()[i]), i, this.displayPaneScrollable, this));
            }
            this.makeStartLabel();
            this.makeEndLabel();
        }
    }

    private void makeStartLabel() {
        Pane sequenceStartPane = new Pane();
        Label startLabel = new Label("DNA SEQUENCE STARTS HERE");
        sequenceStartPane.getChildren().add(startLabel);
        sequenceStartPane.setTranslateX(168);
        sequenceStartPane.setTranslateY(50);
        this.displayPaneScrollable.getChildren().addAll(sequenceStartPane, new Rectangle(10, Math.max(this.graphicalSequence.get(this.inputSequence.getLength() - 1).getCoordinates()[1] + 150, 675), Color.LIGHTGRAY));
    }

    private void makeEndLabel() {
        Pane sequenceEndPane = new Pane();
        Label endLabel = new Label("DNA SEQUENCE ENDS HERE");
        sequenceEndPane.getChildren().add(endLabel);
        sequenceEndPane.setTranslateX(175);
        sequenceEndPane.setTranslateY(this.graphicalSequence.get(this.inputSequence.getLength() - 1).getCoordinates()[1] + 75);
        this.displayPaneScrollable.getChildren().add(sequenceEndPane);
    }

    public void keepSelectionContinuous(int selectedPosition) {
        Color currentColor = Color.WHITE;
        int colorChanges = 0;
        int[] colorChangePositions = new int[4];
        for (GraphicalNucleotide nucleotide : this.graphicalSequence) {
            if (nucleotide.getFill() != currentColor) {
                colorChanges += 1;
                colorChangePositions[colorChanges - 1] = nucleotide.getPosition();
                currentColor = nucleotide.getFill();
            }
        }

        boolean isContinuous = colorChanges <= 2;
        if (!isContinuous) {
            if (this.graphicalSequence.get(selectedPosition).getFill() == Color.VIOLET) {
                for (int i = colorChangePositions[1]; i < colorChangePositions[2]; i++) {
                    this.graphicalSequence.get(i).setFill(Color.VIOLET);
                }
            } else {
                if (colorChangePositions[1] - colorChangePositions[0] < colorChangePositions[3] - colorChangePositions[2]) {
                    for (int i = colorChangePositions[0]; i < colorChangePositions[1]; i++) {
                        this.graphicalSequence.get(i).setFill(Color.WHITE);
                    }
                } else {
                    for (int i = colorChangePositions[2]; i < colorChangePositions[3]; i++) {
                        this.graphicalSequence.get(i).setFill(Color.WHITE);
                    }
                }
            }
        }
    }

    public ArrayList<GraphicalNucleotide> getGraphicalSequence() {
        return this.graphicalSequence;
    }

    public int[] getSelectedRegion() {
        Color currentColor = Color.WHITE;
        int colorChanges = 0;
        int[] colorChangePositions = new int[2];
        for (GraphicalNucleotide nucleotide : this.graphicalSequence) {
            if (currentColor != nucleotide.getFill()) {
                colorChanges += 1;
                currentColor = nucleotide.getFill();
                colorChangePositions[colorChanges - 1] = nucleotide.getPosition();
            }
        }
        return colorChangePositions;
    }
}
