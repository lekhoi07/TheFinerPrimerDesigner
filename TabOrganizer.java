package indy;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

/**
 * This class manages the three tabs in this program.
 */
public class TabOrganizer {
    private TabPane root;

    /**
     * This constructor creates the tabs and adds them to the TabPane.
     */
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
