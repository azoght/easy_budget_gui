package ui.windows;

import javax.swing.*;
import java.awt.*;

// Represents the window used to edit the total budget limit in the Easy Budget! GUI
public class EditBudgetLimitDialog extends DefaultDialog {
    private JTextField limitField = new JTextField();

    /*
     * EFFECTS: constructs a new AddBudgetItemWindow with a parent frame and the total limit of the budget to edit
     */
    public EditBudgetLimitDialog(Frame parent, double limit) {
        super(parent, "Edit Budget Limit", 2);

        JLabel limitLabel = new JLabel("Limit:                                       $");

        limitField.setText(String.valueOf(limit));

        add(limitLabel);
        add(limitField);

        add(okButton);
        add(cancelButton);

        pack();
        setLocationRelativeTo(parent);
    }

    /*
     * EFFECTS: returns the value of the limit text field
     */
    public double getLimitValue() {
        try {
            return Double.parseDouble(limitField.getText());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

}
