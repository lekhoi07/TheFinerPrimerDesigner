package indy;

import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.util.ArrayList;

/**
 * This is the top-level logical class of the program. It instantiates nearly every class in this program so that
 * any class can communicate with any other class because the application is so interconnected.
 */
public class TheFinerPrimerDesigner {
    private TabPane tabPane;
    private Tab inputTab;
    public Tab designTab;
    private Tab resultsTab;
    private Sequence inputSequence;
    private SequenceDisplayer sequenceDisplayer;
    private BorderPane designPane;
    private ScrollPane displayPane, resultsPane;
    private GridPane selectionPane;
    private AutomaticPrimerDesigner automaticDesigner;
    private ArrayList<GraphicalPrimerPair> results;
    private ResultsDisplayer resultsDisplayer;
    private SequenceSelector selector;

    /**
     * This constructor initializes the tabs and sets up all of them logically.
     * @param tabPane
     * @param inputTab
     * @param designTab
     * @param resultsTab
     */
    public TheFinerPrimerDesigner(TabPane tabPane, Tab inputTab, Tab designTab, Tab resultsTab) {
        this.tabPane = tabPane;
        this.inputTab = inputTab;
        this.designTab = designTab;
        this.resultsTab = resultsTab;
        this.createInputTab();
        this.createDesignTab();
        this.createResultsTab();
    }

    /**
     * This helper method creates the input tab graphically.
     */
    private void createInputTab() {
        InputTabPaneOrganizer organizer = new InputTabPaneOrganizer(this);
        this.inputTab.setContent(organizer.getRoot());
    }

    /**
     * This helper method creates the design tab graphically.
     */
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

    /**
     * This helper method creates the results tab graphically.
     */
    private void createResultsTab() {
        this.results = new ArrayList<>();
        this.resultsPane = new ScrollPane();
        Pane resultsPaneScrollable = new Pane();
        this.resultsDisplayer =  new ResultsDisplayer(resultsPaneScrollable);
        this.resultsPane.setContent(resultsPaneScrollable);
        this.resultsTab.setContent(this.resultsPane);
    }

    /**
     * This method is called whenever there is a new input sequence. This method lets every relevant class know
     * above the new input when needed then switches tabs to the display tab.
     * @param newSequence
     */
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

    /**
     * This method is called whenever there are new results, whether those new primer pairs were automatically
     * or manually designed. This method lets all the relevant classes know about the new results and changes the
     * tab to the results page.
     * @param newResults
     * @param deletePrevious
     */
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
