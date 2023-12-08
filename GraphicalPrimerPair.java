package indy;

import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.Arrays;

public class GraphicalPrimerPair {
    private StackPane primerPairPane;
    private Pane root;
    private int position;
    private Primer[] primerPair;

    public GraphicalPrimerPair(Pane root, int position, Primer[] primerPair) {
        this.root = root;
        this.position = position;
        this.primerPair = primerPair;
        this.primerPairPane = new StackPane();
        this.displayGraphicalPrimerPair();
    }

    public StackPane getPrimerPairPane() {
        return this.primerPairPane;
    }

    private void displayGraphicalPrimerPair() {
        Rectangle box = new Rectangle(100, 100);
        box.setFill(Color.WHITE);

        Label primerSeq = new Label("Forward: " + this.primerPair[0].getSequence() + " , Reverse: " + this.primerPair[1].getSequence());
        Label positions = new Label("Start: " + this.primerPair[0].getPosition()[0] + "End: " + this.primerPair[0].getPosition()[1] + "Start: " + this.primerPair[1].getPosition()[0] + "End: " + this.primerPair[1].getPosition()[1]);
        positions.setTranslateY(-10);

        this.primerPairPane.getChildren().addAll(box, primerSeq, positions);
        this.primerPairPane.setTranslateX(this.calcDisplayCoordinates()[0]);
        this.primerPairPane.setTranslateY(this.calcDisplayCoordinates()[1]);
        this.root.getChildren().add(this.primerPairPane);
    }

    private int[] calcDisplayCoordinates() {
        int posX = 100;
        int posY = 100 + 60 * this.position;
        return new int[]{posX, posY};
    }
}
