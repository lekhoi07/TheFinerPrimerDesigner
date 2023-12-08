package indy;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import java.util.ArrayList;

public class ResultsDisplayer {
    private ArrayList<Primer[]> results;
    private BorderPane resultsPaneScrollable;
    private HBox nothingDisplayedPane;

    public ResultsDisplayer(BorderPane resultsPaneScrollable) {
        this.resultsPaneScrollable = resultsPaneScrollable;
        this.displayResults();
    }

    public void displayResults() {
        if (this.results == null || this.results.isEmpty()) {
            this.nothingDisplayedPane = new HBox();
            Label nothingDisplayed = new Label("No results to display.");
            nothingDisplayed.setTranslateX(300);
            nothingDisplayed.setTranslateY(325);
            this.nothingDisplayedPane.getChildren().add(nothingDisplayed);
            this.resultsPaneScrollable.setCenter(this.nothingDisplayedPane);
        } else {
            int position = 0;
            for (Primer[] pair : this.results) {
                GraphicalPrimerPair graphicalPrimerPair = new GraphicalPrimerPair(this.resultsPaneScrollable, position, pair);
                position += 1;
            }
        }
    }

    public void setResults(ArrayList<Primer[]> results) {
        this.results = results;
    }
}
