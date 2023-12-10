package indy;

import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;

public class GraphicalPrimerPair {
    private StackPane primerPairPane;
    private Pane root;
    private int position;
    private Primer[] primerPair;
    private GraphicalPrimer graphicalForwardPrimer, graphicalReversePrimer;
    private double score;
    private TheFinerPrimerDesigner designer;

    public GraphicalPrimerPair(Primer[] primerPair, double score, TheFinerPrimerDesigner designer) {
        this.primerPair = primerPair;
        this.primerPairPane = new StackPane();
        this.score = score;
        this.designer = designer;
    }

    public void displayGraphicalPrimerPair(int position) {
        Rectangle mainBox = new Rectangle(658, 150);
        mainBox.setFill(Color.WHITE);
        mainBox.setStroke(Color.BLACK);
        Rectangle actionBox = new Rectangle(200, 130);
        actionBox.setFill(Color.WHITE);
        actionBox.setStroke(Color.BLACK);
        actionBox.setTranslateX(-215);
        Rectangle dataBox = new Rectangle(400, 130);
        dataBox.setFill(Color.WHITE);
        dataBox.setStroke(Color.BLACK);
        dataBox.setTranslateX(115);

        Label actions = new Label("ACTIONS");
        actions.setTranslateY(-50);
        actions.setTranslateX(-215);
        actions.setUnderline(true);

        Button highlight = new Button("HIDE");
        highlight.setTranslateY(-20);
        highlight.setTranslateX(-215);
        highlight.setOnAction((ActionEvent e) -> this.togglePrimerPair(highlight));
        Button moreInfo = new Button("VIEW PROPERTIES");
        moreInfo.setTranslateY(12);
        moreInfo.setTranslateX(-215);
        moreInfo.setOnAction((ActionEvent e) -> this.displayProperties());
        Button remove = new Button("REMOVE");
        remove.setTranslateY(45);
        remove.setTranslateX(-215);
        remove.setOnAction((ActionEvent e) -> this.removePrimerPair());

        Label primerPairHeader = new Label("PRIMER PAIR " + position);
        primerPairHeader.setTranslateY(-50);
        primerPairHeader.setTranslateX(120);
        primerPairHeader.setUnderline(true);
        Label forwardPrimerSeq = new Label("Forward Primer: 5'" + this.primerPair[0].getSequence().toUpperCase() + "3'");
        forwardPrimerSeq.setTranslateY(-30);
        forwardPrimerSeq.setTranslateX(120);
        Label reversePrimerSeq = new Label("Reverse Primer: 5'" + this.primerPair[1].getSequence().toUpperCase() + "3'");
        reversePrimerSeq.setTranslateY(-10);
        reversePrimerSeq.setTranslateX(120);
        Label score = new Label("Primer Pair Score: " + this.score);
        score.setTranslateX(120);
        score.setTranslateY(10);
        Label ampliconIndex = new Label("Amplicon Start: " + (this.primerPair[0].getPosition()[1] + 1) + ", End: " + (this.primerPair[1].getPosition()[0] - 1));
        ampliconIndex.setTranslateX(120);
        ampliconIndex.setTranslateY(30);
        Label ampliconLength = new Label("Amplicon Length: " + (this.primerPair[1].getPosition()[0] - this.primerPair[0].getPosition()[1] - 1) +" bp");
        ampliconLength.setTranslateX(120);
        ampliconLength.setTranslateY(50);

        this.primerPairPane.getChildren().addAll(mainBox, actionBox, dataBox, primerPairHeader, forwardPrimerSeq, reversePrimerSeq, score, ampliconIndex, ampliconLength, actions, highlight, moreInfo, remove);
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

    private void togglePrimerPair(Button highlight) {
        if (this.graphicalForwardPrimer.isHidden()) {
            highlight.setText("HIDE");
            this.graphicalForwardPrimer.show();
            this.graphicalReversePrimer.show();
        } else {;
            highlight.setText("SHOW");
            this.graphicalForwardPrimer.hide();
            this.graphicalReversePrimer.hide();
        }
    }

    private void displayProperties() {
        Stage propertyStage = new Stage();
        BorderPane propertyPane = new BorderPane();
        TextArea info = new TextArea();
        info.setEditable(false);
        HBox quitButtonPane = new HBox();
        quitButtonPane.setAlignment(Pos.CENTER);
        Button quitButton = new Button("OK");
        quitButton.setOnAction((ActionEvent e) -> propertyStage.close());
        quitButtonPane.getChildren().add(quitButton);
        propertyPane.setTop(info);
        propertyPane.setBottom(quitButtonPane);
        propertyStage.setScene(new Scene(propertyPane));
        propertyStage.show();

        info.setText(
                "Primer Pair Score: " + this.primerPair[0].compatibilityScore(this.primerPair[1]) + "\n" +
                "Amplicon Start: " + (this.primerPair[0].getPosition()[1] + 1) + ", End: " + (this.primerPair[1].getPosition()[0] - 1) + "\n" +
                "Amplicon Length: " + (this.primerPair[1].getPosition()[0] - this.primerPair[0].getPosition()[1] - 1) +" bp\n" +
                "\n" +
                "Forward Primer: 5'" + this.primerPair[0].getSequence().toUpperCase() + "3'" + "\n" +
                "Forward Primer Start: " + this.primerPair[0].getPosition()[0] + ", End: " + this.primerPair[0].getPosition()[1] + "\n" +
                "Forward Primer GC%: " + this.primerPair[0].getGC_Content() * 100 + "%\n" +
                "Forward Primer Melting Temperature: " + this.primerPair[0].getMeltingTemperature() + " degrees C\n" +
                "Forward Primer Length: " + this.primerPair[0].getLength() + " bp\n" +
                "\n" +
                "Reverse Primer: 5'" + this.primerPair[1].getSequence().toUpperCase() + "3'" + "\n" +
                "Reverse Primer Start: " + this.primerPair[1].getPosition()[0] + ", End: " + this.primerPair[1].getPosition()[1] + "\n" +
                "Reverse Primer GC%: " + this.primerPair[0].getGC_Content() * 100 + "%\n" +
                "Reverse Primer Melting Temperature: " + this.primerPair[0].getMeltingTemperature() + " degrees C\n" +
                "Reverse Primer Length: " + this.primerPair[0].getLength() + " bp\n"
        );
    }

    private void removePrimerPair() {
        this.graphicalForwardPrimer.hide();
        this.graphicalReversePrimer.hide();
        ArrayList<GraphicalPrimerPair> newResults = new ArrayList<>(this.designer.getResults());
        newResults.remove(this);
        this.designer.setResults(newResults, true);
    }
}
