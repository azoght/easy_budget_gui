package ui.help;

// Represents a panel displaying instructions to use the Spending Tracker tab
public class SpendingTrackerHelpPanel extends HelpPanel {

    /*
     * EFFECTS: constructs a new spending tracker help panel
     */
    public SpendingTrackerHelpPanel() {
        super("<html><h2 style='font-family: Arial, sans-serif; font-size: 12px;'>Welcome to the Spending "
                + "Tracker!!</h2><p style='font-family: Arial, sans-serif; font-size: 10px;'> Get started by adding "
                + "your expenses: just click ''Add Expense'' in the toolbar, enter the expense details in the new "
                + "window, and click ''OK''. To edit, select an expense in the table, then use the ''Edit Expense'' "
                + "button or double-click. And to keep an eye on your expenses, simply navigate the table. "
                + "You can even filter your expenses by date and/or categories using the panel on the right.</p>");

    }

}
