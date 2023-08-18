package ui.tabs;

import model.Budget;
import model.BudgetItem;
import model.Category;
import model.exceptions.CannotChangeBudgetException;
import model.exceptions.CategoryDoesNotExistException;
import model.exceptions.ZeroOrLessException;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import ui.EasyBudgetGUI;
import ui.windows.AddBudgetItemDialog;
import ui.windows.EditBudgetItemDialog;
import ui.windows.EditBudgetLimitDialog;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

// The tab containing all options for budget in the GUI
public class BudgetTab extends Tab {

    private JButton addItemButton = new JButton("Add Item");
    private JButton editItemButton = new JButton("Edit Item");
    private JButton deleteItemButton = new JButton("Delete Item");
    private JButton editLimitButton = new JButton("Edit total limit");
    private JToolBar toolBar = new JToolBar();
    private JPanel bottomPanel = new JPanel();
    private JPanel viewOptions = new JPanel();
    private JRadioButton viewAsAmountsOption = new JRadioButton("amounts");
    private JRadioButton viewAsPercentagesOption = new JRadioButton("percentages");
    private DefaultTableModel budgetTableModel = new DefaultTableModel();
    private JPanel tablePanel = new JPanel(new BorderLayout());
    private JTable budgetTable = new JTable(budgetTableModel);
    private JScrollPane budgetTableScrollPane = new JScrollPane(budgetTable);
    private ChartPanel myBudget = new ChartPanel(setUpBudgetChart(true));

    /*
     * REQUIRES: EasyBudgetGUI controller that holds this tab
     * EFFECTS:  constructs a new BudgetTab and sets up its listeners and layout
     */
    public BudgetTab(EasyBudgetGUI controller) {
        super(controller);

        toolBar.setFloatable(false);
        budgetTable.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
        viewAsAmountsOption.setSelected(true);
        viewAsPercentagesOption.setSelected(false);

        setUpListeners();
        setUpLayout();
    }

    /*
     * MODIFIES: this
     * EFFECTS:  sets up listeners for components
     */
    @Override
    public void setUpListeners() {
        setUpButtonListeners();
        budgetTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    editBudgetItem();
                }
            }
        });
        setUpViewOptionListeners();
    }

    /*
     * MODIFIES: this
     * EFFECTS:  sets up the listeners for buttons
     */
    private void setUpButtonListeners() {
        addItemButton.addActionListener(e -> addBudgetItem());
        editItemButton.addActionListener(e -> editBudgetItem());
        deleteItemButton.addActionListener(e -> deleteBudgetItem());
        editLimitButton.addActionListener(e -> editBudgetLimit());
    }

    /*
     * MODIFIES: this
     * EFFECTS:  sets up the listeners for the view options
     */
    private void setUpViewOptionListeners() {
        ItemListener viewOptionListener = new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                itemStateChanged(e, e.getSource().equals(viewAsAmountsOption));
            }

            public void itemStateChanged(ItemEvent e, boolean viewAmounts) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    myBudget.setChart(setUpBudgetChart(viewAmounts));
                }
            }
        };
        viewAsAmountsOption.addItemListener(viewOptionListener);
        viewAsPercentagesOption.addItemListener(viewOptionListener);
    }

    /*
     * MODIFIES: this
     * EFFECTS:  sets up the layout of the Budget tab
     */
    @Override
    public void setUpLayout() {
        setLayout(new BorderLayout());

        Box centerBox = Box.createHorizontalBox();
        centerBox.add(Box.createHorizontalGlue()); // Add glue before buttons
        centerBox.add(addItemButton);
        centerBox.add(editItemButton);
        centerBox.add(deleteItemButton);
        centerBox.add(Box.createHorizontalGlue()); // Add glue after buttons
        toolBar.add(centerBox);

        setUpBudgetItemsTable();

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, tablePanel, myBudget);
        splitPane.setResizeWeight(0.1);

        myBudget.setMaximumSize(new Dimension(400, 400));

        ButtonGroup buttonGroup = new ButtonGroup();
        viewOptions.add(viewAsAmountsOption);
        buttonGroup.add(viewAsAmountsOption);
        viewOptions.add(viewAsPercentagesOption);
        buttonGroup.add(viewAsPercentagesOption);

        bottomPanel.add(editLimitButton);
        bottomPanel.add(viewOptions);

        tablePanel.add(toolBar, BorderLayout.NORTH);
        tablePanel.add(budgetTableScrollPane, BorderLayout.CENTER);
        tablePanel.add(bottomPanel, BorderLayout.SOUTH);
        add(splitPane, BorderLayout.CENTER);
    }

    /*
     * EFFECTS: returns the pie chart displaying the user's budget
     */
    private JFreeChart setUpBudgetChart(boolean viewWithAmounts) {
        Budget budget = getController().getBudget();
        assert budget != null;
        DefaultPieDataset<String> dataset = setUpDataSet(budget);
        JFreeChart pieChart = ChartFactory.createPieChart("My Budget", dataset, false, false,
                false);
        PiePlot plot = (PiePlot) pieChart.getPlot();
        plot.setSimpleLabels(true);
        PieSectionLabelGenerator gen;
        if (viewWithAmounts) {
            gen = new StandardPieSectionLabelGenerator(
                    "{0}: ${1}", new DecimalFormat("0"), new DecimalFormat("0%"));
        } else {
            gen = new StandardPieSectionLabelGenerator(
                    "{0}: {2}", new DecimalFormat("0"), new DecimalFormat("0%"));
        }
        plot.setLabelGenerator(gen);
        plot.setSectionPaint("Unbudgeted", Color.gray); // change colour of "Unbudgeted" to gray
        return pieChart;
    }

    /*
     * EFFECTS: returns the dataset which the budget pie chart is based on
     */
    private DefaultPieDataset<String> setUpDataSet(Budget budget) {
        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
        Iterator<BudgetItem> bi = budget.iterator();
        if (bi.hasNext()) {
            do {
                BudgetItem b = bi.next();
                dataset.setValue(b.getCategory().name, b.getLimit());
            } while (bi.hasNext());
        }
        dataset.setValue("Unbudgeted", budget.getTotalLimit() - budget.sumOfLimits());
        return dataset;
    }

    /*
     * MODIFIES: this
     * EFFECTS:  updates the budget chart and compare table in the Compare Tab with information from the user's budget
     */
    private void update() {
        myBudget.setChart(setUpBudgetChart(true));
        viewAsAmountsOption.setSelected(true);
        getController().updateCompareTable();
    }

    /*
     * MODIFIES: this
     * EFFECTS:  sets up the table of budget items
     */
    private void setUpBudgetItemsTable() {
        String[] columnNames = {"Category", "Amount ($)"};
        Budget budget = getController().getBudget();
        Object[][] data = new Object[budget.getItems().size()][columnNames.length];
        int index = 0;
        for (BudgetItem budgetItem: budget.getItems()) {
            data[index][0] = budgetItem.getCategory().name;
            data[index][1] = budgetItem.getLimit();
            index++;
        }
        budgetTableModel = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        budgetTable.setModel(budgetTableModel);
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(budgetTableModel);
        budgetTable.setRowSorter(sorter);
        List<RowSorter.SortKey> sortKeys = new ArrayList<>();

        sortKeys.add(new RowSorter.SortKey(0, SortOrder.ASCENDING));

        sorter.setSortKeys(sortKeys);
        sorter.sort();
    }

    /*
     * MODIFIES: this
     * EFFECTS: adds an item to the budget through a separate dialog
     */
    private void addBudgetItem() {
        AddBudgetItemDialog addDialog = new AddBudgetItemDialog(getController());
        boolean actionDone = false;
        while (!actionDone) {
            addDialog.setVisible(true);
            if (addDialog.isCancelled()) {
                actionDone = true;
            } else if (addDialog.isConfirmed()) {
                String category = addDialog.getCategoryValue();
                double amount = addDialog.getLimitValue();
                try {
                    getController().getBudget().addBudgetItem(amount, Category.stringToCategory(category));
                    budgetTableModel.addRow(new Object[]{category, amount});
                    actionDone = true;
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, e.getMessage(), "Add Budget Item",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
        }
        update();
    }

    /*
     * MODIFIES: this
     * EFFECTS: edits the total limit of the budget through a separate dialog
     */
    private void editBudgetLimit() {
        EditBudgetLimitDialog addDialog = new EditBudgetLimitDialog(getController(),
                getController().getBudget().getTotalLimit());
        boolean actionDone = false;
        while (!actionDone) {
            addDialog.setVisible(true);
            if (addDialog.isCancelled()) {
                actionDone = true;
            } else if (addDialog.isConfirmed()) {
                double limit = addDialog.getLimitValue();
                try {
                    getController().getBudget().setTotalLimit(limit);
                    actionDone = true;
                } catch (CannotChangeBudgetException e) {
                    JOptionPane.showMessageDialog(this, "Invalid limit change!",
                            "Edit Budget Limit", JOptionPane.INFORMATION_MESSAGE);
                } catch (ZeroOrLessException e) {
                    JOptionPane.showMessageDialog(this, e.getMessage(), "Edit Budget Limit",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
        }
        update();
    }

    /*
     * MODIFIES: this
     * EFFECTS:  deletes the selected item in table from the budget
     */
    private void deleteBudgetItem() {
        int selectedRow = budgetTable.getSelectedRow();
        if (selectedRow != -1) {
            int response = JOptionPane.showConfirmDialog(
                    null,
                    "Are you sure you want to delete this item?",
                    "Confirmation",
                    JOptionPane.YES_NO_OPTION
            );

            if (response == JOptionPane.YES_OPTION) {
                Category category = Category.stringToCategory((String) budgetTable.getValueAt(selectedRow, 0));
                try {
                    getController().getBudget().deleteBudgetItem(category);
                    removeRowFromTableWithCategory(category);
                } catch (CategoryDoesNotExistException e) {
                    JOptionPane.showMessageDialog(this, e.getMessage(), "Delete Budget Item",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select an item to delete",
                    "Delete Budget Item", JOptionPane.INFORMATION_MESSAGE);
        }
        update();
    }

    /*
     * MODIFIES: this
     * EFFECTS:  removes the row from budgetTable with category
     */
    private void removeRowFromTableWithCategory(Category category) {
        DefaultTableModel model = (DefaultTableModel) budgetTable.getModel();
        for (int row = 0; row < model.getRowCount(); row++) {
            if (model.getValueAt(row, 0).equals(category.name)) {
                model.removeRow(row);
                break;
            }
        }
    }

    /*
     * MODIFIES: this
     * EFFECTS:  edits the limit of the selected item in table through a separate dialog
     */
    private void editBudgetItem() {
        int selectedRow = budgetTable.getSelectedRow();
        if (selectedRow != -1) {
            Category category = Category.stringToCategory((String) budgetTable.getValueAt(selectedRow, 0));
            double limit = (double) budgetTable.getValueAt(selectedRow, 1);

            EditBudgetItemDialog editDialog = new EditBudgetItemDialog(getController(), limit);

            boolean actionDone = false;
            while (!actionDone) {
                editDialog.setVisible(true);
                if (editDialog.isCancelled()) {
                    actionDone = true;
                } else if (editDialog.isConfirmed()) {
                    double editedLimit = editDialog.getLimitValue();
                    actionDone = tryEditBudgetItem(category, selectedRow, editedLimit);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select an item to edit.",
                    "Edit Budget Item", JOptionPane.INFORMATION_MESSAGE);
        }
        update();
    }

    /*
     * MODIFIES: this
     * EFFECTS:  attempts to edit the limit of the item of the given category, and its corresponding row of budgetTable
     */
    private boolean tryEditBudgetItem(Category category, int selectedRow, double editedLimit) {
        try {
            getController().getBudget().editLimitOf(category, editedLimit);
            budgetTable.setValueAt(editedLimit, selectedRow, 1);
            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Edit Budget Item",
                    JOptionPane.INFORMATION_MESSAGE);
        }
        return false;
    }

}
