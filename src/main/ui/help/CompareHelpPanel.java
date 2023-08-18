package ui.help;

// Represents a panel displaying instructions to use the Compare tab
public class CompareHelpPanel extends HelpPanel {

    /*
     * EFFECTS: constructs a new compare tab help panel
     */
    public CompareHelpPanel() {
        super("<html><h2 style='font-family: Arial, sans-serif; font-size: 12px;'> Welcome to Compare!"
                + "</h2><p style='font-family: Arial, sans-serif; font-size: 10px;'>Here, you will see a table "
                + "comparing your expenses purchased in the last month from the Spending Tracker tab to the budget you "
                + "have created in the Budget Tab. This way, you will know if you went under, over, or on budget for "
                + "each expense category.</p></html>");
    }
}
