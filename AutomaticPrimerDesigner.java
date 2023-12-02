package indy;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

import java.lang.reflect.Array;
import java.util.*;


public class AutomaticPrimerDesigner {
    private Sequence inputSequence;
    private SequenceDisplayer displayer;
    private TheFinerPrimerDesigner designer;

    public AutomaticPrimerDesigner(GridPane root, SequenceDisplayer displayer, TheFinerPrimerDesigner designer) {
        this.displayer = displayer;
        this.designer = designer;

        Button generatePrimers = new Button("GENERATE PRIMERS");
        generatePrimers.setTranslateX(35);
        generatePrimers.setTranslateY(250);
        generatePrimers.setOnAction((ActionEvent e) -> this.generatePrimers());
        root.getChildren().add(generatePrimers);
    }

    private void generatePrimers() {
        int[] amplicon = this.displayer.getSelectedRegion();
        Sequence forwardPrimerRegion = new Sequence(this.inputSequence.getSequence().substring(1, amplicon[0]));
        Sequence reversePrimerRegion = new Sequence(this.inputSequence.getSequence().substring(amplicon[1], this.inputSequence.getLength() - 1));

        ArrayList<Primer> possibleForwardPrimers = this.generatePossiblePrimers(forwardPrimerRegion, true);
        ArrayList<Primer> possibleReversePrimers = this.generatePossiblePrimers(reversePrimerRegion, false);
        if (possibleForwardPrimers.isEmpty() || possibleReversePrimers.isEmpty()) {
            this.designer.setResults(new ArrayList<>());
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

        this.designer.setResults(top10PrimerPairs);
    }

    private ArrayList<Primer> generatePossiblePrimers(Sequence region, boolean isForwardPrimers) {
        ArrayList<Primer> possiblePrimers = new ArrayList<>();
        if (region.getLength() < 20) {
            return possiblePrimers;
        }

        for (int i = 0; i < region.getLength() - 19; i++) {
            if (isForwardPrimers) {
                possiblePrimers.add(new ForwardPrimer(region.getSequence().substring(i, i + 20)));
            } else {
                possiblePrimers.add(new ReversePrimer(region.getSequence().substring(i, i + 20)));
            }
        }
        return possiblePrimers;
    }

    private HashMap<Primer, Double> calculatePrimerScores(ArrayList<Primer> primerList) {
        HashMap<Primer, Double> primerScores = new HashMap<>();
        for (Primer primer : primerList) {
            primerScores.put(primer, this.gcContentScore(primer) + this.dimerizationScore(primer, primer) + this.hairpinScore());
        }
        return primerScores;
    }

    private double gcContentScore(Primer primer) {
        if (Math.abs(primer.getGC_Content() - 0.5) <= 0.1) {
            return 0;
        } else {
            return Math.abs(primer.getGC_Content() - 0.5);
        }
    }

    private double dimerizationScore(Primer primer1, Primer primer2) {
        String seq1 = primer1.getSequence();
        String seq2 = primer2.getComplement().getReverse().getSequence();

        int maxAlignmentScore = 0;

        for (int i = -1 * seq1.length(); i <= seq1.length(); i++) {
            int numComparisons = seq1.length() - Math.abs(i);
            for (int j = 0; j < numComparisons; j++) {
                int alignmentScore = 0;
                if (seq1.toCharArray()[j] == seq2.toCharArray()[j + i]) {
                    alignmentScore += 1;
                }
                if (alignmentScore > maxAlignmentScore) {
                    maxAlignmentScore = alignmentScore;
                }
            }
        }

        return Math.random();
    }

    private double hairpinScore() {
        return Math.random();
    }

    private HashMap<Primer[], Double> calculatePrimerPairCompatibilityScores(Set<Primer> possibleForwardPrimers, Set<Primer> possibleReversePrimers) {
        HashMap<Primer[], Double> primerPairCompatibilityScores = new HashMap<>();
        for (Primer forwardPrimer : possibleForwardPrimers) {
            for (Primer reversePrimer : possibleReversePrimers) {
                double compatibilityScore = this.dimerizationScore(forwardPrimer, reversePrimer) + this.meltingTempCompatibilityScore();
                primerPairCompatibilityScores.put(new Primer[]{forwardPrimer, reversePrimer}, compatibilityScore);
            }
        }
        return primerPairCompatibilityScores;
    }

    private double meltingTempCompatibilityScore() {
        return Math.random();
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
