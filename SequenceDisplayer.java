package indy;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

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
        this.graphicalSequence = new ArrayList<>();
        this.displaySequence();
    }

    public void setInputSequence(Sequence inputSequence) {
        this.inputSequence = inputSequence;
        this.displaySequence();
        System.out.println(inputSequence.sequence);
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
            this.makeStartLabel();
            for (int i = 0; i < this.inputSequence.getLength(); i++) {
                this.graphicalSequence.add(new GraphicalNucleotide(new Sequence("" + this.inputSequence.sequence.toCharArray()[i]), i, this.displayPaneScrollable));
            }
            this.makeEndLabel();
        }
    }

    private void makeStartLabel() {
        Pane sequenceStartPane = new Pane();
        Label startLabel = new Label("DNA SEQUENCE STARTS HERE");
        sequenceStartPane.getChildren().add(startLabel);
        sequenceStartPane.setTranslateX(168);
        sequenceStartPane.setTranslateY(50);
        this.displayPaneScrollable.getChildren().add(sequenceStartPane);
    }

    private void makeEndLabel() {
        Pane sequenceEndPane = new Pane();
        Label endLabel = new Label("DNA SEQUENCE ENDS HERE");
        sequenceEndPane.getChildren().add(endLabel);
        sequenceEndPane.setTranslateX(175);
        sequenceEndPane.setTranslateY(this.graphicalSequence.get(this.inputSequence.getLength() - 1).getCoordinates()[1] + 75);
        this.displayPaneScrollable.getChildren().add(sequenceEndPane);
    }
}
