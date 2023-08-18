package ui.windows;

import com.toedter.calendar.JDateChooser;
import model.Category;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;

// Represents a window in Easy Budget! GUI that allows a user to set details of an expense
public class ExpenseDialog extends DefaultDialog {
    protected JTextField descriptionField = new JTextField();
    protected JTextField vendorField = new JTextField();
    protected JTextField priceField = new JTextField();
    protected JDateChooser dateSelect = new JDateChooser();
    protected JComboBox<String> categorySelect = new JComboBox<>();

    /*
     * EFFECTS: constructs a new ExpenseWindow with a parent frame and title
     */
    public ExpenseDialog(Frame parent, String title) {
        super(parent, title, 6);

        JLabel descriptionLabel = new JLabel("Brief description: ");

        JLabel vendorLabel = new JLabel("Vendor: ");

        JLabel priceLabel = new JLabel("Price:                                       $");

        JLabel dateLabel = new JLabel("Purchase Date: ");

        JLabel categoryLabel = new JLabel("Category: ");
        for (Category c : Category.values()) {
            categorySelect.addItem(c.name);
        }

        add(descriptionLabel);
        add(descriptionField);
        add(vendorLabel);
        add(vendorField);
        add(priceLabel);
        add(priceField);
        add(dateLabel);
        add(dateSelect);
        add(categoryLabel);
        add(categorySelect);

        add(okButton);
        add(cancelButton);

        pack();
        setLocationRelativeTo(parent);
    }

    /*
     * EFFECTS: returns the value of the description text field
     */
    public String getDescriptionValue() {
        return descriptionField.getText();
    }

    /*
     * EFFECTS: returns the value of the vendor text field
     */
    public String getVendorValue() {
        return vendorField.getText();
    }

    /*
     * EFFECTS: returns the value of the price text field
     */
    public double getPriceValue() {
        try {
            return Double.parseDouble(priceField.getText());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /*
     * EFFECTS: returns the value of the date chooser
     */
    public LocalDate getDateValue() {
        try {
            return LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd").format(dateSelect.getDate()));
        } catch (Exception e) {
            return null;
        }
    }

    /*
     * EFFECTS: returns the value of the category dropdown
     */
    public Category getCategoryValue() {
        return Category.stringToCategory((String) categorySelect.getSelectedItem());
    }
}
