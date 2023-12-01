package indy;

import javafx.event.Event;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class GraphicalNucleotide {
    private Pane root;
    private Sequence nucleotide;
    private int[] coordinates;
    private StackPane nucleotidePane;

    public GraphicalNucleotide(Sequence nucleotide, int position, Pane root) {
        this.root = root;
        this.nucleotide = nucleotide;
        this.coordinates = this.calcDisplayCoordinates(position);
        this.displayGraphicalNucleotide();
    }

    private int[] calcDisplayCoordinates(int position) {
        int posX = 30 + 10 * (position % 42);
        int posY = 100 + 60 * (position / 42);
        return new int[]{posX, posY};
    }

    public int[] getCoordinates() {
        return this.coordinates;
    }

    private void displayGraphicalNucleotide() {
        this.nucleotidePane = new StackPane();
        Rectangle selectionBox = new Rectangle(10, 40);
        selectionBox.setFill(Color.WHITE);
        Label nucleotide = new Label(this.nucleotide.sequence);
        nucleotide.setTranslateY(-10);
        Label complementNucleotide = new Label(this.nucleotide.getComplement().sequence);
        complementNucleotide.setTranslateY(10);
        this.nucleotidePane.getChildren().addAll(selectionBox, nucleotide, complementNucleotide);

        this.nucleotidePane.setTranslateX(this.coordinates[0]);
        this.nucleotidePane.setTranslateY(this.coordinates[1]);

        this.nucleotidePane.setFocusTraversable(true);
        this.nucleotidePane.setOnMouseClicked((Event e) -> this.makeNucleotideSelectable(selectionBox));

        this.root.getChildren().add(this.nucleotidePane);

        //todo: temp rectangle remove
        this.root.getChildren().add(new Rectangle(10, 1000));
    }
     private void makeNucleotideSelectable(Rectangle selectionBox) {
        if (selectionBox.getFill() == Color.WHITE) {
            selectionBox.setFill(Color.VIOLET);
        } else {
            selectionBox.setFill(Color.WHITE);
        }
     }
}
