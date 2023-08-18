package ui.tabs;

import model.*;
import ui.EasyBudgetGUI;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;

// The tab containing a table that compares the budget to expenses purchased in the past month
public class CompareTab extends Tab {
    private DefaultTableModel compareTableModel = new DefaultTableModel();
    private JTable compareTable = new JTable(compareTableModel);

    /*
     * REQUIRES: EasyBudgetGUI controller that holds this tab
     * EFFECTS:  constructs a new CompareTab and sets up its layout and table
     */
    public CompareTab(EasyBudgetGUI controller) {
        super(controller);

        compareTable.setSelectionModel(new DefaultListSelectionModel() {
            @Override
            public void setSelectionInterval(int index0, int index1) {
                // nothing done to disallow selection
            }
        });

        setUpLayout();

        setUpCompareTable();
    }

    /*
     * MODIFIES: this
     * EFFECTS:  sets up listeners for components
     */
    @Override
    public void setUpListeners() {

    }

    /*
     * MODIFIES: this
     * EFFECTS:  sets up the layout of the Compare tab
     */
    @Override
    public void setUpLayout() {
        setLayout(new BorderLayout());

        add(new JScrollPane(compareTable), BorderLayout.CENTER);
    }

    /*
     * MODIFIES: this
     * EFFECTS:  sets up compareTable
     */
    public void setUpCompareTable() {
        String[] columnNames = {"Category", "Budgeted ($)", "Actual ($)", "Comparison"};
        Object[][] data = new Object[getController().getBudget().getItems().size()][columnNames.length];
        int index = 0;
        Iterator<BudgetItem> bi = getController().getBudget().iterator();
        if (bi.hasNext()) {
            do {
                BudgetItem b = bi.next();
                data[index][0] = b.getCategory().name;
                data[index][1] = b.getLimit();
                data[index][2] = sumOfExpensesInCategory(b.getCategory());
                data[index][3] = comparisonToString(b.getCategory());
                index++;
            } while (bi.hasNext());
        }
        compareTableModel = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        compareTable.setModel(compareTableModel);
        colourTable();
    }

    /*
     * MODIFIES: this
     * EFFECTS:  changes the colour of each cell in the fourth column of compareTable based on its value
     */
    private void colourTable() {
        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

                Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row,
                        column);

                if (value == "UNDER BUDGET") {
                    component.setBackground(Color.GREEN);
                } else if (value == "ON BUDGET") {
                    component.setBackground(Color.ORANGE);
                } else if (value == "OVER BUDGET") {
                    component.setBackground(Color.RED);
                }

                return component;
            }
        };

        compareTable.getColumnModel().getColumn(3).setCellRenderer(cellRenderer);
    }

    /*
     * EFFECTS; returns the sum of the prices of the past month's expenses with category
     */
    private double sumOfExpensesInCategory(Category category) {
        ArrayList<ExpenseFilter> filters = new ArrayList<>();
        filters.add(new FilterByDate(LocalDate.now().getMonth()));
        double expenseSum = 0;
        for (Expense e: getController().getSpendingTracker().filter(filters)) {
            if (e.getCategory() == category) {
                expenseSum += e.getPrice();
            }
        }
        return expenseSum;
    }

    /*
     * EFFECTS: returns the string to display in the fourth column of compareTable given a category
     */
    private String comparisonToString(Category category) {
        try {
            int compare = getController().getBudget().compareToBudget(getController().getSpendingTracker(), category);
            if (compare == - 1) {
                return "UNDER BUDGET";
            } else if (compare == 1) {
                return "OVER BUDGET";
            } else if (compare == 0) {
                return "ON BUDGET";
            }
        } catch (Exception e) {
            //
        }
        return "UNDER BUDGET";
    }
}
