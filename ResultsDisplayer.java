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

public class ResultsDisplayer {
    private ArrayList<GraphicalPrimerPair> results;
    private Pane resultsPaneScrollable;
    private HBox nothingDisplayedPane;
    private ArrayList<GraphicalPrimerPair> graphicalPrimerPairs;
    private ScrollPane resultsPane;
    private Rectangle scrollRect;

    public ResultsDisplayer(Pane resultsPaneScrollable) {
        this.resultsPaneScrollable = resultsPaneScrollable;
        this.graphicalPrimerPairs = new ArrayList<>();
        this.displayResults();
    }

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

    public void setResults(ArrayList<GraphicalPrimerPair> results) {
        if (this.results != null) {
            for (GraphicalPrimerPair pair : this.results) {
                this.resultsPaneScrollable.getChildren().remove(pair.getPrimerPairPane());
                this.resultsPaneScrollable.getChildren().remove(this.scrollRect);
            }
        }
        this.results = results;
        if (this.results != null) {
            this.displayResults();
        }
    }
}
