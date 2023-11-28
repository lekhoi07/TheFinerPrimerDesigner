package indy;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.shape.Rectangle;

public class TabOrganizer {
    private TabPane root;

    public TabOrganizer() {
        this.root = new TabPane();
        this.root.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        TheFinerPrimerDesigner myDesigner = new TheFinerPrimerDesigner(this.root);
        Tab inputTab = myDesigner.getInputTab();
        Tab designTab = myDesigner.getPrimerDesignTab();
        Tab resultsTab = myDesigner.getResultsTab();
        this.root.getTabs().addAll(inputTab, designTab, resultsTab);
    }

    public TabPane getRoot() {
        return this.root;
    }
}
