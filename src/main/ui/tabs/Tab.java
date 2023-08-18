package ui.tabs;

import ui.EasyBudgetGUI;

import javax.swing.*;
import java.awt.*;

// Represents a tab in the tab pane of the Easy Budget! GUI
public abstract class Tab extends JPanel {

    private final EasyBudgetGUI controller;

    /*
     * REQUIRES: EasyBudgetGUI controller that holds this tab
     * EFFECTS: constructs a new tab with a GUI controller and sets up its size
     */
    public Tab(EasyBudgetGUI controller) {
        this.controller = controller;
        setPreferredSize(new Dimension((int) (EasyBudgetGUI.WIDTH * 0.95), (int) (EasyBudgetGUI.HEIGHT * 0.65)));
    }

    /*
     * EFFECTS: sets up the listeners for components in the tab
     */
    public abstract void setUpListeners();

    /*
     * EFFECTS: sets up the layout of the tab
     */
    public abstract void setUpLayout();

    /*
     * EFFECTS: returns the EasyBudgetGUI controller for this tab
     */
    public EasyBudgetGUI getController() {
        return controller;
    }

    @Override
    public int getWidth() {
        return super.getPreferredSize().width;
    }

    @Override
    public int getHeight() {
        return super.getPreferredSize().height;
    }
}
