package ui.tabs;

import com.toedter.calendar.JDateChooser;
import model.*;
import model.exceptions.ExpenseDoesNotExistException;
import ui.EasyBudgetGUI;
import ui.windows.AddExpenseDialog;
import ui.windows.EditExpenseDialog;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.List;

// The tab containing all options for expenses in the GUI
public class SpendingTrackerTab extends Tab {
    private JButton addExpenseButton = new JButton("Add Expense");
    private JButton editExpenseButton = new JButton("Edit Expense");
    private JButton deleteExpenseButton = new JButton("Delete Expense");
    private JToolBar toolBar = new JToolBar();
    private JPanel tablePanel = new JPanel(new BorderLayout());
    private DefaultTableModel expenseTableModel = new DefaultTableModel();
    private JTable expenseTable = new JTable(expenseTableModel);
    private JPanel filterPanel = new JPanel();
    private JCheckBox dateCheck = new JCheckBox("Filter By Date");
    private JDateChooser startDateSelect = new JDateChooser(new Date());
    private JDateChooser endDateSelect = new JDateChooser(new Date());
    private JCheckBox categoryCheck = new JCheckBox("Filter By Categories");
    private JPanel categoryCheckboxes = new JPanel();
    private FilterByDate dateFilter = new FilterByDate(LocalDate.now().getMonth());
    private FilterByCategories categoryFilter = new FilterByCategories(new HashSet<>());
    private List<ExpenseFilter> filters = new ArrayList<>();


    /*
     * REQUIRES: EasyBudgetGUI controller that holds this tab
     * EFFECTS:  constructs a new SpendingTrackerTab and sets up its listeners and layout
     */
    public SpendingTrackerTab(EasyBudgetGUI controller) {
        super(controller);

        toolBar.setFloatable(false);
        expenseTable.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
        tablePanel.add(expenseTable);
        dateCheck.setFont(new Font("Dialog", Font.ITALIC, 13));
        startDateSelect.setFont(new Font("Tahoma", Font.PLAIN, 10));
        startDateSelect.setCalendar(null);
        endDateSelect.setFont(new Font("Tahoma", Font.PLAIN, 10));
        endDateSelect.setCalendar(null);
        categoryCheck.setFont(new Font("Dialog", Font.ITALIC, 13));

        setUpListeners();
        setUpLayout();
    }

    /*
     * MODIFIES: this
     * EFFECTS:  sets up listeners for components
     */
    @Override
    public void setUpListeners() {
        addExpenseButton.addActionListener(e -> addExpense());
        editExpenseButton.addActionListener(e -> editExpense());
        deleteExpenseButton.addActionListener(e -> deleteExpense());
        expenseTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    editExpense();
                }
            }
        });
    }

    /*
     * MODIFIES: this
     * EFFECTS:  sets up the layout of the Spending Tracker tab
     */
    @Override
    public void setUpLayout() {
        setLayout(new BorderLayout());

        Box centerBox = Box.createHorizontalBox();
        centerBox.add(Box.createHorizontalGlue()); // Add glue before buttons
        centerBox.add(addExpenseButton);
        centerBox.add(editExpenseButton);
        centerBox.add(deleteExpenseButton);
        centerBox.add(Box.createHorizontalGlue()); // Add glue after buttons
        toolBar.add(centerBox);

        setUpExpenseTable(getController().getSpendingTracker().getExpenses());

        JScrollPane tableScrollPane = new JScrollPane(expenseTable);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, tablePanel, filterPanel);
        splitPane.setResizeWeight(0.9);

        tablePanel.add(toolBar, BorderLayout.NORTH);
        tablePanel.add(tableScrollPane, BorderLayout.CENTER);
        add(splitPane, BorderLayout.CENTER);

        setUpFilterPanel();
    }

    /*
     * MODIFIES: this
     * EFFECTS:  sets up the table of expenses
     */
    private void setUpExpenseTable(List<Expense> expenses) {
        String[] columnNames = {"Description", "Vendor", "Price ($)", "Date Purchased", "Category"};
        Object[][] data = new Object[expenses.size()][columnNames.length];
        int index = 0;
        for (Expense expense : expenses) {
            data[index][0] = expense.getDescription();
            data[index][1] = expense.getVendor();
            data[index][2] = expense.getPrice();
            data[index][3] = expense.getDate();
            data[index][4] = expense.getCategory().name;
            index++;
        }
        expenseTableModel = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        expenseTable.setModel(expenseTableModel);

    }

    /*
     * MODIFIES: this
     * EFFECTS:  sets up the filter panel of this tab
     */
    private void setUpFilterPanel() {
        filterPanel.setLayout(new BoxLayout(filterPanel, BoxLayout.Y_AXIS));
        filterPanel.add(new JLabel("Filter View"));

        filterPanel.add(dateCheck);

        JPanel startDatePanel = new JPanel();
        startDatePanel.add(new JLabel("Start Date: "));
        startDateSelect.setEnabled(false);
        startDatePanel.add(startDateSelect);
        filterPanel.add(startDatePanel);

        JPanel endDatePanel = new JPanel();
        endDatePanel.add(new JLabel("End Date: "));
        endDateSelect.setEnabled(false);
        endDatePanel.add(endDateSelect);
        filterPanel.add(endDatePanel);

        filterPanel.add(categoryCheck);
        filterPanel.add(Box.createVerticalGlue());
        categoryCheckboxes.setLayout(new BoxLayout(categoryCheckboxes, BoxLayout.Y_AXIS));
        for (Category c : Category.values()) {
            JCheckBox checkBox = new JCheckBox(c.name);
            checkBox.setEnabled(false);
            categoryCheckboxes.add(checkBox);
        }
        filterPanel.add(categoryCheckboxes);

        addFilterPanelListeners();
    }

    /*
     * MODIFIES: this
     * EFFECTS:  adds listeners to the filter panel
     */
    private void addFilterPanelListeners() {
        addFilterByDateListener();
        addFilterByCategoriesListener();
    }

    /*
     * MODIFIES: this
     * EFFECTS:  adds a listener to the Filter By Date checkbox
     */
    private void addFilterByDateListener() {
        dateCheck.addItemListener(e -> {
            if (dateCheck.isSelected()) {
                dateCheckSelected();
            } else {
                filters.remove(dateFilter);
                startDateSelect.setEnabled(false);
                endDateSelect.setEnabled(false);
                if (filters.size() == 0) {
                    setUpExpenseTable(getController().getSpendingTracker().getExpenses());
                } else {
                    setUpExpenseTable(getController().getSpendingTracker().filter(filters));
                }
            }
        });
    }

    /*
     * REQUIRES: dateCheck.isSelected()
     * MODIFIES: this
     * EFFECTS:  enables startDateSelect and endDateSelect and adds listeners to each
     */
    private void dateCheckSelected() {
        startDateSelect.setEnabled(true);
        endDateSelect.setEnabled(true);
        filters.add(dateFilter);
        setUpExpenseTable(getController().getSpendingTracker().filter(filters));
        startDateSelect.addPropertyChangeListener(evt -> {
            if ("date".equals(evt.getPropertyName())) {
                dateFilter.setStartDate(LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd")
                        .format(startDateSelect.getDate())));
                if (!filters.contains(dateFilter)) {
                    filters.add(dateFilter);
                }
                setUpExpenseTable(getController().getSpendingTracker().filter(filters));
            }
        });
        endDateSelect.addPropertyChangeListener(evt -> {
            if ("date".equals(evt.getPropertyName())) {
                dateFilter.setEndDate(LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd")
                        .format(endDateSelect.getDate())));
                setUpExpenseTable(getController().getSpendingTracker().filter(filters));
            }
        });
    }

    /*
     * MODIFIES: this
     * EFFECTS:  adds a listener to the Filter By Categories checkbox
     */
    private void addFilterByCategoriesListener() {
        categoryCheck.addItemListener(e -> {
            if (categoryCheck.isSelected()) {
                categoryCheckSelected();
            } else {
                filters.remove(categoryFilter);
                if (filters.size() == 0) {
                    setUpExpenseTable(getController().getSpendingTracker().getExpenses());
                } else {
                    setUpExpenseTable(getController().getSpendingTracker().filter(filters));
                }
                for (Component component : categoryCheckboxes.getComponents()) {
                    JCheckBox currentCheckBox = (JCheckBox) component;
                    currentCheckBox.setEnabled(false);
                    currentCheckBox.setSelected(false);
                }
            }
        });
    }

    /*
     * REQUIRES: categoryCheck.isSelected()
     * MODIFIES: this
     * EFFECTS:  enables and selects all category checkboxes and adds a listener to each of them
     */
    private void categoryCheckSelected() {
        for (Component component : categoryCheckboxes.getComponents()) {
            JCheckBox currentCheckBox = (JCheckBox) component;
            categoryFilter.addCategory(Category.stringToCategory(currentCheckBox.getText()));
            currentCheckBox.setEnabled(true);
            currentCheckBox.setSelected(true);
            currentCheckBox.addItemListener(ev -> {
                Category category = Category.stringToCategory(currentCheckBox.getText());
                if (currentCheckBox.isSelected()) {
                    categorySelected(category);
                } else {
                    categoryUnselected(category);
                }
            });
        }
    }

    /*
     * REQUIRES: the checkbox corresponding to category in filterPanel is selected
     * MODIFIES: this
     * EFFECTS:  adds category to categoryFilter and updates expense table
     */
    private void categorySelected(Category category) {
        categoryFilter.addCategory(category);
        if (!filters.contains(categoryFilter)) {
            filters.add(categoryFilter);
        }
        setUpExpenseTable(getController().getSpendingTracker().filter(filters));
    }

    /*
     * REQUIRES: the checkbox corresponding to category in filterPanel is not selected
     * MODIFIES: this
     * EFFECTS:  if categoryCheck is selected, removes category to categoryFilter and updates expense table
     *           otherwise, does nothing
     */
    private void categoryUnselected(Category category) {
        if (categoryCheck.isSelected()) {
            categoryFilter.removeCategory(category);
            if (!filters.contains(categoryFilter)) {
                filters.add(categoryFilter);
            }
            setUpExpenseTable(getController().getSpendingTracker().filter(filters));
        }
    }

    /*
     * MODIFIES: this
     * EFFECTS:  keeps the expense table and the compare table in the Compare Tab up to date
     */
    private void update() {
        setUpExpenseTable(getController().getSpendingTracker().filter(filters));
        getController().updateCompareTable();
    }


    /*
     * MODIFIES: this
     * EFFECTS:  adds an expense through a separate dialog
     */
    private void addExpense() {
        AddExpenseDialog addDialog = new AddExpenseDialog(getController());
        boolean actionDone = false;
        while (!actionDone) {
            addDialog.setVisible(true);
            if (addDialog.isCancelled()) {
                actionDone = true;
            } else if (addDialog.isConfirmed()) {
                String description = addDialog.getDescriptionValue();
                String vendor = addDialog.getVendorValue();
                double price = addDialog.getPriceValue();
                LocalDate date = addDialog.getDateValue();
                Category category = addDialog.getCategoryValue();
                try {
                    getController().getSpendingTracker().addExpense(price, description, vendor, date, category);
                    expenseTableModel.addRow(new Object[]{description, vendor, price, date, category});
                    actionDone = true;
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, e.getMessage(), "Add Expense",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
        }
        update();
    }

    /*
     * MODIFIES: this
     * EFFECTS:  edits an expense through a separate dialog
     */
    private void editExpense() {
        int selectedRow = expenseTable.getSelectedRow();
        if (selectedRow != -1) {
            Expense expenseToEdit = getController().getSpendingTracker().filter(filters).get(selectedRow);
            EditExpenseDialog editDialog = new EditExpenseDialog(getController(), expenseToEdit);
            boolean actionDone = false;
            while (!actionDone) {
                if (editDialog.isCancelled()) {
                    actionDone = true;
                } else if (editDialog.isConfirmed()) {
                    String editedDescription = editDialog.getDescriptionValue();
                    String editedVendor = editDialog.getVendorValue();
                    double editedPrice = editDialog.getPriceValue();
                    LocalDate editedDate = editDialog.getDateValue();
                    Category editedCategory = editDialog.getCategoryValue();
                    actionDone = tryEditBudgetItem(selectedRow, expenseToEdit.getId(), editedDescription, editedVendor,
                            editedPrice, editedDate, editedCategory);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select an expense to edit.",
                    "Edit Expense", JOptionPane.INFORMATION_MESSAGE);
        }
        update();
    }

    /*
     * MODIFIES: this
     * EFFECTS:  attempts to edit the given expense with the given id, and its corresponding row of budgetTable
     */
    private boolean tryEditBudgetItem(int selectedRow, String id, String editedDescription, String editedVendor,
                                      double editedPrice, LocalDate editedDate, Category editedCategory) {
        try {
            SpendingTracker spendingTracker = getController().getSpendingTracker();
            spendingTracker.editDescriptionOf(id, editedDescription);
            expenseTable.setValueAt(editedDescription, selectedRow, 0);
            spendingTracker.editVendorOf(id, editedVendor);
            expenseTable.setValueAt(editedVendor, selectedRow, 1);
            spendingTracker.editPriceOf(id, editedPrice);
            expenseTable.setValueAt(editedPrice, selectedRow, 2);
            spendingTracker.editDateOf(id, editedDate);
            expenseTable.setValueAt(editedDate, selectedRow, 3);
            spendingTracker.editCategoryOf(id, editedCategory);
            expenseTable.setValueAt(editedCategory.name, selectedRow, 4);
            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Edit Expense",
                    JOptionPane.INFORMATION_MESSAGE);
        }
        return false;
    }

    /*
     * MODIFIES: this
     * EFFECTS:  deletes the selected item in table from the spending tracker
     */
    private void deleteExpense() {
        int selectedRow = expenseTable.getSelectedRow();
        if (selectedRow != -1) {
            int response = JOptionPane.showConfirmDialog(
                    null,
                    "Are you sure you want to delete this expense?",
                    "Confirmation",
                    JOptionPane.YES_NO_OPTION
            );

            if (response == JOptionPane.YES_OPTION) {
                String id = getController().getSpendingTracker().filter(filters).get(selectedRow).getId();
                expenseTableModel.removeRow(selectedRow);
                try {
                    getController().getSpendingTracker().deleteExpense(id);
                } catch (ExpenseDoesNotExistException e) {
                    JOptionPane.showMessageDialog(this, e.getMessage(), "Delete Expense",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select an expense to delete",
                    "Delete Expense", JOptionPane.INFORMATION_MESSAGE);
        }
        getController().updateCompareTable();
    }

}
