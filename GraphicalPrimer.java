package indy;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class GraphicalPrimer {
    public GraphicalPrimer(Primer primer, int startIndex, Pane root, SequenceDisplayer displayer, boolean isForward) {
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
            position += 1;
        }
    }
}
