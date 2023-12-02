package indy;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;


public class AutomaticPrimerDesigner {
    private Sequence inputSequence;
    private SequenceDisplayer displayer;

    public AutomaticPrimerDesigner(GridPane root, SequenceDisplayer displayer) {
        this.displayer = displayer;

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
    }

    private ArrayList<Primer> generatePossiblePrimers(Sequence region, boolean isForwardPrimers) {
        ArrayList<Primer> possiblePrimers = new ArrayList<>();
        for (int i = 0; i < region.getLength() - 19; i++) {
            if (isForwardPrimers) {
                possiblePrimers.add(new ForwardPrimer(region.getSequence().substring(i, i + 20)));
            } else {
                possiblePrimers.add(new ReversePrimer(region.getSequence().substring(i, i + 20)));
            }
        }
        return possiblePrimers;
    }

    public void setInputSequence(Sequence inputSequence) {
        this.inputSequence = inputSequence;
    }
}
