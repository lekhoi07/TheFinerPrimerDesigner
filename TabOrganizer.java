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
        Tab inputTab = new Tab("INPUT");
        Tab designTab = new Tab("DESIGN YOUR PRIMERS");
        Tab resultsTab = new Tab("MY DESIGNED PRIMERS");
        new TheFinerPrimerDesigner(this.root, inputTab, designTab, resultsTab);
        this.root.getTabs().addAll(inputTab, designTab, resultsTab);
    }

    public TabPane getRoot() {
        return this.root;
    }
}
