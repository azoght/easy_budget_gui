package ui;

import model.Event;
import model.EventLog;
import persistence.PersistenceManager;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Main {

    public static void main(String[] args) {
        setLookAndFeel();
        EasyBudgetGUI gui = new EasyBudgetGUI();
        gui.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int response = JOptionPane.showConfirmDialog(null, "Do you want to save your "
                                + "budget and spending tracker to file?", "Save", JOptionPane.YES_NO_OPTION);
                if (response == JOptionPane.YES_OPTION) {
                    try {
                        PersistenceManager.saveEasyBudget(gui.getBudget(), gui.getSpendingTracker(),
                                PersistenceManager.EASY_BUDGET_FILEPATH);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                for (Event event : EventLog.getInstance()) {
                    System.out.println(event + "\n");
                }
            }
        });
        SwingUtilities.invokeLater(() -> gui.setVisible(true));
    }

    /*
     * MODIFIES: UIManager
     * EFFECTS:  sets the look and feel of the GUI
     */
    private static void setLookAndFeel() {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


