package ui.windows;

import javax.swing.*;
import java.awt.*;

// Represents a pop-up window (dialog) to perform an action in the Easy Budget! GUI
public class DefaultDialog extends JDialog {

    protected boolean confirmed = false;
    protected boolean cancelled = false;
    protected JButton okButton;
    protected JButton cancelButton;
    private int rows;

    /*
     * EFFECTS: constructs a new window with a parent frame and a title
     */
    public DefaultDialog(Frame parent, String title, int rows) {
        super(parent, title, true);

        this.rows = rows;

        setLayout(new GridLayout(rows, 2));

        okButton = new JButton("OK");
        okButton.addActionListener(e -> {
            confirmed = true;
            dispose();
        });

        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> {
            cancelled = true;
            dispose();
        });

        setResizable(false);
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(400, 35 * rows);
    }
}
