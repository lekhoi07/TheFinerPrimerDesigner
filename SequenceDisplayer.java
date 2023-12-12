package indy;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import java.util.ArrayList;

/**
 * This class displays the template DNA sequence the user inputted graphically on the display tab.
 */
public class SequenceDisplayer {
    private BorderPane root;
    private Sequence inputSequence;
    private HBox nothingDisplayedPane;
    private ArrayList<GraphicalNucleotide> graphicalSequence;
    private Pane displayPaneScrollable;
    private SequenceSelector selector;

    /**
     * This constructor initializes the instance variables then displays the template sequence graphically.
     * @param root
     */
    public SequenceDisplayer(BorderPane root) {
        this.root = root;
        this.displayPaneScrollable = new Pane();
        this.displaySequence();
    }

    public void setInputSequence(Sequence inputSequence) {
        this.inputSequence = inputSequence;
        this.graphicalSequence = new ArrayList<>();
        this.displaySequence();
    }

    public int getInputSequenceLength() {
        return this.inputSequence.getSequence().length();
    }

    public void setDisplayPane(ScrollPane displayPane) {
        this.displayPaneScrollable = new Pane();
        displayPane.setContent(this.displayPaneScrollable);
    }

    /**
     * This method displays that there is no sequence when there is no input yet. Otherwise, it will create a
     * GraphicalNucleotide for each nucleotide in the input sequence.
     */
    private void displaySequence() {
        if (this.inputSequence == null) {
            this.nothingDisplayedPane = new HBox();
            Label nothingDisplayedLabel = new Label("No sequence to display. Upload your DNA template on the previous tab!");
            this.nothingDisplayedPane.setAlignment(Pos.CENTER);
            this.nothingDisplayedPane.getChildren().add(nothingDisplayedLabel);
            this.root.setCenter(this.nothingDisplayedPane);
        } else {
            this.root.getChildren().remove(this.nothingDisplayedPane);
            for (int i = 0; i < this.inputSequence.getLength(); i++) {
                this.graphicalSequence.add(new GraphicalNucleotide(new Sequence("" + this.inputSequence.getSequence().toCharArray()[i]), i, this.displayPaneScrollable, this, true, 0, true));
            }
            this.makeStartLabel();
            this.makeEndLabel();
        }
    }

    /**
     * This helper method creates text that indicates the start of the DNA sequence.
     */
    private void makeStartLabel() {
        Pane sequenceStartPane = new Pane();
        Label startLabel = new Label("DNA SEQUENCE STARTS HERE");
        sequenceStartPane.getChildren().add(startLabel);
        sequenceStartPane.setTranslateX(168);
        sequenceStartPane.setTranslateY(50);
        this.displayPaneScrollable.getChildren().addAll(sequenceStartPane, new Rectangle(10, Math.max(this.graphicalSequence.get(this.inputSequence.getLength() - 1).getCoordinates()[1] + 150, 675), Color.LIGHTGRAY));
    }

    /**
     * This helper method creates text that indicates the end of the template sequence.
     */
    private void makeEndLabel() {
        Pane sequenceEndPane = new Pane();
        Label endLabel = new Label("DNA SEQUENCE ENDS HERE");
        sequenceEndPane.getChildren().add(endLabel);
        sequenceEndPane.setTranslateX(175);
        sequenceEndPane.setTranslateY(this.graphicalSequence.get(this.inputSequence.getLength() - 1).getCoordinates()[1] + 75);
        this.displayPaneScrollable.getChildren().add(sequenceEndPane);
    }

    /**
     * This method helps keep the user's selection continuous because primers and amplicons must be a sequence of
     * consecutive nucleotides on the template DNA.
     * @param selectedPosition
     */
    public void keepSelectionContinuous(int selectedPosition) {
        // Calculate how many times the color changes on the DNA sequence after the user selects one nucleotide.
        Color currentColor = Color.WHITE;
        int colorChanges = 0;
        int[] colorChangePositions = new int[4];
        for (GraphicalNucleotide nucleotide : this.graphicalSequence) {
            if (nucleotide.getFill() != currentColor) {
                colorChanges += 1;
                colorChangePositions[colorChanges - 1] = nucleotide.getPosition();
                currentColor = nucleotide.getFill();
            }
        }

        // If the color changes less than two times, the current selection is continuous and allow the user's selection.
        boolean isContinuous = colorChanges <= 2;
        if (!isContinuous) {
            if (this.graphicalSequence.get(selectedPosition).getFill() == Color.VIOLET) {
                // If not, then if the user is trying to select, then set every color in between the point of selection
                // and the previous selection to violet (selected).
                for (int i = colorChangePositions[1]; i < colorChangePositions[2]; i++) {
                    this.graphicalSequence.get(i).setFill(Color.VIOLET, 1);
                }
            } else {
                // If not, then if the user is trying to de-select, then set every color to the right or the left of
                // the point of selection (whichever one is fewer colors changed) to white (de-selected).
                if (colorChangePositions[1] - colorChangePositions[0] < colorChangePositions[3] - colorChangePositions[2]) {
                    for (int i = colorChangePositions[0]; i < colorChangePositions[1]; i++) {
                        this.graphicalSequence.get(i).setFill(Color.WHITE, 1);
                    }
                } else {
                    for (int i = colorChangePositions[2]; i < colorChangePositions[3]; i++) {
                        this.graphicalSequence.get(i).setFill(Color.WHITE, 1);
                    }
                }
            }
        }
    }


    public ArrayList<GraphicalNucleotide> getGraphicalSequence() {
        return this.graphicalSequence;
    }

    /**
     * This method returns the indices of where the selected region starts and ends through getting the indices
     * of where the color changes on the graphical template sequence.
     * @return
     */
    public int[] getSelectedRegion() {
        Color currentColor = Color.WHITE;
        int colorChanges = 0;
        int[] colorChangePositions = new int[2];
        for (GraphicalNucleotide nucleotide : this.graphicalSequence) {
            if (currentColor != nucleotide.getFill()) {
                colorChanges += 1;
                currentColor = nucleotide.getFill();
                colorChangePositions[colorChanges - 1] = nucleotide.getPosition();
            }
        }
        if (colorChanges == 0) {
            return null;
        }
        return colorChangePositions;
    }

    public void setSelector(SequenceSelector selector) {
        this.selector = selector;
    }

    public void setSelectionText() {
        this.selector.setText(this.getSelectedRegion());
    }

    public Sequence getInputSequence() {
        return this.inputSequence;
    }

    public Pane getDisplayPaneScrollable() {
        return this.displayPaneScrollable;
    }
}
