package indy;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class GraphicalPrimer {
    private ArrayList<GraphicalNucleotide> graphicalPrimer;
    private boolean hidden;

    public GraphicalPrimer(Primer primer, int startIndex, Pane root, SequenceDisplayer displayer, boolean isForward) {
        this.graphicalPrimer = new ArrayList<>();
        this.hidden = false;
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
