package indy;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import java.util.ArrayList;

/**
 * This class manages the graphics of the results tab.
 */
public class ResultsDisplayer {
    private ArrayList<GraphicalPrimerPair> results;
    private Pane resultsPaneScrollable;
    private HBox nothingDisplayedPane;
    private Rectangle scrollRect;

    /**
     * This constructor initialzes the instance variables then calls upon a helper method to display the results.
     * @param resultsPaneScrollable
     */
    public ResultsDisplayer(Pane resultsPaneScrollable) {
        this.resultsPaneScrollable = resultsPaneScrollable;
        this.displayResults();
    }

    /**
     * This helper method displays a message that there are no results if there are no results. Otherwise, it will
     * display the results as GraphicalPrimerPairs.
     */
    private void displayResults() {
        if (this.results == null || this.results.isEmpty()) {
            this.nothingDisplayedPane = new HBox();
            Label nothingDisplayed = new Label("No results to display.");
            nothingDisplayed.setTranslateX(300);
            nothingDisplayed.setTranslateY(325);
            this.nothingDisplayedPane.getChildren().add(nothingDisplayed);
            this.resultsPaneScrollable.getChildren().add(this.nothingDisplayedPane);
            this.resultsPaneScrollable.getChildren().remove(this.scrollRect);
        } else {
            this.resultsPaneScrollable.getChildren().remove(this.nothingDisplayedPane);
            this.scrollRect = new Rectangle(10, Math.max(675, this.results.size() * 170 + 20), Color.LIGHTGRAY);
            this.resultsPaneScrollable.getChildren().addAll(this.scrollRect);
            int position = 1;
            for (GraphicalPrimerPair pair : this.results) {
                this.resultsPaneScrollable.getChildren().remove(pair.getPrimerPairPane());
                pair.setPosition(position);
                pair.setRoot(this.resultsPaneScrollable);
                pair.displayGraphicalPrimerPair(position);
                position += 1;
            }
        }
    }

    /**
     * This method is called by the top-level logic class to notify this class that there are new results to be
     * displayed.
     * @param results
     */
    public void setResults(ArrayList<GraphicalPrimerPair> results) {
        // If there are new results, then remove all the previous results.
        if (this.results != null) {
            for (GraphicalPrimerPair pair : this.results) {
                this.resultsPaneScrollable.getChildren().remove(pair.getPrimerPairPane());
                this.resultsPaneScrollable.getChildren().remove(this.scrollRect);
            }
        }

        // Set the results to the new results.
        this.results = results;

        // Then display the new results.
        if (this.results != null) {
            this.displayResults();
        }
    }
}
