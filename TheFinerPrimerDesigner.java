package indy;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import jdk.internal.util.xml.impl.Input;

import java.util.Optional;

public class TheFinerPrimerDesigner {
    private TabPane tabPane;
    private Tab inputTab;
    public Tab designTab;
    private Tab resultsTab;
    private Sequence inputSequence;
    private SequenceDisplayer displayer;
    private BorderPane designPane;
    private ScrollPane displayPane;

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
        this.displayer = new SequenceDisplayer(this.designPane);
        this.designTab.setContent(this.designPane);
    }

    private void createResultsTab() {
        this.resultsTab = new Tab("MY DESIGNED PRIMERS");
    }

    public void setSequence(Sequence newSequence) {
        this.inputSequence = newSequence;
        this.displayer.setDisplayPane(this.displayPane);
        this.displayer.setInputSequence(newSequence);
        this.tabPane.getSelectionModel().select(this.designTab);

        GridPane selectionPane = new GridPane();
        selectionPane.setStyle("-fx-background-color: #ffffff");
        selectionPane.setPrefWidth(200);
        selectionPane.getChildren().add(new Button("hi"));

        this.displayPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);

        System.out.println(this.displayPane + "hi");

        this.designPane.setCenter(this.displayPane);
        this.designPane.setRight(selectionPane);
        System.out.println("bae");
    }
}
