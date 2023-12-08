package indy;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
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
    private ScrollPane displayPane;
    private GridPane selectionPane;
    private AutomaticPrimerDesigner automaticDesigner;
    private ArrayList<Primer[]> results;
    private BorderPane resultsPaneScrollable;
    private ResultsDisplayer resultsDisplayer;

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
        HBox root = new HBox();
        Button button = new Button("Get input");
        button.setOnAction(e -> {
            Stage inputStage = new Stage();
            inputStage.setTitle("INPUT YOUR DNA SEQUENCE 5' TO 3'");
            InputProcessor processor = new InputProcessor(inputStage, this);
            inputStage.setScene(new Scene(processor.getRoot()));
            inputStage.show();
        });
        root.getChildren().add(button);
        root.setAlignment(Pos.CENTER);
        this.inputTab.setContent(root);
    }

    private void createDesignTab() {
        this.designPane = new BorderPane();
        this.displayPane = new ScrollPane();
        this.selectionPane = new GridPane();
        this.selectionPane.setStyle("-fx-background-color: #ffffff");
        this.selectionPane.setPrefWidth(200);
        this.sequenceDisplayer = new SequenceDisplayer(this.designPane);
        new SequenceSelector(this.selectionPane, this.sequenceDisplayer);
        this.automaticDesigner = new AutomaticPrimerDesigner(this.selectionPane, this.sequenceDisplayer, this);
        this.designTab.setContent(this.designPane);
    }

    private void createResultsTab() {
        this.results = new ArrayList<>();
        ScrollPane resultsPane = new ScrollPane();
        this.resultsPaneScrollable = new BorderPane();
//        this.resultsPaneScrollable.getChildren().add(new Rectangle(100, 1000));
        resultsPane.setContent(this.resultsPaneScrollable);
        this.resultsTab.setContent(resultsPane);
        this.resultsDisplayer =  new ResultsDisplayer(this.resultsPaneScrollable);
    }

    public void setSequence(Sequence newSequence) {
        this.inputSequence = newSequence;
        this.sequenceDisplayer.setDisplayPane(this.displayPane);
        this.sequenceDisplayer.setInputSequence(newSequence);
        this.automaticDesigner.setInputSequence(newSequence);
        this.tabPane.getSelectionModel().select(this.designTab);
        this.displayPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        this.designPane.setCenter(this.displayPane);
        this.designPane.setRight(this.selectionPane);
    }

    public void setResults(ArrayList<Primer[]> newResults, boolean deletePrevious) {
        if (deletePrevious) {
            this.results = new ArrayList<>();
        }
        this.tabPane.getSelectionModel().select(this.resultsTab);
        this.results.addAll(newResults);
        this.resultsDisplayer.setResults(this.results);
        this.resultsDisplayer.displayResults();
    }
}
