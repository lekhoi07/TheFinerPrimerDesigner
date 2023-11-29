package indy;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.Objects;

public class GraphicalNucleotide {
    private BorderPane root;
    private char nucleotide;
    private int position;
    private int[] coordinates;

    public GraphicalNucleotide(char nucleotide, int position, BorderPane root) {
        this.root = root;
        this.nucleotide = nucleotide;
        this.position = position;
        this.coordinates = this.calcDisplayCoordinates(position);
        this.displayGraphicalNucleotide();
    }

    private int[] calcDisplayCoordinates(int position) {
        return new int[]{3, 9};
    }

    private void displayGraphicalNucleotide() {
        StackPane nucleotidePane = new StackPane();
        Rectangle selectionBox = new Rectangle(this.position * 30 + 110, 100, 10, 10);
        selectionBox.setFill(Color.WHITE);
        Label nucleotide = new Label("" + this.nucleotide);
        nucleotidePane.getChildren().addAll(selectionBox, nucleotide);
        this.root.getChildren().add(nucleotidePane);

        nucleotidePane.setTranslateX(this.position * 10 + 100);
        nucleotidePane.setTranslateY(100);

        nucleotidePane.setFocusTraversable(true);
        nucleotidePane.setOnMouseClicked((Event e) -> this.makeNucleotideSelectable(selectionBox));
    }
     private void makeNucleotideSelectable(Rectangle selectionBox) {
        if (selectionBox.getFill() == Color.WHITE) {
            selectionBox.setFill(Color.VIOLET);
        } else {
            selectionBox.setFill(Color.WHITE);
        }

         System.out.println("hi");
     }
}
