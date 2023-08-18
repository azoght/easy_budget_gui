package ui.windows;

import javax.swing.*;
import java.awt.*;

// Represents the window used to edit a budget item in the Easy Budget! GUI
public class EditBudgetItemDialog extends DefaultDialog {
    private JTextField limitField = new JTextField();

    /*
     * EFFECTS: constructs a new EditBudgetItemWindow with a parent frame and the limit of the item to edit
     */
    public EditBudgetItemDialog(Frame parent, double limit) {
        super(parent, "Edit Budget Item", 2);

        JLabel amountLabel = new JLabel("New limit:                                $");
        limitField.setText(String.valueOf(limit));

        add(amountLabel);
        add(limitField);

        add(okButton);
        add(cancelButton);

        pack();
        setLocationRelativeTo(parent);
    }


    /*
     * EFFECTS: gets the value of the limit text field
     */
    public double getLimitValue() {
        try {
            return Double.parseDouble(limitField.getText());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

}
