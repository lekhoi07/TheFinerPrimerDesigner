package indy;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class TheFinerPrimerDesigner {
    private TabPane tabPane;
    private Tab inputTab;
    public Tab designTab;
    private Tab resultsTab;
    private Sequence inputSequence;
    private Sequence selectedSequence;
    private SequenceDisplayer displayer;
    private BorderPane designPane;
    private ScrollPane displayPane;
    private GridPane selectionPane;
    private AutomaticPrimerDesigner automaticDesigner;

    public TheFinerPrimerDesigner(TabPane tabPane) {
        this.tabPane = tabPane;
        this.createInputTab();
        this.createDesignTab();
        this.createResultsTab();
    }

    public Tab getInputTab() {
        return this.inputTab;
    }

    public Tab getPrimerDesignTab() {
        return this.designTab;
    }

    public Tab getResultsTab() {
        return this.resultsTab;
    }

    private void createInputTab() {
        this.inputTab = new Tab("INPUT");
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
        this.designTab = new Tab("DESIGN YOUR PRIMERS");
        this.designPane = new BorderPane();
        this.displayPane = new ScrollPane();
        this.selectionPane = new GridPane();
        this.selectionPane.setStyle("-fx-background-color: #ffffff");
        this.selectionPane.setPrefWidth(200);
        this.displayer = new SequenceDisplayer(this.designPane);
        new SequenceSelector(this.selectionPane, this.displayer);
        this.automaticDesigner = new AutomaticPrimerDesigner(this.selectionPane, this.displayer);
        this.designTab.setContent(this.designPane);
    }

    private void createResultsTab() {
        this.resultsTab = new Tab("MY DESIGNED PRIMERS");
    }

    public void setSequence(Sequence newSequence) {
        this.inputSequence = newSequence;
        this.displayer.setDisplayPane(this.displayPane);
        this.displayer.setInputSequence(newSequence);
        this.automaticDesigner.setInputSequence(newSequence);
        this.tabPane.getSelectionModel().select(this.designTab);
        this.displayPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        this.designPane.setCenter(this.displayPane);
        this.designPane.setRight(this.selectionPane);
    }
}
