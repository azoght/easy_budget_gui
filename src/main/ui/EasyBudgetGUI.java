package ui;

import model.Budget;
import model.BudgetDesigner;
import model.SpendingTracker;
import persistence.PersistenceManager;
import ui.help.BudgetPlannerHelpPanel;
import ui.help.CompareHelpPanel;
import ui.help.HelpPanel;
import ui.help.SpendingTrackerHelpPanel;
import ui.tabs.BudgetTab;
import ui.tabs.CompareTab;
import ui.tabs.SpendingTrackerTab;

import javax.swing.*;
import java.awt.*;

// Represents the graphical user interface of the Easy Budget! application
public class EasyBudgetGUI extends JFrame {

    public static final int WIDTH = 800;
    public static final int HEIGHT = 650;
    private static final Color BACKGROUND_COLOUR = new Color(169, 231, 178);

    private JPanel mainPanel = new JPanel();
    private Budget budget;
    private SpendingTracker spendingTracker;
    private JTabbedPane tabbedPane = new JTabbedPane();
    private BudgetTab budgetTab;
    private SpendingTrackerTab spendingTrackerTab;
    private CompareTab compareTab;

    /*
     * EFFECTS: sets up the GUI
     */
    public EasyBudgetGUI() {
        loadBudgetAndSpendingTracker();
        setTitle("Easy Budget!");
        setBackground(BACKGROUND_COLOUR);
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        mainPanel.setBackground(BACKGROUND_COLOUR);
        mainPanel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setUpTabs();
        add(mainPanel);
        pack();
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }

    /*
     * MODIFIES: this
     * EFFECTS:  allows the user to load their budget and spending tracker from file if they so choose
     */
    private void loadBudgetAndSpendingTracker() {
        int response = JOptionPane.showConfirmDialog(
                null,
                "Would you like to load your budget and spending tracker from last time?",
                "Load",
                JOptionPane.YES_NO_OPTION
        );
        if (response == JOptionPane.YES_OPTION) {
            try {
                budget = PersistenceManager.loadBudget(PersistenceManager.EASY_BUDGET_FILEPATH);
                spendingTracker = PersistenceManager.loadSpendingTracker(PersistenceManager.EASY_BUDGET_FILEPATH);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, e.getMessage(),
                        "Load Budget and Spending Tracker", JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            try {
                budget = new BudgetDesigner().createBudget(1000);
                spendingTracker = new SpendingTracker();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, e.getMessage(),
                        "Load Budget and Spending Tracker", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    /*
     * MODIFIES: this
     * EFFECTS: sets up the tabbed pane and its tabs
     */
    private void setUpTabs() {
        tabbedPane.setTabPlacement(JTabbedPane.TOP);

        budgetTab = new BudgetTab(this);
        JPanel budgetTabPane = new JPanel(new BorderLayout());
        budgetTabPane.add(budgetTab, BorderLayout.CENTER);
        HelpPanel budgetHelpPanel = new BudgetPlannerHelpPanel();
        budgetTabPane.add(budgetHelpPanel, BorderLayout.NORTH);
        tabbedPane.add(budgetTabPane, 0);
        tabbedPane.setTitleAt(0, "Budget");

        spendingTrackerTab = new SpendingTrackerTab(this);
        JPanel spendingTrackerTabPane = new JPanel(new BorderLayout());
        spendingTrackerTabPane.add(spendingTrackerTab, BorderLayout.CENTER);
        HelpPanel spendingTrackerHelpPanel = new SpendingTrackerHelpPanel();
        spendingTrackerTabPane.add(spendingTrackerHelpPanel, BorderLayout.NORTH);
        tabbedPane.add(spendingTrackerTabPane, 1);
        tabbedPane.setTitleAt(1, "Spending Tracker");

        compareTab = new CompareTab(this);
        JPanel compareTabPane = new JPanel(new BorderLayout());
        compareTabPane.add(compareTab, BorderLayout.CENTER);
        HelpPanel compareHelpPanel = new CompareHelpPanel();
        compareTabPane.add(compareHelpPanel, BorderLayout.NORTH);
        tabbedPane.add(compareTabPane, 2);
        tabbedPane.setTitleAt(2, "Compare");

        mainPanel.add(tabbedPane);
    }

    /*
     * MODIFIES: this
     * EFFECTS:  updates the compare table in the Compare Tab
     */
    public void updateCompareTable() {
        compareTab.setUpCompareTable();
    }

    public Budget getBudget() {
        return budget;
    }

    public SpendingTracker getSpendingTracker() {
        return spendingTracker;
    }
}
