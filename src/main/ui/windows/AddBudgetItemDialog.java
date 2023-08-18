package ui.windows;

import model.Category;

import javax.swing.*;
import java.awt.*;

// Represents the window used to add a budget item in the Easy Budget! GUI
public class AddBudgetItemDialog extends DefaultDialog {
    private JComboBox<String> categorySelect = new JComboBox<>();
    private JTextField limitField = new JTextField();

    /*
     * EFFECTS: constructs a new AddBudgetItemWindow with a parent frame
     */
    public AddBudgetItemDialog(Frame parent) {
        super(parent, "Add Budget Item", 3);

        JLabel categoryLabel = new JLabel("Category: ");
        for (Category c : Category.values()) {
            categorySelect.addItem(c.name);
        }
        JLabel limitLabel = new JLabel("Limit:                                       $");

        add(categoryLabel);
        add(categorySelect);
        add(limitLabel);
        add(limitField);

        add(okButton);
        add(cancelButton);

        pack();
        setLocationRelativeTo(parent);
    }

    /*
     * EFFECTS: returns the value of the category dropdown
     */
    public String getCategoryValue() {
        return (String) categorySelect.getSelectedItem();
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
