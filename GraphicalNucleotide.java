package indy;

import javafx.event.Event;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * This class represents a single selectable nucleotide in the graphical representation of the template DNA sequence
 * on the display pane. It is also the building block of the graphical primers that pop up when primers are generated.
 */
public class GraphicalNucleotide {
    private Pane root;
    private Sequence nucleotide;
    private int[] coordinates;
    private StackPane nucleotidePane;
    private SequenceDisplayer displayer;
    private Rectangle selectionBox;
    private int position;
    private boolean selectable;
    private int offset;

    /**
     * This constructor initializes the instance variables, notably its position in the DNA sequence and whether it is
     * selectable by the user or not. It then displays itself graphically.
     * @param nucleotide
     * @param position
     * @param root
     * @param displayer
     * @param selectable
     * @param offset
     * @param generateComplement
     */
    public GraphicalNucleotide(Sequence nucleotide, int position, Pane root, SequenceDisplayer displayer, boolean selectable, int offset, boolean generateComplement) {
        this.root = root;
        this.nucleotide = nucleotide;
        this.displayer = displayer;
        this.position = position;
        this.offset = offset;
        this.coordinates = this.calcDisplayCoordinates(position);
        this.selectable = selectable;
        this.displayGraphicalNucleotide(generateComplement);
    }

    /**
     * This helper method displays the graphical nucleotide graphically.
     * @param generateComplement
     */
    private void displayGraphicalNucleotide(boolean generateComplement) {
        // Generate the pane that houses all the graphically elements.
        this.nucleotidePane = new StackPane();

        // Double the size if the complement also has to be generated.
        if (generateComplement) {
            this.selectionBox = new Rectangle(10, 40);
        } else {
            this.selectionBox = new Rectangle(10, 20);
        }
        this.selectionBox.setFill(Color.WHITE);
        this.selectionBox.setStroke(Color.BLACK);

        // Set the text of the nucleotide to the nucleotide it represents. Add the ' symbol after the 5' and 3' end
        // indicators in the input sequence if needed.
        Label nucleotide = new Label(this.nucleotide.getSequence());
        if (nucleotide.getText().toCharArray()[0] == '5') {
            nucleotide.setText("5'");
        } else if (nucleotide.getText().toCharArray()[0] == '3') {
            nucleotide.setText("3'");
        }

        // Add the text displaying the nucleotide to the pane housing it.
        this.nucleotidePane.getChildren().addAll(this.selectionBox, nucleotide);

        // If the complement needs to be generated, also add the text of the complement nucleotide.
        if (generateComplement) {
            Label complementNucleotide = new Label(this.nucleotide.getComplement().getSequence());
            if (complementNucleotide.getText().toCharArray()[0] == '5') {
                complementNucleotide.setText("5'");
            } else if (complementNucleotide.getText().toCharArray()[0] == '3') {
                complementNucleotide.setText("3'");
            }
            nucleotide.setTranslateY(-10);
            complementNucleotide.setTranslateY(10);
            this.nucleotidePane.getChildren().add(complementNucleotide);
        }

        // Set the nucleotide to be displayed in the correct location on screen.
        this.nucleotidePane.setTranslateX(this.coordinates[0]);
        this.nucleotidePane.setTranslateY(this.coordinates[1]);
        this.nucleotidePane.setFocusTraversable(true);
        this.nucleotidePane.setOnMouseClicked((Event e) -> this.makeNucleotideSelectable(this.selectionBox));

        // Add the nucleotide to the display tab.
        this.root.getChildren().add(this.nucleotidePane);
    }

    /**
     * This helper method makes the graphical nucleotide selectable when clicked.
     * @param selectionBox
     */
    private void makeNucleotideSelectable(Rectangle selectionBox) {
        if (this.nucleotide.getSequence().toCharArray()[0] != '5' && this.nucleotide.getSequence().toCharArray()[0] != '3' && this.selectable) {
            if (selectionBox.getFill() == Color.WHITE) {
                selectionBox.setFill(Color.VIOLET);
            } else {
                selectionBox.setFill(Color.WHITE);
            }
            this.displayer.keepSelectionContinuous(this.position);
        }

        this.displayer.setSelectionText();
    }

    /**
     * This helper method calculates where the nucleotide should be displayed.
     * @param position
     * @return
     */
    private int[] calcDisplayCoordinates(int position) {
        int posX = 30 + 10 * (position % Constants.MAX_NUCLEOTIDES_PER_ROW);
        int posY = this.offset + 100 + Constants.SPACING_BETWEEN_ROWS * (position / Constants.MAX_NUCLEOTIDES_PER_ROW);
        return new int[]{posX, posY};
    }

    public int[] getCoordinates() {
        return this.coordinates;
    }

    public Color getFill() {
        return (Color) this.selectionBox.getFill();
    }

    public void setFill(Color color, double opacity) {
        this.selectionBox.setFill(color);
        this.selectionBox.setOpacity(opacity);
    }

    public int getPosition() {
        return this.position;
    }

    public void show() {
        this.root.getChildren().add(this.nucleotidePane);
    }

    public void hide() {
        this.root.getChildren().remove(this.nucleotidePane);
    }
}
