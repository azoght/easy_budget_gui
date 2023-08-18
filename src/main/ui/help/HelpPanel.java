package ui.help;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

// Represents a panel displaying instructions to use a tab
public class HelpPanel extends JPanel {

    protected JTextPane textPane;
    private static final Color BG_COLOUR = new Color(248, 240, 197);

    /*
     * EFFECTS: constructs a new HelpPanel with text
     */
    public HelpPanel(String text) {
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setBackground(BG_COLOUR);

        textPane = new JTextPane();
        textPane.setBackground(BG_COLOUR);
        textPane.setContentType("text/html");
        textPane.setPreferredSize(new Dimension(300, 130));

        textPane.setText(text);
        textPane.setEditable(false);
        textPane.setCaretPosition(0);

        add(textPane);
    }
}
