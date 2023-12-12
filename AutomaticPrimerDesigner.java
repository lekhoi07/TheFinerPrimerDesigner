package indy;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.TextAlignment;
import java.util.*;

/**
 * This class houses the primary algorithm of this program: automatically generating the top 10 primer pairs for
 * a given amplicon. It does this by computing every possible forward and reverse primer. It then generates every
 * possible primer pair that can be made from the top 20 forward and top 20 reverse primers. Finally, it selects
 * the top 10 pairs and returns them as results. When scoring the primers and primer pairs, a lower score means it
 * is better.
 */
public class AutomaticPrimerDesigner {
    private Sequence inputSequence;
    private SequenceDisplayer displayer;
    private TheFinerPrimerDesigner designer;
    private GridPane root;

    /**
     * This constructor initializes the instance variables and sets up the GUI for the user to run the algorithm.
     * @param root
     * @param displayer
     * @param designer
     */
    public AutomaticPrimerDesigner(GridPane root, SequenceDisplayer displayer, TheFinerPrimerDesigner designer) {
        this.root = root;
        this.displayer = displayer;
        this.designer = designer;
        this.setUpGUI();
    }

    /**
     * This helper method sets up the GUI so that the user can run the designing algorithm.
     */
    private void setUpGUI() {
        // Create text that displays the word "Automatic" above the corresponding button.
        HBox automaticPane = new HBox();
        Label automatic = new Label("Automatic");
        automaticPane.setTranslateX(73);
        automaticPane.setTranslateY(375);
        automaticPane.getChildren().add(automatic);

        // Create button that activates the automatic primer designing algorithm.
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

        // Add both graphical elements graphically.
        this.root.getChildren().addAll(automaticPane, generatePrimers);
    }

    /**
     * This method calculates the top 10 primer pairs that will amplify the region the user selects.
     */
    private void generatePrimers() {
        // Set the amplicon as the user's selected region and check whether the user has selected a region at all.
        int[] amplicon = this.displayer.getSelectedRegion();
        if (amplicon == null) {
            new ErrorMessage("No amplicon selected");
            return;
        }

        // Define the regions where forward and reverse primers may be located.
        Sequence forwardPrimerRegion = new Sequence(this.inputSequence.getSequence().substring(1, amplicon[0]));
        Sequence reversePrimerRegion = new Sequence(this.inputSequence.getSequence().substring(amplicon[1], this.inputSequence.getLength() - 1));

        // Generate a list the possible forward and reverse primers and check to see if any primers can be made.
        ArrayList<Primer> possibleForwardPrimers = this.generatePossiblePrimers(forwardPrimerRegion, true);
        ArrayList<Primer> possibleReversePrimers = this.generatePossiblePrimers(reversePrimerRegion, false);
        if (possibleForwardPrimers.isEmpty() || possibleReversePrimers.isEmpty()) {
            new ErrorMessage("No possible primers");
            return;
        }

        // Create hash maps that associate potential primer to its corresponding goodness score. Trim the hashmap
        // so that it contains the top 20 key-value pairs max.
        HashMap<Primer, Double> forwardPrimerScores = this.calculatePrimerScores(possibleForwardPrimers);
        HashMap<Primer, Double> reversePrimerScores = this.calculatePrimerScores(possibleReversePrimers);

        // Create a hashmap that matches every top forward primer with every top reverse primer to form primer pairs.
        // Then calculate the compatibility scores of those pairs and associated them with their corresponding pairs.
        HashMap<Primer[], Double> primerPairCompatibilityScores = this.calculatePrimerPairCompatibilityScores(forwardPrimerScores.keySet(), reversePrimerScores.keySet());

        // Create a hashmap that stores the overall score of all primer pairs made by combining the individual primer
        // scores and the compatibility score for each pair.
        HashMap<Primer[], Double> overallPrimerPairScores = new HashMap<>();
        for (Primer[] primerPair : primerPairCompatibilityScores.keySet()) {
            overallPrimerPairScores.put(primerPair, forwardPrimerScores.get(primerPair[0]) + reversePrimerScores.get(primerPair[1]) + primerPairCompatibilityScores.get(primerPair));
        }

        // Select only the top 10 primer pairs.
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

        // Generate a list of results so that the final primer pairs can be displayed graphically.
        ArrayList<GraphicalPrimerPair> results = new ArrayList<>();
        for (Primer[] primerPair : top10PrimerPairs) {
            GraphicalPrimerPair graphicalPrimerPair = new GraphicalPrimerPair(primerPair, overallPrimerPairScores.get(primerPair), this.designer);
            results.add(graphicalPrimerPair);
            graphicalPrimerPair.setGraphicalForwardPrimer(new GraphicalPrimer(primerPair[0], primerPair[0].getPosition()[0], this.displayer.getDisplayPaneScrollable(), this.displayer, true));
            graphicalPrimerPair.setGraphicalReversePrimer(new GraphicalPrimer(primerPair[1], primerPair[1].getPosition()[0], this.displayer.getDisplayPaneScrollable(), this.displayer, false));
        }

        // Let the top-level logic class know about the new results.
        this.designer.setResults(results, false);
    }

    /**
     * This helper method takes in a possible region on which the primer can be found and generates a list of the
     * possible primers that can be made. It also takes in a boolean to indicate whether to make forward or reverse
     * primers.
     * @param region
     * @param isForwardPrimers
     * @return
     */
    private ArrayList<Primer> generatePossiblePrimers(Sequence region, boolean isForwardPrimers) {
        ArrayList<Primer> possiblePrimers = new ArrayList<>();

        // Return an empty list if the region where primers can be located is too short for a primer to be made.
        if (region.getLength() < Constants.MIN_PRIMER_LENGTH) {
            return possiblePrimers;
        }

        // Generate every possible substring of the possible primer region of length 18 through 24 bp.
        for (int primerLength = Constants.MIN_PRIMER_LENGTH; primerLength <= Constants.MAX_PRIMER_LENGTH; primerLength++) {
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

        // Return these substrings.
        return possiblePrimers;
    }

    /**
     * This helper method takes in a list of primers and returns a hashmap of each primer in the list and its
     * corresponding goodness score. This list is at most 20 key-value pairs long.
     * @param primerList
     * @return
     */
    private HashMap<Primer, Double> calculatePrimerScores(ArrayList<Primer> primerList) {
        // Create the hashmap that associates primers to its goodness score.
        HashMap<Primer, Double> primerScores = new HashMap<>();
        for (Primer primer : primerList) {
            double score = primer.goodnessScore();
            primerScores.put(primer, score);
        }

        // Trim that hashmap to only get the top 20 primers.
        HashMap<Primer, Double> trimmedPrimerScores = new HashMap<>();
        for (Primer primer : primerScores.keySet()) {
            if (trimmedPrimerScores.size() < 20) {
                trimmedPrimerScores.put(primer, primerScores.get(primer));
            } else {
                Primer maxScorePrimer = this.getMaxScorePrimer(trimmedPrimerScores);
                if (primerScores.get(primer) < primerScores.get(maxScorePrimer)) {
                    trimmedPrimerScores.remove(maxScorePrimer);
                    trimmedPrimerScores.put(primer, primerScores.get(primer));
                }
            }
        }

        return trimmedPrimerScores;
    }

    /**
     * This helper method gets the best-scoring primer from a hashmap of primers and their scores.
     * @param scores
     * @return
     */
    private Primer getMaxScorePrimer(HashMap<Primer, Double> scores) {
        // Select an arbitrary primer in the hashmap to be the best-scoring.
        Primer maxScorePrimer = null;
        for (Primer primer : scores.keySet()) {
            maxScorePrimer = primer;
            break;
        }

        // Check whether the randomly selected primer is actually the greatest by comparing it to every other primer.
        // If there is a primer that scores better, then make the better-scoring primer the new max score primer.
        for (Primer primer : scores.keySet()) {
            if (scores.get(primer) > scores.get(maxScorePrimer)) {
                maxScorePrimer = primer;
            }
        }

        return maxScorePrimer;
    }

    /**
     * This helper method takes in a set of forward primers and a set of reverse primers, generates all possible pairings,
     * then returns a hashmap that associates a pairing with its compatibility score.
     * @param possibleForwardPrimers
     * @param possibleReversePrimers
     * @return
     */
    private HashMap<Primer[], Double> calculatePrimerPairCompatibilityScores(Set<Primer> possibleForwardPrimers, Set<Primer> possibleReversePrimers) {
        HashMap<Primer[], Double> primerPairCompatibilityScores = new HashMap<>();
        for (Primer forwardPrimer : possibleForwardPrimers) {
            for (Primer reversePrimer : possibleReversePrimers) {
                primerPairCompatibilityScores.put(new Primer[]{forwardPrimer, reversePrimer}, forwardPrimer.compatibilityScore(reversePrimer));
            }
        }
        return primerPairCompatibilityScores;
    }

    /**
     * This helper method returns the best-scoring primer pair given a hashmap of primer pairs to its corresponding
     * goodness score.
     * @param primerList
     * @param scores
     * @return
     */
    private Primer[] getMaxScorePair(ArrayList<Primer[]> primerList, HashMap<Primer[], Double> scores) {
        Primer[] maxScorePrimerPair = primerList.get(0);
        for (Primer[] primerPair : primerList) {
            if (scores.get(primerPair) > scores.get(maxScorePrimerPair)) {
                maxScorePrimerPair = primerPair;
            }
        }

        return maxScorePrimerPair;
    }

    /**
     * This method is necessary so that the top-level logic class can tell the algorithm about the sequence of template
     * DNA currently being analyzed.
     * @param inputSequence
     */
    public void setInputSequence(Sequence inputSequence) {
        this.inputSequence = inputSequence;
    }
}
