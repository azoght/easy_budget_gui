package ui.windows;

import model.Category;
import model.Expense;

import java.awt.*;
import java.time.ZoneId;
import java.util.Date;

// Represents the window used to edit an expense in the Easy Budget! GUI
public class EditExpenseDialog extends ExpenseDialog {

    /*
     * EFFECTS: constructs a new EditExpenseWindow with a parent frame and the expense to edit
     */
    public EditExpenseDialog(Frame parent, Expense expense) {
        super(parent, "Edit Expense");

        fillInformationOfExpense(expense);

        setVisible(true);
    }

    /*
     * MODIFIES: this
     * EFFECTS:  fills in components with the details of expense
     */
    private void fillInformationOfExpense(Expense expense) {
        descriptionField.setText(expense.getDescription());
        vendorField.setText(expense.getVendor());
        priceField.setText(String.valueOf(expense.getPrice()));
        dateSelect.setDate(Date.from(expense.getDate().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        for (Category c : Category.values()) {
            categorySelect.addItem(c.name);
        }
        categorySelect.setSelectedItem(expense.getCategory().name);
    }

}
