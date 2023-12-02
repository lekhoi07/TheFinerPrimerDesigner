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
    private SequenceDisplayer displayer;
    private Rectangle selectionBox;
    private int position;

    public GraphicalNucleotide(Sequence nucleotide, int position, Pane root, SequenceDisplayer displayer) {
        this.root = root;
        this.nucleotide = nucleotide;
        this.displayer = displayer;
        this.position = position;
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
        this.selectionBox = new Rectangle(10, 40);
        this.selectionBox.setFill(Color.WHITE);

        Label nucleotide = new Label(this.nucleotide.getSequence());
        if (nucleotide.getText().toCharArray()[0] == '5') {
            nucleotide.setText("5'");
        } else if (nucleotide.getText().toCharArray()[0] == '3') {
            nucleotide.setText("3'");
        }
        nucleotide.setTranslateY(-10);

        Label complementNucleotide = new Label(this.nucleotide.getComplement().getSequence());
        if (complementNucleotide.getText().toCharArray()[0] == '5') {
            complementNucleotide.setText("5'");
        } else if (complementNucleotide.getText().toCharArray()[0] == '3') {
            complementNucleotide.setText("3'");
        }
        complementNucleotide.setTranslateY(10);

        this.nucleotidePane.getChildren().addAll(this.selectionBox, nucleotide, complementNucleotide);
        this.nucleotidePane.setTranslateX(this.coordinates[0]);
        this.nucleotidePane.setTranslateY(this.coordinates[1]);
        this.nucleotidePane.setFocusTraversable(true);
        this.nucleotidePane.setOnMouseClicked((Event e) -> this.makeNucleotideSelectable(this.selectionBox));

        this.root.getChildren().add(this.nucleotidePane);
    }
     private void makeNucleotideSelectable(Rectangle selectionBox) {
        if (this.nucleotide.getSequence().toCharArray()[0] != '5' && this.nucleotide.getSequence().toCharArray()[0] != '3') {
            if (selectionBox.getFill() == Color.WHITE) {
                selectionBox.setFill(Color.VIOLET);
            } else {
                selectionBox.setFill(Color.WHITE);
            }
            this.displayer.keepSelectionContinuous(this.position);
        }
     }

     public Color getFill() {
        return (Color) this.selectionBox.getFill();
     }

     public void setFill(Color color) {
        this.selectionBox.setFill(color);
     }

     public int getPosition() {
        return this.position;
     }
}
