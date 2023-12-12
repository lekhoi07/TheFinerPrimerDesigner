package indy;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.util.ArrayList;

/**
 * This class represents a single result that appears on the results tab. It also sets all the things a user can do
 * to interact with the designed primer pair.
 */
public class GraphicalPrimerPair {
    private StackPane primerPairPane;
    private Pane root;
    private int position;
    private Primer[] primerPair;
    private GraphicalPrimer graphicalForwardPrimer, graphicalReversePrimer;
    private double score;
    private TheFinerPrimerDesigner designer;

    /**
     * This constructor intializes the instance variables.
     * @param primerPair
     * @param score
     * @param designer
     */
    public GraphicalPrimerPair(Primer[] primerPair, double score, TheFinerPrimerDesigner designer) {
        this.primerPair = primerPair;
        this.primerPairPane = new StackPane();
        this.score = score;
        this.designer = designer;
    }

    /**
     * This method displays the primer pair as a result.
     * @param position
     */
    public void displayGraphicalPrimerPair(int position) {
        // Create the rectangles with black borders.
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

        // Create a label called "ACTIONS" below which the actions the user can do are listed.
        Label actions = new Label("ACTIONS");
        actions.setTranslateY(-50);
        actions.setTranslateX(-215);
        actions.setUnderline(true);

        // Create the buttons.
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

        // Create the labels that display information about the primer pair.
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

        // Add everything graphically.
        this.primerPairPane.getChildren().addAll(mainBox, actionBox, dataBox, primerPairHeader, forwardPrimerSeq, reversePrimerSeq, score, ampliconIndex, ampliconLength, actions, highlight, moreInfo, remove);
        this.primerPairPane.setTranslateX(this.calcDisplayCoordinates()[0]);
        this.primerPairPane.setTranslateY(this.calcDisplayCoordinates()[1]);
        this.root.getChildren().add(this.primerPairPane);
    }

    /**
     * This helper method returns the coordinates the graphical primer pair should be displayed at.
     * @return
     */
    private int[] calcDisplayCoordinates() {
        int posX = 20;
        int posY = 20 + Constants.SPACING_BETWEEN_RESULTS * (this.position - 1);
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

    /**
     * This helper method hides or shows the result on the display tab as graphical primers depending on whether
     * the graphical primers are already hidden or not.
     * @param highlight
     */
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

    /**
     * This helper method is called whenever the user clicks on the button that requests more info on the primer pair.
     * It creates a new stage with all the info.
     */
    private void displayProperties() {
        // Create stage that displays information.
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
        propertyStage.setTitle("COMPLETE PRIMER PAIR INFORMATION");
        propertyStage.show();

        // Set the content of that information.
        info.setText(
                "Primer Pair Score: " + (this.primerPair[0].compatibilityScore(this.primerPair[1]) + this.primerPair[0].goodnessScore() + this.primerPair[1].goodnessScore()) + "\n" +
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

    /**
     * This method is called when the user clicks on the remove button. It removes the result both graphically and
     * logically.
     */
    private void removePrimerPair() {
        // Warn the user that removing is undoable.
        WarningMessage warning = new WarningMessage("Are you sure you want to delete this primer pair? This action cannot be undone.");
        warning.getContinueButton().setOnAction((ActionEvent e) -> {
            // If the user decides to continue, remove the graphical primers graphically.
            warning.getWarningStage().close();
            this.graphicalForwardPrimer.hide();
            this.graphicalReversePrimer.hide();

            // ALso remove the result logically while displaying a fade-out animation.
            ArrayList<GraphicalPrimerPair> newResults = new ArrayList<>(this.designer.getResults());
            newResults.remove(this);
            KeyFrame kf = new KeyFrame(Duration.seconds(0.1), (ActionEvent f) -> this.primerPairPane.setOpacity(this.primerPairPane.getOpacity() * .75));
            Timeline fadingAnimation = new Timeline(kf);
            fadingAnimation.setCycleCount(5);
            fadingAnimation.play();
            fadingAnimation.setOnFinished((ActionEvent f) -> this.designer.setResults(newResults, true));
        });
    }
}
