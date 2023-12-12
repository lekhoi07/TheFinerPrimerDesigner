package indy;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.TextAlignment;
import java.util.ArrayList;

/**
 * This class handles the logic of the user designing their own primers manually.
 */
public class ManualPrimerDesigner {
    private GridPane root;
    private boolean forwardPrimerMade;
    private boolean reversePrimerMade;
    private SequenceDisplayer displayer;
    private Primer forwardPrimer;
    private ReversePrimer reversePrimer;
    private GraphicalPrimer graphicalForwardPrimer, graphicalReversePrimer;
    private TheFinerPrimerDesigner designer;

    /**
     * This construct initializes the instance variables and sets up the GUI.
     * @param root
     * @param displayer
     * @param designer
     */
    public ManualPrimerDesigner(GridPane root, SequenceDisplayer displayer, TheFinerPrimerDesigner designer) {
        this.root = root;
        this.forwardPrimerMade = false;
        this.reversePrimerMade = false;
        this.displayer = displayer;
        this.designer = designer;
        this.setUpGUI();
    }

    /**
     * This helper method creates the graphical elements associated with manual designing. This includes two
     * buttons that allow the user to make a forward or reverse primer.
     */
    private void setUpGUI() {
        // Display text to indicate that the following buttons are for manual designing.
        HBox manualPane = new HBox();
        Label manual = new Label("Manual");
        manualPane.setTranslateX(80);
        manualPane.setTranslateY(495);
        manualPane.getChildren().add(manual);

        // Create button that allows forward primers to be made.
        Button makeForwardPrimer = new Button("MAKE SELECTION FORWARD PRIMER");
        makeForwardPrimer.setWrapText(true);
        makeForwardPrimer.setTextAlignment(TextAlignment.CENTER);
        makeForwardPrimer.setMaxWidth(150);
        makeForwardPrimer.setTranslateX(25);
        makeForwardPrimer.setTranslateY(515);
        makeForwardPrimer.setOnAction((ActionEvent e) -> this.makeForwardPrimer());

        // Create button that allows reverse primers to be made.
        Button makeReversePrimer = new Button("MAKE SELECTION REVERSE PRIMER");
        makeReversePrimer.setWrapText(true);
        makeReversePrimer.setTextAlignment(TextAlignment.CENTER);
        makeReversePrimer.setMaxWidth(150);
        makeReversePrimer.setTranslateX(25);
        makeReversePrimer.setTranslateY(565);
        makeReversePrimer.setOnAction((ActionEvent e) -> this.makeReversePrimer());

        // Add all elements graphically.
        this.root.getChildren().addAll(manualPane, makeForwardPrimer, makeReversePrimer);
    }

    /**
     * This method is called whenever the make forward primer button is pressed. It does error checks to see if the
     * forward primer the user is designing is feasible then makes it if true.
     */
    private void makeForwardPrimer() {
        // The user can not make multiple forward primers at a time.
        if (this.forwardPrimerMade) {
            this.displayError("You have an unfinished primer pair consisting of only a forward primer. Create a corresponding reverse primer before proceeding.");
            return;
        }

        // The user can not make a forward primer of length 0 bp.
        if (this.displayer.getSelectedRegion() == null) {
            this.displayError("No region selected.");
            return;
        }

        // The user can not make a forward primer that would make the amplicon length negative or zero.
        if (this.reversePrimer != null) {
            int ampliconLength = this.reversePrimer.getPosition()[0] - this.displayer.getSelectedRegion()[1];
            if (ampliconLength <= -1) {
                this.displayError("Forward primer must be upstream of the reverse primer.");
                return;
            }
            if (ampliconLength == 0) {
                this.displayError("Amplicon can not be 0 bp long.");
                return;
            }
        }

        // Temporarily create the new forward primer.
        this.forwardPrimer = new Primer(this.displayer.getInputSequence().getSequence().substring(this.displayer.getSelectedRegion()[0], this.displayer.getSelectedRegion()[1]));

        // Reject the forward primer if the length is not correct.
        if (this.forwardPrimer.getLength() < Constants.MIN_PRIMER_LENGTH || this.forwardPrimer.getLength() > Constants.MAX_PRIMER_LENGTH) {
            this.displayError("Primers must be 18-24 bp long.");
            this.forwardPrimer = null;
            return;
        }

        // Otherwise, display the newly designed forward primer on the display tab.
        this.forwardPrimer.setPosition(new int[]{this.displayer.getSelectedRegion()[0], this.displayer.getSelectedRegion()[1] - 1});
        this.forwardPrimerMade = true;
        this.graphicalForwardPrimer = new GraphicalPrimer(this.forwardPrimer, this.displayer.getSelectedRegion()[0], this.displayer.getDisplayPaneScrollable(), this.displayer, true);
        this.createPrimerPair();
    }

    /**
     * This method functions in the same way as the makeForwardPrimer method but specifically for reverse Primers.
     * It performs the same error checks.
     */
    private void makeReversePrimer() {
        if (this.reversePrimerMade) {
            this.displayError("You have an unfinished primer pair consisting of only a reverse primer. Create a corresponding forward primer before proceeding.");
            return;
        }

        if (this.displayer.getSelectedRegion() == null) {
            this.displayError("No region selected.");
            return;
        }

        if (this.forwardPrimer != null) {
            int ampliconLength = this.displayer.getSelectedRegion()[0] - this.forwardPrimer.getPosition()[1] - 1;
            if (ampliconLength <= -1) {
                this.displayError("Reverse primer must be downstream of the forward primer.");
                return;
            }
            if (ampliconLength == 0) {
                this.displayError("Amplicon can not be 0 bp long.");
                return;
            }
        }

        this.reversePrimer = new ReversePrimer(this.displayer.getInputSequence().getSequence().substring(this.displayer.getSelectedRegion()[0], this.displayer.getSelectedRegion()[1]));

        if (this.reversePrimer.getLength() < 18 || this.reversePrimer.getLength() > 24) {
            this.displayError("Primers must be 18-24 bp long.");
            this.reversePrimer = null;
            return;
        }

        this.reversePrimer.setPosition(new int[]{this.displayer.getSelectedRegion()[0], this.displayer.getSelectedRegion()[1] - 1});
        this.reversePrimerMade = true;
        this.graphicalReversePrimer = new GraphicalPrimer(this.reversePrimer, this.displayer.getSelectedRegion()[0], this.displayer.getDisplayPaneScrollable(), this.displayer, false);
        this.createPrimerPair();
    }

    /**
     * This method creates the primer pair when the user has successfully manually designed a forward primer and a
     * reverse primer. It displays the primer pair as a new result on the results Tab.
     */
    private void createPrimerPair() {
        if (this.forwardPrimerMade && this.reversePrimerMade) {
            // Display the primer pair graphically on the display tab and the results tab.
            ArrayList<GraphicalPrimerPair> manuallyDesignedPair = new ArrayList<>();
            Primer[] manualPair = new Primer[]{this.forwardPrimer, this.reversePrimer};
            GraphicalPrimerPair graphicalManualPair = new GraphicalPrimerPair(manualPair, this.forwardPrimer.compatibilityScore(this.reversePrimer) + this.forwardPrimer.goodnessScore() + this.reversePrimer.goodnessScore(), this.designer);
            graphicalManualPair.setGraphicalForwardPrimer(this.graphicalForwardPrimer);
            graphicalManualPair.setGraphicalReversePrimer(this.graphicalReversePrimer);
            manuallyDesignedPair.add(graphicalManualPair);
            this.designer.setResults(manuallyDesignedPair, false);

            // Reset the status of manual primer designing.
            this.forwardPrimer = null;
            this.reversePrimer = null;
            this.forwardPrimerMade = false;
            this.reversePrimerMade = false;
        }
    }

    /**
     * This method handles whenever an error message needs to be displayed. It is fundamentally different from the
     * ErrorMessage class because it also allows the user to abort the primer pair they are currently designing in
     * the off case that the user designs a forward primer and no reverse primers are possible.
     * @param message
     */
    private void displayError(String message) {
        ManualPrimerDesignError error = new ManualPrimerDesignError(message);

        // If the primers being designed need to be aborted, reset the status of the manual primer designing and
        // remove every designed primer graphically.
        error.getAbortButton().setOnAction((ActionEvent e) -> {
            if (this.graphicalForwardPrimer != null) {
                this.graphicalForwardPrimer.hide();
            }
            if (this.graphicalReversePrimer != null) {
                this.graphicalReversePrimer.hide();
            }
            this.graphicalForwardPrimer = null;
            this.graphicalReversePrimer = null;
            this.forwardPrimer = null;
            this.reversePrimer = null;
            this.forwardPrimerMade = false;
            this.reversePrimerMade = false;
            error.getErrorStage().close();
        });
    }
}
