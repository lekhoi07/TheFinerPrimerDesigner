package indy;

import doodlejump.Constants;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.ArrayList;

public class TheFinerPrimerDesigner {
    private TabPane tabPane;
    private Tab inputTab;
    public Tab designTab;
    private Tab resultsTab;
    private Sequence inputSequence;
    private Sequence selectedSequence;
    private SequenceDisplayer sequenceDisplayer;
    private BorderPane designPane;
    private ScrollPane displayPane, resultsPane;
    private GridPane selectionPane;
    private AutomaticPrimerDesigner automaticDesigner;
    private ArrayList<GraphicalPrimerPair> results;
    private Pane resultsPaneScrollable;
    private ResultsDisplayer resultsDisplayer;
    private SequenceSelector selector;

    public TheFinerPrimerDesigner(TabPane tabPane, Tab inputTab, Tab designTab, Tab resultsTab) {
        this.tabPane = tabPane;
        this.inputTab = inputTab;
        this.designTab = designTab;
        this.resultsTab = resultsTab;
        this.createInputTab();
        this.createDesignTab();
        this.createResultsTab();
    }

    private void createInputTab() {
        InputTabPaneOrganizer organizer = new InputTabPaneOrganizer(this);
        this.inputTab.setContent(organizer.getRoot());
    }

    private void createDesignTab() {
        this.designPane = new BorderPane();
        this.displayPane = new ScrollPane();
        this.selectionPane = new GridPane();
        this.selectionPane.setStyle("-fx-background-color: #ffffff");
        this.selectionPane.setPrefWidth(200);
        this.sequenceDisplayer = new SequenceDisplayer(this.designPane);
        this.selector = new SequenceSelector(this.selectionPane, this.sequenceDisplayer);
        this.automaticDesigner = new AutomaticPrimerDesigner(this.selectionPane, this.sequenceDisplayer, this);
        new ManualPrimerDesigner(this.selectionPane, this.sequenceDisplayer, this);
        this.designTab.setContent(this.designPane);
    }

    private void createResultsTab() {
        this.results = new ArrayList<>();
        this.resultsPane = new ScrollPane();
        this.resultsPaneScrollable = new Pane();
        this.resultsDisplayer =  new ResultsDisplayer(this.resultsPaneScrollable);
        this.resultsPane.setContent(this.resultsPaneScrollable);
        this.resultsTab.setContent(this.resultsPane);
    }

    public void setInputSequence(Sequence newSequence) {
        if (!this.results.isEmpty()) {
            this.setResults(new ArrayList<>(), true);

        }
        this.inputSequence = newSequence;
        this.selector.setText(null);
        this.sequenceDisplayer.setDisplayPane(this.displayPane);
        this.sequenceDisplayer.setInputSequence(newSequence);
        this.automaticDesigner.setInputSequence(newSequence);
        this.tabPane.getSelectionModel().select(this.designTab);
        this.displayPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        this.designPane.setCenter(this.displayPane);
        this.designPane.setRight(this.selectionPane);
    }

    public void setResults(ArrayList<GraphicalPrimerPair> newResults, boolean deletePrevious) {
        if (deletePrevious) {
            this.results = newResults;
            this.resultsDisplayer.setResults(newResults);
            return;
        }
        this.results.addAll(newResults);
        this.resultsDisplayer.setResults(this.results);
        this.resultsPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        this.tabPane.getSelectionModel().select(this.resultsTab);
    }

    public ArrayList<GraphicalPrimerPair> getResults() {
        return this.results;
    }

    public Sequence getInputSequence() {
        return this.inputSequence;
    }
}
