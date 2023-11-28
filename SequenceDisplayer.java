package indy;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

import java.util.ArrayList;

public class SequenceDisplayer {
    private BorderPane root;
    private Sequence inputSequence;
    private HBox nothingDisplayedPane;
    private ArrayList<GraphicalNucleotide> graphicalSequence;

    public SequenceDisplayer(BorderPane root) {
        this.root = root;
        this.graphicalSequence = new ArrayList<>();
        this.displaySequence();
    }

    public void setInputSequence(Sequence inputSequence) {
        this.inputSequence = inputSequence;
        this.displaySequence();
        System.out.println(inputSequence.sequence);
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
            for (int i = 0; i < this.inputSequence.sequence.toCharArray().length; i++) {
                this.graphicalSequence.add(new GraphicalNucleotide(this.inputSequence.sequence.toCharArray()[i], i, this.root));
            }
        }
    }
}
