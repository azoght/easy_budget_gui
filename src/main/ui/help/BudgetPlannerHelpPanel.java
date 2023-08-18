package ui.help;

// Represents a panel displaying instructions to use the Budget tab
public class BudgetPlannerHelpPanel extends HelpPanel {

    /*
     * EFFECTS: constructs a new budget planner help panel
     */
    public BudgetPlannerHelpPanel() {
        super("<html><h2 style='font-family: Arial, sans-serif; font-size: 12px;'> Welcome to the Budget "
                + "Planner!</h2><p style='font-family: Arial, sans-serif; font-size: 10px;'>To start managing your "
                + "budget, simply click the ''Add Item'' button in the toolbar to create a new budget entry. Editing is"
                + " easy, select an item from the table and click ''Edit Item'', or double-click the item. "
                + "Deleting items is a breeze too, just press ''Delete Item''. Your budget summary, including"
                + " a handy pie chart, awaits on this tab, showing your allocations and providing quick insights.</p>"
                + "</html>");

    }

}
