package indy;

import javafx.scene.control.Label;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

public class GraphicalNucleotide {
    private BorderPane root;
    private char nucleotide;
    private int position;

    public GraphicalNucleotide(char nucleotide, int position, BorderPane root) {
        this.root = root;
        this.nucleotide = nucleotide;
        this.position = position;
        this.displayGraphicalNucleotide();
    }

    private void displayGraphicalNucleotide() {
        HBox nucleotidePane = new HBox();
        Label nucleotide = new Label("" + this.nucleotide);
        nucleotidePane.getChildren().add(nucleotide);
        this.root.getChildren().add(nucleotidePane);

    }
}
