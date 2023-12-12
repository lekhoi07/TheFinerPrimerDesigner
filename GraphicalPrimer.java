package indy;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import java.util.ArrayList;

/**
 * This class represents the primers that pop up graphically when they are designed whether manually or graphically.
 * They are orange when forward primers and blue when reverse primers.
 */
public class GraphicalPrimer {
    private ArrayList<GraphicalNucleotide> graphicalPrimer;
    private boolean hidden;

    /**
     * This constructor initializes the instance variables and calls upon a helper method to display the primer.
     * @param primer
     * @param startIndex
     * @param root
     * @param displayer
     * @param isForward
     */
    public GraphicalPrimer(Primer primer, int startIndex, Pane root, SequenceDisplayer displayer, boolean isForward) {
        this.graphicalPrimer = new ArrayList<>();
        this.hidden = false;
        this.displayGraphicalPrimer(isForward, primer, startIndex, root, displayer);
    }

    /**
     * This helper method displays the primer either above the template sequence if forward or below if reverse.
     * @param isForward
     * @param primer
     * @param startIndex
     * @param root
     * @param displayer
     */
    private void displayGraphicalPrimer(boolean isForward, Primer primer, int startIndex, Pane root, SequenceDisplayer displayer) {
        int offset;
        Color fill;
        String seq;

        if (isForward) {
            offset = -20;
            fill = Color.ORANGE;
            seq = primer.getSequence();
        } else {
            offset = 40;
            fill = Color.LIGHTBLUE;
            seq = primer.getComplement().getSequence();
        }

        int position = 0;
        for (char nucleotide :seq.toCharArray()) {
            GraphicalNucleotide primerNucleotide = new GraphicalNucleotide(new Sequence("" + nucleotide), startIndex + position, root, displayer, false, offset, false);
            primerNucleotide.setFill(fill, 0.50);
            this.graphicalPrimer.add(primerNucleotide);
            position += 1;
        }
    }

    public void show() {
        for (GraphicalNucleotide nucleotide : this.graphicalPrimer) {
            nucleotide.show();
        }
        this.hidden = false;
    }

    public void hide() {
        for (GraphicalNucleotide nucleotide : this.graphicalPrimer) {
            nucleotide.hide();
        }
        this.hidden = true;
    }

    public boolean isHidden() {
        return this.hidden;
    }
}
