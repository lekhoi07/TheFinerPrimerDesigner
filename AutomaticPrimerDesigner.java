package indy;

import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.TextAlignment;

import javax.swing.*;
import java.lang.reflect.Array;
import java.util.*;


public class AutomaticPrimerDesigner {
    private Sequence inputSequence;
    private SequenceDisplayer displayer;
    private TheFinerPrimerDesigner designer;
    private Pane scrollPane;
    private GridPane root;

    public AutomaticPrimerDesigner(GridPane root, SequenceDisplayer displayer, TheFinerPrimerDesigner designer) {
        this.root = root;
        this.displayer = displayer;
        this.designer = designer;
        this.setUpGUI();
    }

    private void setUpGUI() {
        HBox automaticPane = new HBox();
        Label automatic = new Label("Automatic");
        automaticPane.setTranslateX(73);
        automaticPane.setTranslateY(375);
        automaticPane.getChildren().add(automatic);

        Button generatePrimers = new Button("GENERATE PRIMERS FOR SELECTION AS AMPLICON");
        generatePrimers.setWrapText(true);
        generatePrimers.setTextAlignment(TextAlignment.CENTER);
        generatePrimers.setMaxWidth(150);
        generatePrimers.setTranslateX(25);
        generatePrimers.setTranslateY(405);
        generatePrimers.setOnAction((ActionEvent e) -> {
            WarningMessage warning = new WarningMessage("The automatic primer designer is a computationally taxing algorithm that takes a lot of time to run, especially for longer template sequences. Do you wish to proceed?");
            warning.getContinueButton().setOnAction((ActionEvent f) -> {
                this.generatePrimers();
                warning.getWarningStage().close();
            });
        });
        this.root.getChildren().addAll(automaticPane, generatePrimers);
    }

    private void generatePrimers() {
        int[] amplicon = this.displayer.getSelectedRegion();
        if (amplicon == null) {
            new ErrorMessage("No amplicon selected");
            return;
        }

        Sequence forwardPrimerRegion = new Sequence(this.inputSequence.getSequence().substring(1, amplicon[0]));
        Sequence reversePrimerRegion = new Sequence(this.inputSequence.getSequence().substring(amplicon[1], this.inputSequence.getLength() - 1));

        ArrayList<Primer> possibleForwardPrimers = this.generatePossiblePrimers(forwardPrimerRegion, true);
        ArrayList<Primer> possibleReversePrimers = this.generatePossiblePrimers(reversePrimerRegion, false);
        if (possibleForwardPrimers.isEmpty() || possibleReversePrimers.isEmpty()) {
            new ErrorMessage("No possible primers");
            return;
        }

        HashMap<Primer, Double> forwardPrimerScores = this.calculatePrimerScores(possibleForwardPrimers);
        HashMap<Primer, Double> reversePrimerScores = this.calculatePrimerScores(possibleReversePrimers);

        HashMap<Primer[], Double> primerPairCompatibilityScores = this.calculatePrimerPairCompatibilityScores(forwardPrimerScores.keySet(), reversePrimerScores.keySet());

        HashMap<Primer[], Double> overallPrimerPairScores = new HashMap<>();
        for (Primer[] primerPair : primerPairCompatibilityScores.keySet()) {
            overallPrimerPairScores.put(primerPair, forwardPrimerScores.get(primerPair[0]) + reversePrimerScores.get(primerPair[1]) + primerPairCompatibilityScores.get(primerPair));
        }

        ArrayList<Primer[]> top10PrimerPairs = new ArrayList<>();
        for (Primer[] primerPair : overallPrimerPairScores.keySet()) {
            if (top10PrimerPairs.size() < 10) {
                top10PrimerPairs.add(primerPair);
            } else {
                Primer[] maxScorePrimerPair = this.getMaxScorePair(top10PrimerPairs, overallPrimerPairScores);
                if (overallPrimerPairScores.get(primerPair) < overallPrimerPairScores.get(maxScorePrimerPair)) {
                    top10PrimerPairs.remove(maxScorePrimerPair);
                    top10PrimerPairs.add(primerPair);
                }
            }
        }
        ArrayList<GraphicalPrimerPair> results = new ArrayList<>();
        for (Primer[] primerPair : top10PrimerPairs) {
            GraphicalPrimerPair graphicalPrimerPair = new GraphicalPrimerPair(primerPair, overallPrimerPairScores.get(primerPair), this.designer);
            results.add(graphicalPrimerPair);
            graphicalPrimerPair.setGraphicalForwardPrimer(new GraphicalPrimer(primerPair[0], primerPair[0].getPosition()[0], this.displayer.getDisplayPaneScrollable(), this.displayer, true));
            graphicalPrimerPair.setGraphicalReversePrimer(new GraphicalPrimer(primerPair[1], primerPair[1].getPosition()[0], this.displayer.getDisplayPaneScrollable(), this.displayer, false));
        }
        this.designer.setResults(results, false);

    }

    private ArrayList<Primer> generatePossiblePrimers(Sequence region, boolean isForwardPrimers) {
        ArrayList<Primer> possiblePrimers = new ArrayList<>();
        if (region.getLength() < 18) {
            return possiblePrimers;
        }
        for (int primerLength = 18; primerLength < 25; primerLength++) {
            for (int i = 0; i < region.getLength() - primerLength + 1; i++) {
                if (isForwardPrimers) {
                    Primer forwardPrimer = new Primer(region.getSequence().substring(i, i + primerLength));
                    forwardPrimer.setPosition(new int[]{i + 1, i + primerLength});
                    possiblePrimers.add(forwardPrimer);
                } else {
                    Primer reversePrimer = new ReversePrimer(region.getSequence().substring(i, i + primerLength));
                    reversePrimer.setPosition(new int[]{this.inputSequence.getLength() - region.getLength() + i - 1, this.inputSequence.getLength() - region.getLength() + i + primerLength - 2});
                    possiblePrimers.add(reversePrimer);
                }
            }
        }
        return possiblePrimers;
    }

    private HashMap<Primer, Double> calculatePrimerScores(ArrayList<Primer> primerList) {
        HashMap<Primer, Double> primerScores = new HashMap<>();
        for (Primer primer : primerList) {
            primerScores.put(primer, primer.goodnessScore());
        }
        return primerScores;
    }

    private HashMap<Primer[], Double> calculatePrimerPairCompatibilityScores(Set<Primer> possibleForwardPrimers, Set<Primer> possibleReversePrimers) {
        HashMap<Primer[], Double> primerPairCompatibilityScores = new HashMap<>();
        for (Primer forwardPrimer : possibleForwardPrimers) {
            for (Primer reversePrimer : possibleReversePrimers) {
                primerPairCompatibilityScores.put(new Primer[]{forwardPrimer, reversePrimer}, forwardPrimer.compatibilityScore(reversePrimer));
            }
        }
        return primerPairCompatibilityScores;
    }

    private Primer[] getMaxScorePair(ArrayList<Primer[]> primerList, HashMap<Primer[], Double> scores) {
        Primer[] maxScorePrimerPair = primerList.get(0);
        for (Primer[] primerPair : primerList) {
            if (scores.get(primerPair) > scores.get(maxScorePrimerPair)) {
                maxScorePrimerPair = primerPair;
            }
        }

        return maxScorePrimerPair;
    }

    public void setInputSequence(Sequence inputSequence) {
        this.inputSequence = inputSequence;
    }
}
