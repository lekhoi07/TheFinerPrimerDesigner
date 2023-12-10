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
    private GraphicalPrimer graphicalForwardPrimer, graphicalReversePrimer;

    public GraphicalPrimerPair(Primer[] primerPair) {
        this.primerPair = primerPair;
        this.primerPairPane = new StackPane();
    }

    public void displayGraphicalPrimerPair() {
        Rectangle box = new Rectangle(658, 150);
        box.setFill(Color.WHITE);
        box.setStroke(Color.BLACK);

        Label forwardPrimerSeq = new Label("Forward Primer: 5'" + this.primerPair[0].getSequence().toUpperCase() + "3'");
        forwardPrimerSeq.setTranslateY(-60);
        forwardPrimerSeq.setTranslateX(-180);
        Label forwardPositions = new Label("Start Index: " + this.primerPair[0].getPosition()[0] + ", End Index: " + this.primerPair[0].getPosition()[1]);
        forwardPositions.setTranslateY(-45);
        forwardPositions.setTranslateX(-180);
        Label forwardGCContent = new Label("GC%: " + this.primerPair[0].getGC_Content() * 100 + "%");
        forwardGCContent.setTranslateY(-30);
        forwardGCContent.setTranslateX(-180);
        Label forwardMeltingTemp = new Label("Melting Temperature: " + this.primerPair[0].getMeltingTemperature() + " degrees Celsius");
        forwardMeltingTemp.setTranslateY(-15);
        forwardMeltingTemp.setTranslateX(-180);


        Label reversePrimerSeq = new Label("Reverse Primer: 5'" + this.primerPair[1].getSequence().toUpperCase() + "3'");
        reversePrimerSeq.setTranslateY(15);
        reversePrimerSeq.setTranslateX(-180);
        Label reversePositions = new Label("Start Index: " + this.primerPair[1].getPosition()[0] + ", End Index: " + this.primerPair[1].getPosition()[1]);
        reversePositions.setTranslateY(30);
        reversePositions.setTranslateX(-180);
        Label reverseGCContent = new Label("GC%: " + this.primerPair[1].getGC_Content() * 100 + "%");
        reverseGCContent.setTranslateY(45);
        reverseGCContent.setTranslateX(-180);
        Label reverseMeltingTemp = new Label("Melting Temperature: " + this.primerPair[1].getMeltingTemperature() + " degrees Celsius");
        reverseMeltingTemp.setTranslateY(60);
        reverseMeltingTemp.setTranslateX(-180);

        this.primerPairPane.getChildren().addAll(box, forwardPrimerSeq, forwardPositions, forwardGCContent, forwardMeltingTemp, reversePrimerSeq, reversePositions, reverseGCContent, reverseMeltingTemp);
        this.primerPairPane.setTranslateX(this.calcDisplayCoordinates()[0]);
        this.primerPairPane.setTranslateY(this.calcDisplayCoordinates()[1]);
        this.root.getChildren().add(this.primerPairPane);
    }

    private int[] calcDisplayCoordinates() {
        int posX = 20;
        int posY = 20 + 170 * (this.position - 1);
        return new int[]{posX, posY};
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void setRoot(Pane root) {
        this.root = root;
    }

    public StackPane getPrimerPairPane() {
        return this.primerPairPane;
    }

    public void setGraphicalForwardPrimer(GraphicalPrimer graphicalForwardPrimer) {
        this.graphicalForwardPrimer = graphicalForwardPrimer;
    }

    public void setGraphicalReversePrimer(GraphicalPrimer graphicalReversePrimer) {
        this.graphicalReversePrimer = graphicalReversePrimer;
    }
}
