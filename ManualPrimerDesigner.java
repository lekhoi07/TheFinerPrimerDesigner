package indy;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;

import java.util.ArrayList;

public class ManualPrimerDesigner {
    private GridPane root;
    private boolean forwardPrimerMade;
    private boolean reversePrimerMade;
    private SequenceDisplayer displayer;
    private Primer forwardPrimer;
    private ReversePrimer reversePrimer;
    private GraphicalPrimer graphicalForwardPrimer, graphicalReversePrimer;
    private TheFinerPrimerDesigner designer;

    public ManualPrimerDesigner(GridPane root, SequenceDisplayer displayer, TheFinerPrimerDesigner designer) {
        this.root = root;
        this.forwardPrimerMade = false;
        this.reversePrimerMade = false;
        this.displayer = displayer;
        this.designer = designer;

        Button makeForwardPrimer = new Button("MAKE SELECTION FORWARD PRIMER");
        makeForwardPrimer.setWrapText(true);
        makeForwardPrimer.setTextAlignment(TextAlignment.CENTER);
        makeForwardPrimer.setMaxWidth(150);
        makeForwardPrimer.setTranslateX(25);
        makeForwardPrimer.setTranslateY(450);
        makeForwardPrimer.setOnAction((ActionEvent e) -> this.makeForwardPrimer());

        Button makeReversePrimer = new Button("MAKE SELECTION REVERSE PRIMER");
        makeReversePrimer.setWrapText(true);
        makeReversePrimer.setTextAlignment(TextAlignment.CENTER);
        makeReversePrimer.setMaxWidth(150);
        makeReversePrimer.setTranslateX(25);
        makeReversePrimer.setTranslateY(500);
        makeReversePrimer.setOnAction((ActionEvent e) -> this.makeReversePrimer());

        root.getChildren().addAll(makeForwardPrimer, makeReversePrimer);
    }

    private void makeForwardPrimer() {
        if (!this.forwardPrimerMade) {
            this.forwardPrimer = new Primer(this.displayer.getInputSequence().getSequence().substring(this.displayer.getSelectedRegion()[0], this.displayer.getSelectedRegion()[1]));
            this.forwardPrimer.setPosition(new int[]{this.displayer.getSelectedRegion()[0], this.displayer.getSelectedRegion()[1] - 1});
            this.forwardPrimerMade = true;
            this.graphicalForwardPrimer = new GraphicalPrimer(this.forwardPrimer, this.displayer.getSelectedRegion()[0], this.displayer.getDisplayPaneScrollable(), this.displayer, true);
        } else {
            new ErrorMessage("Create a corresponding reverse primer before proceeding");
            return;
        }
        this.createPrimerPair();
    }

    private void makeReversePrimer() {
        if (!this.reversePrimerMade) {
            this.reversePrimer = new ReversePrimer(this.displayer.getInputSequence().getSequence().substring(this.displayer.getSelectedRegion()[0], this.displayer.getSelectedRegion()[1]));
            this.reversePrimer.setPosition(new int[]{this.displayer.getSelectedRegion()[0], this.displayer.getSelectedRegion()[1] - 1});
            this.reversePrimerMade = true;
            this.graphicalReversePrimer = new GraphicalPrimer(this.reversePrimer, this.displayer.getSelectedRegion()[0], this.displayer.getDisplayPaneScrollable(), this.displayer, false);
        } else {
            new ErrorMessage("Create a corresponding forward primer before proceeding");
            return;
        }
        this.createPrimerPair();
    }

    private void createPrimerPair() {
        if (this.forwardPrimerMade && this.reversePrimerMade) {
            ArrayList<GraphicalPrimerPair> manuallyDesignedPair = new ArrayList<>();
            manuallyDesignedPair.add(new GraphicalPrimerPair(new Primer[]{this.forwardPrimer, this.reversePrimer}));
            this.designer.setResults(manuallyDesignedPair, false);
            this.forwardPrimerMade = false;
            this.reversePrimerMade = false;
        }
    }
}
